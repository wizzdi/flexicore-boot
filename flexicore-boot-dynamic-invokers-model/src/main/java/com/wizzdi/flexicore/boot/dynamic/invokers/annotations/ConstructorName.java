package com.wizzdi.flexicore.boot.dynamic.invokers.annotations;

import com.wizzdi.flexicore.boot.base.interfaces.Plugin;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ConstructorName {

    String value() default "";
}
