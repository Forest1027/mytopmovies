package com.forest.mytopmovies.utils;

import com.forest.mytopmovies.aop.LoggingHandler;
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

public class HttpUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingHandler.class);

    private HttpUtil() {
    }

    public static String get(String baseUrl, String uri) throws Exception {
        WebClient client = buildWebClient(baseUrl);
        WebClient.RequestBodySpec bodySpec = buildBodySpec(client, uri);
        return bodySpec.exchangeToMono(clientResponse -> {
            if (clientResponse.statusCode().equals(HttpStatus.OK)) {
                return clientResponse.bodyToMono(String.class);
            } else {
                return clientResponse.createException().flatMap(error -> Mono.error(() -> new TMDBHttpRequestException(error.getMessage())));
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
            LOGGER.debug(String.format("Sending GET request: %s", clientRequest.url()));
            return Mono.just(clientRequest);
        });
    }

    static ExchangeFilterFunction logResponse() {
        return ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
            LOGGER.debug(String.format("Response Status Code: %s", clientResponse.statusCode()));
            return Mono.just(clientResponse);
        });
    }

}
