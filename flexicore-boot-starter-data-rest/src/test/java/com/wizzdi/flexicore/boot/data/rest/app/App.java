package com.wizzdi.flexicore.boot.data.rest.app;

import com.wizzdi.flexicore.boot.base.annotations.plugins.EnableFlexiCorePlugins;
import com.wizzdi.flexicore.boot.base.init.FlexiCoreApplication;
import com.wizzdi.flexicore.boot.data.rest.annotations.EnableFlexiCoreDataRESTPlugins;
import com.wizzdi.flexicore.boot.jpa.annotations.EnableFlexiCoreJPAPlugins;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Arrays;

@EnableFlexiCoreDataRESTPlugins
@EnableFlexiCorePlugins
@EnableFlexiCoreJPAPlugins
@SpringBootApplication
@EnableTransactionManagement(proxyTargetClass = true)
public class App {




	public static void main(String[] args) {


		FlexiCoreApplication app = new FlexiCoreApplication(App.class);
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
