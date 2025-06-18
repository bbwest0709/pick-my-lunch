package com.pickmylunch.api.domain.restaurant.controller;

import com.pickmylunch.api.domain.restaurant.dto.response.*;
import com.pickmylunch.api.domain.restaurant.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/restaurant")
public class RestaurantController {

    private final RestaurantService restaurantService;

    @GetMapping
    public ResponseEntity<Page<RestaurantResponseDto>> getAllRestaurant(Pageable pageable) {
        return ResponseEntity.ok(restaurantService.getAllRestaurants(pageable));
    }

    @GetMapping("/search")
    public ResponseEntity<Page<RestaurantResponseDto>> getRestaurantsBySigungu(Pageable pageable, @RequestParam(required = false) String sigungu) {
        return ResponseEntity.ok(restaurantService.getRestaurantsBySigungu(pageable, sigungu));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestaurantDetailResponseDto> getRestaurantById(@PathVariable String id) {
        return ResponseEntity.ok(restaurantService.getRestaurantById(id));
    }
}
