package com.pickmylunch.batch.pipeline.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pickmylunch.batch.pipeline.repository.RawRestaurantRepository;
import com.pickmylunch.common.entity.RawRestaurant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class RawRestaurantProcessor {

    private final RawRestaurantRepository rawRestaurantRepository;
    private final ObjectMapper objectMapper;

    public List<RawRestaurant> parseOnly(String responseData, String serviceName) {
        List<RawRestaurant> rawList = new ArrayList<>();
        try {
            JsonNode rootNode = objectMapper.readTree(responseData);
            JsonNode rowNodes = rootNode.path(serviceName).path("row");

            for (JsonNode row : rowNodes) {
                String id = row.path("MGTNO").asText();
                String json = row.toString();

                String hash = DigestUtils.sha256Hex(json.getBytes());

                RawRestaurant existing = rawRestaurantRepository.findById(id).orElse(null);

                if (existing == null || !existing.getHash().equals(hash)) {
                    RawRestaurant raw = createRawRestaurant(id, json, hash);
                    rawList.add(raw);
                }
            }
        } catch (Exception e) {
            log.error("[fail] JSON 파싱 실패 {}", e.getMessage());
        }
        return rawList;
    }

    public void processAndSave(String responseData, String serviceName) {
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
        RawRestaurant existing = rawRestaurantRepository.findById(id).orElse(null);
        return existing == null || !existing.getHash().equals(newHash);
    }

}
