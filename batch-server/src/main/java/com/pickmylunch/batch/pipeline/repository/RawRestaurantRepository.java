package com.pickmylunch.batch.pipeline.repository;

import com.pickmylunch.common.entity.RawRestaurant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RawRestaurantRepository extends JpaRepository<RawRestaurant, String> {
}