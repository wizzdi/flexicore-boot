package com.wizzdi.flexicore.boot.websockets.app;

import com.wizzdi.flexicore.boot.base.annotations.plugins.EnableFlexiCorePlugins;
import com.wizzdi.flexicore.boot.websockets.annotations.EnableFlexiCoreWebSocketPlugins;
import com.wizzdi.flexicore.boot.websockets.pluginA.TestWS;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

import java.util.Arrays;

@EnableFlexiCorePlugins
@EnableFlexiCoreWebSocketPlugins
@SpringBootApplication
@ComponentScan( excludeFilters={
		@ComponentScan.Filter(type= FilterType.ASSIGNABLE_TYPE, value=TestWS.class)})
public class App {




	public static void main(String[] args) {


		SpringApplication app = new SpringApplication(App.class);
		app.addListeners(new ApplicationPidFileWriter());
		ConfigurableApplicationContext context=app.run(args);

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
