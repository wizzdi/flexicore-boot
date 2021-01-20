
# ![](https://support.wizzdi.com/wp-content/uploads/2020/05/flexicore-icon-extra-small.png) FlexiCore Boot Starter Data REST [![Build Status](https://jenkins.wizzdi.com/buildStatus/icon?job=wizzdi+organization%flexicore-boot-starter-data-rest%2Fmaster)](https://jenkins.wizzdi.com/job/wizzdi%20organization/job/flexicore-boot-starter-data-rest/job/master/)[![Maven Central](https://img.shields.io/maven-central/v/com.wizzdi/flexicore-boot-starter-data-rest.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22com.wizzdi%22%20AND%20a:%22flexicore-boot-starter-data-rest%22)


For comprehensive information about FlexiCore Boot Starter Data REST please visit our [site](http://wizzdi.com/).

## What it does?

FlexiCore Boot Starter Data REST is a FlexiCore Module that enables Spring's Data REST inside FlexiCore Plugins.

## How to use?
Add the flexicore-boot-starter-data-rest dependency using the latest version available from maven central:

            <dependency>
                <groupId>com.wizzdi</groupId>
                <artifactId>flexicore-boot-starter-data-rest</artifactId>
                <version>LATEST</version>
            </dependency>
Simply annotate your application class or your configuration class with

    @EnableFlexiCoreDataRESTPlugins

## Example
your application class:

    @EnableFlexiCorePlugins  
    @EnableFlexiCoreDataRESTPlugins
    @SpringBootApplication  
    public class App {  
      
       public static void main(String[] args) {  
      
          SpringApplication app = new SpringApplication(App.class);  
      app.addListeners(new ApplicationPidFileWriter());  
      ConfigurableApplicationContext context=app.run(args);  
      
      }
    }
a Spring Data REST Repository:

    @RepositoryRestResource(collectionResourceRel = "books", path = "books")
    @Extension
    public interface BookRepository extends Plugin,PagingAndSortingRepository<Book, Long> {
    
	    List<Book> findByName( @Param("name") String name);
    
    
    }


## Main Dependencies

[FlexiCore Boot](https://github.com/wizzdi/flexicore-boot)


[Spring Boot Starter Data REST](https://search.maven.org/artifact/org.springframework.boot/spring-boot-data-rest)