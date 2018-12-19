package net.unicon.iam.shibboleth.storage;


import net.shibboleth.utilities.java.support.component.ComponentInitializationException;
import org.opensaml.storage.StorageService;
import org.opensaml.storage.StorageServiceTest;
import org.redisson.Redisson;
import org.redisson.config.Config;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import javax.annotation.Nonnull;

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
}
