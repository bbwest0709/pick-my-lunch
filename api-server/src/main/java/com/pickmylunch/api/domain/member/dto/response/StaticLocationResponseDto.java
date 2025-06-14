package com.pickmylunch.api.domain.member.dto.response;

import com.pickmylunch.common.entity.MemberLocation;

public record StaticLocationResponseDto(String name, double lat, double lon, boolean isDefault) {

    public static StaticLocationResponseDto of(MemberLocation location) {
        return new StaticLocationResponseDto(location.getName(), location.getLat(), location.getLon(), location.isDefault());
    }
}
