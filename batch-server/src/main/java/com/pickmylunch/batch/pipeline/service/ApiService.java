package com.pickmylunch.batch.pipeline.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.pickmylunch.batch.pipeline.dto.AddressDto;
import com.pickmylunch.batch.pipeline.repository.*;
import com.pickmylunch.common.entity.*;
import com.pickmylunch.common.entity.enums.*;
import com.pickmylunch.common.util.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.*;
import reactor.core.publisher.Mono;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApiService {

    private final ApiFetcher apiFetcher;
    private final RawRestaurantProcessor rawRestaurantProcessor;
    private final RestaurantProcessor restaurantProcessor;

    @Value("${api.page-size}")
    private int pageSize;

    @Value("${api.max-index}")
    private int maxIndex;

    @Value("${api.service-name}")
    private String serviceName;

    public void executeRawDataLoad() {
        for (int i = 1; i <= maxIndex; i += pageSize) {
            int start = i;
            int end = Math.min(i + pageSize - 1, maxIndex);

            log.info("[start] 데이터 호출 시작: {} ~ {}", start, end);

            try {
                String response = apiFetcher.fetchData(start, end).block();
                if (response != null) {
                    rawRestaurantProcessor.processAndSave(response, serviceName);
                }
            } catch (Exception e) {
                log.error("[fail] 데이터 처리 실패 {}", e.getMessage(), e);
            }
        }
    }

    public void processRawToRestaurant() {
        restaurantProcessor.processRawToRestaurant();
    }
}