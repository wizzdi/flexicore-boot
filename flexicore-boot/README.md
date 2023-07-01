
# ![](https://support.wizzdi.com/wp-content/uploads/2020/05/flexicore-icon-extra-small.png) FlexiCore Boot [![Build Status](https://jenkins.wizzdi.com/buildStatus/icon?job=wizzdi+organization%2Fflexicore-boot%2Fmaster)](https://jenkins.wizzdi.com/job/wizzdi%20organization/job/flexicore-boot/job/master/)[![Maven Central](https://img.shields.io/maven-central/v/com.wizzdi/flexicore-boot.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22com.wizzdi%22%20AND%20a:%22flexicore-boot%22)


For comprehensive information about FlexiCore Boot please visit our [site](http://wizzdi.com/).

## What it does?

FlexiCore Boot enables plugin loading support for your spring boot app it is built on [pf4j](https://pf4j.org/) and depends only on it.

FlexiCore allows full multi-layer dependency between Spring enabled plugins.
## How to use?
Add the flexicore-boot dependency using the latest version available from maven central:

	          <dependency>
                <groupId>com.wizzdi</groupId>
                <artifactId>flexicore-boot</artifactId>
                <version>LATEST</version>
            </dependency>
Simply annotate your application class or your configuration class with

    @EnableFlexiCorePlugins
FlexiCore will automatically load jars and create beans for them.
FlexiCore Boot Plugins allow composing a full spring boot application from plugins by using additional FlexiCore Boot modules such as [flexicore-boot-starter-web](https://github.com/wizzdi/flexicore-boot-starter-web)[,flexicore-boot-starter-actuator](https://github.com/wizzdi/flexicore-boot-starter-actuator),[resteasy-flexicore-boot-starter](https://github.com/wizzdi/resteasy-flexicore-boot-starter) and more.
## Creating Plugins
creating plugins is as simple as creating regular spring beans the only difference is that the bean should be annotated with `@Extension` and implement the empty interface `Plugin` .
the jar containing the class should be packaged using pf4j plugin instructions so it contains a MANIFEST.MF describing it, more on that [here.](https://pf4j.org/doc/packaging.html)
## Example
your application class:

    @EnableFlexiCorePlugins  
    @SpringBootApplication  
    public class App {  
      
       public static void main(String[] args) {  
      
          SpringApplication app = new SpringApplication(App.class);  
      app.addListeners(new ApplicationPidFileWriter());  
      ConfigurableApplicationContext context=app.run(args);  
      
      }
    }
a bean inside a plugin:

    @Extension  
    @Component  
    public class PluginAService implements Plugin, InitializingBean {  
      
       private static final Logger logger= LoggerFactory.getLogger(PluginAService.class);  
      
      @Override  
      public void afterPropertiesSet() throws Exception {  
          logger.info("PluginAService Started!");  
      }  
    }
## Configuration
FlexiCore Boot exposes the following configuration options:
|name|default value  |description|
|--|--|--|
| flexicore.plugins| /home/flexicore/plugins|folder to load plugins from


### Main 3rd Party Dependencies

[Spring Boot](https://github.com/spring-projects/spring-boot)

[Pf4J](https://github.com/pf4j/pf4j)
