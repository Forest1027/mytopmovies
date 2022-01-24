package com.forest.mytopmovies.constants;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("constants.tmdb")
@Getter
@Setter
public class TMDBConstants {

    public String baseUrl;

    public String tmdbKey;

    public String searchMovieAPI;

    public String searchMovieByIdAPI;

}
