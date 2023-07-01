package com.flexicore.init;

import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import org.pf4j.Extension;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {"com.flexicore","com.wizzdi.flexicore.file"})
@Extension
public class FlexiCoreBaseModule implements Plugin {






}
