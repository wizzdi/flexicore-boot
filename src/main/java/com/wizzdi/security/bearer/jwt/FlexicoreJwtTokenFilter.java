package com.wizzdi.security.bearer.jwt;

import com.flexicore.security.SecurityContextBase;
import com.wizzdi.security.adapter.FlexiCoreAuthentication;
import com.wizzdi.security.adapter.FlexiCoreSecurityFilter;
import com.wizzdi.security.adapter.OperationInterceptor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;


@Component
public class FlexicoreJwtTokenFilter extends OncePerRequestFilter implements FlexiCoreSecurityFilter {


    private final JWTSecurityContextCreator JWTSecurityContextCreator;
    private final TokenExtractor tokenExtractor;


    public FlexicoreJwtTokenFilter(JWTSecurityContextCreator JWTSecurityContextCreator, TokenExtractor tokenExtractor) {
        this.JWTSecurityContextCreator = JWTSecurityContextCreator;
        this.tokenExtractor=tokenExtractor;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain)
            throws ServletException, IOException {
        // Get authorization header and validate
        String token=tokenExtractor.extractToken(request);
        if(token==null){
            chain.doFilter(request,response);
            return;
        }
        FlexiCoreAuthentication authentication= JWTSecurityContextCreator.getSecurityContext(token);
        if(authentication==null){
            chain.doFilter(request, response);
            return;
        }
        SecurityContextBase securityContext=authentication.getSecurityContextBase();

        authentication.setDetails(
                new WebAuthenticationDetailsSource().buildDetails(request)
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        request.setAttribute(OperationInterceptor.SECURITY_CONTEXT,securityContext);
        chain.doFilter(request, response);
    }


}