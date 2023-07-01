package com.wizzdi.flexicore.boot.dynamic.invokers.request;

import com.wizzdi.flexicore.boot.dynamic.invokers.response.ParameterInfo;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScanFieldRequest {
    private final Class<?> type;
    private final List<Field> brotherFields;
    private final Field handledField;
    private Map<Field, ParameterInfo> parameterInfoCache;


    public ScanFieldRequest(Class<?> type, List<Field> brotherFields, Field handledField) {
        this.type = type;
        this.brotherFields = brotherFields;
        this.handledField = handledField;
        this.parameterInfoCache =new HashMap<>();
    }

    public ScanFieldRequest(Map<Field, ParameterInfo> parameterInfoCache, Class<?> type, List<Field> brotherFields, Field handledField) {
        this(type, brotherFields, handledField);
        this.parameterInfoCache = parameterInfoCache;
    }

    public Class<?> getType() {
        return type;
    }

    public List<Field> getBrotherFields() {
        return brotherFields;
    }

    public Field getHandledField() {
        return handledField;
    }

    public Map<Field, ParameterInfo> getParameterInfoCache() {
        return parameterInfoCache;
    }
}
