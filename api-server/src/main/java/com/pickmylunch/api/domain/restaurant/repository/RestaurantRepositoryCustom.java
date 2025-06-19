package com.pickmylunch.api.domain.restaurant.repository;

import com.pickmylunch.common.entity.Restaurant;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface RestaurantRepositoryCustom {
    Page<Restaurant> findRestaurantsBySigungu(Pageable pageable, String sigungu);

    @Modifying
    @Transactional
    @Query("UPDATE Restaurant r SET r.totalViewCount = r.totalViewCount + :viewCount WHERE r.id = :restaurantId")
    void incrementTotalViewCount(@Param("restaurantId") String restaurantId, @Param("viewCount") Long viewCount);
}
