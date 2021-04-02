package com.wizzdi.flexicore.boot.base.init;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.lang.Nullable;

import java.util.HashSet;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Stream;

public class FlexiCoreObjectProvider<T> implements ObjectProvider<T> {

	private final Class<T> requiredType;
	private final ObjectProvider<T> beanProvider;
	private final Queue<ApplicationContext> dependenciesContext;

	public FlexiCoreObjectProvider(Class<T> requiredType,ObjectProvider<T> beanProvider, Queue<ApplicationContext> dependenciesContext) {
		this.requiredType=requiredType;
		this.beanProvider=beanProvider;
		this.dependenciesContext=dependenciesContext;
	}

	@Override
	public T getObject() throws BeansException {
		return getObject(new HashSet<>());
	}

	public T getObject(Set<String> visitedContext) throws BeansException {
		try {
			return beanProvider.getObject();
		} catch (NoSuchBeanDefinitionException e) {
			for (ApplicationContext applicationContext : dependenciesContext) {
				try {
					if(visitedContext.add(applicationContext.getId())){
						AutowireCapableBeanFactory autowireCapableBeanFactory = applicationContext.getAutowireCapableBeanFactory();
						ObjectProvider<T> objectProvider =  autowireCapableBeanFactory.getBeanProvider(requiredType);
						return objectProvider instanceof FlexiCoreObjectProvider?((FlexiCoreObjectProvider<T>) objectProvider).getObject(visitedContext): objectProvider.getObject();
					}

				} catch (NoSuchBeanDefinitionException e1) {
				}
			}
			throw e;
		}
	}


	@Override
	public T getObject(Object... args) throws BeansException {
		return getObject(new HashSet<>(),args);
	}

	public T getObject(Set<String> visitedContext,Object... args) throws BeansException {
		try {
			return beanProvider.getObject(args);
		} catch (NoSuchBeanDefinitionException e) {
			for (ApplicationContext applicationContext : dependenciesContext) {
				try {
					if(visitedContext.add(applicationContext.getId())){
						AutowireCapableBeanFactory autowireCapableBeanFactory = applicationContext.getAutowireCapableBeanFactory();
						ObjectProvider<T> objectProvider =  autowireCapableBeanFactory.getBeanProvider(requiredType);
						return objectProvider instanceof FlexiCoreObjectProvider?((FlexiCoreObjectProvider<T>) objectProvider).getObject(visitedContext,args): objectProvider.getObject(args);
					}

				} catch (NoSuchBeanDefinitionException e1) {
				}
			}
			throw e;
		}
	}

	@Override
	@Nullable
	public T getIfAvailable() throws BeansException {
		return getIfAvailable(new HashSet<>());
	}

	@Nullable
	public T getIfAvailable(Set<String> visitedContext) throws BeansException {

		T t = beanProvider.getIfAvailable();
		if (t == null) {
			for (ApplicationContext applicationContext : dependenciesContext) {
				if(visitedContext.add(applicationContext.getId())){
					AutowireCapableBeanFactory autowireCapableBeanFactory = applicationContext.getAutowireCapableBeanFactory();
					ObjectProvider<T> objectProvider =  autowireCapableBeanFactory.getBeanProvider(requiredType);
					t= objectProvider instanceof FlexiCoreObjectProvider?((FlexiCoreObjectProvider<T>) objectProvider).getIfAvailable(visitedContext): objectProvider.getIfAvailable();
					if (t != null) {
						return t;
					}
				}


			}
		}
		return t;
	}

	@Override
	@Nullable
	public T getIfUnique() throws BeansException {
		return getIfUnique(new HashSet<>());
	}

	@Nullable
	public T getIfUnique(Set<String> visitedContext) throws BeansException {
		T t = beanProvider.getIfUnique();
		if (t == null) {
			for (ApplicationContext applicationContext : dependenciesContext) {
				if(visitedContext.add(applicationContext.getId())){
					AutowireCapableBeanFactory autowireCapableBeanFactory = applicationContext.getAutowireCapableBeanFactory();
					ObjectProvider<T> objectProvider =  autowireCapableBeanFactory.getBeanProvider(requiredType);
					t= objectProvider instanceof FlexiCoreObjectProvider?((FlexiCoreObjectProvider<T>) objectProvider).getIfUnique(visitedContext): objectProvider.getIfUnique();
					if (t != null) {
						return t;
					}
				}


			}
		}
		return t;
	}

	@Override
	public Stream<T> stream() {
		return stream(new HashSet<>());
	}

	public Stream<T> stream(Set<String> visitedContext) {
		Stream<T> stream = beanProvider.stream();
		for (ApplicationContext applicationContext : dependenciesContext) {
			if(visitedContext.add(applicationContext.getId())){
				AutowireCapableBeanFactory autowireCapableBeanFactory = applicationContext.getAutowireCapableBeanFactory();
				ObjectProvider<T> objectProvider =  autowireCapableBeanFactory.getBeanProvider(requiredType);
				stream = Stream.concat(stream,objectProvider instanceof FlexiCoreObjectProvider?((FlexiCoreObjectProvider<T>) objectProvider).stream(visitedContext):objectProvider.stream());
			}

		}
		return stream;

	}

	@Override
	public Stream<T> orderedStream() {
		return orderedStream(new HashSet<>());
	}

	public Stream<T> orderedStream(Set<String> visitedContext) {
		Stream<T> stream = beanProvider.orderedStream();
		for (ApplicationContext applicationContext : dependenciesContext) {
			if(visitedContext.add(applicationContext.getId())){
				AutowireCapableBeanFactory autowireCapableBeanFactory = applicationContext.getAutowireCapableBeanFactory();
				ObjectProvider<T> objectProvider =  autowireCapableBeanFactory.getBeanProvider(requiredType);
				stream = Stream.concat(stream,objectProvider instanceof FlexiCoreObjectProvider?((FlexiCoreObjectProvider<T>) objectProvider).orderedStream(visitedContext):objectProvider.orderedStream());
			}
		}
		return stream;
	}
}
