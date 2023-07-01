package com.flexicore.interceptors;

import com.flexicore.interfaces.ServicePlugin;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Configuration
@Extension
public class CorsBean implements ServicePlugin {

    @Value("${flexicore.cores.allowOrigin:@null}")
    private String allowOrigin;
    @Value("${flexicore.cores.allowOriginPattern:*}")
    private String allowOriginPattern;

    @Bean
    public FilterRegistrationBean<CorsFilter> corsFilter() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        final CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        if(allowOrigin!=null){
            config.setAllowedOrigins(Stream.of(allowOrigin).collect(Collectors.toList()));

        }
        config.setAllowedOriginPatterns(Collections.singletonList(allowOriginPattern));
        config.setAllowedHeaders(Collections.singletonList("*"));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "OPTIONS", "DELETE", "PATCH"));
        source.registerCorsConfiguration("/**", config);
        return new FilterRegistrationBean<>(new CorsFilter(source));
    }
}
