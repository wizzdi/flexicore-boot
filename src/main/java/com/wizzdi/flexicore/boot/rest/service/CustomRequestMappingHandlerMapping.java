package com.wizzdi.flexicore.boot.rest.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;

public class CustomRequestMappingHandlerMapping extends RequestMappingHandlerMapping {

	private static final Logger logger= LoggerFactory.getLogger(CustomRequestMappingHandlerMapping.class);

	private final List<Class<?>> excludedClassesFromMappingChange;
	private final Set<String> excludedPackageNames;
	private final String path;

	public CustomRequestMappingHandlerMapping(List<Class<?>> excludedClassesFromMappingChange,Set<String> excludedPackageNames,String path) {
		this.excludedClassesFromMappingChange=excludedClassesFromMappingChange;
		this.path=path;
		this.excludedPackageNames=excludedPackageNames;
	}

	@Override
	protected RequestMappingInfo getMappingForMethod(Method method, Class<?> handlerType) {

		return super.getMappingForMethod(method, handlerType);
	}

	@Override
	protected void registerHandlerMethod(Object handler, Method method, RequestMappingInfo mapping) {
		if(path!=null){
			Class<?> beanType = method.getDeclaringClass();
			RestController restApiController = beanType.getAnnotation(RestController.class);
			if (restApiController != null&&!excludedClassesFromMappingChange.contains(beanType)&&!startsWith(beanType.getPackageName())) {
				PatternsRequestCondition apiPattern = new PatternsRequestCondition(path)
						.combine(mapping.getPatternsCondition());
				logger.debug("controller "+beanType.getCanonicalName() +" prefix re-written to "+apiPattern.getPatterns());


				mapping = new RequestMappingInfo(mapping.getName(), apiPattern,
						mapping.getMethodsCondition(), mapping.getParamsCondition(),
						mapping.getHeadersCondition(), mapping.getConsumesCondition(),
						mapping.getProducesCondition(), mapping.getCustomCondition());
			}
			else{
				logger.debug("controller "+beanType.getCanonicalName() +" prefix remained "+mapping.getPatternsCondition());

			}

		}

		super.registerHandlerMethod(handler, method, mapping);
	}

	public boolean startsWith(String packageName){
		for (String excludedPackageName : excludedPackageNames) {
			if(packageName.startsWith(excludedPackageName)){
				return true;
			}
		}
		return false;
	}
}
