package net.unicon.iam.shibboleth.storage;


import net.shibboleth.utilities.java.support.collection.Pair;
import org.opensaml.storage.AbstractStorageService;
import org.opensaml.storage.MutableStorageRecord;
import org.opensaml.storage.StorageRecord;
import org.opensaml.storage.VersionMismatchException;
import org.redisson.api.RLock;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class RedisStorageService extends AbstractStorageService {
    private final RedissonClient client;

    public RedisStorageService(final RedissonClient client) {
        this.client = client;
    }

    private long getSystemExpiration(Long expiration) {
        return (expiration == null || expiration == 0) ? 0 : expiration - System.currentTimeMillis();
    }

    @Override
    public boolean create(@Nonnull String context, @Nonnull String key, @Nonnull String value, @Nullable Long expiration) throws IOException {
        RMapCache<String, StorageRecord> map = client.getMapCache(context);
        StorageRecord storageRecord = new VersionMutableStorageRecord(value, expiration, 1L);
        if (expiration != null) {
            long ttl = getSystemExpiration(expiration);
            return map.fastPutIfAbsent(key, storageRecord, ttl, TimeUnit.MILLISECONDS);
        } else {
            return map.fastPutIfAbsent(key, storageRecord);
        }
    }

    @Nullable
    @Override
    public StorageRecord read(@Nonnull String context, @Nonnull String key) throws IOException {
        RMapCache<String, StorageRecord> map = client.getMapCache(context);
        return map.get(key);
    }

    @Nonnull
    @Override
    public Pair<Long, StorageRecord> read(@Nonnull String context, @Nonnull String key, long version) throws IOException {
        RMapCache<String, StorageRecord> map = client.getMapCache(context);
        StorageRecord storageRecord = read(context, key);
        if (storageRecord == null) {
            return new Pair<>();
        }
        if ((Long)version != null && version == storageRecord.getVersion()) {
            return new Pair<>(version, null);
        }
        return new Pair<>(storageRecord.getVersion(), storageRecord);
    }

    private Long doUpdate(final Long version, final String context, final String key, final String value, final Long expiration) throws IOException {
        final RLock lock = this.client.getLock(context + ":" + key);
        lock.lock(10, TimeUnit.SECONDS);
        try {
            MutableStorageRecord record = (MutableStorageRecord) this.read(context, key);
            if (record == null) {
                return null;
            }

            if (version != null && version != record.getVersion()) {
                throw new VersionMismatchWrapperException(new VersionMismatchException());
            }

            if (value != null) {
                record.setValue(value);
                record.incrementVersion();
            }

            record.setExpiration(expiration);

            if (expiration != null) {
                long ttl = getSystemExpiration(expiration);
                record.setExpiration(expiration);
                this.client.getMapCache(context).fastPut(key, record, ttl, TimeUnit.MILLISECONDS);
            } else {
                this.client.getMapCache(context).fastPut(key, record);
            }
            return record.getVersion();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public boolean update(@Nonnull String context, @Nonnull String key, @Nonnull String value, @Nullable Long expiration) throws IOException {
        return doUpdate(null, context, key, value, expiration) != null;
    }

    @Nullable
    @Override
    public Long updateWithVersion(long version, @Nonnull String context, @Nonnull String key, @Nonnull String value, @Nullable Long expiration) throws IOException, VersionMismatchException {
        try {
            return doUpdate(version, context, key, value, expiration);
        } catch (VersionMismatchWrapperException e) {
            throw (VersionMismatchException)e.getCause();
        }
    }

    @Override
    public boolean updateExpiration(@Nonnull String context, @Nonnull String key, @Nullable Long expiration) throws IOException {
        return doUpdate(null, context, key, null, expiration) != null;
    }

    private boolean doDelete(Long version, String context, String key) {
        RMapCache map = this.client.getMapCache(context);
        if (!map.containsKey(key)) {
            return false;
        }
        if (version != null && ((StorageRecord)map.get(key)).getVersion() != version) {
            throw new VersionMismatchWrapperException(new VersionMismatchException());
        }
        map.remove(key);
        return true;
    }

    @Override
    public boolean delete(@Nonnull String context, @Nonnull String key) throws IOException {
        return doDelete(null, context, key);
    }

    @Override
    public boolean deleteWithVersion(long version, @Nonnull String context, @Nonnull String key) throws IOException, VersionMismatchException {
        try {
            return doDelete(version, context, key);
        } catch (VersionMismatchWrapperException e) {
            throw (VersionMismatchException)e.getCause();
        }
    }

    @Override
    public void reap(@Nonnull String context) throws IOException {
        // handled internally
    }

    @Override
    public void updateContextExpiration(@Nonnull String context, @Nullable Long expiration) throws IOException {

    }

    @Override
    public void deleteContext(@Nonnull String context) throws IOException {
        this.client.getMapCache(context).destroy();
    }

    public static class VersionMismatchWrapperException extends RuntimeException {
        public VersionMismatchWrapperException(Throwable cause) {
            super(cause);
        }
    }
}
