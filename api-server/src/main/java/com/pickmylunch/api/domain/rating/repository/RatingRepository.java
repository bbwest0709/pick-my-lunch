package com.pickmylunch.api.domain.rating.repository;

import com.pickmylunch.common.entity.Rating;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RatingRepository extends JpaRepository<Rating, Long> {

    Optional<Rating> findByRestaurantIdAndMemberId(String restaurantId, Long memberId);
}
