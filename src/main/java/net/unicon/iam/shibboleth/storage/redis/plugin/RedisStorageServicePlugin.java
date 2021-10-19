package net.unicon.iam.shibboleth.storage.redis.plugin;

import net.shibboleth.idp.plugin.PluginException;
import net.shibboleth.idp.plugin.PropertyDrivenIdPPlugin;

import java.io.IOException;

public class RedisStorageServicePlugin extends PropertyDrivenIdPPlugin {

    public RedisStorageServicePlugin() throws PluginException, IOException {
        super(RedisStorageServicePlugin.class);
    }
}