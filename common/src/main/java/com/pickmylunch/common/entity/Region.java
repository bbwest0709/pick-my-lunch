package com.pickmylunch.common.entity;

import jakarta.persistence.*;
import lombok.*;
import org.locationtech.jts.geom.Point;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Region {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String dosi;

    @Column(nullable = false)
    private String sigungu;

    private double lon;
    private double lat;

    @Column(columnDefinition = "geometry(Point, 4326)")
    private Point location;
}
