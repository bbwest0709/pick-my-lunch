package com.pickmylunch.api.global.redis;

import com.pickmylunch.api.global.exception.BusinessLogicException;
import com.pickmylunch.api.global.exception.code.MemberExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.GeoOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.data.geo.Point;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
public class RedisRepository {

    private final StringRedisTemplate redisTemplate;

    public void save(String key, String value, int minutes) {
        redisTemplate.opsForValue().set(key, value, minutes, TimeUnit.MINUTES);
    }

    public String findByKey(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public void delete(String key) {
        redisTemplate.delete(key);
    }

    public void saveRealTimeLocation(Long memberId, double lon, double lat) {
        String key = generateRealTimeLocationKey(memberId);
        GeoOperations<String, String> geoOps = redisTemplate.opsForGeo();
        geoOps.add(key, new Point(round(lon, 6), round(lat, 6)), String.valueOf(memberId));
        redisTemplate.expire(key, 15, TimeUnit.MINUTES);
    }

    public Point getRealTimeLocation(Long memberId) {
        String key = generateRealTimeLocationKey(memberId);
        GeoOperations<String, String> geoOps = redisTemplate.opsForGeo();
        List<Point> positions = geoOps.position(key, String.valueOf(memberId));
        Point point = getFirstPositionOrThrow(positions);
        return new Point(round(point.getX(), 6), round(point.getY(), 6));
    }

    private Point getFirstPositionOrThrow(List<Point> positions) {
        return Optional.ofNullable(positions)
                .filter(list -> !list.isEmpty())
                .map(list -> list.get(0))
                .orElseThrow(() -> new BusinessLogicException(MemberExceptionCode.REALTIME_LOCATION_NOT_FOUND));
    }

    private double round(double value, int decimalPlaces) {
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(decimalPlaces, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public String generateRealTimeLocationKey(Long memberId) {
        return "member_location:" + memberId;
    }
}
