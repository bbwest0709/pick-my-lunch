package com.pickmylunch.api.domain.restaurant.repository;

import com.pickmylunch.common.entity.*;
import com.querydsl.jpa.impl.JPAQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import com.querydsl.jpa.impl.JPAQueryFactory;

import static com.pickmylunch.common.entity.QRestaurant.restaurant;

@RequiredArgsConstructor
public class RestaurantRepositoryImpl implements RestaurantRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Restaurant> findAllWithPagination(Pageable pageable) {
        var query = createQueryWithPagination(pageable);
        long total = countTotal();

        return new PageImpl<>(query.fetch(), pageable, total);
    }

    private long countTotal() {
        return queryFactory
                .selectFrom(restaurant)
                .where(restaurant.id.isNotNull())
                .fetchCount();
    }

    private JPAQuery<Restaurant> createQueryWithPagination(Pageable pageable) {
        return queryFactory.selectFrom(restaurant)
                .where(restaurant.id.isNotNull())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(restaurant.restaurantName.asc());
    }
}