package com.wizzdi.dynamic.properties.converter.postgresql;


public class DynamicPredicateItem extends DynamicFilterItem {
    private FilterType filterType;
    private Object value;


    public DynamicPredicateItem(FilterType filterType, Object value) {
        this.filterType = filterType;
        this.value = value;
    }

    public DynamicPredicateItem() {
    }

    public FilterType getFilterType() {
        return filterType;
    }

    public <T extends DynamicPredicateItem> T setFilterType(FilterType filterType) {
        this.filterType = filterType;
        return (T) this;
    }

    public Object getValue() {
        return value;
    }

    public <T extends DynamicPredicateItem> T setValue(Object value) {
        this.value = value;
        return (T) this;
    }
}
