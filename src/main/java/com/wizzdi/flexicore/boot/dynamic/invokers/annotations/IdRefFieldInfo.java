package com.wizzdi.flexicore.boot.dynamic.invokers.annotations;

import com.flexicore.annotations.IOperation;
import com.flexicore.annotations.TypeRetention;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@TypeRetention
public @interface IdRefFieldInfo {
    String displayName() default "";
    String description() default "";
    boolean mandatory() default false;
    @AliasFor(annotation = TypeRetention.class,attribute = "value")
    Class<?> refType() default Void.class;
    boolean list() default true;
    boolean actionId() default false;

}
