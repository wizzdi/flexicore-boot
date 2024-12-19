package com.wizzdi.security.adapter;

import com.flexicore.model.SecurityOperation;
import com.wizzdi.flexicore.security.service.ClazzService;
import com.wizzdi.flexicore.security.configuration.SecurityContext;
import com.wizzdi.flexicore.security.service.SecurityOperationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;


@Component
public class OperationInterceptor implements HandlerInterceptor {


	private static final Logger logger= LoggerFactory.getLogger(OperationInterceptor.class);
	public static final String SECURITY_CONTEXT = "securityContext";



	@Autowired
	private SecurityOperationService securityOperationService;


	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		SecurityContext securityContext = (SecurityContext) request.getAttribute(SECURITY_CONTEXT);
		if(securityContext !=null){
			if(handler instanceof HandlerMethod) {
				HandlerMethod handlerMethod= (HandlerMethod) handler;
				Method method = handlerMethod.getMethod();
				if(!BasicErrorController.class.equals(method.getDeclaringClass())){
					String operationId = ClazzService.getIdFromString(method.toString());
					SecurityOperation securityOperation=securityOperationService.getByIdOrNull(operationId);
					if(securityOperation==null){
						logger.error("could not find io operation annotation on method: " + method.getName());
						response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
						return false;
					}
					securityContext.setOperation(securityOperation);
				}


			}
		}

		return true;

	}



}
