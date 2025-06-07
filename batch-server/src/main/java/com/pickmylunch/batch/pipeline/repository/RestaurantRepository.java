package com.pickmylunch.batch.pipeline.repository;

import com.pickmylunch.common.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantRepository extends JpaRepository<Restaurant, String> {
}
