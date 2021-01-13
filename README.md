
# ![](https://support.wizzdi.com/wp-content/uploads/2020/05/flexicore-icon-extra-small.png) FlexiCore Boot Starter Data JPA [![Build Status](https://jenkins.wizzdi.com/buildStatus/icon?job=wizzdi+organization%2Fflexicore-boot-starter-data-jpa%2Fmaster)](https://jenkins.wizzdi.com/job/wizzdi%20organization/job/flexicore-boot-starter-data-jpa/job/master/)[![Maven Central](https://img.shields.io/maven-central/v/com.wizzdi/flexicore-boot-starter-data-jpa.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22com.wizzdi%22%20AND%20a:%22flexicore-boot-starter-data-jpa%22)


For comprehensive information about FlexiCore Boot Starter Data JPA please visit our [site](http://wizzdi.com/).

## What it does?

FlexiCore Boot Starter Data JPA is a FlexiCore Module that provides an opinionated Eclipselink JPA registration for spring boot.

## How to use?
Add the flexicore-boot-starter-data-jpa dependency using the latest version available from maven central:

            <dependency>
                <groupId>com.wizzdi</groupId>
                <artifactId>flexicore-boot-starter-data-jpa</artifactId>
                <version>LATEST</version>
            </dependency>
annotate your application class or your configuration class with

    @EnableFlexiCoreJPAPlugins
you have have to use Spring's [PropertiesLauncher](https://docs.spring.io/spring-boot/docs/current/api/org/springframework/boot/loader/PropertiesLauncher.html) to load your application with entities plugins , here is an example for the command line:

    java -Dloader.main=com.your.company.YourApplicationClass -Dloader.path=file:/path/to/entities/folder -jar your-application-exec.jar 

## Example
your application class:

    @EnableFlexiCorePlugins  
    @EnableFlexiCoreJPAPlugins
    @SpringBootApplication  
    public class App {  
      
       public static void main(String[] args) {  
      
          SpringApplication app = new SpringApplication(App.class);  
      app.addListeners(new ApplicationPidFileWriter());  
      ConfigurableApplicationContext context=app.run(args);  
      
      }
    }
an entity inside an entity plugin:

    @Entity  
    public class TestEntity {  
      
       @Id  
      private String id;  
      
     private String name;  
      
      @Id  
      public String getId() {  
          return id;  
      }  
      
       public <T extends TestEntity> T setId(String id) {  
          this.id = id;  
     return (T) this;  
      }  
      
       public String getName() {  
          return name;  
      }  
      
       public <T extends TestEntity> T setName(String name) {  
          this.name = name;  
     return (T) this;  
      }  
    }

## Configuration
FlexiCore Boot Starter Data JPA exposes the following configuration options:
|name|default value  |description|
|--|--|--|
| flexicore.entities| /home/flexicore/entities/|location of entities plugins should be the same as the one provided for Dloader.path in the command running the application


## Main Dependencies

[FlexiCore Boot](https://github.com/wizzdi/flexicore-boot)

[Eclipselink](https://github.com/eclipse-ee4j/eclipselink)

[Spring Boot Starter Data JPA](https://search.maven.org/artifact/org.springframework.boot/spring-boot-starter-data-jpa)
