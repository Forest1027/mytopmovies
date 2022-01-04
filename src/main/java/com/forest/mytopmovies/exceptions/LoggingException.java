package com.forest.mytopmovies.exceptions;

public class LoggingException extends RuntimeException{

    public LoggingException() {
    }

    public LoggingException(String message) {
        super(message);
    }
}
