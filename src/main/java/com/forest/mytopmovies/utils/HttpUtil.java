package com.forest.mytopmovies.utils;

import com.forest.mytopmovies.exceptions.TMDBHttpRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.function.Supplier;

public class HttpUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpUtil.class);

    private HttpUtil() {
    }

    public static String get(String baseUrl, String uri) throws TMDBHttpRequestException {
        WebClient client = buildWebClient(baseUrl);
        WebClient.RequestBodySpec bodySpec = buildBodySpec(client, uri);
        return bodySpec.exchangeToMono(clientResponse -> {
            if (clientResponse.statusCode().equals(HttpStatus.OK)) {
                return clientResponse.bodyToMono(String.class);
            } else if(clientResponse.statusCode().equals(HttpStatus.NOT_FOUND)){
                return clientResponse.createException().flatMap(error -> Mono.error(() -> {
                    LOGGER.error(error.getMessage());
                    return new TMDBHttpRequestException("Movie is not found. Please double check with search api \"/api/v1/search/movies\"");
                }));
            }else {
                return clientResponse.createException().flatMap(error -> Mono.error(() -> {
                    LOGGER.error(error.getMessage());
                    return new TMDBHttpRequestException("Failure when execute request to TMDB API");
                }));
            }
        }).block();
    }

    static WebClient buildWebClient(String baseUrl) {
        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory(baseUrl);
        factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.NONE);
        return WebClient.builder().uriBuilderFactory(factory)
                .baseUrl(baseUrl)
                .filters(filterFunctions -> {
                    filterFunctions.add(logRequest());
                    filterFunctions.add(logResponse());
                }).build();
    }

    static WebClient.RequestBodySpec buildBodySpec(WebClient client, String uri) {
        return client.method(HttpMethod.GET)
                .uri(uri).header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON, MediaType.TEXT_HTML, MediaType.APPLICATION_XHTML_XML)
                .acceptCharset(StandardCharsets.UTF_8);
    }

    static ExchangeFilterFunction logRequest() {
        return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
            LOGGER.debug("Sending GET request: {}", clientRequest.url());
            return Mono.just(clientRequest);
        });
    }

    static ExchangeFilterFunction logResponse() {
        return ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
            LOGGER.debug("Response Status Code: {}", clientResponse.statusCode());
            return Mono.just(clientResponse);
        });
    }

    static Mono<Object> notFoundErrorSupplier(Exception error) {
        return Mono.error(() -> {
            LOGGER.error(error.getMessage());
            return new TMDBHttpRequestException("Failure when execute request to TMDB API");
        });
    }

}
