package com.forest.mytopmovies.entity;

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
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "mtm_movie_lists")
@Getter
@Setter
public class MovieList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "movie_list_name")
    private String movieListName;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE
                    , CascadeType.DETACH, CascadeType.REFRESH})
    @JoinTable(
            name = "mtm_movie_movie_list",
            joinColumns = @JoinColumn(name = "movie_list_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "movie_id ", referencedColumnName = "id"))
    private List<Movie> movies;

}
