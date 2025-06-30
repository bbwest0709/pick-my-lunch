package com.pickmylunch.api.domain.rating.dto.request;

import com.pickmylunch.common.entity.Rating;
import com.pickmylunch.common.entity.Restaurant;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record PostRatingRequestDto(
        @NotNull
        @Max(5)
        BigDecimal score,
        @NotBlank
        String content
) {

    public Rating toEntity(Long memberId, Restaurant restaurant) {
        return Rating.builder()
                .score(score)
                .memberId(memberId)
                .restaurant(restaurant)
                .content(content)
                .build();
    }
}