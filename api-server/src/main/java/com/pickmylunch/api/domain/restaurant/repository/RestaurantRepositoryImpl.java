package com.pickmylunch.api.domain.restaurant.repository;

import com.pickmylunch.common.entity.Restaurant;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;


import static com.pickmylunch.common.entity.QRestaurant.restaurant;

@RequiredArgsConstructor
public class RestaurantRepositoryImpl implements RestaurantRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Restaurant> findRestaurantsBySigungu(Pageable pageable, String sigungu, String sort, boolean ascending) {
        BooleanExpression predicate = condSigungu(sigungu);
        OrderSpecifier<?> orderSpecifier = getOrderSpecifier(sort, ascending);
        var query = createQueryWithPagination(pageable, predicate, orderSpecifier);
        long total = countTotal(predicate);
        return new PageImpl<>(query.fetch(), pageable, total);
    }

    private OrderSpecifier<?> getOrderSpecifier(String sort, boolean ascending) {
        if ("rating".equalsIgnoreCase(sort)) {
            return ascending ? restaurant.ratingAverage.asc() : restaurant.ratingAverage.desc();
        } else if ("viewCount".equalsIgnoreCase(sort)) {
            return ascending ? restaurant.totalViewCount.asc() : restaurant.totalViewCount.desc();
        } else {
            return restaurant.id.asc();
        }
    }

    private BooleanExpression condSigungu(String sigungu) {
        return sigungu == null ? null : restaurant.sigungu.eq(sigungu);
    }

    private long countTotal(BooleanExpression predicate) {
        return queryFactory
                .selectFrom(restaurant)
                .where(predicate)
                .fetchCount();
    }

    private JPAQuery<Restaurant> createQueryWithPagination(Pageable pageable, BooleanExpression predicate, OrderSpecifier<?> orderSpecifier) {
        return queryFactory.selectFrom(restaurant)
                .where(predicate)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(orderSpecifier);
    }
}
