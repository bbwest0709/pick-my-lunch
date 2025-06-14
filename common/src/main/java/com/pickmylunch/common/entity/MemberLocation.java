package com.pickmylunch.common.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberLocation extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    private Member member;

    private String name;

    private Double lon;

    private Double lat;

    private boolean isDefault;

    public void changeIsDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }

    public void update(String name, Double lat, Double lon) {
        Optional.ofNullable(name).ifPresent(n -> this.name = n);
        Optional.ofNullable(lat).ifPresent(l -> this.lat = l);
        Optional.ofNullable(lon).ifPresent(l -> this.lon = l);
    }
}
