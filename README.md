
# ![](https://support.wizzdi.com/wp-content/uploads/2020/05/flexicore-icon-extra-small.png) FlexiCore Dynamic Invokers Model [![Build Status](https://jenkins.wizzdi.com/buildStatus/icon?job=wizzdi+organization%2Fflexicore-dynamic-invokers-model%2Fmaster)](https://jenkins.wizzdi.com/job/wizzdi%20organization/job/flexicore-dynamic-invokers-model/job/master/)[![Maven Central](https://img.shields.io/maven-central/v/com.wizzdi/flexicore-dynamic-invokers-model.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22com.wizzdi%22%20AND%20a:%22flexicore-dynamic-invokers-model%22)


For comprehensive information about FlexiCore Dynamic Invokers Model please visit our [site](http://wizzdi.com/).

## What it does?

FlexiCore Dynamic Invokers Model is a FlexiCore Module and a FlexiCore Entity Plugin.
FlexiCore Dynamic Invokers Model defines the entities required by [FlexiCore Security Service](https://github.com/wizzdi/flexicore-security-service) for its multi-tenancy access control support.

## How to use as a Module?
Add the flexicore-dynamic-invokers-model dependency to your main app using the latest version available from maven central:

            <dependency>
                <groupId>com.wizzdi</groupId>
                <artifactId>flexicore-dynamic-invokers-model</artifactId>
                <version>LATEST</version>
            </dependency>
            
## How to use as an Entity Plugin?
Add the flexicore-dynamic-invokers-model dependency to your main app using the latest version available from maven central:

            <dependency>
                <groupId>com.wizzdi</groupId>
                <artifactId>flexicore-dynamic-invokers-model</artifactId>
                <version>LATEST</version>
                <scope>provided</scope>
            </dependency>
add the flexicore-dynamic-invokers-model.jar to your [entities folder.](https://github.com/wizzdi/flexicore-boot-starter-data-jpa)
## Enable Dynamic Invokers 
you can add a field of type `Baseclass` to your entity:

    @EnableFlexiCorePlugins  
    @EnableDynamicInvokersPlugins
    @SpringBootApplication  
    public class App {

       public static void main(String[] args) {  
      
        SpringApplication app = new SpringApplication(App.class);  
        app.addListeners(new ApplicationPidFileWriter());  
        ConfigurableApplicationContext context=app.run(args);  
      
      }
    }
## Main Dependencies

[FlexiCore Boot](https://github.com/wizzdi/flexicore-boot)
