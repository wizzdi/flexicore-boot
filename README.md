
# ![](https://support.wizzdi.com/wp-content/uploads/2020/05/flexicore-icon-extra-small.png) FlexiCore Boot Dependencies [![Build Status](https://jenkins.wizzdi.com/buildStatus/icon?job=wizzdi+organization%2Fflexicore-boot-dependencies%2Fmaster)](https://jenkins.wizzdi.com/job/wizzdi%20organization/job/flexicore-boot-dependencies/job/master/)[![Maven Central](https://img.shields.io/maven-central/v/com.wizzdi/flexicore-boot-dependencies.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22com.wizzdi%22%20AND%20a:%22flexicore-boot-dependencies%22)


For comprehensive information about FlexiCore Boot please visit our [site](http://wizzdi.com/).

## What it does?

BOM for all of FlexiCore's Boot projects
## How to use?
Add the flexicore-boot-dependencies dependency using the latest version available from maven central:

	             <dependencyManagement>
                    <dependencies>
                        <dependency>
                        <!-- Import dependency management from FleiCore Boot -->
                            <groupId>org.springframework.boot</groupId>
                            <artifactId>spring-boot-dependencies</artifactId>
                            <version>2.3.0.RELEASE</version>
                            <type>pom</type>
                            <scope>import</scope>
                        </dependency>
                    </dependencies>
                </dependencyManagement>
              
Now Add dependencies without specifying versions.   

    <dependencies>        
        <dependency>
            <groupId>com.wizzdi</groupId>
            <artifactId>flexicore-boot</artifactId>
        </dependency>
    </dependencies>
