package com.wizzdi.flexicore.security.annotations;

import com.wizzdi.flexicore.security.init.FlexiCoreSecurityModule;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Import(FlexiCoreSecurityModule.class)
@Configuration
public @interface EnableFlexiCoreSecurity {
}
