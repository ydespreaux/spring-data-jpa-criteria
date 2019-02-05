# Spring Data Jpa Criteria

Spring Data JPA with criteria implementation

## Introduction

## Versions

|   spring-data-jpa-criteria   |    spring-boot   |
|:----------------------------:|:----------------:|
|           1.1.0              |   2.1.0.RELEASE  |

## Maven dependency

```xml
<dependency>
    <groupId>com.github.ydespreaux.spring.data</groupId>
    <artifactId>spring-data-jpa-criteria</artifactId>
    <version>1.1.0</version>
</dependency>
```

## Quick Start



```java
@SpringBootApplication
public class SampleJpaCriteriaApplication {

    public static void main(String[] args) {
        SpringApplication.run(SampleJpaCriteriaApplication.class, args);
    }
}
```

```java
@Getter
@Setter
@Entity
@Table(name = "ARTIST")
public class Artist {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(
            name = "native",
            strategy = "native"
    )
    private Integer id;

    @Column(name = "DISPLAY_NAME", nullable = false)
    private String displayName;

    @OneToMany(mappedBy = "artist", cascade = CascadeType.ALL)
    private Set<Album> albums;
}
```

```java
@Getter
@Setter
@Entity
@Table(name = "ALBUM")
public class Album {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(
            name = "native",
            strategy = "native"
    )
    private Integer id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private Integer year;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ARTIST_ID", nullable = false, foreignKey = @ForeignKey(name = "FK_ALBUM_ARTIST_ID"))
    private Artist artist;

    @OneToMany(mappedBy = "album", cascade = {CascadeType.ALL})
    private List<Song> songs;
}
```

```java
@Getter
@Setter
@Entity
@Table(name = "SONG")
public class Song {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(
            name = "native",
            strategy = "native"
    )
    private Integer id;

    @Column(name = "TRACK", nullable = false)
    private Integer track;

    @Column(name = "TITLE", nullable = false, length = 150)
    private String title;

    /**
     * the workstations property.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ALBUM_ID", foreignKey = @ForeignKey(name = "FK_SONG_ALBUM_ID"))
    private Album album;
}
```


```java
public interface ArtistRepository extends JpaCriteriaRepository<Artist, Integer> {
}
```

```java
public interface AlbumRepository extends JpaCriteriaRepository<Album, Integer> {
}
```

```java
public interface SongRepository extends JpaRepository<Song, Integer> {
}
```

```java
@Configuration
@EnableAutoConfiguration
@EntityScan(basePackages = {"com.github.ydespreaux.sample.jpa.criteria.domain"})
@EnableJpaCriteriaRepositories(basePackages = {"com.github.ydespreaux.sample.jpa.criteria.repository"})
public class JpaConfiguration {
}
```

```java
```
