package com.pickmylunch.api.domain.restaurant.dto.response;

import com.pickmylunch.common.entity.Restaurant;
import com.pickmylunch.common.entity.enums.Category;

public record RestaurantResponseDto(
        String id,
        String restaurantName,
        Category category,
        String jibunAddress,
        String doroAddress,
        double lon,
        double lat,
        double ratingAverage,
        String restaurantTel) {

    public static RestaurantResponseDto of(Restaurant restaurant) {
        return new RestaurantResponseDto(
                restaurant.getId(),
                restaurant.getRestaurantName(),
                restaurant.getCategory(),
                restaurant.getJibunDetailAddress(),
                restaurant.getDoroDetailAddress(),
                restaurant.getLocation().getX(),
                restaurant.getLocation().getY(),
                restaurant.getRatingAverage(),
                restaurant.getRestaurantTel()
        );
    }
}
