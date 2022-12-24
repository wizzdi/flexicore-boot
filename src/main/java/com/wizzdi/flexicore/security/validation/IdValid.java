package com.wizzdi.flexicore.security.validation;

import com.flexicore.model.Basic;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = IdValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface IdValid {

    String message() default "could not find id for field";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};


    String field() default "";
    Class<? extends Basic> fieldType() default Basic.class;
    String targetField() default "";
    @Target({ ElementType.TYPE })
    @Retention(RetentionPolicy.RUNTIME)
    @interface List {
        IdValid[] value();
    }
}
