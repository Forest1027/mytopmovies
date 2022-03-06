package com.forest.mytopmovies.controller.user;

import com.forest.mytopmovies.datamodels.entity.User;
import com.forest.mytopmovies.datamodels.params.user.UserCreateParam;
import com.forest.mytopmovies.datamodels.params.user.UserLoginParam;
import com.forest.mytopmovies.service.user.UserAuthService;
import com.forest.mytopmovies.service.user.UserCrudService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/users")
@AllArgsConstructor
public class UserController {
    private final UserAuthService authService;

    private final UserCrudService userCrudService;

    private final PasswordEncoder passwordEncoder;


    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody UserCreateParam user) {
        userCrudService.save(
                User.builder()
                        .username(user.username())
                        .password(passwordEncoder.encode(user.password()))
                        .email(user.email())
                        .active(true)
                        .build()
        );
        return login(new UserLoginParam(user.username(), user.password()));
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody UserLoginParam user) {
        return authService.login(user.username(), user.password())
                .map(token -> new ResponseEntity<>(token, HttpStatus.OK))
                .orElseThrow(() -> new AccessDeniedException("Invalid login and/or password"));
    }
}
