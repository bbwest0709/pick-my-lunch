package com.pickmylunch.batch.pipeline.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.NullNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JsonUtil {
    private final ObjectMapper mapper = new ObjectMapper();

    public JsonNode safeReadTree(String json) {
        try {
            return mapper.readTree(json);
        } catch (Exception e) {
         log.error("[fail] JSON 파싱 실패", e);
         return NullNode.getInstance();
        }
    }
}
