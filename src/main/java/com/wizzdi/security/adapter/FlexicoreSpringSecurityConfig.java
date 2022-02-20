package com.wizzdi.security.adapter;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletResponse;

@Configuration
public class FlexicoreSpringSecurityConfig extends WebSecurityConfigurerAdapter {
    private final ObjectProvider<FlexiCoreSecurityFilter> flexiCoreSecurityFilters;
    private final ObjectProvider<SecurityPathConfigurator> securityPathConfigurators;
    private final UserDetailsService userDetailsService;

    public FlexicoreSpringSecurityConfig(ObjectProvider<FlexiCoreSecurityFilter> flexiCoreSecurityFilters, @Lazy UserDetailsService userDetailsService, ObjectProvider<SecurityPathConfigurator> securityPathConfigurators) {
        this.flexiCoreSecurityFilters = flexiCoreSecurityFilters;
        this.userDetailsService = userDetailsService;
        this.securityPathConfigurators=securityPathConfigurators;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
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

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);

    }


    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

}
