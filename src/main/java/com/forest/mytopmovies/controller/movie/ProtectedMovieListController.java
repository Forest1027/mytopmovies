package com.forest.mytopmovies.controller.movie;

import com.forest.mytopmovies.entity.MovieList;
import com.forest.mytopmovies.params.movie.MovieListParam;
import com.forest.mytopmovies.service.movie.MovieListService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/protected/movielist")
@AllArgsConstructor
public class ProtectedMovieListController {

    private final MovieListService movieListService;

    @PostMapping
    @Operation(security = {@SecurityRequirement(name = "Authorization-Token")})
    public ResponseEntity<MovieList> createMovieList(@RequestBody MovieListParam movieListParam, Authentication authentication) {
        return new ResponseEntity<>(movieListService.createMovieList(movieListParam), HttpStatus.OK);
    }
}
