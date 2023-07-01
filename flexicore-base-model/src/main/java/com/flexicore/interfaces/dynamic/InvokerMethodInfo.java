package com.flexicore.interfaces.dynamic;

import com.flexicore.annotations.IOperation;
import com.flexicore.model.Baseclass;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@com.wizzdi.flexicore.boot.dynamic.invokers.annotations.InvokerMethodInfo
public @interface InvokerMethodInfo {
    @AliasFor(annotation = com.wizzdi.flexicore.boot.dynamic.invokers.annotations.InvokerMethodInfo.class,attribute = "displayName")
    String displayName() default "";
    @AliasFor(annotation = com.wizzdi.flexicore.boot.dynamic.invokers.annotations.InvokerMethodInfo.class,attribute = "description")

    String description() default "";
    @AliasFor(annotation = com.wizzdi.flexicore.boot.dynamic.invokers.annotations.InvokerMethodInfo.class,attribute = "categories")

    String[] categories() default {};
    @AliasFor(annotation = com.wizzdi.flexicore.boot.dynamic.invokers.annotations.InvokerMethodInfo.class,attribute = "relatedMethodNames")

    String[] relatedMethodNames() default {};
    @AliasFor(annotation = com.wizzdi.flexicore.boot.dynamic.invokers.annotations.InvokerMethodInfo.class,attribute = "access")

    IOperation.Access access() default IOperation.Access.allow;
    @AliasFor(annotation = com.wizzdi.flexicore.boot.dynamic.invokers.annotations.InvokerMethodInfo.class,attribute = "relatedClasses")

    Class<? extends Baseclass>[] relatedClasses() default {};

}
