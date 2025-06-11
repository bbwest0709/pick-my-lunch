package com.pickmylunch.batch.pipeline.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.concurrent.TimeoutException;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApiFetcher {

    private final WebClient.Builder webClient;

    @Value("${api.key}")
    private String apiKey;

    @Value("${api.base-url}")
    private String baseUrl;

    @Value("${api.service-name}")
    private String serviceName;

    @Value("${api.format-type}")
    private String formatType;

    @Retryable(value = {WebClientResponseException.class, TimeoutException.class}, maxAttempts = 3, backoff = @Backoff(delay = 2000, multiplier = 2))
    public Mono<String> fetchData(int start, int end) {
        return webClient
                .baseUrl(baseUrl)
                .build()
                .get()
                .uri(buildUri(start, end))
                .retrieve()
                .bodyToMono(String.class)
                .doOnError(e -> log.error("API fetch error: {}", e.getMessage()));
    }

    private String buildUri(int start, int end) {
        return String.format("%s/%s/%s/%d/%d", apiKey, formatType, serviceName, start, end);
    }
}
