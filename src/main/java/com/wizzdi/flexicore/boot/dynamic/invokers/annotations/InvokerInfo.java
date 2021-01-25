package com.wizzdi.flexicore.boot.dynamic.invokers.annotations;

import com.flexicore.annotations.OperationsInside;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@OperationsInside
public @interface InvokerInfo {
    String displayName() default "";
    String description() default "";

}
