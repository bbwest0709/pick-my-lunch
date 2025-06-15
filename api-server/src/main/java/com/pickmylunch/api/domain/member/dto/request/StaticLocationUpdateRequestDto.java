package com.pickmylunch.api.domain.member.dto.request;

public record StaticLocationUpdateRequestDto(String name,
                                             Double lat,
                                             Double lon) {
}
