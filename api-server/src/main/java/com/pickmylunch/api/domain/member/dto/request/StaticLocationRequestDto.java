package com.pickmylunch.api.domain.member.dto.request;

import com.pickmylunch.common.entity.*;

public record StaticLocationRequestDto(
        String name,
        double lat,
        double lon,
        boolean isDefault) {

    public MemberLocation of(Member member) {
        return MemberLocation.builder()
                .member(member)
                .name(this.name)
                .lon(this.lon)
                .lat(this.lat)
                .isDefault(this.isDefault)
                .build();
    }
}