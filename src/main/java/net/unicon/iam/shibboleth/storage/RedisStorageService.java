package net.unicon.iam.shibboleth.storage;


import net.shibboleth.utilities.java.support.collection.Pair;
import org.opensaml.storage.AbstractStorageService;
import org.opensaml.storage.StorageRecord;
import org.opensaml.storage.VersionMismatchException;
import org.redisson.api.RedissonClient;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;

public class RedisStorageService extends AbstractStorageService {
    private final RedissonClient client;

    public RedisStorageService(final RedissonClient client) {
        this.client = client;
    }

    @Override
    public boolean create(@Nonnull String context, @Nonnull String key, @Nonnull String value, @Nullable Long expiration) throws IOException {
        return false;
    }

    @Nullable
    @Override
    public StorageRecord read(@Nonnull String context, @Nonnull String key) throws IOException {
        return null;
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
