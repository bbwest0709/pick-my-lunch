package com.pickmylunch.api.domain.region.repository;

import com.pickmylunch.api.domain.region.repository.projection.RegionKeyView;
import com.pickmylunch.common.entity.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.*;

public interface RegionRepository extends JpaRepository<Region, Long> {
    @Query("SELECT r.dosi, r.sigungu FROM Region r")
    List<RegionKeyView> findAllDosiAndSigungu();
}
