package com.wizzdi.flexicore.security.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = ClazzValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ClazzValid {

    String message() default "could not find clazz for field";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
