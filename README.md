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

## Quick Start

### Add SpringBoot application

```java
@SpringBootApplication
public class SampleJpaCriteriaApplication {

    public static void main(String[] args) {
        SpringApplication.run(SampleJpaCriteriaApplication.class, args);
    }
}
```

### Add domains

#### Artist model

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

#### Album model

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

#### Song model

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

### Add repositories

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

### Add configuration

```java
@Configuration
@EnableAutoConfiguration
@EntityScan(basePackages = {"com.github.ydespreaux.sample.jpa.criteria.domain"})
@EnableJpaCriteriaRepositories(basePackages = {"com.github.ydespreaux.sample.jpa.criteria.repository"})
public class JpaConfiguration {
}
```

### Add a rest controller for tests

```java
@RestController
@RequestMapping("/api/songs")
public class SongRestController {

    @Autowired
    private SongRepository repository;

    @GetMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<SongDetail>> findSongByQuery(
            @RequestParam(required = false, value = "artist") String artist,
            @RequestParam(required = false, value = "year") Integer year,
            @RequestParam(required = false, value = "title") String title) {

        Criteria criteria = new Criteria();
        if (StringUtils.hasText(artist)) {
            criteria = criteria.and("album.artist.displayName").contains(artist);
        }
        if (StringUtils.hasText(title)) {
            criteria = criteria.and("title").contains(title);
        }
        if (year != null) {
            criteria = criteria.and("album.year").eq(year);
        }
        return ResponseEntity.ok(this.repository.findAll(criteria, new QueryOptions().withAssociation("album", "album.artist"))
                .stream()
                .map(this::generateDetail)
                .collect(Collectors.toList()));
    }

    /**
     *
     * @param song
     * @return
     */
    private SongDetail generateDetail(Song song) {
        return SongDetail.builder()
                .id(song.getId())
                .artist(song.getAlbum().getArtist().getDisplayName())
                .album(song.getAlbum().getTitle())
                .year(song.getAlbum().getYear())
                .track(song.getTrack())
                .title(song.getTitle())
                .build();
    }

    @Data
    @Builder
    private static class SongDetail {
        private Integer id;
        private String artist;
        private String album;
        private Integer year;
        private Integer track;
        private String title;
    }
}
```


