package com.forest.mytopmovies.constants;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("constants.tmdb")
public class TMDBConstants {

    public String baseUrl;

    public String tmdbKey;

    public String searchMovieAPI;

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getTmdbKey() {
        return tmdbKey;
    }

    public void setTmdbKey(String tmdbKey) {
        this.tmdbKey = tmdbKey;
    }

    public String getSearchMovieAPI() {
        return searchMovieAPI;
    }

    public void setSearchMovieAPI(String searchMovieAPI) {
        this.searchMovieAPI = searchMovieAPI;
    }
}
