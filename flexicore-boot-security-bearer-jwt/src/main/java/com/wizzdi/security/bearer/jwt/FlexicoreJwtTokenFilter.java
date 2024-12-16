package com.wizzdi.security.bearer.jwt;

import com.wizzdi.flexicore.security.configuration.SecurityContext;
import com.wizzdi.security.adapter.FlexiCoreAuthentication;
import com.wizzdi.security.adapter.FlexiCoreSecurityFilter;
import com.wizzdi.security.adapter.OperationInterceptor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.HandlerMapping;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;


@Component
public class FlexicoreJwtTokenFilter extends OncePerRequestFilter implements FlexiCoreSecurityFilter {


    private final JWTSecurityContextCreator JWTSecurityContextCreator;
    private final TokenExtractor tokenExtractor;
    private final RequestAttributeSecurityContextRepository requestAttributeSecurityContextRepository=new RequestAttributeSecurityContextRepository();
    private final HandlerMapping handlerMapping;


    public FlexicoreJwtTokenFilter(JWTSecurityContextCreator JWTSecurityContextCreator, TokenExtractor tokenExtractor, @Qualifier("requestMappingHandlerMapping") HandlerMapping handlerMapping) {
        this.JWTSecurityContextCreator = JWTSecurityContextCreator;
        this.tokenExtractor=tokenExtractor;
        this.handlerMapping=handlerMapping;

    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain)
            throws ServletException, IOException {
        // Get authorization header and validate
        String token=tokenExtractor.extractToken(request);
        FlexiCoreAuthentication authentication= token!=null?JWTSecurityContextCreator.getSecurityContext(token):null;
        if(authentication==null){
            if(isProtected(request)){
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED,"Invalid token");
            }
            else{
                chain.doFilter(request, response);
            }
            return;

        }
        SecurityContext securityContext=authentication.getSecurityContextBase();

        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        request.setAttribute(OperationInterceptor.SECURITY_CONTEXT,securityContext);
        this.requestAttributeSecurityContextRepository.saveContext(SecurityContextHolder.getContext(),request,response);
        chain.doFilter(request, response);
    }

    private boolean isProtected(HttpServletRequest request) {
        try {
            HandlerExecutionChain handler = handlerMapping.getHandler(request);
            if(handler.getHandler() instanceof HandlerMethod handlerMethod){
                Method method = handlerMethod.getMethod();
                return Arrays.stream(method.getParameterTypes()).anyMatch(SecurityContext.class::isAssignableFrom);
            }
        } catch (Exception e) {
            logger.debug("unable to determine if request is protected",e);
        }
        return false;

    }


}
