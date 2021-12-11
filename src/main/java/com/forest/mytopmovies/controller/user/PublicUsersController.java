package com.forest.mytopmovies.controller.user;

import com.forest.mytopmovies.entity.User;
import com.forest.mytopmovies.service.user.UserAuthService;
import com.forest.mytopmovies.service.user.UserCrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public/users")
public class PublicUsersController {
    @Autowired
    private UserAuthService authService;

    @Autowired
    private UserCrudService userCrudService;

    @GetMapping("/")
    public String main() {
        return "Hello Spring";
    }

    @PostMapping("/register")
    public String register(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            @RequestParam(value = "email", required = false) String email) {
        userCrudService.save(
                User.builder()
                        .withUsername(username)
                        .withPassword(password)
                        .withEmail(email)
                        .build()
        );
        return login(username, password);
    }

    @PostMapping("/login")
    public String login(
            @RequestParam("username") String username,
            @RequestParam("password") String password) {
        return authService.login(username, password).orElseThrow(() -> new RuntimeException("Invalid login and/or password"));
    }
}
