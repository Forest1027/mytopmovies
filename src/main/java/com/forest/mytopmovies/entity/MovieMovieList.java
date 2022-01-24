package com.forest.mytopmovies.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "mtm_movie_movie_list")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class MovieMovieList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @ManyToOne
    @JoinColumn(name = "movie_id")
    Movie movie;

    @ManyToOne
    @JoinColumn(name = "movie_list_id")
    MovieList movieList;
}
