package com.pickmylunch.batch.pipeline.step;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pickmylunch.batch.pipeline.service.*;
import com.pickmylunch.common.entity.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class RawRestaurantItemReader implements ItemReader<RawRestaurant> {

    private final ApiFetcher apiFetcher;
    private final RawRestaurantProcessor rawRestaurantProcessor;
    private final ApiProperties apiProperties;

    private int start = 1;
    private int totalCount = -1;
    private Iterator<RawRestaurant> rawRestaurantIterator = Collections.emptyIterator();

    @Override
    public RawRestaurant read() {
        if (!rawRestaurantIterator.hasNext()) {
            if (totalCount != -1 && start > totalCount) {
                log.info("[end] API 데이터를 모두 읽었습니다.");
                return null;
            }

            try {
                if(totalCount == -1) {
                    String response = apiFetcher.fetchData(1, 1).block();
                    totalCount = parseTotalCount(response);
                    log.info("[info] 총 데이터 개수: {}", totalCount);
                }

                int end = Math.min(start + apiProperties.getPageSize() - 1, totalCount);
                log.info("[start] 데이터 호출 시작: {} ~ {}", start, end);
                String response = apiFetcher.fetchData(start, end).block();

                if (response != null) {
                    List<RawRestaurant> rawList = rawRestaurantProcessor.parseOnly(response, apiProperties.getServiceName());
                    rawRestaurantIterator = rawList.iterator();
                } else {
                    rawRestaurantIterator = Collections.emptyIterator();
                }
                start += apiProperties.getPageSize();

            } catch (Exception e) {
                log.error("[fail] API 호출 실패: {}", e.getMessage());
            }
        }
        return rawRestaurantIterator.hasNext() ? rawRestaurantIterator.next() : null;
    }

    private int parseTotalCount(String response) throws Exception {
        JsonNode root = new ObjectMapper().readTree(response);
        return root.path(apiProperties.getServiceName()).path("list_total_count").asInt();
    }
}
