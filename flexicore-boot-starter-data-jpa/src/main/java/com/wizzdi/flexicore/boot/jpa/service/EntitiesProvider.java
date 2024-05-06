package com.wizzdi.flexicore.boot.jpa.service;

import org.reflections.Reflections;
import org.reflections.util.ConfigurationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;

import jakarta.persistence.Converter;
import jakarta.persistence.Entity;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Configuration
public class EntitiesProvider {

	private static final Logger logger = LoggerFactory.getLogger(EntitiesProvider.class);
	@Value("${flexicore.entities:/home/flexicore/entities}")
	private String entitiesPath;
	@Autowired
	private ApplicationContext context;

	/**
	 * this will return all entities in flexicore and in ${flexicore.entities} path
	 * we make sure to limit the search so this wont cause the loading of unwanted classes with that loader
	 * in fact if we did do that several app critical classes(direct FC dependencies) will be loaded by the Reflection library
	 * causing ClassNotFound exceptions and making meta model classes fields types to be null
	 *
	 * @return entities discovered
	 */

	@Bean
	@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
	@Primary
	public Reflections reflections(){
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

		List<URL> entitiesJarsUrls;
		entitiesJarsUrls = getEntitiesJarsUrls();
		Map<String, Object> beansWithAnnotation = context.getBeansWithAnnotation(SpringBootApplication.class);
		entitiesJarsUrls.addAll(beansWithAnnotation.values().stream().map(f -> f.getClass().getProtectionDomain().getCodeSource().getLocation()).collect(Collectors.toSet()));
		ConfigurationBuilder configuration = ConfigurationBuilder.build()
				.addClassLoaders(classLoader)
				.setUrls(entitiesJarsUrls);
		return new Reflections(configuration);
	}

	@Bean
	@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
	@Primary
	public EntitiesHolder getEntities(Reflections reflections) {

		Set<Class<?>> typesAnnotatedWith = reflections.getTypesAnnotatedWith(Entity.class);
		Set<Class<?>> converters = new HashSet<>(reflections.getTypesAnnotatedWith(Converter.class));
		converters.addAll(typesAnnotatedWith);
		return new EntitiesHolder(converters);
	}




	private List<URL> getEntitiesJarsUrls() {
		File file = new File(entitiesPath);
		if (file.exists() && file.isDirectory()) {
			File[] jars = file.listFiles();
			if (jars != null) {
				return Stream.of(jars).filter(f -> f.getName().endsWith(".jar")).map(EntitiesProvider::getJarURL).filter(Objects::nonNull).collect(Collectors.toList());
			}
		}
		return new ArrayList<>();
	}

	private static URL getJarURL(File f) {
		try {
			return new URI("jar", f.toURI() +"!/",null ).toURL();
		} catch (MalformedURLException | URISyntaxException e) {
			logger.error("failed getting jar url for file" + f.getAbsolutePath());
		}
		return null;
	}

}
