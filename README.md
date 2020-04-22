[![Build Status](https://dev.azure.com/melanga0355/Spring%20Config%20Server/_apis/build/status/melangad.spring-config-rest-server?branchName=master)](https://dev.azure.com/melanga0355/Spring%20Config%20Server/_build/latest?definitionId=1&branchName=master)

# Introduction
This is an alternative for Spring Cloud Config which has its own client and server. This project is for the client component of the config server.

The server component is using database as the configuration source. Please refer to server project more details on server features.

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
    <groupId>io.github.melangad</groupId>
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
You can enable or disable Config Client using following property
```
io.github.melangad.spring.config.remote.enable=true
```

Use following property to set the config server URI
SERVER_HOST - Config Server Host & port
```
io.github.melangad.spring.config.server.url=http://SERVER_HOST/config
```

Configurations are stored in the server against a unique label and you need to provide the label in the configurations.
```
io.github.melangad.spring.config.label=LABEL
```
If server has feedback endpoint and if you need to provide state of the client configuration as a feedback to the server, set feedback endpoint.
```
io.github.melangad.spring.config.server.feedbackur=http://SERVER_HOST/config/feedback
```
## Config Manage
Config Manager provides several APIs to query configuration state.
* Current Configuration version
* Last update time of the configuration on client side
* Method to invoke feedback call. You need to provide a unique client ID on invoking feedback