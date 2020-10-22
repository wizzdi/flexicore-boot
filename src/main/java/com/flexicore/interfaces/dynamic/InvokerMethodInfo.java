package com.flexicore.interfaces.dynamic;

import com.flexicore.annotations.IOperation;
import com.flexicore.model.Baseclass;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface InvokerMethodInfo {
    String displayName() default "";
    String description() default "";
    String[] categories() default {};
    String[] relatedMethodNames() default {};
    IOperation.Access access() default IOperation.Access.allow;
    Class<? extends Baseclass>[] relatedClasses() default {};

}
