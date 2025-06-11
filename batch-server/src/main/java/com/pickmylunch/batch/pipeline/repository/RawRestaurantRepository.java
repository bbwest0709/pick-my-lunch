package com.pickmylunch.batch.pipeline.repository;

import com.pickmylunch.common.entity.RawRestaurant;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

public interface RawRestaurantRepository extends JpaRepository<RawRestaurant, String> {

    @Modifying
    @Transactional
    @Query("update RawRestaurant r set r.isUpdated = false where r.id in :ids")
    void updateIsUpdatedFalseByIds(@Param("ids") List<String> ids);
}