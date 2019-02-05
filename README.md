# Spring Data Jpa Criteria

Spring Data JPA with criteria implementation

## Introduction

## Versions

|   spring-data-jpa-criteria   |    spring-boot   |
|:----------------------------:|:----------------:|
|           1.1.0              |   2.1.0.RELEASE  |

## Add maven dependency

```xml
<dependency>
    <groupId>com.github.ydespreaux.spring.data</groupId>
    <artifactId>spring-data-jpa-criteria</artifactId>
    <version>1.1.0</version>
</dependency>
```

## Quick Start with Spring Boot


### JpaCriteriaRepository repository


### Add configuration

```java
@Configuration
@EnableAutoConfiguration
@EntityScan(basePackages = {"com.github.ydespreaux.sample.jpa.criteria.domain"})
@EnableJpaCriteriaRepositories(basePackages = {"com.github.ydespreaux.sample.jpa.criteria.repository"})
public class JpaConfiguration {
}
```

## Samples

Sample with Spring Boot and embedded database H2 :

https://github.com/ydespreaux/sample-project/tree/master/sample-spring-data-jpa-criteria
