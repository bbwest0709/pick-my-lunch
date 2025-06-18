package com.pickmylunch.api.domain.region.repository;

import com.pickmylunch.common.entity.Region;

public interface RegionRepositoryCustom {
    Region findBySigungu(String sigungu);
}
