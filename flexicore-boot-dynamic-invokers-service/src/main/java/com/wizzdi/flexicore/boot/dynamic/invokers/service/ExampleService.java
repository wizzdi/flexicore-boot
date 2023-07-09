package com.wizzdi.flexicore.boot.dynamic.invokers.service;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.boot.dynamic.invokers.annotations.ListFieldInfo;
import org.pf4j.Extension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.ClassUtils;
import org.springframework.web.client.HttpServerErrorException;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.util.*;

@Extension
@Service
public class ExampleService implements Plugin {

	private static final Logger logger= LoggerFactory.getLogger(ExampleService.class);


	public Object getExample(Class<?> c) {

		Object exampleCached = getExampleCached(c);
		if (exampleCached == null) {
			throw new HttpServerErrorException(HttpStatus.SERVICE_UNAVAILABLE,"Class " + c + " is not suitable for examples");
		}
		return exampleCached;

	}

	@Cacheable(value = "exampleCache", key = "#c.getCanonicalName()", unless = "#result==null",cacheManager = "exampleCacheManager")
	public Object getExampleCached(Class<?> c) {
		return generateExample(c);
	}

	@CachePut(value = "exampleCache", key = "#c.getCanonicalName()", unless = "#result==null",cacheManager = "exampleCacheManager")
	public Object updateExampleCache(Class<?> c,Object value){
		return value;
	}

	private String getSetterName(String name) {
		if (name.startsWith("get")) {
			return name.replaceFirst("get", "set");
		}

		if (name.startsWith("is")) {
			return name.replaceFirst("is", "set");
		}
		return null;
	}


	private Object generateExample(Class<?> c) {
		if (ClassUtils.isPrimitiveOrWrapper(c) || c.equals(String.class)) {
			return getPrimitiveValue(c);
		}
		if (c.isArray()) {
			return Array.newInstance(c, 0);
		}
		if (isKnownType(c)) {
			return getKnownTypeValue(c);
		}


		Object example = null;
		try {
			example = c.getConstructor().newInstance();
			updateExampleCache(c, example);
			BeanInfo beanInfo = Introspector.getBeanInfo(c, Object.class);
			for (PropertyDescriptor propertyDescriptor : beanInfo.getPropertyDescriptors()) {
				try {
					Method getter = propertyDescriptor.getReadMethod();
					if (getter != null) {
						if (AnnotatedElementUtils.findMergedAnnotation(getter,JsonIgnore.class) == null) {
							String setterName = getSetterName(getter.getName());
							if (setterName != null) {
								try{
									Method setter = c.getMethod(setterName, propertyDescriptor.getPropertyType());
									Class<?> toRet = getter.getReturnType();
									if (Collection.class.isAssignableFrom(toRet)) {
										ListFieldInfo listFieldInfo = AnnotatedElementUtils.findMergedAnnotation(getter,ListFieldInfo.class);
										if (listFieldInfo != null) {
											Class<?> collectionType = listFieldInfo.listType();
											Object o = getExampleCached(collectionType);
											Collection collection = (Collection) toRet.newInstance();
											collection.add(o);
											setter.invoke(example, collection);

										}
									} else {
										setter.invoke(example, getExampleCached(toRet));
									}
								}
								catch (NoSuchMethodException e){
									logger.debug("failed setting example value for " + propertyDescriptor.getName());
								}

							}
						}
					}
				} catch (Exception e) {
					logger.info("failed setting example value for " + propertyDescriptor.getName());
				}

			}
			return example;
		} catch (Exception e) {
			logger.error( "failed getting example value for " + c.getCanonicalName(), e);
		}
		return example;
	}

	private Object getKnownTypeValue(Class<?> c) {
		if (c.equals(LocalDateTime.class)) return LocalDateTime.now();
		if (c.equals(OffsetDateTime.class)) return OffsetDateTime.now();
		if (c.equals(ZonedDateTime.class)) return ZonedDateTime.now();
		if (c.equals(Date.class)) return Date.from(Instant.now());
		if (c.equals(List.class)) return new ArrayList<>();
		if (c.equals(Map.class)) return new HashMap<>();


		return null;
	}
	private static final Set<String> knownTypes = new HashSet<>(Arrays.asList(OffsetDateTime.class.getCanonicalName(),LocalDateTime.class.getCanonicalName(),
			Date.class.getCanonicalName(), ZonedDateTime.class.getCanonicalName(), List.class.getCanonicalName(), Map.class.getCanonicalName()));


	private boolean isKnownType(Class<?> c) {
		return knownTypes.contains(c.getCanonicalName());
	}

	private Object getPrimitiveValue(Class<?> c) {
		if (c.equals(String.class)) return "string";
		if (c.equals(int.class) || c.equals(Integer.class)) return 0;
		if (c.equals(double.class) || c.equals(Double.class)) return 0.0;
		if (c.equals(float.class) || c.equals(Float.class)) return 0.0f;
		if (c.equals(byte.class) || c.equals(Byte.class)) return (byte) 0;
		if (c.equals(short.class) || c.equals(Short.class)) return (short) 0;
		if (c.equals(long.class) || c.equals(Long.class)) return 0L;
		if (c.equals(boolean.class) || c.equals(Boolean.class)) return false;

		return null;
	}
}
