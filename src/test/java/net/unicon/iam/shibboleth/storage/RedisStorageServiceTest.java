package net.unicon.iam.shibboleth.storage;


import net.shibboleth.utilities.java.support.component.ComponentInitializationException;
import org.opensaml.storage.StorageRecord;
import org.opensaml.storage.StorageService;
import org.opensaml.storage.StorageServiceTest;
import org.redisson.Redisson;
import org.redisson.config.Config;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.annotation.Nonnull;
import java.io.IOException;

public class RedisStorageServiceTest extends StorageServiceTest {
    RedisStorageService redisStorageService;

    @BeforeClass
    @Override
    protected void setUp() throws ComponentInitializationException {
        Config config = new Config();
        config.useSingleServer().setAddress("redis://localhost:6379");

        this.redisStorageService = new RedisStorageService(Redisson.create(config));
        this.redisStorageService.setId("test");
        super.setUp();
    }

    @AfterClass
    @Override
    protected void tearDown() {
        super.tearDown();
    }

    @Nonnull
    @Override
    protected StorageService getStorageService() {
        return this.redisStorageService;
    }

    @Test
    public void testUpdateContextExpiration() throws IOException {
        String context = "testContext";

        for (int i = 0; i < 10; i++) {
            this.shared.create(context, Integer.toString(i), Integer.toString(i), System.currentTimeMillis() + 500000);
        }

        Long newExpiration = System.currentTimeMillis() + 1000000;
        this.shared.updateContextExpiration(context, newExpiration);

        for (int i = 0; i < 10; i++) {
            StorageRecord record = this.shared.read(context, Integer.toString(i));
            assert record.getExpiration().equals(newExpiration);
        }
    }
}
