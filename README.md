shibboleth-idp-gradle-overlay
=============================
The Shibboleth Identity Provider web application built using a Gradle overlay. (A similar yet much uglier experiment done with Maven [exists here](https://github.com/UniconLabs/shibboleth-idp-webapp))

> This project was developed as part of Unicon's [Open Source Support program](https://unicon.net/support). Professional support/integration assistance for this module is available. For more information, visit <https://unicon.net/opensource/shibboleth>.

## Requirements

- JDK 8+
- Tomcat 8+

## Initial Setup

Run once:

```bash
./gradlew clean unpackShibboleth
```

## Build

Run afterwards:

```bash
./gradlew build overlay
```

## IntelliJ IDEA

Create a Run Configuration based on a Tomcat server. If you don't have a Tomcat instance available, [download one](https://tomcat.apache.org/) 
as a zip archive and configure it inside IDEA. When done, select that as the application server in the "Run Configuration" screen.
download one as a zip archive and configure it inside IDEA. When done, select that as the application server in the Run Configuration screen.


### Server

- Specify a URL: `https://org.example.net:8443/idp`
- Add the following VM options:

```bash
-Didp.home=<project-path>/build/tmp/unpackShibboleth/shibboleth-idp
```

- For "Tomcat Server Settings", use ports `8080`, `8443` and `1099`.
- For "Before launch" tasks, add `build overlay`.

![image](https://user-images.githubusercontent.com/1205228/27300133-77aa7ac4-54e3-11e7-8f8a-23d64bfc689a.png)


Your external Tomcat server must have enabled port `8443` in its `conf/server.xml` file:

```xml
<Connector
   protocol="org.apache.coyote.http11.Http11NioProtocol"
   port="8443" maxThreads="200"
   scheme="https" secure="true" SSLEnabled="true"
   keystoreFile="${user.home}/mykeystore" keystorePass="changeit"
   clientAuth="false" sslProtocol="TLS"/>
```
### Deployment

- Add an `External Source` for deployment, and select `<project-path>/build/tmp/unpackShibboleth/shibboleth-idp/webapp`
- Use `/idp` as the application context path.

![image](https://user-images.githubusercontent.com/1205228/27300083-5a491f44-54e3-11e7-995e-aed262d32cd2.png)

### Logs

- Select `Tomcat Localhost Log` and `Tomcat Catalina Log`
- Add a new log entry with the alias `idp` and the log file 
location `<project-path>/build/tmp/unpackShibboleth/shibboleth-idp/logs/idp-*.log`

![image](https://user-images.githubusercontent.com/1205228/27300159-8fa44e20-54e3-11e7-84df-46600f68a2d3.png)

## Usage

### Overlay
The `src/test/overlay` directory contains files that will be laid on top of the originals. 
Mimic the same directory structure as the IdP itself and add files for customizations.

### Authentication

Authentication is mocked using a dummy JAAS connector. Use any password/username you like. All is welcome.

### Logs

Logging is controlled by the `logback.xml` file in the `overlay` directory. `DEBUG` level is turned on by default for a number of packages.

## Build, Run and Remove docker image

Build the docker image without running it. This will also rebuild your shibboleth:

```bash
./gradlew buildDockerImage
```

Build and run the docker image, if you see an error that says that you cannot have duplicate images, 
then run the `cleanDockerImage` task below before running this task again:

```bash
./gradlew runDockerImage
```

Stop and remove the docker image if it is running.
```bash
./gradlew cleanDockerImage
```
