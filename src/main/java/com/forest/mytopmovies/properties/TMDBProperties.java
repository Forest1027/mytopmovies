package com.forest.mytopmovies.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("constants.tmdb")
@Data
public class TMDBProperties {

    public String baseUrl;

    public String tmdbKey;

    public String searchMovieAPI;

    public String searchMovieByIdAPI;

}
