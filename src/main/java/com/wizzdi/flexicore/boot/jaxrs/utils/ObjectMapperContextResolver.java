/*******************************************************************************
 *  Copyright (C) FlexiCore, Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Avishay Ben Natan And Asaf Ben Natan, October 2015
 ******************************************************************************/
package com.wizzdi.flexicore.boot.jaxrs.utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.wizzdi.flexicore.boot.rest.views.Views;


import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Provider
public class ObjectMapperContextResolver implements ContextResolver<ObjectMapper> {

    private Map<ClassLoader, ObjectMapper> classLoaderObjectMapperMap = new ConcurrentHashMap<>();
    private static ObjectMapper defaultMapper = null;
    private static final String[] formats = new String[]{"yyyy-MM-dd'T'HH:mm:ss.SSS", "yyyy-MM-dd'T'HH:mm:ss.SSSXXX"};


    static{
        defaultMapper = createClassLoaderObjectMapper(null);

    }

    public static ObjectMapper createClassLoaderObjectMapper(ClassLoader classLoader) {
        return createClassLoaderObjectMapper(classLoader, Views.Unrefined.class);
    }

    public static void configureToDefault(ObjectMapper objectMapper){
        configureObjectMapper(null, Views.Unrefined.class,objectMapper);
    }


    public static ObjectMapper createClassLoaderObjectMapper(ClassLoader classLoader, Class<? extends Views.Unrefined> view) {
        ObjectMapper mapper = new ObjectMapper();
        mapper = configureObjectMapper(classLoader, view, mapper);
        return mapper;
    }

    private static ObjectMapper configureObjectMapper(ClassLoader classLoader, Class<? extends Views.Unrefined> view, ObjectMapper mapper) {
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(MapperFeature.IGNORE_DUPLICATE_MODULE_REGISTRATIONS, false);
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        mapper.configure(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE, false);
        mapper.configure(DeserializationFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS,false);
        JavaTimeModule module = new JavaTimeModule();
        CustomOffsetDateTimeSerializer customOffsetDateTimeSerializer = new CustomOffsetDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX"));
        module.addSerializer(OffsetDateTime.class, customOffsetDateTimeSerializer);

        mapper.registerModule(module);

        mapper = setView(mapper, view);
        if (classLoader != null) {
            TypeFactory tf = TypeFactory.defaultInstance().withClassLoader(classLoader);
            mapper.setTypeFactory(tf);
            System.out.println("Created Object Mapper For " + classLoader);


        } else {
            System.out.println("Created Default Object Mapper");
        }
        return mapper;
    }

    public static ObjectMapper getDefaultMapper() {
        return defaultMapper;
    }

    public static ObjectMapper setView(ObjectMapper objectMapper, Class<? extends Views.Unrefined> c) {
        return objectMapper.setConfig(objectMapper.getSerializationConfig().withView(c));
    }

    @Override
    public ObjectMapper getContext(Class<?> type) {
            return defaultMapper;
    }


}
