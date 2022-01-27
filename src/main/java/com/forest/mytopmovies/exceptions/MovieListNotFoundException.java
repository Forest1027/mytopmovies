package com.forest.mytopmovies.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class MovieListNotFoundException extends RuntimeException {
    public MovieListNotFoundException(int id) {
        super("Movie list with id " + id + " is not found.");
    }
}
