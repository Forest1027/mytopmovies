package com.forest.mytopmovies.controller.movie;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.forest.mytopmovies.datamodels.entity.User;
import com.forest.mytopmovies.datamodels.params.movie.MovieListMovieUpdateParam;
import com.forest.mytopmovies.datamodels.params.movie.MovieListParam;
import com.forest.mytopmovies.datamodels.params.movie.MovieListUpdateParam;
import com.forest.mytopmovies.datamodels.pojos.MovieListPojo;
import com.forest.mytopmovies.datamodels.pojos.PagePojo;
import com.forest.mytopmovies.service.movie.MovieListService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/protected/movielist")
@AllArgsConstructor
public class ProtectedMovieListController {

    private final MovieListService movieListService;

    @PostMapping
    @Operation(security = {@SecurityRequirement(name = "Authorization-Token")})
    public ResponseEntity<MovieListPojo> createMovieList(@RequestBody MovieListParam movieListParam, Authentication authentication) throws JsonProcessingException {
        User user = (User) authentication.getPrincipal();
        return new ResponseEntity<>(movieListService.createMovieList(movieListParam, user), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(security = {@SecurityRequirement(name = "Authorization-Token")})
    public ResponseEntity<String> deleteMovieList(@PathVariable int id, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return new ResponseEntity<>(movieListService.deleteMovieList(id, user), HttpStatus.OK);
    }

    @PutMapping
    @Operation(security = {@SecurityRequirement(name = "Authorization-Token")})
    public ResponseEntity<MovieListPojo> updateMovieList(@RequestBody MovieListUpdateParam movieListParam, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return new ResponseEntity<>(movieListService.updateMovieList(movieListParam, user), HttpStatus.OK);
    }

    @GetMapping
    @Operation(security = {@SecurityRequirement(name = "Authorization-Token")})
    public ResponseEntity<PagePojo<MovieListPojo>> getMovieList(@RequestParam(required = false) String name, @RequestParam(required = false) Integer page, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return new ResponseEntity<>(movieListService.getMovieLists(name, page, user), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(security = {@SecurityRequirement(name = "Authorization-Token")})
    public ResponseEntity<MovieListPojo> getMovieListById(@PathVariable int id, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return new ResponseEntity<>(movieListService.getMovieListsByUserAndId(id, user), HttpStatus.OK);
    }

    @PostMapping("/movies")
    @Operation(security = {@SecurityRequirement(name = "Authorization-Token")})
    public ResponseEntity<MovieListPojo> addToMovieList(@RequestBody MovieListMovieUpdateParam movieListParam, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return new ResponseEntity<>(movieListService.addMoviesToList(movieListParam, user), HttpStatus.OK);
    }

    @DeleteMapping("/movies")
    @Operation(security = {@SecurityRequirement(name = "Authorization-Token")})
    public ResponseEntity<MovieListPojo> deleteFromMovieList(@RequestBody MovieListMovieUpdateParam movieListParam, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return new ResponseEntity<>(movieListService.deleteFromMovieList(movieListParam, user), HttpStatus.OK);
    }
}
