package com.wizzdi.flexicore.boot.rest.service;

import com.wizzdi.flexicore.boot.rest.interfaces.ApiPathChangeExclusion;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcRegistrations;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Configuration
public class ExternalStaticFilesConfig {
    // I assign filePath and pathPatterns using @Value annotation
    @Value("${flexicore.externalStatic:/home/flexicore/ui/}")
    private String externalStatic;
    @Value("${flexicore.externalStaticMapping:/**}")
    private String externalStaticMapping;

    @Value("${flexicore.internalStaticLocation:classpath:/static/}")
    private String internalStaticLocation;
    @Value("${flexicore.internalStaticMapping:/FlexiCore/**}")
    private String internalStaticMapping;
    @Value("${flexicore.api.path:#{null}}")
    private String apiPath;

    @Bean
    public WebMvcRegistrations webMvcRegistrations(ObjectProvider<ApiPathChangeExclusion> objectProvider){
        return new WebMvcRegistrations() {
            @Override
            public RequestMappingHandlerMapping getRequestMappingHandlerMapping() {
                Map<String, ? extends Class<?>> collect = objectProvider.stream().map(f -> f.getExclusion()).flatMap(List::stream).collect(Collectors.toMap(f -> f.getCanonicalName(), f -> f, (a, b) -> a));
                Set<String> packageNames=objectProvider.stream().map(f -> f.getExcludedPackages()).flatMap(Set::stream).collect(Collectors.toSet());
                return new CustomRequestMappingHandlerMapping(new ArrayList<>(collect.values()),packageNames,apiPath);
            }
        };
    }

    @Bean
    public WebMvcConfigurer webMvcConfigurerAdapter() {
        return new WebMvcConfigurer() {


            @Override
            public void addResourceHandlers(ResourceHandlerRegistry registry) {
                registry.addResourceHandler(internalStaticMapping)
                        .addResourceLocations(internalStaticLocation);
                registry.addResourceHandler(externalStaticMapping)
                        .addResourceLocations("file:" + externalStatic);
            }

            @Override
            public void addViewControllers(ViewControllerRegistry registry) {
                registry.addViewController("/FlexiCore").setViewName("redirect:/FlexiCore/"); //delete these two lines if looping with directory as below
                registry.addViewController("/FlexiCore/").setViewName("forward:/FlexiCore/index.html"); //delete these two lines if looping with directory as below
                registry.addViewController("/notFound").setViewName("forward:/index.html");
                registry.addViewController("/").setViewName("forward:/index.html");

                String[] directories = listDirectories(externalStatic);
                if(directories!=null){
                    for (String subDir : directories){
                        registry.addViewController("/"+subDir).setViewName("redirect:/" + subDir + "/");
                        registry.addViewController("/"+subDir+"/").setViewName("forward:/" + subDir + "/index.html");
                    }
                }

            }
        };
    }
    @Bean
    public WebServerFactoryCustomizer<ConfigurableServletWebServerFactory> containerCustomizer() {
        return container -> {
            container.addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND,
                    "/notFound"));
        };
    }

    private String[] listDirectories(String root){
        File file = new File(root);
        return file.list((current, name) -> new File(current, name).isDirectory());
    }
}