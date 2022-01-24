package com.forest.mytopmovies.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "mtm_genres")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Genre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "genre_name")
    private String genreName;

    @Column(name = "tmdb_id")
    private int tmdbId;

}
