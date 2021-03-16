package com.wizzdi.flexicore.boot.data.rest.service;

import com.wizzdi.flexicore.boot.base.init.FlexiCorePluginManager;
import org.pf4j.PluginManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.data.repository.support.Repositories;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
public class RepositoriesProvider {

	@Lazy
	@Autowired
	private FlexiCorePluginManager pluginManager;

	@Autowired
	private ApplicationContext applicationContext;

	@Lazy
	@Bean
	@Primary
	public Repositories repositories(){
		List<Repositories> repositories = pluginManager.getPluginApplicationContexts().stream().map(f -> new Repositories(f)).collect(Collectors.toList());
		return new CombinedRepositories(applicationContext,repositories);
	}
}
