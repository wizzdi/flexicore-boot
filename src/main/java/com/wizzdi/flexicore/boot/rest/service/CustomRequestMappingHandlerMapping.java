package com.wizzdi.flexicore.boot.rest.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerTypePredicate;
import org.springframework.web.servlet.mvc.condition.PathPatternsRequestCondition;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

public class CustomRequestMappingHandlerMapping extends RequestMappingHandlerMapping {

	private static final Logger logger= LoggerFactory.getLogger(CustomRequestMappingHandlerMapping.class);

	private final List<Class<?>> excludedClassesFromMappingChange;
	private final Set<String> excludedPackageNames;
	private final String path;

	public CustomRequestMappingHandlerMapping(List<Class<?>> excludedClassesFromMappingChange,Set<String> excludedPackageNames,String path) {
		this.excludedClassesFromMappingChange=excludedClassesFromMappingChange;
		this.path=path;
		this.excludedPackageNames=excludedPackageNames;
		if(path!=null){
			setPathPrefixes(Map.of(path, this::shouldPrefix));
		}
	}

	private boolean shouldPrefix(Class<?> aClass){
		RestController restApiController = aClass.getAnnotation(RestController.class);
		return restApiController != null&&!excludedClassesFromMappingChange.contains(aClass)&&!startsWith(aClass.getPackageName());
	}

	@Override
	protected RequestMappingInfo getMappingForMethod(Method method, Class<?> handlerType) {

		return super.getMappingForMethod(method, handlerType);
	}


	@Override
	protected void registerHandlerMethod(Object handler, Method method, RequestMappingInfo mapping) {
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
