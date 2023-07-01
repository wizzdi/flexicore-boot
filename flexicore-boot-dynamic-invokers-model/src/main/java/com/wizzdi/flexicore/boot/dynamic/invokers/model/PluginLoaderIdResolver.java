package com.wizzdi.flexicore.boot.dynamic.invokers.model;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.DatabindContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.jsontype.impl.TypeIdResolverBase;
import com.wizzdi.flexicore.boot.base.init.FlexiCorePluginManager;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Component
public class PluginLoaderIdResolver extends TypeIdResolverBase implements ApplicationContextAware {

	private JavaType superType;
	private static final AtomicBoolean init=new AtomicBoolean(false);
	private static final List<ClassLoader> classLoaders=new ArrayList<>();

	@Override
	public void init(JavaType baseType) {
		superType = baseType;
	}


	@Override
	public String idFromValue(Object obj) {
		return idFromValueAndType(obj, obj.getClass());
	}

	@Override
	public JavaType typeFromId(DatabindContext context, String id) throws IOException {
		ClassNotFoundException e=null;
		for (ClassLoader classLoader : classLoaders) {
			try {
				Class<?> c = Class.forName(id, true, classLoader);
				return context.getTypeFactory().withClassLoader(classLoader).constructSpecializedType(superType,c);

			}
			catch (ClassNotFoundException classNotFoundException){
				e=classNotFoundException;
			}
		}
		throw new IOException("could not find type "+id,e);
	}

	@Override
	public String idFromValueAndType(Object obj, Class<?> subType) {
		return subType.getCanonicalName();
	}

	@Override
	public JsonTypeInfo.Id getMechanism() {
		return JsonTypeInfo.Id.CLASS;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		if(init.compareAndSet(false,true)){
			classLoaders.addAll(applicationContext.getBean(FlexiCorePluginManager.class).getStartedPlugins().stream().map(f->f.getPluginClassLoader()).collect(Collectors.toList()));
			classLoaders.add(PluginLoaderIdResolver.class.getClassLoader());
		}
	}
}
