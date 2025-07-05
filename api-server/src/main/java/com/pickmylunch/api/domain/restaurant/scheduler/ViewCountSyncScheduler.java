package com.pickmylunch.api.domain.restaurant.scheduler;

import com.pickmylunch.api.domain.restaurant.cache.ViewCountService;
import com.pickmylunch.api.domain.restaurant.repository.*;
import com.pickmylunch.api.global.redis.RedisRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ViewCountSyncScheduler {

    private final RedisRepository redis;
    private final RestaurantRepository restaurantRepository;
    private final ViewCountService viewCountService;

    @Scheduled(cron = "0 0 * * * *")
    public void syncAllViewCountsToDb() {
        log.info("ViewCountSyncScheduler: Redis 조회수 DB 동기화 시작");
        Set<String> keys = redis.getAllRestaurantViewKeys();
        for (String key : keys) {
            String restaurantId = key.split(":")[1];
            Long viewCount = redis.getViewCount(restaurantId);

            if (viewCount > 0) {
                restaurantRepository.incrementTotalViewCount(restaurantId, viewCount);
                viewCountService.refreshCacheByHourlyCount(restaurantId);
                redis.resetViewCount(restaurantId);
                log.info("ViewCountSyncScheduler: {} 조회수 {}만큼 DB 반영", restaurantId, viewCount);
            }
        }
        log.info("ViewCountSyncScheduler: Redis 조회수 DB 동기화 종료");
    }
}