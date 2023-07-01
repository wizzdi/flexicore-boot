package com.wizzdi.dynamic.properties.converter.postgresql;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonSubTypes({
        @JsonSubTypes.Type(value = DynamicPredicateItem.class, name = "DynamicPredicateItem"),
        @JsonSubTypes.Type(value = DynamicNodeItem.class, name = "DynamicNodeItem")
})
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", visible = true)
public abstract class DynamicFilterItem {

}
