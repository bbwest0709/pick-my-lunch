package com.pickmylunch.api.domain.region.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pickmylunch.common.entity.Region;
import org.locationtech.jts.geom.Point;

public record RegionDto(String dosi, String sigungu, double lon, double lat, @JsonIgnore Point location) {

    public static Region of(RegionDto dto) {
        return Region.builder()
                .dosi(dto.dosi())
                .sigungu(dto.sigungu())
                .lon(dto.lon())
                .lat(dto.lat())
                .location(dto.location())
                .build();
    }

    public static RegionDto from(Region region) {
        return new RegionDto(
                region.getDosi(),
                region.getSigungu(),
                region.getLon(),
                region.getLat(),
                region.getLocation()
        );
    }
}
