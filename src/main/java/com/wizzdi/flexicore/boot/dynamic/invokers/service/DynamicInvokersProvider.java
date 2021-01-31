package com.wizzdi.flexicore.boot.dynamic.invokers.service;

import com.flexicore.annotations.IOperation;
import com.flexicore.annotations.OperationsInside;
import com.flexicore.model.Baseclass;
import com.flexicore.security.SecurityContextBase;
import com.wizzdi.flexicore.boot.base.init.FlexiCorePluginManager;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.boot.dynamic.invokers.annotations.FieldInfo;
import com.wizzdi.flexicore.boot.dynamic.invokers.annotations.IdRefFieldInfo;
import com.wizzdi.flexicore.boot.dynamic.invokers.annotations.Invoker;
import com.wizzdi.flexicore.boot.dynamic.invokers.annotations.ListFieldInfo;
import com.wizzdi.flexicore.boot.dynamic.invokers.interfaces.InvokerMethodScanner;
import com.wizzdi.flexicore.boot.dynamic.invokers.interfaces.InvokerParameterScanner;
import com.wizzdi.flexicore.boot.dynamic.invokers.response.InvokerInfo;
import com.wizzdi.flexicore.boot.dynamic.invokers.response.InvokerMethodInfo;
import com.wizzdi.flexicore.boot.dynamic.invokers.response.ParameterInfo;
import com.wizzdi.flexicore.security.response.PaginationResponse;
import org.pf4j.Extension;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.stream.Collectors;

import static com.wizzdi.flexicore.boot.dynamic.invokers.utils.InvokerUtils.*;

@Configuration
@Extension
public class DynamicInvokersProvider implements Plugin {

	@Bean
	@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
	@ConditionalOnMissingBean(InvokerParameterScanner.class)
	public InvokerParameterScanner invokerParameterScanner(){
		return this::scanField;
	}

	@Bean
	@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
	@ConditionalOnMissingBean(InvokerMethodScanner.class)
	public InvokerMethodScanner invokerMethodScanner(){
		return this::scanMethod;
	}


	@Lazy
	@Bean
	@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
	public List<InvokerInfo> invokerInfos(FlexiCorePluginManager flexiCorePluginManager,InvokerMethodScanner invokerMethodScanner,InvokerParameterScanner invokerParameterScanner){
		List<ApplicationContext> collect = flexiCorePluginManager.getStartedPlugins().stream().map(f -> flexiCorePluginManager.getApplicationContext(f)).collect(Collectors.toList());
		collect.add(flexiCorePluginManager.getApplicationContext());
		return collect.stream().map(f -> getInvokers(f)).flatMap(List::stream).map(f-> scan(invokerMethodScanner, invokerParameterScanner, f)).collect(Collectors.toList());
	}

	private InvokerInfo scan(InvokerMethodScanner invokerMethodScanner, InvokerParameterScanner invokerParameterScanner, Object invoker) {
		InvokerInfo invokerInfo = new InvokerInfo(invoker);
		Class<?> invokerClass=invokerInfo.getName();
		Method[] methods = invokerClass.getDeclaredMethods();
		for (Method method : methods) {
			InvokerMethodInfo invokerMethodInfo=invokerMethodScanner.scan( invokerClass, method);
			if(invokerMethodInfo!=null){
				invokerInfo.addInvokerMethodInfo(invokerMethodInfo);
				Optional<Parameter> parameterOptional=getBodyParameter(method);
				if(parameterOptional.isPresent()){
					Parameter parameter=parameterOptional.get();
					String parameterHolderType=parameter.getType().getCanonicalName();
					invokerMethodInfo.setParameterHolderType(parameterHolderType);
					List<Field> allFields = getAllFields(parameter.getType());
					for (Field field : allFields) {
						ParameterInfo parameterInfo=invokerParameterScanner.scan(parameter, allFields, field);
						if(parameterInfo!=null){
							invokerMethodInfo.addParameterInfo(parameterInfo);
						}


					}
				}
			}
		}
		return invokerInfo;
	}

	private Optional<Parameter> getBodyParameter(Method method) {
		Parameter[] parameters=method.getParameters();
		return Arrays.stream(parameters).filter(this::isBodyParameter).findFirst();

	}

	private boolean isBodyParameter(Parameter parameter) {
		return((!parameter.getType().equals(String.class)&&!SecurityContextBase.class.isAssignableFrom(parameter.getType()))||AnnotatedElementUtils.findMergedAnnotation(parameter, RequestBody.class)!=null);
	}

	private ParameterInfo scanField(Parameter parameter, List<Field> allFields, Field field) {
		IdRefFieldInfo idRefFieldInfo = AnnotatedElementUtils.findMergedAnnotation(field,IdRefFieldInfo.class);
		if(idRefFieldInfo !=null){
			return new ParameterInfo(field, idRefFieldInfo);
		}
		else{
			FieldInfo fieldInfo = AnnotatedElementUtils.findMergedAnnotation(field,FieldInfo.class);
			if(fieldInfo !=null){
				return new ParameterInfo(field, fieldInfo);
			}
			else{
				ListFieldInfo listFieldInfo = AnnotatedElementUtils.findMergedAnnotation(field,ListFieldInfo.class);
				if(listFieldInfo !=null){
					return new ParameterInfo(field, listFieldInfo);
				}
				else{

					return detectAutomatically(field, allFields, parameter.getType().getDeclaredFields());
				}

			}
		}
	}

	private InvokerMethodInfo scanMethod(Class<?> invokerClass, Method method) {
		com.wizzdi.flexicore.boot.dynamic.invokers.annotations.InvokerMethodInfo InvokerMethodInfo = AnnotatedElementUtils.findMergedAnnotation(method, com.wizzdi.flexicore.boot.dynamic.invokers.annotations.InvokerMethodInfo.class);
		if (InvokerMethodInfo != null) {
			return new InvokerMethodInfo(method, InvokerMethodInfo);
		}
		else{
			RequestMapping requestMapping = AnnotatedElementUtils.findMergedAnnotation(method, RequestMapping.class);
			IOperation iOperation=AnnotatedElementUtils.findMergedAnnotation(method,IOperation.class);

			if(requestMapping!=null||iOperation!=null){
				if(iOperation==null){
					iOperation=getIOOperation(method);
				}
				com.wizzdi.flexicore.boot.dynamic.invokers.annotations.InvokerMethodInfo invokerMethodInfo=getInvokerMethodInfo(invokerClass, method,iOperation);
				return new InvokerMethodInfo(method, invokerMethodInfo);
			}

		}
		return null;
	}

	private List<Object> getInvokers(ApplicationContext f) {
		Map<String, Object> restControllers = f.getBeansWithAnnotation(RestController.class);
		Map<String, Object> ops = f.getBeansWithAnnotation(OperationsInside.class);
		restControllers.putAll(ops);
		Map<String, Invoker> invokers = f.getBeansOfType(Invoker.class);

		restControllers.putAll(invokers);

		return new ArrayList<>(restControllers.values());
	}


}
