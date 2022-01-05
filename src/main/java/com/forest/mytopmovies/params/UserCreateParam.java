package com.forest.mytopmovies.params;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public record UserCreateParam(
        @NotNull(message = "Username cannot be empty") @NotBlank(message = "Username cannot be empty") String username,
        @NotNull(message = "Password cannot be empty") @NotBlank(message = "Password cannot be empty") String password,
        @Email(message = "Email format needs to be followed") String email) {
}
