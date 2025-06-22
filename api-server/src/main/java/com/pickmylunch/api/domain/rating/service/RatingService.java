package com.pickmylunch.api.domain.rating.service;

import com.pickmylunch.api.domain.rating.dto.request.PostRatingReqDto;
import com.pickmylunch.api.domain.rating.repository.RatingRepository;
import com.pickmylunch.api.global.exception.BusinessLogicException;
import com.pickmylunch.api.global.exception.code.RatingExceptionCode;
import com.pickmylunch.common.entity.Rating;
import com.pickmylunch.common.entity.Restaurant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class RatingService {

    private final RatingRepository ratingRepository;

    public Long postRating(Long restaurantId, PostRatingReqDto reqDto, Long memberId) {
        Restaurant savedRestaurant = validRestaurantExist(restaurantId);
        validAlreadyPost(restaurantId, memberId);

        Rating rating = ratingRepository.save(reqDto.toEntity(memberId, savedRestaurant));
        return rating.getId();
    }

    //TODO : Restaurant 관련 구조
    private Restaurant validRestaurantExist(Long restaurantId) {
        return null;
    }

    private void validAlreadyPost(Long restaurantId, Long memberId) {
        ratingRepository.findByRestaurantIdAndMemberId(restaurantId, memberId)
            .ifPresent(rating -> {
                throw new BusinessLogicException(RatingExceptionCode.ALREADY_RATE);
            });
    }
}
