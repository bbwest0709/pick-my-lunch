package com.pickmylunch.api.global.security.config;

import com.pickmylunch.api.global.security.handler.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.*;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.*;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.*;
import org.springframework.web.cors.*;

import java.util.*;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtFilterDsl jwtFilterDsl;
    private final AuthenticationEntryPointCustomHandler authenticationEntryPointCustomHandler;
    private final AccessDeniedCustomHandler accessDeniedCustomHandler;
    private final LogoutSuccessCustomHandler logoutSuccessCustomHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.with(jwtFilterDsl, JwtFilterDsl::build);
        http.headers(
                headerConfig -> headerConfig.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable)
        );
        http.authorizeHttpRequests(
                        authorize ->
                                authorize
                                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                                        .requestMatchers("/api/members/exists/**").permitAll()
                                        .requestMatchers("/api/members").permitAll()
                                        .requestMatchers("/api/login", "/api/auth/reissue").permitAll()
                                        .requestMatchers("/api/members/me/**", "/api/members/me/alerts/recommendation").authenticated()
                                        .requestMatchers("/api/members/locations/**").authenticated()
                                        .anyRequest().authenticated()
                )
                .sessionManagement(
                        session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(apiConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable);
        http.exceptionHandling(
                exception -> exception
                        .authenticationEntryPoint(authenticationEntryPointCustomHandler)
                        .accessDeniedHandler(accessDeniedCustomHandler)
        );
        http.logout(
                logout -> logout.logoutSuccessHandler(logoutSuccessCustomHandler).logoutUrl("/api/logout")
        );
        return http.build();
    }

    private CorsConfigurationSource apiConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(List.of(
//                "https://pickmylunch.com",
                "http://localhost:3000",
                "http://localhost:8080"
        ));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowCredentials(true);
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type", "Location", "Refresh"));
        configuration.setExposedHeaders(List.of("Authorization", "Refresh"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}