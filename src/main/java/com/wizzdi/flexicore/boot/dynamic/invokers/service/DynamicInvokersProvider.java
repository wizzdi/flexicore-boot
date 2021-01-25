package com.wizzdi.flexicore.boot.dynamic.invokers.service;

import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.boot.dynamic.invokers.annotations.Invoker;
import com.wizzdi.flexicore.boot.dynamic.invokers.response.InvokerInfo;
import org.pf4j.Extension;
import org.pf4j.PluginManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.util.List;
import java.util.stream.Collectors;

@Configuration
@Extension
public class DynamicInvokersProvider implements Plugin {



	@Bean
	@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
	public List<InvokerInfo> invokerInfos(PluginManager flexiCorePluginManager){
		return flexiCorePluginManager.getExtensions(Invoker.class).stream().map(f->new InvokerInfo(f)).collect(Collectors.toList());
	}
}
