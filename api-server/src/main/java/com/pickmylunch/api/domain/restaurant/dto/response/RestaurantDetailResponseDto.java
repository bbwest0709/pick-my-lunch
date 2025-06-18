package com.pickmylunch.api.domain.restaurant.dto.response;

import com.pickmylunch.common.entity.Restaurant;
import com.pickmylunch.common.entity.enums.Category;
import org.locationtech.jts.geom.Point;

public record RestaurantDetailResponseDto(
        String id,
        String restaurantName,
        String restaurantTel,
        String dosi,
        Category category,
        String sigungu,
        String jibunDetailAddress,
        String doroDetailAddress,
        Point location,
        double ratingAverage,
        long totalViewCount
) {
    public static RestaurantDetailResponseDto of(Restaurant restaurant) {
        return new RestaurantDetailResponseDto(
                restaurant.getId(),
                restaurant.getRestaurantName(),
                restaurant.getRestaurantTel(),
                restaurant.getDosi(),
                restaurant.getCategory(),
                restaurant.getSigungu(),
                restaurant.getJibunDetailAddress(),
                restaurant.getDoroDetailAddress(),
                restaurant.getLocation(),
                restaurant.getRatingAverage(),
                restaurant.getTotalViewCount()
        );
    }
}
