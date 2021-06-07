package com.wizzdi.dynamic.properties.converter;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import javax.persistence.Converter;
import java.util.Map;

@Converter(autoApply = false)
@Component
public class JsonConverter implements javax.persistence.AttributeConverter<Map<String, Object>, Object>, ApplicationContextAware {

    private static final long serialVersionUID = 1L;
    private static JsonConverterImplementation implementation = null;

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

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        JsonConverter.implementation = applicationContext.getBean(JsonConverterImplementation.class);
    }
}