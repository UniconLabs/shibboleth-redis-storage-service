# Shibboleth Identity Provider Redis Storage Service

This package provides a storage service implementation for the Shibboleth IdP (v4.1 or later) that is based on redis [ https://redis.io/ ].
The service is deployed as a Shibboleth Plugin (see [ https://shibboleth.atlassian.net/wiki/spaces/IDP4/pages/1294074003/PluginInstallation ])

* Single bean configuration
* Tested with AWS Elasticache

## System Requirements

- Shibboleth IdP v4.1

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

## Licensing

Licensed under the terms of the Apache License, v2. Please see [LICENSE](LICENSE) or [http://www.apache.org/licenses/LICENSE-2.0](http://www.apache.org/licenses/LICENSE-2.0) for more information.

## Included libraries and dependency tree

      +--- commons-configuration:commons-configuration:1.10
      |    \--- commons-logging:commons-logging:1.1.1
      \--- org.redisson:redisson:3.10.1
            +--- io.netty:netty-common:4.1.32.Final
            +--- io.netty:netty-codec:4.1.32.Final
            |    \--- io.netty:netty-transport:4.1.32.Final
            |         +--- io.netty:netty-buffer:4.1.32.Final
            |         |    \--- io.netty:netty-common:4.1.32.Final
            |         \--- io.netty:netty-resolver:4.1.32.Final
            |              \--- io.netty:netty-common:4.1.32.Final
            +--- io.netty:netty-buffer:4.1.32.Final (*)
            +--- io.netty:netty-transport:4.1.32.Final (*)
            +--- io.netty:netty-resolver-dns:4.1.32.Final
            |    +--- io.netty:netty-resolver:4.1.32.Final (*)
            |    +--- io.netty:netty-codec-dns:4.1.32.Final
            |    |    \--- io.netty:netty-codec:4.1.32.Final (*)
            |    \--- io.netty:netty-transport:4.1.32.Final (*)
            +--- io.netty:netty-handler:4.1.32.Final
            |    +--- io.netty:netty-buffer:4.1.32.Final (*)
            |    +--- io.netty:netty-transport:4.1.32.Final (*)
            |    \--- io.netty:netty-codec:4.1.32.Final (*)
            +--- javax.cache:cache-api:1.0.0
            +--- io.projectreactor:reactor-core:3.2.3.RELEASE
            |    \--- org.reactivestreams:reactive-streams:1.0.2
            +--- io.reactivex.rxjava2:rxjava:2.1.13
            |    \--- org.reactivestreams:reactive-streams:1.0.2
            +--- de.ruedigermoeller:fst:2.56
            |    \--- org.objenesis:objenesis:2.5.1
            +--- com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.9.8
            |    \--- org.yaml:snakeyaml:1.23
            \--- org.jodd:jodd-bean:3.7.1
                 \--- org.jodd:jodd-core:3.7.1