package com.pickmylunch.batch.pipeline.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.pickmylunch.batch.pipeline.util.*;
import com.pickmylunch.batch.pipeline.util.dto.*;
import com.pickmylunch.batch.pipeline.repository.*;
import com.pickmylunch.batch.pipeline.util.AddressParser;
import com.pickmylunch.common.entity.*;
import com.pickmylunch.common.entity.enums.Category;
import com.pickmylunch.common.util.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class RestaurantProcessor {

    private final RawRestaurantRepository rawRestaurantRepository;
    private final RestaurantRepository restaurantRepository;
    private final GeometryUtil geometryUtil;
    private final JsonUtil jsonUtil;

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
            JsonNode rootNode = jsonUtil.safeReadTree(rawRestaurant.getJsonData());

            Point location = extractLocation(rootNode);

            if (shouldSkip(rootNode, location)) return null;

            AddressDto jibunAddress = AddressParser.parse(rootNode.path(JsonFieldConstants.SITEWHLADDR).asText());
            AddressDto doroAddress = AddressParser.parse(rootNode.path(JsonFieldConstants.RDNWHLADDR).asText());

            return toRestaurantEntity(rawRestaurant, rootNode, jibunAddress, doroAddress, location);

        } catch (Exception e) {
            log.error("[fail] 데이터 전처리 실패 id: {}", rawRestaurant.getId(), e);
            throw e;
        }
    }

    private Restaurant toRestaurantEntity(RawRestaurant rawRestaurant, JsonNode rootNode, AddressDto jibunAddress, AddressDto doroAddress, Point location) {
        return Restaurant.builder()
                .id(rawRestaurant.getId())
                .restaurantName(rootNode.path(JsonFieldConstants.BPLCNM).asText())
                .category(Category.of(rootNode.path(JsonFieldConstants.UPTAENM).asText()))
                .restaurantTel(rootNode.path(JsonFieldConstants.SITETEL).asText().replaceAll("\\s+", ""))
                .jibunDetailAddress(jibunAddress.detail())
                .doroDetailAddress(doroAddress.detail())
                .dosi(jibunAddress.dosi())
                .sigungu(jibunAddress.sigungu())
                .location(location)
                .build();
    }

    private Point extractLocation(JsonNode rootNode) {
        String x = rootNode.path(JsonFieldConstants.X).asText();
        String y = rootNode.path(JsonFieldConstants.Y).asText();

        if (x.isEmpty() || y.isEmpty()) {
            return null;
        }

        try {
            double longitude = Double.parseDouble(x);
            double latitude = Double.parseDouble(y);
            return geometryUtil.createPointFromTM(longitude, latitude);
        } catch (NumberFormatException e) {
            log.warn("[warn] 좌표 변환 실패 x: {}, y: {}", x, y);
            return null;
        }
    }

    public boolean shouldUpdate(RawRestaurant rawRestaurant) {
        return rawRestaurant.isUpdated();
    }

    private boolean shouldSkip(JsonNode rootNode, Point location) {
        return isClosed(rootNode) || Objects.isNull(location);
    }

    private boolean isClosed(JsonNode rootNode) {
        return JsonFieldConstants.CLOSED_STATUS.equals(rootNode.path(JsonFieldConstants.TRDSTATENM).asText());
    }
}
