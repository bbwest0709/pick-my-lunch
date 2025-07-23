package com.pickmylunch.api.domain.rating.service;

import com.pickmylunch.api.domain.rating.dto.request.FindRatingListResponseDto;
import com.pickmylunch.api.domain.rating.dto.request.*;
import com.pickmylunch.api.domain.rating.dto.response.FindRatingListRequestDto;
import com.pickmylunch.api.domain.rating.dto.response.FindRatingResponseDto;
import com.pickmylunch.api.domain.rating.repository.RatingRepository;
import com.pickmylunch.api.domain.restaurant.repository.RestaurantRepository;
import com.pickmylunch.api.global.exception.BusinessLogicException;
import com.pickmylunch.api.global.exception.code.*;
import com.pickmylunch.common.entity.*;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class RatingService {

    private final RatingRepository ratingRepository;
    private final RestaurantRepository restaurantRepository;

    @Transactional(rollbackFor = Exception.class)
    public Long postRating(String restaurantId, PostRatingRequestDto reqDto, Long memberId) {
        Restaurant savedRestaurant = validRestaurantExist(restaurantId);
        validAlreadyPost(restaurantId, memberId);

        Rating rating = ratingRepository.save(reqDto.toEntity(memberId, savedRestaurant));
        return rating.getId();
    }

    @Transactional(rollbackFor = Exception.class)
    @CachePut(value = "rating_details", key = "#ratingId")
    public Long putRating(Long ratingId, PutRatingRequestDto reqDto, Long memberId) {
        Rating rating = validRatingExist(ratingId);
        validMemberSame(rating, memberId);
        rating.put(reqDto.toEntity());
        return rating.getId();
    }

    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = "rating_details", key = "#ratingId")
    public void delRating(Long ratingId, Long memberId) {
        ratingRepository.findById(ratingId)
                .ifPresent(rating -> {
                    if (!rating.getMemberId().equals(memberId)) {
                        throw new BusinessLogicException(RatingExceptionCode.MEMBER_NOT_SAME);
                    }
                    ratingRepository.delete(rating);
                });
    }

    public Page<FindRatingListResponseDto> findRatingList(FindRatingListRequestDto dto, Pageable pageable) {
        return ratingRepository.findRatingList(pageable, dto);
    }

    private Restaurant validRestaurantExist(String restaurantId) {
        return restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new BusinessLogicException(RestaurantExceptionCode.RESTAURANT_NOT_FOUND));
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
        if (!rating.getMemberId().equals(memberId)) {
            throw new BusinessLogicException(RatingExceptionCode.MEMBER_NOT_SAME);
        }
    }

    @Cacheable(value = "rating_details", key = "#ratingId")
    public FindRatingResponseDto findRatingDetail(Long ratingId) {
        return ratingRepository.findRatingDetail(ratingId)
                .orElseThrow(
                        () -> new BusinessLogicException(RatingExceptionCode.ENTITY_NOT_FOUND)
                );
    }
}
