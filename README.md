
# ![](https://support.wizzdi.com/wp-content/uploads/2020/05/flexicore-icon-extra-small.png) FlexiCore Boot Dynamic Invokers [![Build Status](https://jenkins.wizzdi.com/buildStatus/icon?job=wizzdi+organization%flexicore-boot-dynamic-invokers%2Fmaster)](https://jenkins.wizzdi.com/job/wizzdi%20organization/job/flexicore-boot-dynamic-invokers/job/master/)[![Maven Central](https://img.shields.io/maven-central/v/com.wizzdi/flexicore-boot-dynamic-invokers.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22com.wizzdi%22%20AND%20a:%22flexicore-boot-dynamic-invokers%22)


For comprehensive information about FlexiCore Boot Dynamic Invokers please visit our [site](http://wizzdi.com/).

## What it does?

FlexiCore Boot Dynamic Invokers is a FlexiCore Module that Dynamic Invokers for FlexiCore Plugins.

## How to use?
Add the flexicore-boot-dynamic-invokers dependency using the latest version available from maven central:

            <dependency>
                <groupId>com.wizzdi</groupId>
                <artifactId>flexicore-boot-dynamic-invokers</artifactId>
                <version>LATEST</version>
            </dependency>
Simply annotate your application class or your configuration class with

    @EnableDynamicInvokersPlugins

## Example
your application class:

    @EnableFlexiCorePlugins  
    @EnableFlexiCoreSecurity
    @EnableDynamicInvokersPlugins
    @SpringBootApplication  
    public class App {  
      
       public static void main(String[] args) {  
      
          SpringApplication app = new SpringApplication(App.class);  
      app.addListeners(new ApplicationPidFileWriter());  
      ConfigurableApplicationContext context=app.run(args);  
      
      }
    }
a Dynamic Invoker:

    @InvokerInfo(displayName = "test invoker",description = "test invoker")
    @Extension
    public class TestInvoker implements Invoker {



    @InvokerMethodInfo(displayName = "listTests",description = "lists all Clazzes")
    public PaginationResponse<TestEntity> listTests(TestFilter filter, SecurityContextBase securityContext) {
        if(filter==null||filter.getPageSize()==null){
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST,"page size must be provided");
        }
        return new PaginationResponse<>(Collections.singletonList(new TestEntity().setName("test").setDescription("test")),filter,1);
     }

    @Override
    public Class<?> getHandlingClass() {
        return TestEntity.class;
     }
    }
   


## Main Dependencies

[FlexiCore Boot](https://github.com/wizzdi/flexicore-boot)


[FlexiCore Security](https://github.com/wizzdi/flexicore-security-service)
