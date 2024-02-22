package com.wizzdi.dynamic.properties.converter.postgresql;

import java.util.Map;

public class AuthorFilter {

    private Map<String, DynamicFilterItem> dynamicPropertiesFilter;

    private Map<String,DynamicFilterItem> staticPropertiesFilter;


    public Map<String, DynamicFilterItem> getDynamicPropertiesFilter() {
        return dynamicPropertiesFilter;
    }

    public <T extends AuthorFilter> T setDynamicPropertiesFilter(Map<String, DynamicFilterItem> dynamicPropertiesFilter) {
        this.dynamicPropertiesFilter = dynamicPropertiesFilter;
        return (T) this;
    }

    public Map<String, DynamicFilterItem> getStaticPropertiesFilter() {
        return staticPropertiesFilter;
    }

    public <T extends AuthorFilter> T setStaticPropertiesFilter(Map<String, DynamicFilterItem> staticPropertiesFilter) {
        this.staticPropertiesFilter = staticPropertiesFilter;
        return (T) this;
    }
}
