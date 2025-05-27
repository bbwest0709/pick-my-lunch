package com.pickmylunch.common.entity;

import com.pickmylunch.common.entity.enums.Category;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Point;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Restaurant {

    @Id
    private String id;

    @Column(nullable = false)
    private String restaurantName;

    @Column(nullable = false)
    private String dosi;

    @Enumerated(value = EnumType.STRING)
    private Category category;

    private String sigungu;

    private String jibunDetailAddress;

    private String doroDetailAddress;

    @Column(columnDefinition = "geometry(Point, 4326)")
    private Point location;

    private double ratingAverage;
}
