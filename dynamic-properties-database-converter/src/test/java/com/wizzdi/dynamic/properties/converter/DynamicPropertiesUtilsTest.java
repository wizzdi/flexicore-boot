package com.wizzdi.dynamic.properties.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class DynamicPropertiesUtilsTest {

    @Test
    void updateDynamic() throws JsonProcessingException {
        ObjectMapper objectMapper=new ObjectMapper();
        TypeReference<Map<String,Object>> mapType=new TypeReference<>() {};
        Map<String,Object> empty=new HashMap<>();

        String init= """
                {"first":123,"second":"321"}
                """;

        Map<String,Object> initMap = objectMapper.readValue(init, mapType);
        Map<String, Object> merged = DynamicPropertiesUtils.updateDynamic(initMap, empty);
        Assertions.assertNotNull(merged);
        Assertions.assertEquals(123,merged.get("first"));
        Assertions.assertEquals("321",merged.get("second"));

        String afterUpdate= """
                {"first":null}
                """;
        Map<String,Object> afterUpdateMap = objectMapper.readValue(afterUpdate, mapType);

        merged = DynamicPropertiesUtils.updateDynamic(afterUpdateMap, initMap);
        Assertions.assertNotNull(merged);
        Assertions.assertNull(merged.get("first"));
        Assertions.assertEquals("321",merged.get("second"));

        merged = DynamicPropertiesUtils.updateDynamic(afterUpdateMap, merged);
        Assertions.assertNull(merged);


    }
}