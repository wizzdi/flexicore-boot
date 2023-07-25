package com.wizzdi.flexicore.common.user.annotations;

import com.wizzdi.flexicore.common.user.init.CommonUserModule;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Import(CommonUserModule.class)
@Configuration
public @interface EnableCommonUserModule {
}
