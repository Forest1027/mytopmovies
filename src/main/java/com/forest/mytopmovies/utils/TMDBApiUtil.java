package com.forest.mytopmovies.utils;

import com.forest.mytopmovies.constants.TMDBConstants;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriUtils;

@Component
public class TMDBApiUtil {

    private final TMDBConstants tmdbConstants;

    public TMDBApiUtil(TMDBConstants tmdbConstants) {
        this.tmdbConstants = tmdbConstants;
    }

    public String searchMovies(String movieName, int page) throws Exception {
        String uri = tmdbConstants.searchMovieAPI + "?api_key=" + tmdbConstants.tmdbKey + "&page=" + page + "&query=" + UriUtils.encode(movieName, "UTF-8");
        return HttpUtil.get(tmdbConstants.baseUrl, uri);
    }

}
