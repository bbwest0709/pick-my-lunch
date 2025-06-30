package com.pickmylunch.api.domain.rating.repository;

import static com.pickmylunch.common.entity.QMember.*;
import static com.pickmylunch.common.entity.QRating.rating;
import static com.pickmylunch.common.entity.QRestaurant.restaurant;

import com.pickmylunch.api.domain.rating.dto.request.FindRatingListResponseDto;
import com.pickmylunch.api.domain.rating.dto.request.QFindRatingListResponseDto;
import com.pickmylunch.api.domain.rating.dto.response.FindRatingListRequestDto;
import com.pickmylunch.api.domain.rating.dto.response.FindRatingResponseDto;
import com.pickmylunch.api.domain.rating.dto.response.QFindRatingResponseDto;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.micrometer.common.util.StringUtils;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

@RequiredArgsConstructor
public class RatingRepositoryImpl implements RatingRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<FindRatingListResponseDto> findRatingList(Pageable pageable, FindRatingListRequestDto dto) {
        List<FindRatingListResponseDto> content = queryFactory
            .select(
                new QFindRatingListResponseDto(
                    rating.id,
                    member.id,
                    member.memberName,
                    member.email,
                    rating.score,
                    rating.content,
                    rating.createdAt,
                    rating.updatedAt,
                    restaurant.id,
                    restaurant.restaurantName,
                    restaurant.restaurantTel,
                    restaurant.category
                )
            )
            .from(rating)
            .innerJoin(member).on(rating.memberId.eq(member.id))
            .innerJoin(restaurant).on(rating.restaurant.id.eq(restaurant.id))
            .where(
                isRestaurantIdEq(dto.restaurantId()),
                isMemberIdEq(dto.memberId())
            )
            .limit(pageable.getPageSize())
            .offset(pageable.getOffset())
            .fetch();

        JPAQuery<Long> count = queryFactory.select(rating.id.count())
            .from(rating)
            .where(
                isRestaurantIdEq(dto.restaurantId()),
                isMemberIdEq(dto.memberId())
            );

        return PageableExecutionUtils.getPage(content, pageable, count::fetchOne);
    }

    @Override
    public Optional<FindRatingResponseDto> findRatingDetail(Long ratingId) {
        FindRatingResponseDto entity = queryFactory.select(
                new QFindRatingResponseDto(
                    rating.id,
                    member.id,
                    member.memberName,
                    member.email,
                    rating.score,
                    rating.content,
                    rating.createdAt,
                    rating.updatedAt,
                    restaurant.id,
                    restaurant.restaurantName,
                    restaurant.restaurantTel,
                    restaurant.category
                ))
            .from(rating)
            .where(
                rating.id.eq(ratingId)
            )
            .fetchOne();

        return Optional.ofNullable(entity);
    }

    private BooleanExpression isRestaurantIdEq(String restaurantId) {
        return StringUtils.isBlank(restaurantId) ? null : rating.restaurant.id.eq(restaurantId);
    }

    private BooleanExpression isMemberIdEq(Long memberId) {
        return Objects.isNull(memberId) ? null : rating.memberId.eq(memberId);
    }

}
