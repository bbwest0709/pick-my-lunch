package com.pickmylunch.batch.pipeline.service;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "api")
public class ApiProperties {
    private String key;
    private String baseUrl;
    private String serviceName;
    private String formatType;
    private int pageSize;
    private int maxIndex;
}
