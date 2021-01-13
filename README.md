
# ![](https://support.wizzdi.com/wp-content/uploads/2020/05/flexicore-icon-extra-small.png) FlexiCore Security Model [![Build Status](https://jenkins.wizzdi.com/buildStatus/icon?job=wizzdi+organization%2Fflexicore-security-model%2Fmaster)](https://jenkins.wizzdi.com/job/wizzdi%20organization/job/flexicore-security-model/job/master/)[![Maven Central](https://img.shields.io/maven-central/v/com.wizzdi/flexicore-security-model.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22com.wizzdi%22%20AND%20a:%22flexicore-security-model%22)


For comprehensive information about FlexiCore Security Model please visit our [site](http://wizzdi.com/).

## What it does?

FlexiCore Security Model is a FlexiCore Module and a FlexiCore Entity Plugin.
FlexiCore Security Model defines the entities required by [FlexiCore Security Service](https://github.com/wizzdi/flexicore-security-service) for its multi-tenancy access control support.

## How to use as a Module?
Add the flexicore-security-model dependency to your main app using the latest version available from maven central:

            <dependency>
                <groupId>com.wizzdi</groupId>
                <artifactId>flexicore-security-model</artifactId>
                <version>LATEST</version>
            </dependency>
            
## How to use as an Entity Plugin?
Add the flexicore-security-model dependency to your main app using the latest version available from maven central:

            <dependency>
                <groupId>com.wizzdi</groupId>
                <artifactId>flexicore-security-model</artifactId>
                <version>LATEST</version>
                <scope>provided</scope>
            </dependency>
add the flexicore-security-model.jar to your [entities folder.](https://github.com/wizzdi/flexicore-boot-starter-data-jpa)
## Creating Entities With Data Access Control
you can add a field of type `Baseclass` to your entity:

    @Entity  
    public class TestEntity{  
      
       @Id  
      private String id;  
      @ManyToOne(targetEntity = Baseclass.class)  
       private Baseclass security;  
      
      @Id  
      public String getId() {  
          return id;  
      }  
      
       public <T extends TestEntity> T setId(String id) {  
          this.id = id;  
     return (T) this;  
      }  
      
     
      
       @ManyToOne(targetEntity = Baseclass.class)  
       public Baseclass getSecurity() {  
          return security;  
      }  
      
       public <T extends TestEntity> T setSecurity(Baseclass security) {  
          this.security = security;  
     return (T) this;  
      }  
    }
or you can directly inherit from `Baseclass`

    @Entity  
    public class TestEntity extends Baseclass{  
      
      @ManyToOne(targetEntity = Baseclass.class)  
       private Baseclass security;  
      
       @ManyToOne(targetEntity = Baseclass.class)  
       public Baseclass getSecurity() {  
          return security;  
      }  
      
       public <T extends TestEntity> T setSecurity(Baseclass security) {  
          this.security = security;  
     return (T) this;  
      }  
    }
## Main Dependencies

[FlexiCore Boot](https://github.com/wizzdi/flexicore-boot)

[FlexiCore Boot Starter Data JPA](https://github.com/wizzdi/flexicore-boot-starter-data-jpa)
