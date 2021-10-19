# Shibboleth Identity Provider Redis Storage Service

This package provides a storage service implementation for the Shibboleth IdP (v4.1 or later) that is based on redis [ https://redis.io/ ].
The service is deployed as a Shibboleth Plugin (see [ https://shibboleth.atlassian.net/wiki/spaces/IDP4/pages/1294074003/PluginInstallation ])

* Single bean configuration
* Tested with AWS Elasticache

## Getting started

1. Download the distribution from [ TBD ]. Download either the `.tar.gz` or `.zip` file **and** the associated GPG signature file (the `.asc` file).
2. Install the plugin following instructions at - [ https://shibboleth.atlassian.net/wiki/spaces/IDP4/pages/1294074003/PluginInstallation ]
3. Add storage service bean to `global.xml`. For example:

        <bean id="my.RedisStorageService"
              class="net.unicon.iam.shibboleth.storage.RedisStorageService"
              xmlns:redisson="http://redisson.org/schema/redisson"
              xsi:schemaLocation="
                     http://redisson.org/schema/redisson
                     http://redisson.org/schema/redisson/redisson.xsd">
            <constructor-arg name="client">
                <!-- https://github.com/redisson/redisson/wiki/14.-Integration%20with%20frameworks -->
                <redisson:client>
                    <!-- AWS ElasticCache should hand failover, etc -->
                    <redisson:single-server address="redis://redis:6379" />
                </redisson:client>
            </constructor-arg>
        </bean>
        
    For more configuration information of the bean, refer to [ https://github.com/redisson/redisson/wiki/14.-Integration%20with%20frameworks ]
4. Set appropriate configuration in `idp.properties`. For example (property value set to the bean id from previous step):

        idp.cas.StorageService=my.RedisStorageService

5. Restart servlet container