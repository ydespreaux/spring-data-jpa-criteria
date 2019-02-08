# Spring Data Jpa Criteria

Spring Data JPA with criteria implementation

## Introduction

This library defines a repository for performing dynamic queries using a criteria API.

## Versions

|   spring-data-jpa-criteria   |    spring-boot   |
|:----------------------------:|:----------------:|
|           1.0.1              |   1.5.x.RELEASE  |

## Add maven dependency

```xml
<dependency>
    <groupId>com.github.ydespreaux.spring.data</groupId>
    <artifactId>spring-data-jpa-criteria</artifactId>
    <version>1.0.1</version>
</dependency>
```

## API Criteria

##### Constructors

```java
public Criteria();
public Criteria(String fieldName);
public Criteria(Field field);
```

##### Conjunctions

```java
public Criteria and(Field field);
public Criteria and(String fieldName);
public Criteria and(Criteria criteria);
public Criteria and(Criteria... criterias);
public Criteria and(List<Criteria> criterias);
public Criteria or(Field field);
public Criteria or(Criteria criteria);
```

##### Operators

```java
public Criteria eq(Object value);
public Criteria notEq(Object value);
public Criteria isNull();
public Criteria isNotNull();
public Criteria contains(String search);
public Criteria startsWith(String search);
public Criteria endsWith(String search);
public Criteria between(Comparable<?> lowerBound, Comparable<?> upperBound);
public Criteria lessThanEqual(Comparable<?> upperBound);
public Criteria lessThan(Comparable<?> upperBound);
public Criteria greaterThanEqual(Comparable<?> lowerBound);
public Criteria greaterThan(Comparable<?> lowerBound);
public Criteria in(Object... values);
public Criteria in(Iterable<?> values);
public Criteria notIn(Object... values);
public Criteria notIn(Iterable<?> values);
```

### Samples

###### Condition : field <> 'value'
```java
Criteria criteria = new Criteria("field").notEq("value");
```
###### Condition : field == 'value'
```java
Criteria criteria = new Criteria("field").eq("value");
```
###### Condition : value1 <= field <= value2
```java
Criteria criteria = new Criteria("field").between("value1", "value2");
```
###### Condition : field like '%value%'
```java
Criteria criteria = new Criteria("field").contains("value");
```
###### Condition : field like '%value'
```java
Criteria criteria = new Criteria("field").endsWith("value");
```
###### Condition : field like 'value%'
```java
Criteria criteria = new Criteria("field").startsWith("value");
```
###### Condition : field > 10
```java
Criteria criteria = new Criteria("field").greaterThan(10);
```
###### Condition : field >= 10
```java
Criteria criteria = new Criteria("field").greaterThanEqual(10);
```
###### Condition : field < 10
```java
Criteria criteria = new Criteria("field").lessThan(10);
```
###### Condition : field <= 10
```java
Criteria criteria = new Criteria("field").lessThanEqual(10);
```
###### Condition : field in ('value1', 'value2')
```java
Criteria criteria = new Criteria("field").in("value1", "value2");
```
###### Condition : field not in ('value1', 'value2')
```java
Criteria criteria = new Criteria("field").notIn("value1", "value2");
```
###### Condition : field_1 = 'values_1' AND field_2 != 'values_2' AND field_3 like '%value_3%'
```java
Criteria criteria = new Criteria("field_1").eq("value_1")
                .and("field_2").notEq("value_2")
                .and("field_3").contains("value_3");
```
###### Condition : field_1 = 'values_1' OR field_2 != 'values_2' OR field_3 like '%value_3%'
```java
Criteria criteria = new Criteria("field_1").eq("value_1")
                .or("field_2").notEq("value_2")
                .or("field_3").contains("value_3");
```
###### Condition : field_1 = 'value_1' OR (field_2 != 'value_2' AND field_3 like '%value_3%')
```java
Criteria criteria = new Criteria("field_1").eq("value_1")
                .or(new Criteria("field_2").notEq("value_2").and("field_3").contains("value_3"));
```
###### Condition : (field_1 = 'value_1' OR field_2 = 'value_2') AND field_3 = 'value_3'
```java
Criteria criteria = new Criteria("field_1").eq("value_1")
                .or("field_2").eq("value_2")
                .and("field_3").eq("value_3");
```

## JpaCriteriaRepository repository

The JpaCriteriaRepository interface implements search methods that take into account the criteria API.

```java
public interface JpaCriteriaRepository<T, ID> extends JpaRepository<T, ID> {
    T findOne(@Nullable Criteria criteria);
    T findOne(@Nullable Criteria criteria, QueryOptions options);
    List<T> findAll(@Nullable Criteria criteria);
    List<T> findAll(@Nullable Criteria criteria, QueryOptions options);
    List<T> findAll(@Nullable Criteria criteria, Sort sort);
    List<T> findAll(@Nullable Criteria criteria, Sort sort, QueryOptions options);
    Page<T> findAll(@Nullable Criteria criteria, Pageable pageable);
    Page<T> findAll(@Nullable Criteria criteria, Pageable pageable, QueryOptions options);
    long count(@Nullable Criteria criteria);
    long count(@Nullable Criteria criteria, QueryOptions options);
}
```

Sample:

```java
public interface AuthorRepository extends JpaCriteriaRepository<Author, Long> {
}
```

## Spring Boot configuration

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
