package com.pickmylunch.api.global.security.details;

import com.pickmylunch.common.entity.Member;
import io.jsonwebtoken.Claims;
import lombok.Getter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;


@Getter
public class AuthUser extends User {
    private final Long id;

    public AuthUser(Member member) {
        super(member.getMemberName(),
                member.getPassword(),
                AuthorityUtils.createAuthorityList(member.getRole().getRole()));
        this.id = member.getId();
    }

    public AuthUser(Claims claims) {
        super(claims.getSubject(),
                "",
                AuthorityUtils.commaSeparatedStringToAuthorityList(claims.get("authorities", String.class)));
        this.id = claims.get("id", Long.class);
    }
}
