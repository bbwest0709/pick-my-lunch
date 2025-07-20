package com.pickmylunch.api.global.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.pickmylunch.api.domain.rating.dto.response.FindRatingResponseDto;
import com.pickmylunch.api.domain.restaurant.dto.response.RestaurantDetailResponseDto;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.*;
import org.springframework.data.redis.cache.*;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.*;

import java.math.*;
import java.time.*;

@Configuration
@EnableCaching
public class RedisConfig {

    @Bean
    public RedisCacheManager redisCacheManager(RedisConnectionFactory connectionFactory) {
        ObjectMapper objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .setSerializationInclusion(JsonInclude.Include.NON_NULL);

        SimpleModule bigDecimalModule = new SimpleModule();
        bigDecimalModule.addSerializer(BigDecimal.class, ToStringSerializer.instance);
        objectMapper.registerModule(bigDecimalModule);

        Jackson2JsonRedisSerializer<RestaurantDetailResponseDto> restaurantSerializer =
                new Jackson2JsonRedisSerializer<>(RestaurantDetailResponseDto.class);
        restaurantSerializer.setObjectMapper(objectMapper);

        Jackson2JsonRedisSerializer<FindRatingResponseDto> ratingSerializer =
                new Jackson2JsonRedisSerializer<>(FindRatingResponseDto.class);
        ratingSerializer.setObjectMapper(objectMapper);

        RedisCacheConfiguration cacheConfig = RedisCacheConfiguration.defaultCacheConfig()
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .entryTtl(Duration.ofHours(1))
                .disableCachingNullValues();

        RedisCacheManager redisCacheManager = RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(cacheConfig)
                .withCacheConfiguration("restaurant_details", RedisCacheConfiguration.defaultCacheConfig()
                        .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(restaurantSerializer)))
                .withCacheConfiguration("rating_details", RedisCacheConfiguration.defaultCacheConfig()
                        .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(ratingSerializer)))
                .build();

        return redisCacheManager;
    }
}