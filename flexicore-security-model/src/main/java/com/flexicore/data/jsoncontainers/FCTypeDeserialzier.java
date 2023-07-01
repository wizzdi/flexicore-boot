package com.flexicore.data.jsoncontainers;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.jsontype.impl.AsPropertyTypeDeserializer;

import java.io.IOException;

public class FCTypeDeserialzier extends AsPropertyTypeDeserializer {

    public FCTypeDeserialzier(JavaType bt, TypeIdResolver idRes, String typePropertyName, boolean typeIdVisible, JavaType defaultImpl) {
        super(bt, idRes, typePropertyName, typeIdVisible, defaultImpl);
    }

    public FCTypeDeserialzier(JavaType bt, TypeIdResolver idRes, String typePropertyName, boolean typeIdVisible, JavaType defaultImpl, JsonTypeInfo.As inclusion) {
        super(bt, idRes, typePropertyName, typeIdVisible, defaultImpl, inclusion);
    }

    public FCTypeDeserialzier(AsPropertyTypeDeserializer src, BeanProperty property) {
        super(src, property);
    }

    @Override
    public Object deserializeTypedFromAny(JsonParser p, DeserializationContext ctxt) throws IOException {
        return super.deserializeTypedFromAny(p, ctxt);
    }

    @Override
    public Object deserializeTypedFromObject(JsonParser p, DeserializationContext ctxt) throws IOException {
        return super.deserializeTypedFromObject(p, ctxt);
    }
}
