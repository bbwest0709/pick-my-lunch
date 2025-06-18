package com.pickmylunch.api.domain.restaurant.repository;

import com.pickmylunch.common.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantRepository extends JpaRepository<Restaurant, String>, RestaurantRepositoryCustom {
}
