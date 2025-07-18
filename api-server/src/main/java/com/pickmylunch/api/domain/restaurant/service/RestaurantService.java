package com.pickmylunch.api.domain.restaurant.service;

import com.pickmylunch.api.domain.restaurant.dto.response.*;
import com.pickmylunch.api.domain.restaurant.repository.*;
import com.pickmylunch.api.global.exception.BusinessLogicException;
import com.pickmylunch.api.global.exception.code.RestaurantExceptionCode;
import com.pickmylunch.api.global.redis.RedisRepository;
import com.pickmylunch.common.entity.*;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final RedisRepository redis;

    public Page<RestaurantResponseDto> getAllRestaurants(Pageable pageable) {
        return restaurantRepository.findAll(pageable)
                .map(RestaurantResponseDto::of);
    }

    @Cacheable(value="restaurant_details", key="#id")
    public RestaurantDetailResponseDto getRestaurantById(String id) {
        Restaurant restaurant = findById(id);
        incrementViewCount(id);
        return RestaurantDetailResponseDto.of(restaurant);
    }

    public Page<RestaurantResponseDto> getRestaurantsBySigungu(Pageable pageable, String sigungu, String sort, boolean ascending) {
        return restaurantRepository.findRestaurantsBySigungu(pageable, sigungu, sort, ascending)
                .map(RestaurantResponseDto::of);
    }

    public List<String> getRestaurantIdsByTotalViewCount(long threshold) {
        return restaurantRepository.findIdsByTotalViewCountGreaterThanEqual(threshold);
    }

    private Restaurant findById(String id) {
        return restaurantRepository.findById(id).orElseThrow(() -> new BusinessLogicException(RestaurantExceptionCode.RESTAURANT_NOT_FOUND));
    }

    private void incrementViewCount(String id) {
        redis.incrementViewCount(id);
    }

}