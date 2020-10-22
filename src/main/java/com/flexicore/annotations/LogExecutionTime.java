package com.flexicore.annotations;

import javax.interceptor.InterceptorBinding;
import java.lang.annotation.*;

/**
 * Created by Asaf on 18/12/2016.
 */
@Retention(RetentionPolicy.RUNTIME)
@InterceptorBinding
@Target({ElementType.METHOD, ElementType.TYPE})
@Inherited
public @interface LogExecutionTime {

}
