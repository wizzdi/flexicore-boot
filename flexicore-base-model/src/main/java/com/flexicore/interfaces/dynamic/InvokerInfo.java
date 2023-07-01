package com.flexicore.interfaces.dynamic;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@com.wizzdi.flexicore.boot.dynamic.invokers.annotations.InvokerInfo
public @interface InvokerInfo {
    @AliasFor(annotation = com.wizzdi.flexicore.boot.dynamic.invokers.annotations.InvokerInfo.class,attribute = "displayName")
    String displayName() default "";
    @AliasFor(annotation = com.wizzdi.flexicore.boot.dynamic.invokers.annotations.InvokerInfo.class,attribute = "description")
    String description() default "";

}
