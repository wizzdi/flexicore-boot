package com.flexicore.interceptors;

import com.flexicore.annotations.OperationsInside;
import com.flexicore.data.jsoncontainers.OperationInfo;
import com.flexicore.model.Operation;
import com.flexicore.model.Tenant;
import com.flexicore.model.User;
import com.flexicore.model.security.SecurityPolicy;
import com.flexicore.model.security.TotpSecurityPolicy;
import com.flexicore.request.FinishTotpSetupRequest;
import com.flexicore.request.RecoverTotpRequest;
import com.flexicore.request.TotpAuthenticationRequest;
import com.flexicore.rest.TotpRESTService;
import com.flexicore.security.SecurityContext;
import com.flexicore.service.impl.SecurityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.Session;
import java.lang.reflect.Method;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;



@Component
public class SecurityInterceptor implements HandlerInterceptor {

	private static final String AUTHENTICATION_KEY = "authenticationKey";
	private static final String SECURITY_CONTEXT_NAME="securityContext";

	private static final Logger logger= LoggerFactory.getLogger(SecurityInterceptor.class);
	private static Method setupTotpMethod;

	private static Method finishSetupTotpMethod;

	private static Method totpAuthenticationMethod;
	private static Method totpRecoveryMethod;


	@Autowired
	@Lazy
	private SecurityService securityService;


	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		String authenticationkey = request.getHeader(AUTHENTICATION_KEY);
		if(handler instanceof HandlerMethod){
			HandlerMethod handlerMethod= (HandlerMethod) handler;
			Method method = handlerMethod.getMethod();
			String methodName=method.getName();
			Session websocketSession = null;

			if (authenticationkey != null && !authenticationkey.isEmpty()) {
				OperationInfo operationInfo = securityService.getIOperation(method);

				SecurityContext securityContext = securityService.getSecurityContext(authenticationkey, operationInfo.getOperationId());
				if (securityContext == null) {
					return deny(websocketSession, handlerMethod, response);
				}
				User user = securityContext.getUser();
				List<Tenant> tenants = securityContext.getTenants();
				Operation operation = securityContext.getOperation();


				OperationsInside operationsInside = AnnotatedElementUtils.findMergedAnnotation(method,OperationsInside.class);
				if (operationInfo.getiOperation() == null) {
					logger.error("could not find io operation annotation on method: " + methodName);
					return deny(websocketSession,handlerMethod,response);
				}
				if (user == null) {
					logger.error("could not determine user");
					return deny(websocketSession, handlerMethod, response);

				}
				if (tenants == null) {
					logger.error("could not determine tenants");
					return deny(websocketSession, handlerMethod, response);
				}

				if (operation == null) {
					logger.error("could not determine operation for method " + methodName);
					return deny(websocketSession, handlerMethod, response);
				}
				if (user.isTotpEnabled() && !isTotpAuthentication(method) && !securityContext.isTotpVerified()) {
					logger.error("user has totp enabled but did not validate it yet and the calling method is not totp authentication");
					return deny(websocketSession, handlerMethod, response);
				}
				TotpSecurityPolicy totpSecurityPolicy = securityContext.getSecurityPolicies() == null ? null : securityContext.getSecurityPolicies().stream().filter(f -> f instanceof TotpSecurityPolicy && ((TotpSecurityPolicy) f).isForceTotp()).map(f -> (TotpSecurityPolicy) f).min(Comparator.comparing(SecurityPolicy::getStartTime)).orElse(null);
				if (totpSecurityPolicy != null && !user.isTotpEnabled()) {
					OffsetDateTime policyStartedForUser = securityContext.getUser().getCreationDate().isAfter(totpSecurityPolicy.getStartTime()) ? securityContext.getUser().getCreationDate() : totpSecurityPolicy.getStartTime();
					if (OffsetDateTime.now().isAfter(policyStartedForUser.plus(totpSecurityPolicy.getAllowedConfigureOffsetMs(), ChronoUnit.MILLIS))) {
						logger.error("totp policy is enforced , and the user did not configured totp before the grace period of " + totpSecurityPolicy.getAllowedConfigureOffsetMs() + " ms");
						return deny(websocketSession, handlerMethod, response);
					}
					if (!isTotpConfigureMethods(method)) {
						logger.error("totp policy is enforced , the user does not have totp enabled and the calling method is not configure totp");
						return deny(websocketSession, handlerMethod, response);
					}


				}

				if (securityService.checkIfAllowed(user, tenants, operation, operationInfo.getiOperation().access())) {
					request.setAttribute(SECURITY_CONTEXT_NAME,securityContext);
					return true;
				} else {
					return deny(websocketSession, handlerMethod, response);
				}

			} else {
				return deny(websocketSession, handlerMethod, response);


			}
		}
		return true;

	}

	private boolean deny(Session websocketSession, HandlerMethod method, HttpServletResponse response) {
		boolean stopProcessing = Arrays.stream(method.getMethodParameters()).anyMatch(this::hasSecurityAnnotation);
		if(stopProcessing){
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		}
		return !stopProcessing;
	}

	private boolean hasSecurityAnnotation(MethodParameter methodParameter) {
		RequestAttribute requestAttribute=methodParameter.getParameterAnnotation(RequestAttribute.class);
		return requestAttribute!=null&&requestAttribute.required()&&(requestAttribute.name().equals(SECURITY_CONTEXT_NAME)||(requestAttribute.name().isEmpty()&&SECURITY_CONTEXT_NAME.equals(methodParameter.getParameterName())));
	}

	private boolean isTotpConfigureMethods(Method method) {
		Method setupTotpMethod=getSetupTotpMethod();
		Method finishSetupTotpMethod=getFinishSetupTotpMethod();
		return method.equals(setupTotpMethod)||method.equals(finishSetupTotpMethod);
	}

	private boolean isTotpAuthentication(Method method) {
		Method totpAuthenticationMethod= getTotpAuthenticationMethod();
		Method totpRecoveryMethod= getTotpRecoveryMethod();

		return method.equals(totpAuthenticationMethod)||method.equals(totpRecoveryMethod);
	}

	private Method getSetupTotpMethod() {
		if(setupTotpMethod==null){
			try {
				setupTotpMethod = TotpRESTService.class.getMethod("setupTotp", String.class, SecurityContext.class);
			}
			catch (Exception e){
				logger.error("could not find totp setupTotp method");
			}
		}
		return setupTotpMethod;
	}

	private Method getFinishSetupTotpMethod() {
		if(finishSetupTotpMethod==null){
			try {
				finishSetupTotpMethod = TotpRESTService.class.getMethod("finishSetupTotp", String.class, FinishTotpSetupRequest.class, SecurityContext.class);
			}
			catch (Exception e){
				logger.error("could not find totp finishSetupTotp method");
			}
		}
		return finishSetupTotpMethod;
	}

	private Method getTotpAuthenticationMethod() {
		if(totpAuthenticationMethod==null){
			try {
				totpAuthenticationMethod = TotpRESTService.class.getMethod("authenticateTotp", String.class, TotpAuthenticationRequest.class, SecurityContext.class);
			}
			catch (Exception e){
				logger.error("could not find totp authentication Method");
			}
		}
		return totpAuthenticationMethod;
	}
	private Method getTotpRecoveryMethod() {
		if(totpRecoveryMethod==null){
			try {
				totpRecoveryMethod = TotpRESTService.class.getMethod("recoverTotp", String.class, RecoverTotpRequest.class, SecurityContext.class);
			}
			catch (Exception e){
				logger.error("could not find totp recover method");
			}
		}
		return totpRecoveryMethod;
	}

	private Session getWebsocketSession(Object[] parameters) {
		return parameters != null ? Stream.of(parameters).filter(f -> f instanceof Session).map(f -> (Session) f).findAny().orElse(null) : null;
	}

}
