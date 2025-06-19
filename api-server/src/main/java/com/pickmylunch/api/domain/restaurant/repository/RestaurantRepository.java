package com.pickmylunch.api.domain.restaurant.repository;

import com.pickmylunch.common.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface RestaurantRepository extends JpaRepository<Restaurant, String>, RestaurantRepositoryCustom {

    @Modifying
    @Transactional
    @Query("UPDATE Restaurant r SET r.totalViewCount = r.totalViewCount + :viewCount WHERE r.id = :restaurantId")
    void incrementTotalViewCount(@Param("restaurantId") String restaurantId, @Param("viewCount") Long viewCount);

}
