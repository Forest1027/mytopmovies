package com.forest.mytopmovies.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class TokenExpiredException extends RuntimeException{
    public TokenExpiredException() {
        super("Token has expired. Please log in again.");
    }
}
