package com.forest.mytopmovies.controller.movie;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.forest.mytopmovies.datamodels.entity.User;
import com.forest.mytopmovies.datamodels.pojos.MovieListPojo;
import com.forest.mytopmovies.datamodels.pojos.MoviePojo;
import com.forest.mytopmovies.datamodels.pojos.PagePojo;
import com.forest.mytopmovies.exceptions.TMDBHttpRequestException;
import com.forest.mytopmovies.service.movie.MovieListService;
import com.forest.mytopmovies.service.movie.MovieService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import org.hibernate.validator.constraints.Range;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/search")
@AllArgsConstructor
public class SearchController {

    private final MovieService movieService;

    private final MovieListService movieListService;


    @GetMapping("/movies")
    public ResponseEntity<PagePojo<MoviePojo>> searchMovieByName(@RequestParam String query, @RequestParam(required = false) @Valid @Range(min = 1) Integer page) throws TMDBHttpRequestException, JsonProcessingException {
        return new ResponseEntity<>(movieService.searchTMDBMovieByName(query, page == null ? 1 : page), HttpStatus.OK);
    }

    @GetMapping("/movielists")
    @Operation(security = {@SecurityRequirement(name = "Authorization-Token")})
    public ResponseEntity<PagePojo<MovieListPojo>> getMovieList(@RequestParam String query, @RequestParam(required = false) Integer page, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return new ResponseEntity<>(movieListService.getMovieLists(query, page, user), HttpStatus.OK);
    }
}
