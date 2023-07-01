package com.wizzdi.dynamic.properties.converter;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.util.HashMap;
import java.util.Map;

@Entity
public class Author {

    @Id
    private String id;

    private String name;


    @Column(columnDefinition = "jsonb")
    @Convert(converter = JsonConverter.class)
    private Map<String, Object> dynamicProperties=new HashMap<>();

    @Id
    public String getId() {
        return id;
    }

    public <T extends Author> T setId(String id) {
        this.id = id;
        return (T) this;
    }

    @Column(columnDefinition = "jsonb")
    @Convert(converter = JsonConverter.class)
    public Map<String, Object> getDynamicProperties() {
        return dynamicProperties;
    }

    public <T extends Author> T setDynamicProperties(Map<String, Object> dynamicProperties) {
        this.dynamicProperties = dynamicProperties;
        return (T) this;
    }

    public String getName() {
        return name;
    }

    public <T extends Author> T setName(String name) {
        this.name = name;
        return (T) this;
    }
}
