package com.wizzdi.security.bearer.jwt;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Import(FlexiCoreJWTSecurityModule.class)
@Configuration
public @interface EnableBearerJwtAuthentication {
}
