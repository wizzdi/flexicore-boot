package com.wizzdi.flexicore.boot.dynamic.invokers.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface FieldInfo {
    String displayName() default "";
    String description() default "No Description";
    boolean mandatory() default false;
    String defaultValue() default "";
    boolean enableRegexValidation() default false;
    String regexValidation() default "";
    boolean rangeEnabled() default false;
    double rangeMin() default Double.NEGATIVE_INFINITY;
    double rangeMax() default Double.POSITIVE_INFINITY;
    double valueSteps() default Double.MIN_VALUE;
    boolean actionIdHolder() default false;
    boolean ignoreSubParameters() default false;



}
