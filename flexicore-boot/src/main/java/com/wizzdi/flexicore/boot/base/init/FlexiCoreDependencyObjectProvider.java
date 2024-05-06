package com.wizzdi.flexicore.boot.base.init;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.config.DependencyDescriptor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.cglib.proxy.*;
import org.springframework.context.ApplicationContext;
import org.springframework.core.ResolvableType;
import org.springframework.lang.Nullable;
import org.springframework.util.ObjectUtils;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FlexiCoreDependencyObjectProvider implements ObjectProvider<Object>, Serializable {

	private static final Constructor<?> streamDependencyDescriptorConstructor;
	private static final Class<?> streamDependencyDescriptor;
	private static final Class<?> nested;
	private static final Constructor<?> nestedConstructor;


	static {
		try {
			streamDependencyDescriptor = Class.forName("org.springframework.beans.factory.support.DefaultListableBeanFactory$StreamDependencyDescriptor");
			streamDependencyDescriptorConstructor= streamDependencyDescriptor.getDeclaredConstructor(DependencyDescriptor.class, boolean.class);
			streamDependencyDescriptorConstructor.setAccessible(true);

			nested	 = Class.forName("org.springframework.beans.factory.support.DefaultListableBeanFactory$NestedDependencyDescriptor");

			nestedConstructor= nested.getDeclaredConstructor(DependencyDescriptor.class);
			nestedConstructor.setAccessible(true);

		} catch (Throwable e) {
			throw new RuntimeException("failed getting required classes for FlexiCoreDependencyObjectProvider",e);
		}
	}


	private final DefaultListableBeanFactory original;
	private final Queue<ApplicationContext> applicationContexts;
	private final DependencyDescriptor descriptor;

	private final boolean optional;

	@Nullable
	private final String beanName;

	private static DependencyDescriptor getStreaming(DependencyDescriptor dependencyDescriptor,boolean ordered){
		try {
			return (DependencyDescriptor) streamDependencyDescriptorConstructor.newInstance(dependencyDescriptor,ordered);
		} catch (Throwable e) {
			throw new RuntimeException("failed creating streaming descriptor",e);
		}
	}

	private static DependencyDescriptor getNested(DependencyDescriptor dependencyDescriptor){
		try {
			return (DependencyDescriptor) nestedConstructor.newInstance(dependencyDescriptor);
		} catch (Throwable e) {
			throw new RuntimeException("failed creating nested descriptor",e);
		}
	}

	private static <T>  T getNestedProxy(DependencyDescriptor dependencyDescriptor, Callback callback) {
		Enhancer enhancer = new Enhancer();
		enhancer.setSuperclass(nested);
		enhancer.setCallback(callback);
		return (T) enhancer.create(new Class[]{DependencyDescriptor.class},new Object[]{dependencyDescriptor});
	}

	public FlexiCoreDependencyObjectProvider(DefaultListableBeanFactory original, Queue<ApplicationContext> applicationContexts, DependencyDescriptor descriptor, @Nullable String beanName) {
		this.original = original;
		this.applicationContexts = applicationContexts;
		this.descriptor = getNested(descriptor);
		this.optional = (this.descriptor.getDependencyType() == Optional.class);
		this.beanName = beanName;
	}

	@Override
	public Object getObject() throws BeansException {
		if (this.optional) {
			return createOptionalDependency(this.descriptor, this.beanName);
		} else {
			return getObject(this.descriptor, true);
		}
	}

	private Object getObject(DependencyDescriptor descriptor, boolean throwing) {
		Object result = PluginResolveUtils.resolveDependency(original,descriptor, this.beanName, null, null);
		if (result == null) {
			for (DefaultListableBeanFactory defaultListableBeanFactory : getBeanFactories(applicationContexts)) {
				if(original!=defaultListableBeanFactory){
					result = PluginResolveUtils.resolveDependency(defaultListableBeanFactory,descriptor, this.beanName, null, null);
					if (result != null) {
						return result;
					}
				}

			}
			if (throwing) {
				throw new NoSuchBeanDefinitionException(this.descriptor.getResolvableType());

			}
		}
		return result;
	}

	@Override
	public Object getObject(final Object... args) throws BeansException {
		if (this.optional) {
			return createOptionalDependency(this.descriptor, this.beanName, args);
		} else {
			DependencyDescriptor descriptorToUse = new DependencyDescriptor(this.descriptor) {
				@Override
				public Object resolveCandidate(String beanName, Class<?> requiredType, BeanFactory beanFactory) {
					return beanFactory.getBean(beanName, args);
				}
			};
			return getObject(descriptorToUse, this);
		}
	}

	private List<DefaultListableBeanFactory> getBeanFactories(Queue<ApplicationContext> applicationContexts) {
		return applicationContexts.stream().map(f -> f.getAutowireCapableBeanFactory()).filter(f -> f instanceof DefaultListableBeanFactory).map(f -> (DefaultListableBeanFactory) f).collect(Collectors.toList());
	}

	@Override
	@Nullable
	public Object getIfAvailable() throws BeansException {
		if (this.optional) {
			return createOptionalDependency(this.descriptor, this.beanName);
		} else {
			DependencyDescriptor descriptorToUse = new DependencyDescriptor(this.descriptor) {
				@Override
				public boolean isRequired() {
					return false;
				}
			};
			return getObject(descriptorToUse, false);

		}
	}

	@Override
	@Nullable
	public Object getIfUnique() throws BeansException {
		DependencyDescriptor descriptorToUse = new DependencyDescriptor(this.descriptor) {
			@Override
			public boolean isRequired() {
				return false;
			}

			@Override
			@Nullable
			public Object resolveNotUnique(ResolvableType type, Map<String, Object> matchingBeans) {
				return null;
			}
		};
		if (this.optional) {
			return createOptionalDependency(descriptorToUse, this.beanName);
		} else {
			return getObject(descriptorToUse, false);

		}
	}

	@Nullable
	protected Object getValue() throws BeansException {
		if (this.optional) {
			return createOptionalDependency(this.descriptor, this.beanName);
		} else {
			return getObject(this.descriptor, false);

		}
	}

	@Override
	public Stream<Object> stream() {
		return resolveStream(false);
	}

	@Override
	public Stream<Object> orderedStream() {
		return resolveStream(true);
	}

	@SuppressWarnings("unchecked")
	private Stream<Object> resolveStream(boolean ordered) {
		DependencyDescriptor descriptorToUse = getStreaming(this.descriptor, ordered);
		Object result = PluginResolveUtils.resolveDependency(original,descriptorToUse, this.beanName, null, null);
		Stream<Object> objectStream = result instanceof Stream ? (Stream<Object>) result : Stream.of(result);
		for (DefaultListableBeanFactory applicationContext : getBeanFactories(applicationContexts)) {
			if(original!=applicationContext){
				Object resultInner = PluginResolveUtils.resolveDependency(applicationContext,descriptorToUse,this.beanName,null,null);
				Stream<Object> objectStreamInner = resultInner instanceof Stream ? (Stream<Object>) resultInner : Stream.of(resultInner);
				objectStream = Stream.concat(objectStream, objectStreamInner);
			}

		}
		return objectStream;
	}


	private Optional<?> createOptionalDependency(
			DependencyDescriptor descriptor, @Nullable String beanName, final Object... args) {
		DependencyDescriptor descriptorToUse = getNestedProxy(descriptor, (MethodInterceptor) (o, method, objects, methodProxy) -> {
            if (method.getName().equals("isRequired")) {
                return false;
            }
            if (method.getName().equals("resolveCandidate") && !ObjectUtils.isEmpty(args)) {
                String beanName1 = (String) objects[0];
                Class<?> requiredType = (Class<?>) objects[1];
                BeanFactory beanFactory = (BeanFactory) objects[2];
                return beanFactory.getBean(beanName1, args);
            }
            return methodProxy.invokeSuper(o, args);

        });

		Object result = PluginResolveUtils.resolveDependency(original,descriptorToUse, beanName, null, null);
		Optional<?> o = result instanceof Optional ? (Optional<?>) result : Optional.ofNullable(result);
		if (o.isEmpty()) {
			for (DefaultListableBeanFactory applicationContext : getBeanFactories(applicationContexts)) {
				if(original!=applicationContext){
					result = PluginResolveUtils.resolveDependency(applicationContext,descriptorToUse, beanName, null, null);
					o = result instanceof Optional ? (Optional<?>) result : Optional.ofNullable(result);
					if (o.isPresent()) {
						return o;
					}
				}

			}
		}
		return o;
	}
}


