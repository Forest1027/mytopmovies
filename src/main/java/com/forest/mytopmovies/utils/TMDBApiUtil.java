package com.forest.mytopmovies.utils;

import com.forest.mytopmovies.constants.TMDBConstants;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriUtils;

import java.net.http.HttpResponse;

@Component
public class TMDBApiUtil {

    private final TMDBConstants tmdbConstants;

    public TMDBApiUtil(TMDBConstants tmdbConstants) {
        this.tmdbConstants = tmdbConstants;
    }

    public HttpResponse<String> searchMovies(String movieName, int page) throws Exception {
        String uri = tmdbConstants.baseUrl + tmdbConstants.searchMovieAPI + "?api_key=" + tmdbConstants.tmdbKey + "&page=" + page + "&query=" + UriUtils.encode(movieName, "UTF-8");
        return HttpUtil.get(uri);
    }

}
