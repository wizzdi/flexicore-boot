package com.wizzdi.flexicore.boot.dynamic.invokers.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface IdRefFieldInfo {
    String displayName() default "";
    String description() default "";
    boolean mandatory() default false;
    Class<?> refType() default Void.class;
    boolean list() default true;
    boolean actionId() default false;

}
