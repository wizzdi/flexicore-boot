package com.wizzdi.flexicore.boot.base.init;

import com.wizzdi.flexicore.boot.base.interfaces.ContextCustomizer;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import org.pf4j.PluginWrapper;
import org.pf4j.spring.SpringExtensionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.ApplicationContext;
import org.springframework.util.ClassUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;

public class FlexiCoreExtensionFactory extends SpringExtensionFactory {

    private final FlexiCorePluginManager pluginManager;
    private final Map<String, FlexiCoreApplicationContext> contextCache = new ConcurrentHashMap<>();
    private final Queue<ApplicationContext> pluginsApplicationContexts = new LinkedBlockingQueue<>();
    private final Logger logger = LoggerFactory.getLogger(FlexiCoreExtensionFactory.class);


    public FlexiCoreExtensionFactory(FlexiCorePluginManager pluginManager) {
        super(pluginManager, true);
        this.pluginManager = pluginManager;
    }


    @Override
    public <T> T create(Class<T> extensionClass) {
        try {
                PluginWrapper pluginWrapper = this.pluginManager.whichPlugin(extensionClass);
                ApplicationContext pluginContext = pluginWrapper!=null?getApplicationContext(pluginWrapper):pluginManager.getApplicationContext();
                T extension=null;
                try {
                    extension = pluginContext.getBean(extensionClass);
                }
                catch (Throwable ignored){ }
                if(!extensionClass.isInterface()&&pluginWrapper!=null&&(extension==null||!ClassUtils.getUserClass(extension).equals(extensionClass))){
                    extension = this.createWithoutSpring(extensionClass);
                    if (extension != null) {

                        pluginContext.getAutowireCapableBeanFactory().autowireBean(extension);


                    }
                }
                return extension;




        }
        catch (Exception e){
            logger.error("failed creating extension class",e);
            return null;

        }


    }


    public ApplicationContext getApplicationContext(PluginWrapper pluginWrapper) {
        if(pluginWrapper==null){
            return pluginManager.getApplicationContext();
        }
        String pluginId = pluginWrapper!=null?pluginWrapper.getPluginId():"core-extensions";
        FlexiCoreApplicationContext applicationContext = contextCache.get(pluginId);
        if (applicationContext == null) {
            long start=System.currentTimeMillis();
            applicationContext = createApplicationContext(pluginWrapper);
            contextCache.put(pluginId, applicationContext);
            List<String> dependencies = pluginWrapper!=null?pluginWrapper.getDescriptor().getDependencies().parallelStream().map(f -> f.getPluginId()).sorted().collect(Collectors.toList()):new ArrayList<>();
            List<ApplicationContext> dependenciesContexts=dependencies.stream().map(f->pluginManager.getPlugin(f)).filter(f->f!=null).map(this::getApplicationContext).collect(Collectors.toList());
            applicationContext.getAutowireCapableBeanFactory().addDependenciesContext(dependenciesContexts);
            for (ContextCustomizer applicationCustomizer : pluginManager.getApplicationCustomizers()) {
                applicationCustomizer.customize(applicationContext,pluginWrapper,pluginManager);
            }
            applicationContext.refresh();
            pluginsApplicationContexts.add(applicationContext);
            logger.debug("creating context for "+pluginId +" took "+(System.currentTimeMillis()-start)+"ms");

        }
        return applicationContext;
    }

    private FlexiCoreApplicationContext createApplicationContext(PluginWrapper pluginWrapper) {
        String pluginId = pluginWrapper!=null?pluginWrapper.getPluginId():null;
        List<Class<? extends Plugin>> beanClasses = pluginManager.getExtensionClasses(Plugin.class, pluginId);
        ClassLoader pluginClassLoader = pluginWrapper!=null?pluginWrapper.getPluginClassLoader():Thread.currentThread().getContextClassLoader();
        FlexiCoreApplicationContext applicationContext = new FlexiCoreApplicationContext();
        applicationContext.setParent(pluginManager.getApplicationContext());
        applicationContext.setClassLoader(pluginClassLoader);
        for (Class<? extends Plugin> beanClass : beanClasses) {
            applicationContext.register(beanClass);
        }

        return applicationContext;

    }



    public Queue<ApplicationContext> getPluginsApplicationContexts() {
        return pluginsApplicationContexts;
    }

}
