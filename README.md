
# ![](https://support.wizzdi.com/wp-content/uploads/2020/05/flexicore-icon-extra-small.png) FlexiCore Boot Starter Actuator [![Build Status](https://jenkins.wizzdi.com/buildStatus/icon?job=wizzdi+organization%2Fflexicore-boot-starter-actuator%2Fmaster)](https://jenkins.wizzdi.com/job/wizzdi%20organization/job/flexicore-boot-starter-actuator/job/master/)[![Maven Central](https://img.shields.io/maven-central/v/com.wizzdi/flexicore-boot-starter-actuator.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22com.wizzdi%22%20AND%20a:%22flexicore-boot-starter-actuator%22)


For comprehensive information about FlexiCore Boot Starter Actuator please visit our [site](http://wizzdi.com/).

## What it does?

FlexiCore Boot Starter Actuator is a FlexiCore Module that enables Spring's Health Contributors inside FlexiCore Plugins.

## How to use?
Add the flexicore-boot-starter-actuator dependency using the latest version available from maven central:

            <dependency>
                <groupId>com.wizzdi</groupId>
                <artifactId>flexicore-boot-starter-actuator</artifactId>
                <version>LATEST</version>
            </dependency>
Simply annotate your application class or your configuration class with

    @EnableFlexiCoreHealthPlugins

## Example
your application class:

    @EnableFlexiCorePlugins  
    @EnableFlexiCoreHealthPlugins
    @SpringBootApplication  
    public class App {  
      
       public static void main(String[] args) {  
      
          SpringApplication app = new SpringApplication(App.class);  
      app.addListeners(new ApplicationPidFileWriter());  
      ConfigurableApplicationContext context=app.run(args);  
      
      }
    }
a HealthIndicator inside a plugin:

    @Component  
    @Extension  
    public class TestHealth implements HealthIndicator, Plugin {  
      
      
      
       @Override  
      public Health health() {  
          return new Health.Builder().withDetail("test","123").up().build();  
      }  
    }

## Main Dependencies

[FlexiCore Boot](https://github.com/wizzdi/flexicore-boot)

[FlexiCore Boot Starter Web](https://github.com/wizzdi/flexicore-boot-starter-web)

[Spring Boot Starter Actuator](https://search.maven.org/artifact/org.springframework.boot/spring-boot-starter-actuator)
