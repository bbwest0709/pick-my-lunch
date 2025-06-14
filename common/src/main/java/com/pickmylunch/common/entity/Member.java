package com.pickmylunch.common.entity;

import com.pickmylunch.common.entity.enums.MemberRole;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String memberName;

    @Column(nullable = false)
    private String password;

    @Column(unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    private MemberRole role;

    @Builder.Default
    private boolean active = true;

    private boolean recommendationOptIn;

    public void updateRecommendationOptIn(boolean enabled) {
        this.recommendationOptIn = enabled;
    }

    public void deactivate() {
        this.active = false;
    }

    public void anonymize(String anonymizedName, String anonymizedEmail) {
        this.memberName = anonymizedName;
        this.email = anonymizedEmail;
    }
}
