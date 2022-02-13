package com.wizzdi.security.adapter;

import com.flexicore.model.Baseclass;
import com.flexicore.model.SecurityOperation;
import com.flexicore.security.SecurityContextBase;
import com.wizzdi.flexicore.security.service.SecurityOperationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;


@Component
public class OperationInterceptor implements HandlerInterceptor {


	private static final Logger logger= LoggerFactory.getLogger(OperationInterceptor.class);
	public static final String SECURITY_CONTEXT = "securityContext";



	@Autowired
	private SecurityOperationService securityOperationService;


	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		SecurityContextBase securityContextBase = (SecurityContextBase) request.getAttribute(SECURITY_CONTEXT);
		if(securityContextBase!=null){
			if(handler instanceof HandlerMethod) {
				HandlerMethod handlerMethod= (HandlerMethod) handler;
				Method method = handlerMethod.getMethod();
				String operationId = Baseclass.generateUUIDFromString(method.toString());
				SecurityOperation securityOperation=securityOperationService.getByIdOrNull(operationId,SecurityOperation.class,null);
				if(securityOperation==null){
					logger.error("could not find io operation annotation on method: " + method.getName());
					response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
					return false;
				}
				securityContextBase.setOperation(securityOperation);

			}
		}

		return true;

	}



}
