package com.flexicore.init;

import com.flexicore.interfaces.Plugin;
import org.jboss.resteasy.springboot.ResteasyEmbeddedServletInitializer;
import org.pf4j.Extension;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Extension
@Configuration
@AutoConfigureAfter(WebMvcAutoConfiguration.class)
@EnableConfigurationProperties
public class RestEasyAdapter extends ResteasyEmbeddedServletInitializer implements Plugin {




}
