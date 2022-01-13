package com.forest.mytopmovies.utils;

import com.forest.mytopmovies.aop.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HttpUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingHandler.class);

    private HttpUtil() {
    }

    public static HttpResponse<String> get(String uri) throws Exception {
        HttpClient client = HttpClient.newBuilder().build();
        LOGGER.debug(String.format("Sending GET request: %s", uri));
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uri))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        LOGGER.debug(String.format("Response: %s", response));
        return response;
    }
}
