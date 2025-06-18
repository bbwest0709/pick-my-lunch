package com.pickmylunch.api.domain.restaurant.repository;

import com.pickmylunch.common.entity.Restaurant;
import org.springframework.data.domain.*;

public interface RestaurantRepositoryCustom {
    Page<Restaurant> findAllWithPagination(Pageable pageable);
}
