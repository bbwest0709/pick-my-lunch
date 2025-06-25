package com.pickmylunch.batch.pipeline.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.text.MessageFormat;
import java.util.concurrent.TimeoutException;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApiFetcher {

    private final WebClient.Builder webClient;
    private final ApiProperties apiProperties;

    @Retryable(value = {WebClientResponseException.class, TimeoutException.class}, maxAttempts = 3, backoff = @Backoff(delay = 2000, multiplier = 2))
    public Mono<String> fetchData(int start, int end) {
        return webClient
                .baseUrl(apiProperties.getBaseUrl())
                .build()
                .get()
                .uri(buildUri(start, end))
                .retrieve()
                .bodyToMono(String.class)
                .doOnError(e -> log.error("API fetch error: {}", e.getMessage()));
    }

    private String buildUri(int start, int end) {
        return String.format("%s/%s/%s/%d/%d",
                apiProperties.getKey(),
                apiProperties.getFormatType(),
                apiProperties.getServiceName(),
                start,
                end);
    }
}
