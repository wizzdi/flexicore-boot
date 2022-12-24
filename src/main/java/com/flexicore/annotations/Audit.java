package com.flexicore.annotations;

import jakarta.enterprise.util.Nonbinding;
import jakarta.interceptor.InterceptorBinding;
import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@InterceptorBinding
@Target({ElementType.METHOD, ElementType.TYPE})
@Inherited
public @interface Audit {
    @Nonbinding String auditType() default "Default";
}
