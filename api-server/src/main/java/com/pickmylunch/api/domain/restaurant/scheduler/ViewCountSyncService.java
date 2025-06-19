package com.pickmylunch.api.domain.restaurant.scheduler;

import com.pickmylunch.api.domain.restaurant.repository.RestaurantRepository;
import com.pickmylunch.api.global.redis.RedisRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class ViewCountSyncService {

    private final RedisRepository redis;
    private final RestaurantRepository restaurantRepository;

    @Scheduled(cron = "0 0 * * * *")
    public void syncAllViewCountsToDb() {
        Set<String> keys = redis.getAllRestaurantViewKeys();
        for (String key : keys) {
            String restaurantId = key.split(":")[1];
            Long viewCount = redis.getViewCount(restaurantId);

            if (viewCount > 0) {
                restaurantRepository.incrementTotalViewCount(restaurantId, viewCount);

                redis.resetViewCount(restaurantId);
            }
        }
    }
}