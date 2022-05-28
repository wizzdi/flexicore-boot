package com.wizzdi.flexicore.security.test.app;

import com.flexicore.model.Baseclass;
import com.flexicore.model.Basic;
import com.flexicore.model.security.SecurityPolicy;
import com.wizzdi.flexicore.boot.base.annotations.plugins.EnableFlexiCorePlugins;
import com.wizzdi.flexicore.boot.jpa.annotations.EnableFlexiCoreJPAPlugins;
import com.wizzdi.flexicore.boot.jpa.service.EntitiesHolder;
import com.wizzdi.flexicore.boot.test.helper.PluginJar;
import com.wizzdi.flexicore.security.annotations.EnableFlexiCoreSecurity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.HashSet;

@EnableFlexiCorePlugins
@EnableFlexiCoreJPAPlugins
@EnableFlexiCoreSecurity
@SpringBootApplication
public class App {


	private static final Logger logger= LoggerFactory.getLogger(App.class);
	public static void main(String[] args) {

		SpringApplication app = new SpringApplication(App.class);
		app.addListeners(new ApplicationPidFileWriter());
		ConfigurableApplicationContext context=app.run(args);

	}

	@Bean
	public EntitiesHolder entitiesHolder(){
		return new EntitiesHolder(new HashSet<>(Arrays.asList(Baseclass.class, Basic.class, SecurityPolicy.class)));
	}



	@Bean
	public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
		return args -> {

			System.out.println("Let's inspect the beans provided by Spring Boot:");

			String[] beanNames = ctx.getBeanDefinitionNames();
			Arrays.sort(beanNames);
			for (String beanName : beanNames) {
				System.out.println(beanName);
			}
			System.out.println("total of "+beanNames.length +" beans");



		};
	}
}
