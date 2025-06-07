package com.pickmylunch.batch.pipeline.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pickmylunch.batch.pipeline.dto.AddressDto;
import com.pickmylunch.batch.pipeline.repository.RawRestaurantRepository;
import com.pickmylunch.batch.pipeline.repository.RestaurantRepository;
import com.pickmylunch.batch.pipeline.util.AddressParser;
import com.pickmylunch.common.entity.RawRestaurant;
import com.pickmylunch.common.entity.Restaurant;
import com.pickmylunch.common.entity.enums.Category;
import com.pickmylunch.common.util.GeometryUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RestaurantProcessor {

    private final RawRestaurantRepository rawRestaurantRepository;
    private final RestaurantRepository restaurantRepository;
    private final ObjectMapper objectMapper;
    private final GeometryUtil geometryUtil;

    public void processRawToRestaurant() {
        List<RawRestaurant> rawRestaurants = rawRestaurantRepository.findAll();

        for (RawRestaurant rawRestaurant : rawRestaurants) {
            if (shouldUpdate(rawRestaurant)) {
                try {
                    Restaurant restaurant = convertToProcessedRestaurant(rawRestaurant);
                    if (restaurant != null) {
                        restaurantRepository.save(restaurant);
                        rawRestaurant.setUpdated(false);
                        rawRestaurantRepository.save(rawRestaurant);
                    }
                } catch (Exception e) {
                    log.error("[fail] 데이터 전처리 실패 id: {}", rawRestaurant.getId(), e);
                }
            }
        }
    }

    public Restaurant convertToProcessedRestaurant(RawRestaurant rawRestaurant) {
        try {
            JsonNode rootNode = objectMapper.readTree(rawRestaurant.getJsonData());
            Point location = extractLocation(rootNode);

            if (shouldSkip(rootNode, rawRestaurant.getId(), location)) {
                return null;
            }

            AddressDto jibunAddress = AddressParser.parse(rootNode.path("SITEWHLADDR").asText());
            AddressDto doroAddress = AddressParser.parse(rootNode.path("RDNWHLADDR").asText());

            return toRestaurantEntity(rawRestaurant, rootNode, jibunAddress, doroAddress, location);

        } catch (Exception e) {
            log.error("[fail] 데이터 파싱 실패 id: {}", rawRestaurant.getId(), e);
            return null;
        }
    }

    private Restaurant toRestaurantEntity(RawRestaurant rawRestaurant, JsonNode rootNode, AddressDto jibunAddress, AddressDto doroAddress, Point location) {
        return Restaurant.builder()
                .id(rawRestaurant.getId())
                .restaurantName(rootNode.path("BPLCNM").asText())
                .category(Category.of(rootNode.path("UPTAENM").asText()))
                .restaurantTel(rootNode.path("SITETEL").asText().replaceAll("\\s+", ""))
                .jibunDetailAddress(jibunAddress.detail())
                .doroDetailAddress(doroAddress.detail())
                .dosi(jibunAddress.dosi())
                .sigungu(jibunAddress.sigungu())
                .location(location)
                .build();
    }

    private Point extractLocation(JsonNode rootNode) {
        String x = rootNode.path("X").asText();
        String y = rootNode.path("Y").asText();

        if (x.isEmpty() || y.isEmpty()) {
            return null;
        }

        double longitude = Double.parseDouble(x);
        double latitude = Double.parseDouble(y);

        return geometryUtil.createPoint(longitude, latitude);
    }

    public boolean shouldUpdate(RawRestaurant rawRestaurant) {
        return rawRestaurant.isUpdated();
    }

    private boolean shouldSkip(JsonNode rootNode, String id, Point location) {
        if (isClosed(rootNode) || location == null) {
            if (isClosed(rootNode)) {
                log.info("[info] 폐업 데이터 스킵 - id: {}", id);
            } else {
                log.warn("[warn] 위도/경도 정보 없음 - id: {}", id);
            }
            return true;
        }
        return false;
    }

    private boolean isClosed(JsonNode rootNode) {
        return "폐업".equals(rootNode.path("TRDSTATENM").asText());
    }
}
