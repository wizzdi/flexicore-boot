package com.wizzdi.flexicore.boot.dynamic.invokers.service;

import com.wizzdi.flexicore.boot.base.init.FlexiCorePluginManager;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.boot.dynamic.invokers.annotations.Invoker;
import com.wizzdi.flexicore.boot.dynamic.invokers.response.InvokerInfo;
import org.pf4j.Extension;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
@Extension
public class DynamicInvokersProvider implements Plugin {



	@Bean
	@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
	public List<InvokerInfo> invokerInfos(FlexiCorePluginManager flexiCorePluginManager){
		List<ApplicationContext> collect = flexiCorePluginManager.getStartedPlugins().stream().map(f -> flexiCorePluginManager.getApplicationContext(f)).collect(Collectors.toList());
		collect.add(flexiCorePluginManager.getApplicationContext());
		return collect.stream().map(f -> getInvokers(f)).flatMap(List::stream).map(f->new InvokerInfo(f)).collect(Collectors.toList());
	}

	private List<Object> getInvokers(ApplicationContext f) {
		Map<String, Object> restControllers = f.getBeansWithAnnotation(RestController.class);
		Map<String, Invoker> invokers = f.getBeansOfType(Invoker.class);
		restControllers.putAll(invokers);
		return new ArrayList<>(restControllers.values());
	}
}
