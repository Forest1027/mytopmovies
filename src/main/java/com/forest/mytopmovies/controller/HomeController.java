package com.forest.mytopmovies.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
@AllArgsConstructor
public class HomeController {
    @GetMapping("/")
    public String index() {
        return "Welcome to MTM Rest API";
    }
}
