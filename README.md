[![Build Status](https://dev.azure.com/melanga0355/Spring%20Config%20Server/_apis/build/status/melangad.spring-config-rest-server?branchName=master)](https://dev.azure.com/melanga0355/Spring%20Config%20Server/_build/latest?definitionId=1&branchName=master)

# Introduction
This is an alternative for Spring Cloud Config which has its own client and server. This project is for the client component of the config server.

The server component is using database as the configuration source. Please refer to [server project](https://github.com/melangad/spring-config-rest-server) more details on server features.

# Client
Client can be easily integrated with any Spring Boot project. Client provides following features at the moment.

* Retrieve latest configurations from the Config Server
* Actuator endpoint to query the configuration version and last update timestamp on the client side

# Quick Start
To use the client side in your application, add following dependency on your pom.xml file.

[source,xml,indent=0]
.pom.xml
```
<dependency>
    <groupId>io.github.melangad.spring.config</groupId>
    <artifactId>spring-config-rest-client</artifactId>
    <version>0.0.1-SNAPSHOT</version>
</dependency>
```

If you need to enable the Actuator, create following beans under a Spring Configuration.

```
@Autowired
Environment environment;

@Bean
public ConfigManager configManager() {
    return new ConfigManager(environment);
}

@Bean
public ConfigReportEndpoint configReportEndpoint() {
    return new ConfigReportEndpoint();
}
```

## Configurations
You csn enable or disable Config Client usign following property
```
io.github.melangad.spring.config.remote.enable=true
```

Use following property to set the config server URI
SERVER_HOST - Config Server Host & port
APPLICATION_ID - Unique application ID for the client

Configurations are stored in the server against a unique Application ID and you need to provide the relevant application ID as part of the URI
```
io.github.melangad.spring.config.remote.url=http://SERVER_HOST/config/APPLICATION_ID
```


