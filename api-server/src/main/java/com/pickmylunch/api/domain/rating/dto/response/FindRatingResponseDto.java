package com.pickmylunch.api.domain.rating.dto.response;

import com.pickmylunch.common.entity.enums.Category;
import com.querydsl.core.annotations.QueryProjection;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record FindRatingResponseDto (
    Long ratingId,
    Long memberId,
    String memberName,
    String email,
    BigDecimal score,
    String content,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,

    String restaurantId,
    String restaurantName,
    String restaurantTel,
    Category category
) {

    @QueryProjection
    public FindRatingResponseDto{}
}
