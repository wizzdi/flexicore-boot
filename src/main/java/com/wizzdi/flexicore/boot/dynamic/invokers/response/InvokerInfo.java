package com.wizzdi.flexicore.boot.dynamic.invokers.response;

import com.flexicore.annotations.IOperation;
import com.flexicore.model.Baseclass;
import com.wizzdi.flexicore.boot.dynamic.invokers.annotations.Invoker;
import com.wizzdi.flexicore.security.response.PaginationResponse;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

import static com.wizzdi.flexicore.boot.dynamic.invokers.utils.InvokerUtils.getMethodSubject;

public class InvokerInfo {


	private Class<?> name;
	private String description;
	private String displayName;
	private Class<?> handlingType;
	private List<InvokerMethodInfo> methods = new ArrayList<>();

	public InvokerInfo() {
	}

	public InvokerInfo(InvokerInfo other, Set<String> allowedOps) {
		this.name = other.name;
		this.description = other.description;
		this.displayName = other.displayName;
		this.handlingType = other.handlingType;
		this.methods = other.methods.parallelStream().filter(f -> allowedOps.contains(f.getId())).collect(Collectors.toList());

	}

	public InvokerInfo(Object invoker) {
		Class<?> invokerClass = ClassUtils.getUserClass(invoker.getClass());
		com.wizzdi.flexicore.boot.dynamic.invokers.annotations.InvokerInfo InvokerInfo = AnnotatedElementUtils.findMergedAnnotation(invokerClass, com.wizzdi.flexicore.boot.dynamic.invokers.annotations.InvokerInfo.class);
		name = invokerClass;
		displayName = InvokerInfo != null && !InvokerInfo.displayName().isEmpty() ? InvokerInfo.displayName() : invokerClass.getName();
		description = InvokerInfo != null && !InvokerInfo.description().isEmpty() ? InvokerInfo.description() : "No Description";
		handlingType = invoker instanceof Invoker ? ((Invoker) invoker).getHandlingClass() : getAutomatically(invokerClass);



	}
	public void addInvokerMethodInfo(InvokerMethodInfo invokerMethodInfo){
		this.methods.add(invokerMethodInfo);
	}


    private Class<?> getAutomatically(Class<?> invokerClass) {
		Map<Class<?>, List<Class<?>>> collect = Arrays.stream(invokerClass.getDeclaredMethods()).filter(f -> !f.isBridge()).map(f -> getMethodSubject(f)).collect(Collectors.groupingBy(f -> f));
		return collect.entrySet().stream().max(Comparator.comparing(e -> e.getValue().size())).map(f -> f.getKey()).orElse(null);
	}



	public Class<?> getName() {
		return name;
	}

	public InvokerInfo setName(Class<? extends Invoker> name) {
		this.name = name;
		return this;
	}

	public String getDescription() {
		return description;
	}

	public InvokerInfo setDescription(String description) {
		this.description = description;
		return this;
	}

	public String getDisplayName() {
		return displayName;
	}

	public InvokerInfo setDisplayName(String displayName) {
		this.displayName = displayName;
		return this;
	}

	public List<InvokerMethodInfo> getMethods() {
		return methods;
	}

	public InvokerInfo setMethods(List<InvokerMethodInfo> methods) {
		this.methods = methods;
		return this;
	}

	public Class<?> getHandlingType() {
		return handlingType;
	}

	public InvokerInfo setHandlingType(Class<?> handlingType) {
		this.handlingType = handlingType;
		return this;
	}

	@Override
	public String toString() {
		return "InvokerInfo{" +
				"name=" + name +
				", description='" + description + '\'' +
				", displayName='" + displayName + '\'' +
				", handlingType=" + handlingType +
				", methods=" + methods +
				'}';
	}
}
