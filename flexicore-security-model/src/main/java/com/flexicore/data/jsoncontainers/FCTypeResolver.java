package com.flexicore.data.jsoncontainers;

import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.annotation.NoClass;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.jsontype.impl.StdTypeResolverBuilder;

import java.util.Collection;

public class FCTypeResolver extends StdTypeResolverBuilder {

    @Override
    public TypeSerializer buildTypeSerializer(SerializationConfig config, JavaType baseType, Collection<NamedType> subtypes) {
        return super.buildTypeSerializer(config, baseType, subtypes);
    }

    @Override
    public TypeDeserializer buildTypeDeserializer(DeserializationConfig config, JavaType baseType, Collection<NamedType> subtypes) {
        JavaType defaultImpl;
        if ((_defaultImpl == Void.class)
                || (_defaultImpl == NoClass.class)) {
            defaultImpl = config.getTypeFactory().constructType(_defaultImpl);
        } else {
            defaultImpl = config.getTypeFactory()
                    .constructSpecializedType(baseType, baseType.getRawClass());
        }
        return useForType(baseType)?super.buildTypeDeserializer(config, baseType, subtypes):new FCTypeDeserialzier(baseType,idResolver(config, baseType,this.subTypeValidator(config), subtypes, false, true),getTypeProperty(),_typeIdVisible,defaultImpl);
    }

    public boolean useForType(JavaType t) {
        return t.isJavaLangObject();
    }
}
