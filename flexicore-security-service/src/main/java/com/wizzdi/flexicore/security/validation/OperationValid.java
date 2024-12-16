package com.wizzdi.flexicore.security.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = OperationValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface OperationValid {

    String message() default "could not find clazz for field";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    String targetField();
    String sourceField();

}
