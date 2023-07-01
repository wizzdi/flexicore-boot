package com.wizzdi.flexicore.boot.jpa.init.eclipselink;

import com.wizzdi.dynamic.annotations.service.AnnotationTransformer;
import com.wizzdi.dynamic.annotations.service.TransformAnnotations;
import com.wizzdi.flexicore.boot.jpa.service.EntitiesHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.orm.jpa.JpaBaseConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.autoconfigure.transaction.TransactionManagerCustomizers;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.AbstractJpaVendorAdapter;
import org.springframework.orm.jpa.vendor.EclipseLinkJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.jta.JtaTransactionManager;

import javax.sql.DataSource;
import java.net.MalformedURLException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Configuration
@EnableTransactionManagement(proxyTargetClass = true)
public class EclipseLinkJpaConfiguration extends JpaBaseConfiguration {


    private static final Logger logger = LoggerFactory.getLogger(EclipseLinkJpaConfiguration.class);


    @Autowired
    Environment env;


    protected EclipseLinkJpaConfiguration(DataSource dataSource, JpaProperties properties, ObjectProvider<JtaTransactionManager> jtaTransactionManager) {
        super(dataSource, properties, jtaTransactionManager);


    }

    @Bean
    @Primary
    public PlatformTransactionManager transactionManager(ObjectProvider<TransactionManagerCustomizers> transactionManagerCustomizers) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        TransactionManagerCustomizers managerCustomizersIfAvailable = transactionManagerCustomizers.getIfAvailable();
        if (managerCustomizersIfAvailable != null) {
            managerCustomizersIfAvailable.customize(transactionManager);
        }

        return transactionManager;
    }

  /*  @Bean
    @Primary
    public PlatformTransactionManager transactionManager(PlatformTransactionManager platformTransactionManager){
        return platformTransactionManager;
    }*/

    @Bean
    @Primary
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(final EntityManagerFactoryBuilder builder, @Autowired DataSource dataSource, @Autowired List<EntitiesHolder> entitiesHolder,@Autowired ObjectProvider<AnnotationTransformer<?>> annotationTransformers) throws ClassNotFoundException, MalformedURLException {

        Set<Class<?>> entities = entitiesHolder.stream().map(f->f.getEntities()).flatMap(Set::stream).collect(Collectors.toSet());
        logger.debug("Discovered Entities: " + entities.stream().map(f -> f.getCanonicalName()).collect(Collectors.joining(System.lineSeparator())));
        for (Class<?> entity : entities) {
            if(entity.isAnnotationPresent(TransformAnnotations.class)){
                annotationTransformers.forEach(f->f.applyTransformation(entity));
            }
        }
        Class<?>[] entitiesArr = new Class<?>[entities.size()];
        entities.toArray(entitiesArr);
        EntityManagerFactoryBuilder.Builder primary = builder.dataSource(dataSource)
                .packages(entitiesArr)
                .persistenceUnit("primary")
                .properties(getVendorProperties())
                .jta(true);
        final LocalContainerEntityManagerFactoryBean ret = primary.build();

        return ret;
    }




    @Override
    protected AbstractJpaVendorAdapter createJpaVendorAdapter() {
        return new EclipseLinkJpaVendorAdapter();
    }

    @Override
    protected Map<String, Object> getVendorProperties() {
        Map<String, Object> props = new HashMap<>();
        props.put("javax.persistence.schema-generation.create-database-schemas", "true");
        //add all properties starting with eclipselink to vendor properties
        MutablePropertySources propSrcs = ((AbstractEnvironment) env).getPropertySources();
        StreamSupport.stream(propSrcs.spliterator(), false)
                .filter(ps -> ps instanceof EnumerablePropertySource)
                .map(ps -> ((EnumerablePropertySource) ps).getPropertyNames())
                .flatMap(Arrays::stream)
                .filter(f -> f.startsWith("eclipselink"))
                .forEach(propName -> props.put(propName, env.getProperty(propName)));
        return props;
    }





}
