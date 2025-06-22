package com.pickmylunch.api.domain.rating.service;

import com.pickmylunch.api.domain.rating.dto.request.PostRatingReqDto;
import com.pickmylunch.api.domain.rating.dto.request.PutRatingReqDto;
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

    public Long postRating(String restaurantId, PostRatingReqDto reqDto, Long memberId) {
        Restaurant savedRestaurant = validRestaurantExist(restaurantId);
        validAlreadyPost(restaurantId, memberId);

        Rating rating = ratingRepository.save(reqDto.toEntity(memberId, savedRestaurant));
        return rating.getId();
    }

    public Long putRating(Long ratingId, PutRatingReqDto reqDto, Long memberId) {
        Rating rating = validRatingExist(ratingId);
        validMemberSame(rating, memberId);
        rating.put(reqDto.toEntity());
        return rating.getId();
    }

    public void delRating(Long ratingId, Long memberId) {
        ratingRepository.findById(ratingId)
            .ifPresent(rating -> {
                if(!rating.getMemberId().equals(memberId)) {
                    throw new BusinessLogicException(RatingExceptionCode.MEMBER_NOT_SAME);
                }
                ratingRepository.delete(rating);
            });
    }

    /*
    * VALID
    * */
    //TODO : Restaurant 관련 구조
    private Restaurant validRestaurantExist(String restaurantId) {
        return null;
    }

    private void validAlreadyPost(String restaurantId, Long memberId) {
        ratingRepository.findByRestaurantIdAndMemberId(restaurantId, memberId)
            .ifPresent(rating -> {
                throw new BusinessLogicException(RatingExceptionCode.ALREADY_RATE);
            });
    }

    private Rating validRatingExist(Long ratingId) {
        return ratingRepository.findById(ratingId)
            .orElseThrow(() -> new BusinessLogicException(RatingExceptionCode.ENTITY_NOT_FOUND));
    }

    private void validMemberSame(Rating rating, Long memberId) {
        if(!rating.getMemberId().equals(memberId)) {
            throw new BusinessLogicException(RatingExceptionCode.MEMBER_NOT_SAME);
        }
    }
}
