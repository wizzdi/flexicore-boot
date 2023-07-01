package com.wizzdi.security.adapter;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class FlexicoreSpringSecurityConfig {


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,ObjectProvider<FlexiCoreSecurityFilter> flexiCoreSecurityFilters,ObjectProvider<SecurityPathConfigurator> securityPathConfigurators) throws Exception {
        // Enable CORS and disable CSRF
        http = http.cors().and().csrf().disable();

        // Set session management to stateless
        http = http
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and();

        // Set unauthorized requests exception handler
        http = http
                .exceptionHandling()
                .authenticationEntryPoint(
                        (request, response, ex) -> {
                            response.sendError(
                                    HttpServletResponse.SC_UNAUTHORIZED,
                                    ex.getMessage()
                            );
                        }
                )
                .and();
        ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry expressionInterceptUrlRegistry = http.authorizeRequests();
        for (SecurityPathConfigurator securityPathConfigurator : securityPathConfigurators) {
            expressionInterceptUrlRegistry=securityPathConfigurator.configure(expressionInterceptUrlRegistry);
        }



        // Add JWT token filter
        for (FlexiCoreSecurityFilter flexiCoreSecurityFilter : flexiCoreSecurityFilters) {
            http.addFilterBefore(
                    flexiCoreSecurityFilter,
                    UsernamePasswordAuthenticationFilter.class
            );
        }
        return http.build();
    }




    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http,UserDetailsService userDetailsService) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(userDetailsService);
        return authenticationManagerBuilder.build();
    }

}
