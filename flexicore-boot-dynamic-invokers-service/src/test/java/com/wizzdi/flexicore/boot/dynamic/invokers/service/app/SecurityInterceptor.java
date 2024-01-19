package com.wizzdi.flexicore.boot.dynamic.invokers.service.app;

import com.flexicore.security.SecurityContextBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * this is a fake security interceptor using the admin security context every time used for testing.
 */

@Component
public class SecurityInterceptor implements HandlerInterceptor {

	private static final String AUTHENTICATION_KEY = "authenticationKey";

	@Autowired
	@Qualifier("adminSecurityContext")
	private SecurityContextBase securityContextBase;


	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
			request.setAttribute("securityContext",securityContextBase);
		return true;
	}
}
