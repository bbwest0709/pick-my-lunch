package com.pickmylunch.batch.pipeline.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

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
        return String.format("%s/%s/%s/%s/%d/%d", baseUrl, apiKey, formatType, serviceName, start, end);
    }
}
