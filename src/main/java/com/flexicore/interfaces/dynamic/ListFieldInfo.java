package com.flexicore.interfaces.dynamic;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@com.wizzdi.flexicore.boot.dynamic.invokers.annotations.ListFieldInfo
public @interface ListFieldInfo {
    @AliasFor(annotation = com.wizzdi.flexicore.boot.dynamic.invokers.annotations.ListFieldInfo.class,attribute = "displayName")
    String displayName() default "";
    @AliasFor(annotation = com.wizzdi.flexicore.boot.dynamic.invokers.annotations.ListFieldInfo.class,attribute = "description")

    String description() default "";
    @AliasFor(annotation = com.wizzdi.flexicore.boot.dynamic.invokers.annotations.ListFieldInfo.class,attribute = "mandatory")

    boolean mandatory() default false;
    @AliasFor(annotation = com.wizzdi.flexicore.boot.dynamic.invokers.annotations.ListFieldInfo.class,attribute = "listType")

    Class<?> listType() default Void.class;
    
}
