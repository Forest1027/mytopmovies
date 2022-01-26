package com.forest.mytopmovies.datamodels.params.user;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public record UserLoginParam(
        @NotNull(message = "Username cannot be empty") @NotBlank(message = "Username cannot be empty") String username,
        @NotNull(message = "Password cannot be empty") @NotBlank(message = "Password cannot be empty") String password) {
}
