package com.wizzdi.flexicore.boot.rest.resolvers;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.DatabindContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.jsontype.impl.TypeIdResolverBase;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

public class CrossLoaderResolver extends TypeIdResolverBase {

    private static Map<String, Class<?>> types = new HashMap<>();
    private JavaType superType;
    private static final Queue<ClassLoader> classLoaders=new ConcurrentLinkedQueue<>();

    @Override
    public void init(JavaType baseType) {
        superType = baseType;
        classLoaders.add(Thread.currentThread().getContextClassLoader());
    }


    @Override
    public String idFromValue(Object obj) {
        return idFromValueAndType(obj, obj.getClass());
    }

    @Override
    public JavaType typeFromId(DatabindContext context, String id) throws IOException {
        Class<?> c = types.get(id);
        if(c==null){
            try {
                c= Class.forName(id,true,superType.getRawClass().getClassLoader());
            } catch (ClassNotFoundException e) {
                for (ClassLoader classLoader : classLoaders) {
                    try {
                        c = Class.forName(id, true, classLoader);
                        break;
                    }
                    catch (ClassNotFoundException ignored){

                    }

                }
                if(c==null){
                    throw new IOException("could not find type "+id,e);
                }
            }
        }

        return context.getTypeFactory().withClassLoader(c.getClassLoader()).constructSpecializedType(superType,c);
    }

    @Override
    public String idFromValueAndType(Object obj, Class<?> subType) {
        return subType.getCanonicalName();
    }

    @Override
    public JsonTypeInfo.Id getMechanism() {
        return JsonTypeInfo.Id.CLASS;
    }

    public static void registerClassLoader(ClassLoader... classLoaders){
        CrossLoaderResolver.classLoaders.addAll(Arrays.asList(classLoaders));
    }

    public static void registerClassLoader(List<ClassLoader> classLoaders){
        CrossLoaderResolver.classLoaders.addAll(classLoaders);
    }

    public static void registerClass(Class<?> c){
        types.put(c.getCanonicalName(),c);
    }
    public static void registerClass(String id,Class<?> c){
        types.put(id,c);
    }


    public static void registerClass(Class<?>... c){
        for (Class<?> aClass : c) {
            types.put(aClass.getCanonicalName(),aClass);
        }
    }

    public static Class<?> getRegisteredClass(String classFullName) {
        return types.computeIfAbsent(classFullName,f-> getClass(classFullName));
    }

    private static Class<?> getClass(String classFullName) {
        try {
            return Class.forName(classFullName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

}
