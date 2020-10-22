package com.flexicore.init;


import com.flexicore.annotations.OperationsInside;
import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.interfaces.AspectPlugin;
import com.flexicore.interfaces.Plugin;
import com.flexicore.interfaces.RestServicePlugin;
import com.flexicore.interfaces.ServicePlugin;
import com.flexicore.interfaces.dynamic.InvokerInfo;
import com.flexicore.model.Clazz;
import com.flexicore.model.Tenant;
import com.flexicore.request.ClazzFilter;
import com.flexicore.request.TenantFilter;
import com.flexicore.rest.JaxRsActivator;
import com.flexicore.security.SecurityContext;
import com.flexicore.service.BaseclassService;
import com.flexicore.service.SecurityService;
import com.flexicore.service.impl.ClassScannerService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.pf4j.Extension;
import org.pf4j.PluginManager;
import org.pf4j.PluginWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.aspectj.annotation.AspectJProxyFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javax.ws.rs.ext.Provider;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@PluginInfo(version = 1)
@Extension
@Component
public class Init implements ServicePlugin {

    private static final Logger logger = LoggerFactory.getLogger(Init.class);
    private static final Comparator<PluginWrapper> PLUGIN_COMPARATOR_FOR_REST = Comparator.comparing(f -> f.getDescriptor().getVersion());


    @Autowired
    private ClassScannerService classScannerService;

    @Autowired
    private SecurityService securityService;

    @Autowired
    @Lazy
    private PluginManager pluginManager;

    @EventListener
    public void getStartingContext(ContextRefreshedEvent contextRefreshedEvent) throws Exception {

        logger.info("registering classes");
        classScannerService.registerClasses();
        logger.info("Initializing classes");
        List<Clazz> clazzes = classScannerService.InitializeClazzes(); // must be done first!

        logger.info("Initializing operations");
        classScannerService.InitializeOperations();
        try {
            classScannerService.createDefaultObjects();
            classScannerService.createSwaggerTags();
            classScannerService.initializeInvokers();

            registerFilterClasses();
            registerBaseServices();


        } catch (Exception ex) {
            logger.error("Error while initializing the system", ex);
        }


    }

    private void registerBaseServices() {
        long start = System.currentTimeMillis();
        SecurityContext securityContext = securityService.getAdminUserSecurityContext();

        List<PluginWrapper> startedPlugins = pluginManager.getStartedPlugins().stream().sorted(PLUGIN_COMPARATOR_FOR_REST).collect(Collectors.toList());
        List<? extends AspectPlugin> aspects = pluginManager.getExtensions(AspectPlugin.class);

        for (PluginWrapper startedPlugin : startedPlugins) {
            logger.info("REST Registration handling plugin: " + startedPlugin);

            List<? extends RestServicePlugin> restPlugins = pluginManager.getExtensions(RestServicePlugin.class, startedPlugin.getPluginId());
            for (RestServicePlugin plugin : restPlugins) {

                logger.info("REST class " + plugin);
                try {
                    AspectJProxyFactory factory = new AspectJProxyFactory(plugin);
                    for (AspectPlugin aspect : aspects) {
                        factory.addAspect(aspect);
                    }
                    factory.setProxyTargetClass(true);
                    Object proxy = factory.getProxy(plugin.getClass().getClassLoader());
                    JaxRsActivator.addSingletones(proxy);
                } catch (Exception e) {
                    logger.error("Failed registering REST service " + plugin.getClass(), e);
                }


            }


            List<Class<? extends Plugin>> classes = pluginManager.getExtensionClasses(Plugin.class, startedPlugin.getPluginId());
            for (Class<? extends Plugin> c : classes) {

            /*if (c.isAnnotationPresent(ServerEndpoint.class)) {
                try {
                    serverContainer.addEndpoint(c);
                } catch (DeploymentException e) {
                    logger.log(Level.SEVERE,"failed adding WS",e);
                }
            }
*/
                if (c.isAnnotationPresent(Provider.class)) {
                    JaxRsActivator.addProvider(c);
                }
                if (c.isAnnotationPresent(OperationsInside.class)) {
                    classScannerService.registerOperationsInclass(c); // Adds
                }
                if (c.isAnnotationPresent(InvokerInfo.class)) {
                    classScannerService.registerInvoker(c, securityContext);
                }
                if (c.isAnnotationPresent(OpenAPIDefinition.class)) {
                    classScannerService.addSwaggerTags(c, securityContext);
                }
                Tag[] annotationsByType = c.getAnnotationsByType(Tag.class);
                if (annotationsByType.length != 0) {
                    classScannerService.addSwaggerTags(c, securityContext);
                }

            }
            logger.debug("registering " + startedPlugin.getPluginId() + " for basic services took " + (System.currentTimeMillis() - start));
        }

    }


    private void registerFilterClasses() {
        BaseclassService.registerFilterClass(TenantFilter.class, Tenant.class);
        BaseclassService.registerFilterClass(ClazzFilter.class, Clazz.class);


    }
}
