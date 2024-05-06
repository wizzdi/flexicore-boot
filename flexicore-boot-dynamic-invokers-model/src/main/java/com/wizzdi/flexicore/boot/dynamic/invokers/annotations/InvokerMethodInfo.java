package com.wizzdi.flexicore.boot.dynamic.invokers.annotations;

import com.flexicore.annotations.IOperation;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD,ElementType.ANNOTATION_TYPE})
@IOperation
public @interface InvokerMethodInfo {
    @AliasFor(annotation = IOperation.class,attribute = "Name")
    String displayName() default "";
    @AliasFor(annotation = IOperation.class,attribute = "Description")
    String description() default "";
    String[] categories() default {};
    String[] relatedMethodNames() default {};
    @AliasFor(annotation = IOperation.class,attribute = "access")
    IOperation.Access access() default IOperation.Access.allow;
    @AliasFor(annotation = IOperation.class,attribute = "relatedClazzes")
    Class<?>[] relatedClasses() default {};

}
