package com.pickmylunch.batch.pipeline.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.pickmylunch.batch.pipeline.dto.AddressDto;
import com.pickmylunch.batch.pipeline.repository.*;
import com.pickmylunch.common.entity.*;
import com.pickmylunch.common.entity.enums.*;
import com.pickmylunch.common.util.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.*;
import reactor.core.publisher.Mono;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApiService {

    private final RawRestaurantRepository rawRestaurantRepository;
    private final RestaurantRepository restaurantRepository;
    private final ObjectMapper objectMapper;
    private final WebClient.Builder webClient;
    private final GeometryUtil geometryUtil;

    @Value("${api.key}")
    private String apiKey;

    @Value("${api.base-url}")
    private String baseUrl;

    @Value("${api.service-name}")
    private String serviceName;

    @Value("${api.format-type}")
    private String formatType;

    @Value("${api.page-size}")
    private int pageSize;

    @Value("${api.max-index}")
    private int maxIndex;

    public void executeRawDataLoad() {
        for (int i = 1; i <= maxIndex; i += pageSize) {
            int start = i;
            int end = Math.min(i + pageSize - 1, maxIndex);

            log.info("[start] 데이터 호출 시작: {} ~ {}", start, end);

            try {
                String response = fetchData(start, end).block();
                processAndSave(response);
            } catch (WebClientResponseException e) {
                log.error("[fail] API 호출 실패 status: {}, message: {}", e.getRawStatusCode(), e.getMessage());
            } catch (Exception e) {
                log.error("[fail] 데이터 처리 실패 {}", e.getMessage());
            }
        }
    }

    public Mono<String> fetchData(int start, int end) {
        String uri = buildUrl(start, end);
        return webClient
                .baseUrl(baseUrl)
                .build()
                .get()
                .uri(uri)
                .retrieve()
                .bodyToMono(String.class);
    }

    private String buildUrl(int start, int end) {
        return String.format("%s/%s/%s/%s/%d/%d", baseUrl, apiKey, formatType, serviceName, start, end);
    }

    public void processAndSave(String responseData) {
        try {
            JsonNode rootNode = objectMapper.readTree(responseData);
            JsonNode rowNodes = rootNode.path(serviceName).path("row");

            for (JsonNode row : rowNodes) {
                String id = row.path("MGTNO").asText();
                String json = row.toString();
                saveRawData(id, json);
            }

        } catch (Exception e) {
            log.error("[fail] JSON 처리 실패 {}", e.getMessage());
        }
    }

    public void saveRawData(String id, String jsonData) {
        String hash = DigestUtils.sha256Hex(jsonData.getBytes());
        if (isChanged(id, hash)) {
            RawRestaurant raw = createRawRestaurant(id, jsonData, hash);
            log.info("json data : {}", jsonData);
            rawRestaurantRepository.save(raw);
            log.info("[success] 원시 데이터 저장 - id : {}", id);
        } else {
            log.info("[success] 변경 없음 - ID: {}", id);
        }
    }

    private RawRestaurant createRawRestaurant(String id, String jsonData, String hash) {
        return RawRestaurant.builder()
                .id(id)
                .jsonData(jsonData)
                .hash(hash)
                .isUpdated(true)
                .build();
    }

    private boolean isChanged(String id, String newHash) {
        RawRestaurant existing = findRawRestaurantById(id);
        return isNewOrUpdated(existing, newHash);
    }

    private RawRestaurant findRawRestaurantById(String id) {
        return rawRestaurantRepository.findById(id).orElse(null);
    }

    private boolean isNewOrUpdated(RawRestaurant existing, String newHash) {
        return existing == null || !existing.getHash().equals(newHash);
    }

    public void processRawToRestaurant() {
        List<RawRestaurant> rawRestaurants = rawRestaurantRepository.findAll();

        for(RawRestaurant rawRestaurant : rawRestaurants) {
            if(rawRestaurant.isUpdated()) {
                try {
                    Restaurant processRestaurant = convertToProcessdRestaurant(rawRestaurant);
                    if (processRestaurant != null) {
                        restaurantRepository.save(processRestaurant);
                        rawRestaurant.setUpdated(false);
                        rawRestaurantRepository.save(rawRestaurant);
                    }

                } catch (Exception e) {
                    log.error("[fail] 데이터 전처리 실패 id: {}", e.getMessage());
                }
            }
        }
    }

    private Restaurant convertToProcessdRestaurant(RawRestaurant rawRestaurant) {
        try {
            JsonNode rootNode = objectMapper.readTree(rawRestaurant.getJsonData());

            Point location = extractLocation(rootNode);

            if (location == null) {
                log.warn("[wran] 위도/경도 정보 없음 - id: {}", rawRestaurant.getId());
                return null;
            }

            AddressDto jibunAddress = parseAddress(rootNode.path("SITEWHLADDR").asText());
            AddressDto doroAddress = parseAddress(rootNode.path("RDNWHLADDR").asText());

            return toRestaurantEntity(rawRestaurant, rootNode, jibunAddress, doroAddress, location);

        } catch (JsonProcessingException e) {
            log.error("[fail] 데이터 파싱 실패", e.getMessage());
        }
        return null;
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

        double longitude = Double.valueOf(x);
        double latitude = Double.valueOf(y);

        return geometryUtil.createPoint(longitude, latitude);
    }

    private AddressDto parseAddress(String address) {
        if (address == null || address.isEmpty()) {
            return new AddressDto("", "", "");
        }
        String [] parts = address.split(" ", 3);
        return new AddressDto(
                getOrEmpty(parts, 0),
                getOrEmpty(parts, 1),
                getOrEmpty(parts, 2)
        );
    }

    private String getOrEmpty(String[] array, int index) {
        return index < array.length ? array[index] : "";
    }

}