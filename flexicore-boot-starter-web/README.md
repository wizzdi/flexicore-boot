
# ![](https://support.wizzdi.com/wp-content/uploads/2020/05/flexicore-icon-extra-small.png) FlexiCore Boot Starter Web [![Build Status](https://jenkins.wizzdi.com/buildStatus/icon?job=wizzdi+organization%2Fflexicore-boot-starter-web%2Fmaster)](https://jenkins.wizzdi.com/job/wizzdi%20organization/job/flexicore-boot-starter-web/job/master/)[![Maven Central](https://img.shields.io/maven-central/v/com.wizzdi/flexicore-boot-starter-web.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22com.wizzdi%22%20AND%20a:%22flexicore-boot-starter-web%22)


For comprehensive information about FlexiCore Boot Starter Web please visit our [site](http://wizzdi.com/).

## What it does?

FlexiCore Boot Starter Web is a FlexiCore Module that enables Spring's RestController inside FlexiCore Plugins.

## How to use?
Add the flexicore-boot-starter-web dependency using the latest version available from maven central:

		<dependency>
                <groupId>com.wizzdi</groupId>
                <artifactId>flexicore-boot-starter-web</artifactId>
                <version>LATEST</version>
            </dependency>
Simply annotate your application class or your configuration class with

    @EnableFlexiCoreRESTPlugins

## Example
your application class:

    @EnableFlexiCorePlugins  
    @EnableFlexiCoreRESTPlugins
    @SpringBootApplication  
    public class App {  
      
       public static void main(String[] args) {  
      
          SpringApplication app = new SpringApplication(App.class);  
      app.addListeners(new ApplicationPidFileWriter());  
      ConfigurableApplicationContext context=app.run(args);  
      
      }
    }
a RestController inside a plugin:

    @Extension  
    @RestController  
    public class PluginAService implements Plugin {  
      
       @GetMapping("/test")  
       public String createTestEntity(@RequestParam(name="name", required=false, defaultValue="Stranger") String name) {  
          return "hello "+name;  
      }  
      
      
    }

## Configuration
FlexiCore Boot Starter Web exposes the following configuration options:
|name|default value  |description|
|--|--|--|
| flexicore.externalStatic| /home/flexicore/ui/|location of ui
| flexicore.externalStaticMapping| /**|mapping of ui
| flexicore.internalStaticLocation| classpath:/static/|location of internally provided ui
| flexicore.internalStaticMapping| /FlexiCore/**|mapping of internally provided ui


## Main Dependencies

[FlexiCore Boot](https://github.com/wizzdi/flexicore-boot)

[Spring Boot Starter Web](https://search.maven.org/artifact/org.springframework.boot/spring-boot-starter-web)
