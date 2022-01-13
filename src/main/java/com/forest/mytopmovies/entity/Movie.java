package com.forest.mytopmovies.entity;

import lombok.Builder;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "mtm_movies")
@Builder
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "original_title", length = 50)
    private String originalTitle;

    @Column(name = "title", length = 50)
    private String title;

    @Column(name = "original_language", length = 10)
    private String originalLanguage;

    @Column(name = "overview", columnDefinition = "TEXT")
    private String overview;

    @Column(name = "vote_average")
    private double averageVote;

    @Column(name = "release_date")
    private Date releaseDate;

    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE
                    , CascadeType.DETACH, CascadeType.REFRESH})
    @JoinTable(
            name = "mtm_movie_genre",
            joinColumns = @JoinColumn(name = "movie_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id ", referencedColumnName = "id"))
    private List<Genres> genres;

    @Column(name = "tmdb_id")
    private int tmdbId;

    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE
                    , CascadeType.DETACH, CascadeType.REFRESH})
    @JoinTable(
            name = "mtm_movie_movie_list",
            joinColumns = @JoinColumn(name = "movie_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "movielist_id ", referencedColumnName = "id"))
    private List<MovieList> movieLists;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public double getAverageVote() {
        return averageVote;
    }

    public void setAverageVote(double averageVote) {
        this.averageVote = averageVote;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public List<Genres> getGenres() {
        return genres;
    }

    public void setGenres(List<Genres> genres) {
        this.genres = genres;
    }

    public int getTmdbId() {
        return tmdbId;
    }

    public void setTmdbId(int tmdbId) {
        this.tmdbId = tmdbId;
    }

    public List<MovieList> getMovieLists() {
        return movieLists;
    }

    public void setMovieLists(List<MovieList> movieLists) {
        this.movieLists = movieLists;
    }
}
