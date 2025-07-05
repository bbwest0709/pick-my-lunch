package com.pickmylunch.api.domain.restaurant.repository;

import com.pickmylunch.common.entity.Restaurant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import org.locationtech.jts.geom.Point;

import java.util.*;

public interface RestaurantRepository extends JpaRepository<Restaurant, String>, RestaurantRepositoryCustom {

    @Modifying
    @Transactional
    @Query("UPDATE Restaurant r SET r.totalViewCount = r.totalViewCount + :viewCount WHERE r.id = :restaurantId")
    void incrementTotalViewCount(@Param("restaurantId") String restaurantId, @Param("viewCount") Long viewCount);

    // 평점 높은 순 정렬
    @Query("SELECT r FROM Restaurant r " +
            "WHERE ST_Distance(r.location, :location) <= :range " +
            "ORDER BY r.ratingAverage DESC")
    Page<Restaurant> findRestaurantsWithinRangeByRating(
            @Param("location") Point location,
            @Param("range") double range,
            Pageable pageable);

    // 거리 가까운 순 정렬
    @Query("SELECT r FROM Restaurant r " +
            "WHERE ST_Distance(r.location, :location) <= :range " +
            "ORDER BY ST_Distance(r.location, :location) ASC")
    Page<Restaurant> findRestaurantsWithinRangeByDistance(
            @Param("location") Point location,
            @Param("range") double range,
            Pageable pageable);

    // 추천 식당 (랜덤)
    @Query("SELECT r FROM Restaurant r " +
            "WHERE ST_Distance(r.location, :memberLocation) <= 1000 " +
            "AND r.ratingAverage > (" +
            "    SELECT AVG(r2.ratingAverage) FROM Restaurant r2 " +
            "    WHERE ST_Distance(r2.location, :memberLocation) <= 1000" +
            ") " +
            "ORDER BY function('RANDOM')")
    Optional<Restaurant> findRecommendedRestaurantForMember(Point memberLocation);

    @Query("SELECT r.id FROM Restaurant r WHERE r.totalViewCount >= :threshold")
    List<String> findIdsByTotalViewCountGreaterThanEqual(@Param("threshold") long threshold);
}
