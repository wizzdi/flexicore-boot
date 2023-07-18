
# ![](https://support.wizzdi.com/wp-content/uploads/2020/05/flexicore-icon-extra-small.png) Resteasy Flexicore Boot Starter [![Build Status](https://jenkins.wizzdi.com/buildStatus/icon?job=wizzdi+organization%2Fresteasy-flexicore-boot-starter%2Fmaster)](https://jenkins.wizzdi.com/job/wizzdi%20organization/job/resteasy-flexicore-boot-starter/job/master/)[![Maven Central](https://img.shields.io/maven-central/v/com.wizzdi/resteasy-flexicore-boot-starter.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22com.wizzdi%22%20AND%20a:%22resteasy-flexicore-boot-starter%22)


For comprehensive information about Resteasy FlexiCore Boot Starter please visit our [site](http://wizzdi.com/).

## What it does?

Rest Easy FlexiCore Boot Starter is a FlexiCore Module that enables Rest Easy's JAX-RS inside FlexiCore Plugins.

## How to use?
Add the resteasy-flexicore-boot-starter dependency using the latest version available from maven central:

            <dependency>
                <groupId>com.wizzdi</groupId>
                <artifactId>resteasy-flexicore-boot-starter</artifactId>
                <version>LATEST</version>
            </dependency>
Simply annotate your application class or your configuration class with

    @EnableFlexiCoreJAXRSPlugins

## Example
your application class:

    @EnableFlexiCorePlugins  
    @EnableFlexiCoreJAXRSPlugins
    @SpringBootApplication  
    public class App {  
      
       public static void main(String[] args) {  
      
          SpringApplication app = new SpringApplication(App.class);  
      app.addListeners(new ApplicationPidFileWriter());  
      ConfigurableApplicationContext context=app.run(args);  
      
      }
    }
a JAX-RS annotated bean inside a plugins:

    @Path("/testEntity")  
    @RequestScoped  
    @Component  
    @Extension  
    public class PluginAService implements Plugin {  
      
       @GET  
       @Path("/test")  
       public String test(@RequestParam(name="name", required=false, defaultValue="Stranger") String name) {  
          return "hello "+name;  
      }  
      
      
    }


## Main Dependencies

[FlexiCore Boot](https://github.com/wizzdi/flexicore-boot)

[FlexiCore Boot Starter Web](https://github.com/wizzdi/flexicore-boot-starter-web)

[RestEasy Spring Boot Starter](https://github.com/resteasy/resteasy-spring-boot)
