package com.wizzdi.flexicore.boot.data.rest.annotations;

import com.wizzdi.flexicore.boot.data.rest.init.DataRESTPluginHandlerModule;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.data.rest.RepositoryRestMvcAutoConfiguration;
import org.springframework.boot.autoconfigure.http.HttpMessageConvertersAutoConfiguration;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Import(DataRESTPluginHandlerModule.class)
@Configuration
@AutoConfigureAfter({ HttpMessageConvertersAutoConfiguration.class, JacksonAutoConfiguration.class })
@AutoConfigureBefore({ RepositoryRestMvcAutoConfiguration.class })

public @interface EnableFlexiCoreDataRESTPlugins {
}
