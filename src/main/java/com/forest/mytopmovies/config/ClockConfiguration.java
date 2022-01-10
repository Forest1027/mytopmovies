package com.forest.mytopmovies.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

@Configuration
public class ClockConfiguration {

    @Profile(value = "!test")
    @Bean
    public Clock clock() {
        return Clock.systemDefaultZone();
    }

    @Profile(value = "test")
    @Bean
    public Clock testClock() {
        return Clock.fixed(LocalDateTime.of(2021, 12, 14, 21, 0).toInstant(ZoneOffset.UTC), ZoneId.systemDefault());
    }
}
