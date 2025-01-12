package com.wizzdi.flexicore.boot.dynamic.invokers.response;

import com.wizzdi.flexicore.boot.dynamic.invokers.annotations.Invoker;
import com.wizzdi.flexicore.security.response.PaginationResponse;
import org.pf4j.PluginWrapper;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.util.ClassUtils;

import java.util.*;
import java.util.stream.Collectors;

import static com.wizzdi.flexicore.boot.dynamic.invokers.utils.InvokerUtils.getMethodSubject;

public class InvokerInfo {


	private Class<?> name;
	private String description;
	private String displayName;
	private Class<?> handlingType;
	private List<InvokerMethodInfo> methods = new ArrayList<>();
	private String pluginId;
	private List<Class<?>> handlingTypeImplementedInterfaces=new ArrayList<>();

	public InvokerInfo() {
	}

	public InvokerInfo(InvokerInfo other, Set<String> allowedOps) {
		this.name = other.name;
		this.description = other.description;
		this.displayName = other.displayName;
		this.handlingType = other.handlingType;
		this.handlingTypeImplementedInterfaces=other.handlingTypeImplementedInterfaces;
		this.methods = other.methods.parallelStream().filter(f -> allowedOps.contains(f.getId())).collect(Collectors.toList());

	}

	public InvokerInfo(Object invoker,PluginWrapper pluginWrapper) {
		Class<?> invokerClass = ClassUtils.getUserClass(invoker.getClass());
		com.wizzdi.flexicore.boot.dynamic.invokers.annotations.InvokerInfo InvokerInfo = AnnotatedElementUtils.findMergedAnnotation(invokerClass, com.wizzdi.flexicore.boot.dynamic.invokers.annotations.InvokerInfo.class);
		name = invokerClass;
		displayName = InvokerInfo != null && !InvokerInfo.displayName().isEmpty() ? InvokerInfo.displayName() : invokerClass.getName();
		description = InvokerInfo != null && !InvokerInfo.description().isEmpty() ? InvokerInfo.description() : "No Description";
		handlingType = invoker instanceof Invoker ? ((Invoker) invoker).getHandlingClass() : getAutomatically(invokerClass);
		handlingTypeImplementedInterfaces= Optional.ofNullable(handlingType).stream().map(f->handlingType.getInterfaces()).flatMap(Arrays::stream).toList();
		this.pluginId=pluginWrapper!=null?pluginWrapper.getPluginId():null;



	}
	public void addInvokerMethodInfo(InvokerMethodInfo invokerMethodInfo){
		this.methods.add(invokerMethodInfo);
	}


    private Class<?> getAutomatically(Class<?> invokerClass) {
		Map<Class<?>, List<Class<?>>> collect = Arrays.stream(invokerClass.getDeclaredMethods()).filter(f -> !f.isBridge()).map(f -> getMethodSubject(f))
				.filter(f->f!=null&&!f.equals(PaginationResponse.class)).collect(Collectors.groupingBy(f -> f));
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

	public String getPluginId() {
		return pluginId;
	}

	public <T extends InvokerInfo> T setPluginId(String pluginId) {
		this.pluginId = pluginId;
		return (T) this;
	}

	public List<Class<?>> getHandlingTypeImplementedInterfaces() {
		return handlingTypeImplementedInterfaces;
	}

	public <T extends InvokerInfo> T setHandlingTypeImplementedInterfaces(List<Class<?>> handlingTypeImplementedInterfaces) {
		this.handlingTypeImplementedInterfaces = handlingTypeImplementedInterfaces;
		return (T) this;
	}

	@Override
	public String toString() {
		return "InvokerInfo{" +
				"name=" + name +
				", description='" + description + '\'' +
				", displayName='" + displayName + '\'' +
				", pluginId='" + pluginId + '\'' +
				", handlingType=" + handlingType +
				", methods=" + methods +
				'}';
	}
}
