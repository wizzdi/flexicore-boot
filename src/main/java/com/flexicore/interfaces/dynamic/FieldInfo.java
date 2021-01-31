package com.flexicore.interfaces.dynamic;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@com.wizzdi.flexicore.boot.dynamic.invokers.annotations.FieldInfo
public @interface FieldInfo {
    @AliasFor(annotation = com.wizzdi.flexicore.boot.dynamic.invokers.annotations.FieldInfo.class,attribute = "displayName")
    String displayName() default "";
    @AliasFor(annotation = com.wizzdi.flexicore.boot.dynamic.invokers.annotations.FieldInfo.class,attribute = "description")
    String description() default "No Description";
    @AliasFor(annotation = com.wizzdi.flexicore.boot.dynamic.invokers.annotations.FieldInfo.class,attribute = "mandatory")
    boolean mandatory() default false;
    @AliasFor(annotation = com.wizzdi.flexicore.boot.dynamic.invokers.annotations.FieldInfo.class,attribute = "defaultValue")
    String defaultValue() default "";
    @AliasFor(annotation = com.wizzdi.flexicore.boot.dynamic.invokers.annotations.FieldInfo.class,attribute = "enableRegexValidation")
    boolean enableRegexValidation() default false;
    @AliasFor(annotation = com.wizzdi.flexicore.boot.dynamic.invokers.annotations.FieldInfo.class,attribute = "regexValidation")
    String regexValidation() default "";
    @AliasFor(annotation = com.wizzdi.flexicore.boot.dynamic.invokers.annotations.FieldInfo.class,attribute = "rangeEnabled")
    boolean rangeEnabled() default false;
    @AliasFor(annotation = com.wizzdi.flexicore.boot.dynamic.invokers.annotations.FieldInfo.class,attribute = "rangeMin")
    double rangeMin() default Double.NEGATIVE_INFINITY;
    @AliasFor(annotation = com.wizzdi.flexicore.boot.dynamic.invokers.annotations.FieldInfo.class,attribute = "rangeMax")
    double rangeMax() default Double.POSITIVE_INFINITY;
    @AliasFor(annotation = com.wizzdi.flexicore.boot.dynamic.invokers.annotations.FieldInfo.class,attribute = "valueSteps")
    double valueSteps() default Double.MIN_VALUE;



}
