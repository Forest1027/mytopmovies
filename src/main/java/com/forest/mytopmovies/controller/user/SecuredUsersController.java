package com.forest.mytopmovies.controller.user;

import com.forest.mytopmovies.entity.User;
import com.forest.mytopmovies.service.user.UserAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class SecuredUsersController {
    @Autowired
    private UserAuthService authService;

    @GetMapping("/current")
    public User getCurrent(@AuthenticationPrincipal User user) {
        return user;
    }

    @GetMapping("/logout")
    public boolean logout(@AuthenticationPrincipal User user) {
        authService.logout(user);
        return true;
    }

}
