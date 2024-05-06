package com.wizzdi.dynamic.properties.converter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.postgresql.util.PGobject;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Converter(autoApply = false)
@Component
public class PostgresqlJsonConverter implements JsonConverterImplementation, AttributeConverter<Map<String, Object>, Object> {

    private static final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    private static TypeReference<Map<String, Object>> type= new TypeReference<>() {};

    @Override
    public Object convertToDatabaseColumn(Map<String, Object> objectValue) {
        try {
            PGobject out = new PGobject();
            out.setType("jsonb");
            String value = mapper.writeValueAsString(objectValue);
            out.setValue(value);
            return out;
        } catch (Exception e) {
            throw new IllegalArgumentException("Unable to serialize to json field ", e);
        }
    }

    @Override
    public Map<String, Object> convertToEntityAttribute(Object dataValue) {
        try {
            if (dataValue instanceof PGobject && ((PGobject) dataValue).getType().equals("jsonb")) {
                String value = ((PGobject) dataValue).getValue();
                return mapper.readValue(value,type);
            }
            return null;
        } catch (IOException e) {
            throw new IllegalArgumentException("Unable to deserialize to json field ", e);
        }
    }
}