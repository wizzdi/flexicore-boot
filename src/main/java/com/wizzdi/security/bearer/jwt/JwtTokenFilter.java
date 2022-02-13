package com.wizzdi.security.bearer.jwt;

import com.flexicore.model.SecurityUser;
import com.flexicore.security.SecurityContextBase;
import com.wizzdi.flexicore.security.interfaces.SecurityContextProvider;
import com.wizzdi.flexicore.security.service.SecurityUserService;
import com.wizzdi.security.adapter.FlexiCoreAuthentication;
import com.wizzdi.security.adapter.FlexiCoreSecurityFilter;
import com.wizzdi.security.adapter.FlexicoreUserDetails;
import com.wizzdi.security.adapter.OperationInterceptor;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.springframework.util.ObjectUtils.isEmpty;

@Component
public class JwtTokenFilter extends OncePerRequestFilter implements FlexiCoreSecurityFilter {

    private final JwtTokenUtil jwtTokenUtil;
    private final SecurityUserService securityUserService;
    private final SecurityContextProvider securityContextProvider;
    private final TokenExtractor tokenExtractor;


    public JwtTokenFilter(JwtTokenUtil jwtTokenUtil,
                          SecurityUserService securityUserService, SecurityContextProvider securityContextProvider,TokenExtractor tokenExtractor) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.securityUserService = securityUserService;
        this.securityContextProvider=securityContextProvider;
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
        Jws<Claims> claimsJws = jwtTokenUtil.getClaims(token);
        if (claimsJws==null) {
            chain.doFilter(request, response);
            return;
        }
        // Get user identity and set it on the spring security context
        String id = jwtTokenUtil.getId(claimsJws);
        SecurityUser securityUser = securityUserService.getByIdOrNull(id, SecurityUser.class, null);
        FlexicoreUserDetails userDetails = getUserDetails(securityUser);
        SecurityContextBase securityContext = securityContextProvider.getSecurityContext(securityUser);
        FlexiCoreAuthentication
                authentication = new FlexiCoreAuthentication(
                userDetails, securityContext
        );

        authentication.setDetails(
                new WebAuthenticationDetailsSource().buildDetails(request)
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        request.setAttribute(OperationInterceptor.SECURITY_CONTEXT,securityContext);
        chain.doFilter(request, response);
    }

    private FlexicoreUserDetails getUserDetails(SecurityUser securityUser) {
        return new FlexicoreUserDetails(securityUser.getId(),null, null);
    }

}