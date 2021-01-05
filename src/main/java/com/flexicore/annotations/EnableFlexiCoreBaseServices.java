package com.flexicore.annotations;

import com.flexicore.init.FlexiCoreBaseModule;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Import(FlexiCoreBaseModule.class)
@Configuration
public @interface EnableFlexiCoreBaseServices {
}
