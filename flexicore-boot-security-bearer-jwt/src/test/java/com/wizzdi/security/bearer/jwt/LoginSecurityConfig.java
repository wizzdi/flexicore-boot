package com.wizzdi.security.bearer.jwt;

import com.wizzdi.security.adapter.SecurityPathConfigurator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

@Configuration
public class LoginSecurityConfig {





   @Bean
   @Order(99)
    public SecurityPathConfigurator loginPath(){
        return f -> {
            f.requestMatchers("/api/public/**").permitAll();
            f.anyRequest().authenticated();
            return f;
        };


    }
}
