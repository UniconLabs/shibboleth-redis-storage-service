package net.unicon.iam.shibboleth.storage;


import com.fasterxml.jackson.databind.module.SimpleModule;
import net.shibboleth.utilities.java.support.collection.Pair;
import net.unicon.iam.shibboleth.storage.jackson.StorageRecordDeserializer;
import net.unicon.iam.shibboleth.storage.jackson.StorageRecordSerializer;
import org.opensaml.storage.AbstractStorageService;
import org.opensaml.storage.MutableStorageRecord;
import org.opensaml.storage.StorageRecord;
import org.opensaml.storage.VersionMismatchException;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.Codec;
import org.redisson.codec.JsonJacksonCodec;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class RedisStorageService extends AbstractStorageService {
    private final RedissonClient client;

    public RedisStorageService(final RedissonClient client) {
        this.client = client;

        Codec codec = this.client.getConfig().getCodec();
        if (codec instanceof JsonJacksonCodec) {
            JsonJacksonCodec jsonJacksonCodec = (JsonJacksonCodec)codec;
            SimpleModule module = new SimpleModule();
            module.addDeserializer(StorageRecord.class, new StorageRecordDeserializer());
            module.addSerializer(StorageRecord.class, new StorageRecordSerializer());
            jsonJacksonCodec.getObjectMapper().registerModule(module);
        }
    }

    private long getSystemExpiration(Long expiration) {
        return (expiration == null || expiration == 0) ? 0 : expiration - System.currentTimeMillis();
    }

    @Override
    public boolean create(@Nonnull String context, @Nonnull String key, @Nonnull String value, @Nullable Long expiration) throws IOException {
        RMapCache<String, StorageRecord> map = client.getMapCache(context);
        StorageRecord storageRecord = new MutableStorageRecord(value, expiration);
        if (expiration != null) {
            return map.fastPutIfAbsent(key, storageRecord, getSystemExpiration(expiration), TimeUnit.MILLISECONDS);
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
        return null;
    }

    @Override
    public boolean update(@Nonnull String context, @Nonnull String key, @Nonnull String value, @Nullable Long expiration) throws IOException {
        return false;
    }

    @Nullable
    @Override
    public Long updateWithVersion(long version, @Nonnull String context, @Nonnull String key, @Nonnull String value, @Nullable Long expiration) throws IOException, VersionMismatchException {
        return null;
    }

    @Override
    public boolean updateExpiration(@Nonnull String context, @Nonnull String key, @Nullable Long expiration) throws IOException {
        return false;
    }

    @Override
    public boolean delete(@Nonnull String context, @Nonnull String key) throws IOException {
        return false;
    }

    @Override
    public boolean deleteWithVersion(long version, @Nonnull String context, @Nonnull String key) throws IOException, VersionMismatchException {
        return false;
    }

    @Override
    public void reap(@Nonnull String context) throws IOException {

    }

    @Override
    public void updateContextExpiration(@Nonnull String context, @Nullable Long expiration) throws IOException {

    }

    @Override
    public void deleteContext(@Nonnull String context) throws IOException {

    }
}
