package com.forest.mytopmovies.controller.movie;

import com.forest.mytopmovies.entity.Movie;

import com.forest.mytopmovies.pojos.PagePojo;
import com.forest.mytopmovies.service.movie.MovieService;
import org.hibernate.validator.constraints.Range;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/public/movies")
public class PublicMovieController {

    private MovieService movieService;

    public PublicMovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping
    public ResponseEntity<PagePojo<Movie>> searchMovieByName(@RequestParam String movieName, @RequestParam(required = false) @Valid @Range(min = 1) Integer page) throws Exception {
        return new ResponseEntity<>(movieService.searchTMDBMovieByName(movieName, page == null ? 1 : page), HttpStatus.OK);
    }

}
