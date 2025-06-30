package com.pickmylunch.api.domain.rating.dto.response;

public record FindRatingListRequestDto(
    String restaurantId,
    Long memberId
) {

}
