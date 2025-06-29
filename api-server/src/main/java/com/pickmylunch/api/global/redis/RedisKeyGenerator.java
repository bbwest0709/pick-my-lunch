package com.pickmylunch.api.global.redis;

public class RedisKeyGenerator {
    private static final String RESTAURANT_VIEW_KEY_PATTERN = "restaurant:%s:v";
    private static final String RESTAURANT_VIEW_KEY_PATTERN_ALL = "restaurant:*:v";
    private static final String MEMBER_LOCATION_KEY_PATTERN = "member_location:%d";

    public static String generateRestaurantViewKey(String restaurantId) {
        return String.format(RESTAURANT_VIEW_KEY_PATTERN, restaurantId);
    }

    public static String generateRestaurantViewKeyPattern() {
        return RESTAURANT_VIEW_KEY_PATTERN_ALL;
    }

    public static String generateRealTimeLocationKey(Long memberId) {
        return String.format(MEMBER_LOCATION_KEY_PATTERN, memberId);
    }
}
