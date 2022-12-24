package com.wizzdi.dynamic.properties.converter;

import jakarta.persistence.Converter;
import java.util.Map;

@Converter(autoApply = false)
public class JsonConverter implements jakarta.persistence.AttributeConverter<Map<String, Object>, Object> {

    private static final long serialVersionUID = 1L;
    static JsonConverterImplementation implementation = null;

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