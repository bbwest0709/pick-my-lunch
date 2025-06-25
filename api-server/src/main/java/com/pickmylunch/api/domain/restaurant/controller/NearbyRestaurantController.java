package com.pickmylunch.api.domain.restaurant.controller;

import com.pickmylunch.api.domain.restaurant.service.LocationService;
import com.pickmylunch.api.domain.restaurant.dto.response.RestaurantResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/restaurants/nearby")
public class NearbyRestaurantController {

    private final LocationService locationService;

    @GetMapping
    public ResponseEntity<Page<RestaurantResponseDto>> findRestaurantsWithinRange(
            @RequestParam double lat,
            @RequestParam double lon,
            @RequestParam(defaultValue = "1") double range,
            @RequestParam(defaultValue = "distance") String sort,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<RestaurantResponseDto> result = locationService.getRestaurantsWithinRange(lat, lon, range, sort, page, size);
        return ResponseEntity.ok(result);
    }
}
