package com.wizzdi.dynamic.properties.converter;

import com.wizzdi.flexicore.boot.jpa.service.EntitiesProvider;
import jakarta.persistence.Converter;
import org.springframework.stereotype.Component;

import java.util.Map;

@Converter(autoApply = false)
public class JsonConverter implements jakarta.persistence.AttributeConverter<Map<String, Object>, Object> {

    private final JsonConverterImplementation implementation;

    public JsonConverter() {
        this.implementation = EntitiesProvider.getContext().getBean(JsonConverterImplementation.class);
    }

    @Override
    public Object convertToDatabaseColumn(Map<String, Object> objectValue) {
        if (implementation == null) {
            throw new IllegalArgumentException("Unable to serialize to json field , no concrete implementation");
        }
        return implementation.convertToDatabaseColumn(objectValue);

    }

    @Override
    public Map<String, Object> convertToEntityAttribute(Object dataValue) {
        if (implementation == null) {
            throw new IllegalArgumentException("Unable to deserialize from json field , no concrete implementation");
        }
        return implementation.convertToEntityAttribute(dataValue);

    }

}
