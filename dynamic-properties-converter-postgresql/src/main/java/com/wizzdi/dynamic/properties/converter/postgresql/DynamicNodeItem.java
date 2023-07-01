package com.wizzdi.dynamic.properties.converter.postgresql;

import java.util.Map;

public class DynamicNodeItem extends DynamicFilterItem{

    private Map<String,DynamicFilterItem> children;

    public DynamicNodeItem(Map<String, DynamicFilterItem> children) {
        this.children = children;
    }

    public DynamicNodeItem() {
    }

    public Map<String, DynamicFilterItem> getChildren() {
        return children;
    }

    public <T extends DynamicNodeItem> T setChildren(Map<String, DynamicFilterItem> children) {
        this.children = children;
        return (T) this;
    }
}
