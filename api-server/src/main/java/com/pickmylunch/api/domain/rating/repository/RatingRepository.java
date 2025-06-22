package com.pickmylunch.api.domain.rating.repository;

import com.pickmylunch.common.entity.Rating;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RatingRepository extends JpaRepository<Rating, Long> {

    @Query("""
            select r from Rating r
            where 
              r.restaurant.id = :restaurantId
            and
              r.memberId = :memberId
        """)
    Optional<Rating> findByRestaurantIdAndMemberId(@Param("restaurantId") Long restaurantId,
        @Param("memberId") Long memberId);
}
