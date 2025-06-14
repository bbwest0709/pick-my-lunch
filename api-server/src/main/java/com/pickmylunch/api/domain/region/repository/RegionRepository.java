package com.pickmylunch.api.domain.region.repository;

import com.pickmylunch.common.entity.Region;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegionRepository extends JpaRepository<Region, Long> {
}
