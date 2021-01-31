package com.test.init;

import com.flexicore.annotations.EnableFlexiCoreBaseServices;
import com.flexicore.annotations.InheritedComponent;
import com.wizzdi.flexicore.boot.base.annotations.plugins.EnableFlexiCorePlugins;
import com.wizzdi.flexicore.boot.dynamic.invokers.annotations.EnableDynamicInvokersPlugins;
import com.wizzdi.flexicore.boot.health.annotations.EnableFlexiCoreHealthPlugins;
import com.wizzdi.flexicore.boot.jaxrs.annotations.EnableFlexiCoreJAXRSPlugins;
import com.wizzdi.flexicore.boot.jpa.annotations.EnableFlexiCoreJPAPlugins;
import com.wizzdi.flexicore.boot.rest.annotations.EnableFlexiCoreRESTPlugins;
import com.wizzdi.flexicore.security.annotations.EnableFlexiCoreSecurity;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

import java.util.Arrays;

@SpringBootApplication
@ComponentScan(basePackages = {"com.flexicore","com.test.init"},includeFilters =@ComponentScan.Filter(InheritedComponent.class))
@EnableMongoRepositories
@EnableJpaRepositories(basePackages = {"com.flexicore.data"})
@EntityScan(basePackages = {"com.flexicore.model"})
@EnableWebSocket
@EnableFlexiCoreHealthPlugins
@EnableFlexiCoreJAXRSPlugins
@EnableFlexiCorePlugins
@EnableFlexiCoreJPAPlugins
@EnableFlexiCoreRESTPlugins
@EnableFlexiCoreSecurity
@EnableDynamicInvokersPlugins
public class FlexiCoreApplication {


    public static void main(String[] args) {


        SpringApplication app = new SpringApplication(FlexiCoreApplication.class);
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

        };
    }
}
