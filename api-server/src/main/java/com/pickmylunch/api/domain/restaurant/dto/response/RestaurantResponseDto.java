package com.pickmylunch.api.domain.restaurant.dto.response;

import com.pickmylunch.common.entity.Restaurant;
import com.pickmylunch.common.entity.enums.Category;

public record RestaurantResponseDto(
        String id,
        String restaurantName,
        Category category,
        double lon,
        double lat,
        double ratingAverage) {

    public static RestaurantResponseDto of(Restaurant restaurant) {
        return new RestaurantResponseDto(
                restaurant.getId(),
                restaurant.getRestaurantName(),
                restaurant.getCategory(),
                restaurant.getLocation().getX(),
                restaurant.getLocation().getY(),
                restaurant.getRatingAverage()
        );
    }
}
