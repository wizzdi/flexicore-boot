package com.wizzdi.flexicore.boot.swagger.service;

import com.wizzdi.flexicore.boot.base.init.FlexiCorePluginManager;
import org.springdoc.api.AbstractOpenApiResource;
import org.springdoc.core.customizers.OpenApiBuilderCustomizer;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
public class SpringDocPluginConfiguration {

	@Bean
	@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
	@Lazy
	public OpenApiBuilderCustomizer openApiBuilderCustomizer(FlexiCorePluginManager pluginManager){
		return openApiService -> {
			List<Map<String, Object>> restControllers = pluginManager.getStartedPlugins().stream().map(pluginManager::getApplicationContext).map(f -> f.getBeansWithAnnotation(RestController.class)).collect(Collectors.toList());
			restControllers.forEach(openApiService::addMappings);
			restControllers.stream().map(Map::values).flatMap(Collection::stream).forEach(f->AbstractOpenApiResource.addRestControllers(ClassUtils.getUserClass(f.getClass())));
		};
	}
}
