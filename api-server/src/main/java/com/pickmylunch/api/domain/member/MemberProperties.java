package com.pickmylunch.api.domain.member;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "app.member")
public class MemberProperties {
    private String anonymizedName;
    private String anonymizedEmail;
}