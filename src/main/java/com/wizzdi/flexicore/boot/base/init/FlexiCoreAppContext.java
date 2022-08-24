package com.wizzdi.flexicore.boot.base.init;

import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext;

public class FlexiCoreAppContext extends AnnotationConfigServletWebServerApplicationContext {


    public FlexiCoreAppContext(FlexiCoreAppBeanFactory beanFactory) {
        super(beanFactory);
    }

}
