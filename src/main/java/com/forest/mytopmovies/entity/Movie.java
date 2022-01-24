package com.forest.mytopmovies.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "mtm_movies")
@Builder
@Getter
@Setter
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

    @ManyToMany(fetch = FetchType.EAGER,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE
                    , CascadeType.DETACH, CascadeType.REFRESH})
    @JoinTable(
            name = "mtm_movie_genre",
            joinColumns = @JoinColumn(name = "movie_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id ", referencedColumnName = "id"))
    private List<Genre> genres;

    @Column(name = "tmdb_id")
    private int tmdbId;

}
