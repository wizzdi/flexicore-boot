package com.wizzdi.flexicore.boot.dynamic.invokers.annotations;

import com.flexicore.annotations.TypeRetention;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@TypeRetention
@Target(ElementType.TYPE)
@Repeatable(VirtualField.List.class)
public @interface VirtualField {
    String name();
    String mappedBy();
    @AliasFor(annotation = TypeRetention.class,attribute = "value")
    Class<?> type();
    boolean list() default true;

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @interface List{
        VirtualField[] value() default {};
    }
}
