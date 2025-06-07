package com.pickmylunch.batch.pipeline.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${spring.webclient.max-in-memory-size:10MB}")
    private int maxInMemorySize;

    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder()
                .codecs(configurer ->
                        configurer.defaultCodecs()
                                .maxInMemorySize(maxInMemorySize));
    }

}
