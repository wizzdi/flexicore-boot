package com.wizzdi.flexicore.boot.data.rest.plugin;

import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import org.pf4j.Extension;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.metamodel.Type;

@Extension
@Configuration
@EnableJpaRepositories
public class Config implements Plugin, RepositoryRestConfigurer {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
		config.exposeIdsFor(
				entityManager.getMetamodel().getEntities().stream()
						.map(Type::getJavaType)
						.toArray(Class[]::new));
	}
}
