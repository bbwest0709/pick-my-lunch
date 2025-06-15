package com.pickmylunch.api.domain.region.repository.projection;

import com.pickmylunch.api.domain.region.repository.*;
import com.pickmylunch.common.entity.*;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import static com.pickmylunch.common.entity.QRegion.region;

@RequiredArgsConstructor
public class RegionRepositoryImpl implements RegionRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Region findBySigungu(String sigungu) {
        return queryFactory.selectFrom(region)
                .where(condSigungu(sigungu))
                .fetchOne();
    }

    private BooleanExpression condDosi(String dosi) {
        return dosi == null ? null : region.dosi.eq(dosi);
    }

    private BooleanExpression condSigungu(String sigungu) {
        return sigungu == null ? null : region.sigungu.eq(sigungu);
    }
}
