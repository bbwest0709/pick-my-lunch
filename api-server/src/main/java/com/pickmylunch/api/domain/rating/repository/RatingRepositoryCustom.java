package com.pickmylunch.api.domain.rating.repository;

import com.pickmylunch.api.domain.rating.dto.request.FindRatingListResponseDto;
import com.pickmylunch.api.domain.rating.dto.response.FindRatingListRequestDto;
import com.pickmylunch.api.domain.rating.dto.response.FindRatingResponseDto;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RatingRepositoryCustom {

    Page<FindRatingListResponseDto> findRatingList(Pageable pageable, FindRatingListRequestDto dto);
    Optional<FindRatingResponseDto> findRatingDetail(Long ratingId);

}
