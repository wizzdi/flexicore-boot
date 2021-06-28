package com.wizzdi.flexicore.boot.dynamic.invokers.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface ListFieldInfo {
    String displayName() default "";
    String description() default "";
    boolean mandatory() default false;
    Class<?> listType() default Void.class;
    boolean ignoreSubParameters() default false;


}
