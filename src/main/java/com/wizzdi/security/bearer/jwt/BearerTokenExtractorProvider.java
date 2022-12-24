package com.wizzdi.security.bearer.jwt;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;

import static org.springframework.util.ObjectUtils.isEmpty;

@ConditionalOnMissingBean(TokenExtractor.class)
@Configuration
public class BearerTokenExtractorProvider {

    @Bean
    public TokenExtractor tokenExtractor() {
        return this::extractToken;
    }

    public String extractToken(HttpServletRequest request) {
        final String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (isEmpty(header) || !header.startsWith("Bearer ")) {
            return null;
        }

        // Get jwt token and validate
        return header.split(" ")[1].trim();
    }
}
