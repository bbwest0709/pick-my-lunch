package com.pickmylunch.api.domain.rating.dto.request;

import com.pickmylunch.common.entity.Rating;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record PutRatingReqDto(
    @NotNull
    BigDecimal score,
    @NotBlank
    String content
) {

    public Rating toEntity(){
        return Rating.builder()
            .score(score)
            .content(content)
            .build();
    }
}
