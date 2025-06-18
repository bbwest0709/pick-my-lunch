package com.pickmylunch.api.domain.restaurant.controller;

import com.pickmylunch.api.domain.restaurant.dto.response.RestaurantDetailResponseDto;
import com.pickmylunch.api.domain.restaurant.dto.response.RestaurantResponseDto;
import com.pickmylunch.api.domain.restaurant.repository.RestaurantRepository;
import com.pickmylunch.api.domain.restaurant.service.RestaurantService;
import com.pickmylunch.common.entity.Restaurant;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/restaurant")
public class RestaurantController {

    private final RestaurantService restaurantService;

    @GetMapping
    public ResponseEntity<Page<RestaurantResponseDto>> getAllRestaurant(Pageable pageable) {
        return ResponseEntity.ok(restaurantService.getAllRestaurants(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestaurantDetailResponseDto> getRestaurantById(@PathVariable String id) {
        return ResponseEntity.ok(restaurantService.getRestaurantById(id));
    }
}
