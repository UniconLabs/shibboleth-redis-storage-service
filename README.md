# Shibboleth Identity Provider Redis Storage Service

This package provides a storage service implementation for the Shibboleth IdP that is based on redis [ https://redis.io/ ].

* Single bean configuration
* Tested with AWS Elasticache

## Getting started

1. Download the distribution from [ https://bintray.com/uniconiam/generic/shibboleth-redis-storage-service ]. You'll want
to download either the `.tar` or `.zip` file.
1. Unarchive the downloaded file.
1. Copy `./shibboleth-redis-storage-service-{version}/edit-webapp` to Shibboleth installation directory
(e.g., `/opt/shibboleth-idp`).
1. Rebuild Shibboleth IdP WAR
1. Add storage service bean to `global.xml`. For example:

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
1. Set appropriate configuration in `idp.properties`. For example:

        idp.cas.StorageService=my.RedisStorageService
1. Restart servlet container