package com.pickmylunch.api.domain.restaurant.cache;

import com.pickmylunch.api.domain.restaurant.dto.response.RestaurantDetailResponseDto;
import com.pickmylunch.api.domain.restaurant.service.RestaurantService;
import com.pickmylunch.api.global.redis.RedisKeyGenerator;
import com.pickmylunch.api.global.redis.RedisRepository;
import com.pickmylunch.api.global.security.utils.ObjectMapperUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ViewCountService {

    private final RedisRepository redis;
    private final ObjectMapperUtils objectMapper;
    private final RestaurantService restaurantService;

    private static final long HOURLY_THRESHOLD  = 10;
    private static final long TOTAL_THRESHOLD = 1000;

    public boolean shouldCacheByHourlyCount(String restaurantId) {
        long hourlyViewCount = redis.getViewCount(restaurantId);
        return hourlyViewCount >= HOURLY_THRESHOLD;
    }

    public RestaurantDetailResponseDto getCachedRestaurantDetail(String restaurantId) {
        String cachedValue = redis.findByKey(RedisKeyGenerator.generateRestaurantDetailKey(restaurantId));
        if(cachedValue == null) return null;
        return objectMapper.toEntity(cachedValue, RestaurantDetailResponseDto.class);
    }

    public void refreshCacheByHourlyCount(String restaurantId) {
        if (shouldCacheByHourlyCount(restaurantId)) {
            cacheRestaurantDetail(restaurantId);
        }
    }

    public void refreshCacheByTotalCount() {
        List<String> restaurantIds = restaurantService.getRestaurantIdsByTotalViewCount(TOTAL_THRESHOLD);
        restaurantIds.forEach(this::cacheRestaurantDetail);
    }

    private void cacheRestaurantDetail(String restaurantId) {
        RestaurantDetailResponseDto latestDetail = restaurantService.getRestaurantById(restaurantId);
        String cachedValue = objectMapper.toStringValue(latestDetail);
        redis.save(RedisKeyGenerator.generateRestaurantDetailKey(restaurantId), cachedValue, 60);
    }
}
