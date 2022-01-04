package com.forest.mytopmovies.exceptions;

public class UserExistsException extends RuntimeException {
    public UserExistsException(String username) {
        super("User with username " + username + " already exists. Please use a different username to register.");
    }

}
