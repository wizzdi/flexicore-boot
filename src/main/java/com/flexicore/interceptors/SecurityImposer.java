/*******************************************************************************
 *  Copyright (C) FlexiCore, Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Avishay Ben Natan And Asaf Ben Natan, October 2015
 ******************************************************************************/
package com.flexicore.interceptors;

import com.flexicore.annotations.OperationsInside;
import com.flexicore.data.jsoncontainers.OperationInfo;
import com.flexicore.exceptions.UnAuthorizedCustomException;
import com.flexicore.interfaces.AspectPlugin;
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
import com.flexicore.security.SecurityContextBase;
import com.flexicore.service.impl.SecurityService;
import io.jsonwebtoken.JwtException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.pf4j.Extension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import jakarta.websocket.CloseReason;
import jakarta.websocket.Session;
import java.lang.reflect.Method;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

/**
 * uses Aspect Oriented Programming (through JavaEE support) to enforce security
 * Access granularity is specified in a separate UML diagram
 *
 * @author Avishay Ben Natan
 */


@Aspect
@Component
@Order(0)
@Extension
public class SecurityImposer implements AspectPlugin {


	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	@Autowired
	@Lazy
	private SecurityService securityService;
	private static final Logger logger = LoggerFactory.getLogger(SecurityImposer.class);
	private static Method setupTotpMethod;

	private static Method finishSetupTotpMethod;

	private static Method totpAuthenticationMethod;
	private static Method totpRecoveryMethod;

	@Around("execution(@com.flexicore.annotations.Protected * *(..)) || within(@(@com.flexicore.annotations.Protected *) *)|| within(@com.flexicore.annotations.Protected *)")
	public Object transformReturn(ProceedingJoinPoint joinPoint) throws Throwable {
		long start = System.currentTimeMillis();
		Session websocketSession = null;
		try {
			MethodSignature signature = (MethodSignature) joinPoint.getSignature();
			Method method = signature.getMethod();
			Object[] parameters = joinPoint.getArgs();
			String methodName = method.getName();
			logger.info("Method is: " + methodName + " , on Thread " + Thread.currentThread().getName());
			String authenticationkey = (String) parameters[0];
			websocketSession = getWebsocketSession(parameters);
			SecurityContextBase existing = Stream.of(parameters).filter(f -> f instanceof SecurityContextBase).map(f -> (SecurityContextBase) f).findFirst().orElse(null);
			if(existing!=null){
				logger.debug("existing security context - will not authenticate again");
				Object procceed = procceed(existing, joinPoint, methodName, parameters, start);
				return procceed;
			}


			if (authenticationkey != null && !authenticationkey.isEmpty()) {
				OperationInfo operationInfo = securityService.getIOperation(method);

				SecurityContext securityContext = securityService.getSecurityContext(authenticationkey, operationInfo.getOperationId());
				if (securityContext == null) {
					return deny(websocketSession);
				}
				User user = securityContext.getUser();
				List<Tenant> tenants = securityContext.getTenants();
				Operation operation = securityContext.getOperation();


				OperationsInside operationsInside = AnnotatedElementUtils.findMergedAnnotation(method, OperationsInside.class);
				if (operationInfo.getiOperation() == null) {
					logger.error("could not find io operation annotation on method: " + methodName);
					return deny(websocketSession);
				}
				if (user == null) {
					logger.error("could not determine user");
					return deny(websocketSession);

				}
				if (tenants == null) {
					logger.error("could not determine tenants");
					return deny(websocketSession);
				}

				if (operation == null) {
					logger.error("could not determine operation for method " + methodName);
					return deny(websocketSession);
				}
				if (user.isTotpEnabled() && !isTotpAuthentication(method) && !securityContext.isTotpVerified()) {
					logger.error("user has totp enabled but did not validate it yet and the calling method is not totp authentication");
					return deny(websocketSession,UnAuthorizedCustomException.Errors.TOTP_REQUIRED.ordinal());
				}
				TotpSecurityPolicy totpSecurityPolicy = securityContext.getSecurityPolicies() == null ? null : securityContext.getSecurityPolicies().stream().filter(f -> f instanceof TotpSecurityPolicy && ((TotpSecurityPolicy) f).isForceTotp()).map(f -> (TotpSecurityPolicy) f).min(Comparator.comparing(SecurityPolicy::getStartTime)).orElse(null);
				if (totpSecurityPolicy != null && !user.isTotpEnabled()) {
					OffsetDateTime policyStartedForUser = securityContext.getUser().getCreationDate().isAfter(totpSecurityPolicy.getStartTime()) ? securityContext.getUser().getCreationDate() : totpSecurityPolicy.getStartTime();
					if (OffsetDateTime.now().isAfter(policyStartedForUser.plus(totpSecurityPolicy.getAllowedConfigureOffsetMs(), ChronoUnit.MILLIS))) {
						logger.error("totp policy is enforced , and the user did not configured totp before the grace period of " + totpSecurityPolicy.getAllowedConfigureOffsetMs() + " ms");
						return deny(websocketSession,UnAuthorizedCustomException.Errors.TOTP_LOCKED_OUT.ordinal());
					}
					if (!isTotpConfigureMethods(method)) {
						logger.error("totp policy is enforced , the user does not have totp enabled and the calling method is not configure totp");
						return deny(websocketSession,UnAuthorizedCustomException.Errors.TOTP_SETUP_REQUIRED.ordinal());
					}


				}

				if (securityService.checkIfAllowed(user, tenants, operation, operationInfo.getiOperation().access())) {
					Object procceed = procceed(securityContext, joinPoint, methodName, parameters, start);
					return procceed;
				} else {
					return deny(websocketSession);
				}

			} else {
				return deny(websocketSession);


			}
		} catch (JwtException e) {
			logger.error("security check failed with error", e);
			return deny(websocketSession);
		}

	}

	private boolean isTotpConfigureMethods(Method method) {
		Method setupTotpMethod = getSetupTotpMethod();
		Method finishSetupTotpMethod = getFinishSetupTotpMethod();
		return method.equals(setupTotpMethod) || method.equals(finishSetupTotpMethod);
	}

	private boolean isTotpAuthentication(Method method) {
		Method totpAuthenticationMethod = getTotpAuthenticationMethod();
		Method totpRecoveryMethod = getTotpRecoveryMethod();

		return method.equals(totpAuthenticationMethod) || method.equals(totpRecoveryMethod);
	}

	private Method getSetupTotpMethod() {
		if (setupTotpMethod == null) {
			try {
				setupTotpMethod = TotpRESTService.class.getMethod("setupTotp", String.class, SecurityContext.class);
			} catch (Exception e) {
				logger.error("could not find totp setupTotp method");
			}
		}
		return setupTotpMethod;
	}

	private Method getFinishSetupTotpMethod() {
		if (finishSetupTotpMethod == null) {
			try {
				finishSetupTotpMethod = TotpRESTService.class.getMethod("finishSetupTotp", String.class, FinishTotpSetupRequest.class, SecurityContext.class);
			} catch (Exception e) {
				logger.error("could not find totp finishSetupTotp method");
			}
		}
		return finishSetupTotpMethod;
	}

	private Method getTotpAuthenticationMethod() {
		if (totpAuthenticationMethod == null) {
			try {
				totpAuthenticationMethod = TotpRESTService.class.getMethod("authenticateTotp", String.class, TotpAuthenticationRequest.class, SecurityContext.class);
			} catch (Exception e) {
				logger.error("could not find totp authentication Method");
			}
		}
		return totpAuthenticationMethod;
	}

	private Method getTotpRecoveryMethod() {
		if (totpRecoveryMethod == null) {
			try {
				totpRecoveryMethod = TotpRESTService.class.getMethod("recoverTotp", String.class, RecoverTotpRequest.class, SecurityContext.class);
			} catch (Exception e) {
				logger.error("could not find totp recover method");
			}
		}
		return totpRecoveryMethod;
	}

	private Session getWebsocketSession(Object[] parameters) {
		return parameters != null ? Stream.of(parameters).filter(f -> f instanceof Session).map(f -> (Session) f).findAny().orElse(null) : null;
	}


	private Object procceed(SecurityContextBase securityContext, ProceedingJoinPoint proceedingJoinPoint, String methodName, Object[] parameters, long start) throws Throwable {
		Object param = parameters[parameters.length - 1];
		if (param instanceof Session) {
			Session session = (Session) param;
			session.getUserProperties().put("securityContext", securityContext);
		} else {
			parameters[parameters.length - 1] = securityContext;
		}
		Object o = proceedingJoinPoint.proceed(parameters);
		long timeTaken = System.currentTimeMillis() - start;

		logger.info("request to " + methodName + " took: " + timeTaken + "ms");
		return o;


	}

	private Object deny(Session websocketSession) {
		return deny(websocketSession, -1);
	}


	private Object deny(Session websocketSession, int code) {
		String reason = "user is not authorized for this resource";
		closeWSIfNecessary(websocketSession, reason);
		throw new UnAuthorizedCustomException(reason).setErrorCode(code);
	}

	private void closeWSIfNecessary(Session websocketSession, String reason) {
		if (websocketSession != null && websocketSession.isOpen()) {
			try {
				String id = websocketSession.getId();

				websocketSession.close(new CloseReason(CloseReason.CloseCodes.CANNOT_ACCEPT, reason));
				logger.warn("Closed WS " + id + " for being unauthorized");
			} catch (Exception e) {
				logger.error("failed closing WS", e);
			}
		}
	}

}
