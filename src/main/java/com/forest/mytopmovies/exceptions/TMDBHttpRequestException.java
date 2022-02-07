package com.forest.mytopmovies.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class TMDBHttpRequestException extends RuntimeException {

    public TMDBHttpRequestException(String message) {
        super(message);
    }
}
