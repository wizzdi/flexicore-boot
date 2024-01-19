package com.wizzdi.flexicore.file.annotations;

import com.wizzdi.flexicore.file.init.FileModule;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Import(FileModule.class)
@Configuration
public @interface EnableFileModule {
}
