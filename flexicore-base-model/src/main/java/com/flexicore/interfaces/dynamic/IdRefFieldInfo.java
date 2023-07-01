package com.flexicore.interfaces.dynamic;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@com.wizzdi.flexicore.boot.dynamic.invokers.annotations.IdRefFieldInfo
public @interface IdRefFieldInfo {
    @AliasFor(annotation=com.wizzdi.flexicore.boot.dynamic.invokers.annotations.IdRefFieldInfo.class,attribute = "displayName")
    String displayName() default "";
    @AliasFor(annotation=com.wizzdi.flexicore.boot.dynamic.invokers.annotations.IdRefFieldInfo.class,attribute = "description")

    String description() default "";
    @AliasFor(annotation=com.wizzdi.flexicore.boot.dynamic.invokers.annotations.IdRefFieldInfo.class,attribute = "mandatory")

    boolean mandatory() default false;
    @AliasFor(annotation=com.wizzdi.flexicore.boot.dynamic.invokers.annotations.IdRefFieldInfo.class,attribute = "refType")

    Class<?> refType() default Void.class;
    @AliasFor(annotation=com.wizzdi.flexicore.boot.dynamic.invokers.annotations.IdRefFieldInfo.class,attribute = "list")

    boolean list() default true;
    @AliasFor(annotation=com.wizzdi.flexicore.boot.dynamic.invokers.annotations.IdRefFieldInfo.class,attribute = "actionId")

    boolean actionId() default false;

}
