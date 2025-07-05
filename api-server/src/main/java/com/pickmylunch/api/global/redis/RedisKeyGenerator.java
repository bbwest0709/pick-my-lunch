package com.pickmylunch.api.global.redis;

public class RedisKeyGenerator {
    private static final String RESTAURANT_VIEW_KEY_FORMAT = "restaurant:%s:v";
    private static final String RESTAURANT_VIEW_KEY_PATTERN_ALL = "restaurant:*:v";
    private static final String MEMBER_LOCATION_KEY_FORMAT = "member_location:%d";
    private static final String RESTAURANT_DETAIL_KEY = "restaurant_details::%s";
    private static final String RESTAURANT_DETAIL_KEY_PATTERN_ALL = "restaurant_details::*";

    public static String generateRestaurantViewKey(String restaurantId) {
        return String.format(RESTAURANT_VIEW_KEY_FORMAT, restaurantId);
    }

    public static String generateRestaurantViewKeyPattern() {
        return RESTAURANT_VIEW_KEY_PATTERN_ALL;
    }

    public static String generateRealTimeLocationKey(Long memberId) {
        return String.format(MEMBER_LOCATION_KEY_FORMAT, memberId);
    }

    public static String generateRestaurantDetailKey(String restaurantId) {
        return String.format(RESTAURANT_DETAIL_KEY, restaurantId);
    }

    public static String generateRestaurantDetailKeyPattern() {
        return RESTAURANT_DETAIL_KEY_PATTERN_ALL;
    }
}
