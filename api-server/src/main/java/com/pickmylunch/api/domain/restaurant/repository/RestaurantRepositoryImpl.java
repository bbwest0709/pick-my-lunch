package com.pickmylunch.api.domain.restaurant.repository;

import com.pickmylunch.common.entity.*;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import com.querydsl.jpa.impl.JPAQueryFactory;

import static com.pickmylunch.common.entity.QRestaurant.restaurant;

@RequiredArgsConstructor
public class RestaurantRepositoryImpl implements RestaurantRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Restaurant> findRestaurantsBySigungu(Pageable pageable, String sigungu) {
        BooleanExpression predicate  = condSigungu(sigungu);
        var query = createQueryWithPagination(pageable, predicate);
        long total = countTotal();
        return new PageImpl<>(query.fetch(), pageable, total);
    }

    private BooleanExpression condSigungu(String sigungu) {
        return sigungu == null ? null : restaurant.sigungu.eq(sigungu);
    }

    private long countTotal() {
        return queryFactory
                .selectFrom(restaurant)
                .where(restaurant.id.isNotNull())
                .fetchCount();
    }

    private JPAQuery<Restaurant> createQueryWithPagination(Pageable pageable, BooleanExpression predicate) {
        return queryFactory.selectFrom(restaurant)
                .where(predicate)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(restaurant.totalViewCount.desc());
    }

}
