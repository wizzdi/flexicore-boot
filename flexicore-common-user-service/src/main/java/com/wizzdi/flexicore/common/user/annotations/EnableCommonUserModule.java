package com.wizzdi.flexicore.common.user.annotations;

import com.wizzdi.flexicore.common.user.init.CommonUserModule;
import com.wizzdi.flexicore.security.init.FlexiCoreSecurityModule;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
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
