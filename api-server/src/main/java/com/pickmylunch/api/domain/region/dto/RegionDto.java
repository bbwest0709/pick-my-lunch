package com.pickmylunch.api.domain.region.dto;

import com.pickmylunch.common.entity.Region;

public record RegionDto(String dosi, String sigungu, double lon, double lat) {

    public static Region of(RegionDto dto) {
        return Region.builder()
                .dosi(dto.dosi())
                .sigungu(dto.sigungu())
                .lon(dto.lon())
                .lat(dto.lat())
                .build();
    }
}
