package com.pickmylunch.api.domain.restaurant.service;

import com.pickmylunch.api.domain.restaurant.dto.response.RestaurantDetailResponseDto;
import com.pickmylunch.api.domain.restaurant.dto.response.RestaurantResponseDto;
import com.pickmylunch.api.domain.restaurant.repository.RestaurantRepository;
import com.pickmylunch.api.global.exception.BusinessLogicException;
import com.pickmylunch.api.global.exception.code.RestaurantExceptionCode;
import com.pickmylunch.common.entity.Restaurant;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;

    @Transactional(readOnly = true)
    public Page<RestaurantResponseDto> getAllRestaurants(Pageable pageable) {
        return restaurantRepository.findAll(pageable)
                .map(RestaurantResponseDto::of);
    }

    @Transactional(readOnly = true)
    public RestaurantDetailResponseDto getRestaurantById(String id) {
        Restaurant restaurant = findById(id);
        return RestaurantDetailResponseDto.of(restaurant);
    }

    private Restaurant findById(String id) {
        return restaurantRepository.findById(id).orElseThrow(() -> new BusinessLogicException(RestaurantExceptionCode.RESTAURANT_NOT_FOUND));
    }
}