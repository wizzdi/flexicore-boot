
# ![](https://support.wizzdi.com/wp-content/uploads/2020/05/flexicore-icon-extra-small.png) FlexiCore Boot
[![CI/CD Pipeline](https://github.com/wizzdi/flexicore-boot/actions/workflows/main.yml/badge.svg)](https://github.com/wizzdi/flexicore-boot/actions/workflows/main.yml)[![Maven Central](https://img.shields.io/maven-central/v/com.wizzdi/flexicore-boot-dependencies.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22com.wizzdi%22%20AND%20a:%22flexicore-boot-dependencies%22)


For comprehensive information about FlexiCore Boot please visit our [site](http://wizzdi.com/).

## What it does?

BOM for all of FlexiCore's Boot projects
## How to use?
Add the flexicore-boot-dependencies dependency using the latest version available from maven central:

	             <dependencyManagement>
                    <dependencies>
                        <dependency>
                        <!-- Import dependency management from FleiCore Boot -->
                            <groupId>com.wizzdi</groupId>
                            <artifactId>flexicore-boot-dependencies</artifactId>
                            <version>7.0.1</version> <!-- use latest version -->
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
