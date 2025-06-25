package com.pickmylunch.api.domain.restaurant.service;

import com.pickmylunch.api.domain.restaurant.dto.response.*;
import com.pickmylunch.api.domain.restaurant.repository.RestaurantRepository;
import com.pickmylunch.api.global.exception.BusinessLogicException;
import com.pickmylunch.api.global.exception.code.RestaurantExceptionCode;
import com.pickmylunch.common.entity.*;
import com.pickmylunch.common.util.GeometryUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Point;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LocationService {
    private static final String RESTAURANT_SORT_OPTION_RATING = "rating";
    private static final String RESTAURANT_SORT_OPTION_DISTANCE = "distance";

    private final GeometryUtil geometryUtil;
    private final RestaurantRepository restaurantRepository;

    public Page<RestaurantResponseDto> getRestaurantsWithinRange(double lat, double lon, double range, String sort, int page, int size) {
        Point location = geometryUtil.createPoint(lon, lat);
        double distanceInMeters = range * 1000;

        Pageable pageable = PageRequest.of(page, size);
        Page<Restaurant> restaurantPage;

        if (RESTAURANT_SORT_OPTION_RATING.equalsIgnoreCase(sort)) {
            restaurantPage = restaurantRepository.findRestaurantsWithinRangeByRating(location, distanceInMeters, pageable);
        } else if (RESTAURANT_SORT_OPTION_DISTANCE.equalsIgnoreCase(sort)) {
            restaurantPage = restaurantRepository.findRestaurantsWithinRangeByDistance(location, distanceInMeters, pageable);
        } else {
            throw new BusinessLogicException(RestaurantExceptionCode.INVALID_SORT_TYPE);
        }
        return restaurantPage.map(RestaurantResponseDto::of);
    }
}