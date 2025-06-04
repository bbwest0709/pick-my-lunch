package com.pickmylunch.common.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RawRestaurant {

    @Id
    private String id;

    @Column(columnDefinition = "TEXT")
    private String jsonData;

    private String hash;

    private boolean isUpdated;

    public void setUpdated(boolean flag) {
        this.isUpdated = flag;
    }

}
