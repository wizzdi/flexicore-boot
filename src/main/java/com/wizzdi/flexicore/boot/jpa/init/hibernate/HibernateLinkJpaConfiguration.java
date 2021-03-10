package com.wizzdi.flexicore.boot.jpa.init.hibernate;

import com.wizzdi.flexicore.boot.jpa.init.hibernate.cn.xdean.jex.AnnotationUtil;
import com.wizzdi.flexicore.boot.jpa.service.EntitiesHolder;
import org.hibernate.annotations.ColumnTransformer;
import org.hibernate.boot.model.naming.ImplicitNamingStrategy;
import org.hibernate.boot.model.naming.PhysicalNamingStrategy;
import org.hibernate.cfg.AvailableSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.autoconfigure.orm.jpa.*;
import org.springframework.boot.autoconfigure.transaction.TransactionManagerCustomizers;
import org.springframework.boot.jdbc.SchemaManagementProvider;
import org.springframework.boot.jdbc.metadata.CompositeDataSourcePoolMetadataProvider;
import org.springframework.boot.jdbc.metadata.DataSourcePoolMetadata;
import org.springframework.boot.jdbc.metadata.DataSourcePoolMetadataProvider;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.boot.orm.jpa.hibernate.SpringJtaPlatform;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.jndi.JndiLocatorDelegate;
import org.springframework.orm.hibernate5.SpringBeanContainer;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.AbstractJpaVendorAdapter;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.jta.JtaTransactionManager;
import org.springframework.util.ClassUtils;

import javax.sql.DataSource;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Configuration
@EnableTransactionManagement(proxyTargetClass = true)
public class HibernateLinkJpaConfiguration extends JpaBaseConfiguration {


    private static final Logger logger = LoggerFactory.getLogger(HibernateLinkJpaConfiguration.class);



    private static final String JTA_PLATFORM = "hibernate.transaction.jta.platform";

    private static final String PROVIDER_DISABLES_AUTOCOMMIT = "hibernate.connection.provider_disables_autocommit";

    /**
     * {@code NoJtaPlatform} implementations for various Hibernate versions.
     */
    private static final String[] NO_JTA_PLATFORM_CLASSES = {
            "org.hibernate.engine.transaction.jta.platform.internal.NoJtaPlatform",
            "org.hibernate.service.jta.platform.internal.NoJtaPlatform" };
    @Autowired
    Environment env;


    private final HibernateProperties hibernateProperties;

    private final HibernateDefaultDdlAutoProvider defaultDdlAutoProvider;

    private DataSourcePoolMetadataProvider poolMetadataProvider;

    private final List<HibernatePropertiesCustomizer> hibernatePropertiesCustomizers;

    HibernateLinkJpaConfiguration(DataSource dataSource, JpaProperties jpaProperties,
                              ConfigurableListableBeanFactory beanFactory, ObjectProvider<JtaTransactionManager> jtaTransactionManager,
                              HibernateProperties hibernateProperties,
                              ObjectProvider<Collection<DataSourcePoolMetadataProvider>> metadataProviders,
                              ObjectProvider<SchemaManagementProvider> providers,
                              ObjectProvider<PhysicalNamingStrategy> physicalNamingStrategy,
                              ObjectProvider<ImplicitNamingStrategy> implicitNamingStrategy,
                              ObjectProvider<HibernatePropertiesCustomizer> hibernatePropertiesCustomizers) {
        super(dataSource, jpaProperties, jtaTransactionManager);
        this.hibernateProperties = hibernateProperties;
        this.defaultDdlAutoProvider = new HibernateDefaultDdlAutoProvider(providers);
        this.poolMetadataProvider = new CompositeDataSourcePoolMetadataProvider(metadataProviders.getIfAvailable());
        this.hibernatePropertiesCustomizers = determineHibernatePropertiesCustomizers(
                physicalNamingStrategy.getIfAvailable(), implicitNamingStrategy.getIfAvailable(), beanFactory,
                hibernatePropertiesCustomizers.orderedStream().collect(Collectors.toList()));
    }

    @Bean
    @Primary
    public PlatformTransactionManager jpaTransactionManager(ObjectProvider<TransactionManagerCustomizers> transactionManagerCustomizers) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        TransactionManagerCustomizers managerCustomizersIfAvailable = transactionManagerCustomizers.getIfAvailable();
        if (managerCustomizersIfAvailable != null) {
            managerCustomizersIfAvailable.customize(transactionManager);
        }

        return transactionManager;
    }


    @Bean
    @Primary
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(final EntityManagerFactoryBuilder builder, @Autowired DataSource dataSource, @Autowired List<EntitiesHolder> entitiesHolder,@Autowired ObjectProvider<EncryptionConfigurations> encryptionConfigurations) {
        encryptionConfigurations.stream().map(f->f.getEncryptionConfigurations()).flatMap(List::stream).forEach(this::applyEncryption);
        Map<String, Object> vendorProperties = getVendorProperties();
        customizeVendorProperties(vendorProperties);
        Set<Class<?>> entities = entitiesHolder.stream().map(f->f.getEntities()).flatMap(Set::stream).collect(Collectors.toSet());
        logger.debug("Discovered Entities: " + entities.stream().map(f -> f.getCanonicalName()).collect(Collectors.joining(System.lineSeparator())));
        Class<?>[] entitiesArr = new Class<?>[entities.size()];
        entities.toArray(entitiesArr);
        EntityManagerFactoryBuilder.Builder primary = builder.dataSource(dataSource)
                .packages(entitiesArr)
                .persistenceUnit("primary")
                .properties(vendorProperties)
                .jta(isJta());
        final LocalContainerEntityManagerFactoryBean ret = primary.build();

        return ret;
    }

    private Class<?> encryptFields(Class<?> clazz, Map<String, List<EncryptionConfiguration>> encryptionConfig) {
        List<EncryptionConfiguration> encryptionConfigurations = encryptionConfig.get(clazz.getCanonicalName());
        if(encryptionConfigurations!=null){
            for (EncryptionConfiguration encryptionConfiguration : encryptionConfigurations) {
                applyEncryption(encryptionConfiguration);
            }
        }
        return clazz;
    }

    private void applyEncryption(EncryptionConfiguration encryptionConfiguration) {
        Method getter = encryptionConfiguration.getGetter();
        ColumnTransformer columnTransformer=getColumnTransformer(encryptionConfiguration);
        AnnotationUtil.addAnnotation(getter, columnTransformer);
        logger.info("changed method "+getter.getName()+"("+System.identityHashCode(getter) +")"+" on class "+encryptionConfiguration.getClazz().getName() +"("+System.identityHashCode(encryptionConfiguration.getClazz())+")");

    }

    private ColumnTransformer getColumnTransformer(EncryptionConfiguration encryptionConfiguration) {
        return new ColumnTransformer(){
            @Override
            public String forColumn() {
                return encryptionConfiguration.getForColumn();
            }

            @Override
            public String read() {
                return encryptionConfiguration.getRead();
            }

            @Override
            public String write() {
                return encryptionConfiguration.getWrite();
            }

            @Override
            public Class<? extends Annotation> annotationType() {
                return ColumnTransformer.class;
            }
        };
    }


    @Override
    protected Map<String, Object> getVendorProperties() {
        Supplier<String> defaultDdlMode = () -> this.defaultDdlAutoProvider.getDefaultDdlAuto(getDataSource());
        return new LinkedHashMap<>(this.hibernateProperties
                .determineHibernateProperties(getProperties().getProperties(), new HibernateSettings()
                        .ddlAuto(defaultDdlMode).hibernatePropertiesCustomizers(this.hibernatePropertiesCustomizers)));
    }


    private List<HibernatePropertiesCustomizer> determineHibernatePropertiesCustomizers(
            PhysicalNamingStrategy physicalNamingStrategy, ImplicitNamingStrategy implicitNamingStrategy,
            ConfigurableListableBeanFactory beanFactory,
            List<HibernatePropertiesCustomizer> hibernatePropertiesCustomizers) {
        List<HibernatePropertiesCustomizer> customizers = new ArrayList<>();
        if (ClassUtils.isPresent("org.hibernate.resource.beans.container.spi.BeanContainer",
                getClass().getClassLoader())) {
            customizers.add((properties) -> properties.put(AvailableSettings.BEAN_CONTAINER,
                    new SpringBeanContainer(beanFactory)));
        }
        if (physicalNamingStrategy != null || implicitNamingStrategy != null) {
            customizers.add(
                    new NamingStrategiesHibernatePropertiesCustomizer(physicalNamingStrategy, implicitNamingStrategy));
        }
        customizers.addAll(hibernatePropertiesCustomizers);
        return customizers;
    }

    @Override
    protected AbstractJpaVendorAdapter createJpaVendorAdapter() {
        return new HibernateJpaVendorAdapter();
    }

    @Override
    protected void customizeVendorProperties(Map<String, Object> vendorProperties) {
        super.customizeVendorProperties(vendorProperties);
        if (!vendorProperties.containsKey(JTA_PLATFORM)) {
            configureJtaPlatform(vendorProperties);
        }
        if (!vendorProperties.containsKey(PROVIDER_DISABLES_AUTOCOMMIT)) {
            configureProviderDisablesAutocommit(vendorProperties);
        }
    }

    private void configureJtaPlatform(Map<String, Object> vendorProperties) throws LinkageError {
        JtaTransactionManager jtaTransactionManager = getJtaTransactionManager();
        // Make sure Hibernate doesn't attempt to auto-detect a JTA platform
        if (jtaTransactionManager == null) {
            vendorProperties.put(JTA_PLATFORM, getNoJtaPlatformManager());
        }
        // As of Hibernate 5.2, Hibernate can fully integrate with the WebSphere
        // transaction manager on its own.
        else if (!runningOnWebSphere()) {
            configureSpringJtaPlatform(vendorProperties, jtaTransactionManager);
        }
    }

    private void configureProviderDisablesAutocommit(Map<String, Object> vendorProperties) {
        if (isDataSourceAutoCommitDisabled() && !isJta()) {
            vendorProperties.put(PROVIDER_DISABLES_AUTOCOMMIT, "true");
        }
    }

    private boolean isDataSourceAutoCommitDisabled() {
        DataSourcePoolMetadata poolMetadata = this.poolMetadataProvider.getDataSourcePoolMetadata(getDataSource());
        return poolMetadata != null && Boolean.FALSE.equals(poolMetadata.getDefaultAutoCommit());
    }

    private boolean runningOnWebSphere() {
        return ClassUtils.isPresent("com.ibm.websphere.jtaextensions.ExtendedJTATransaction",
                getClass().getClassLoader());
    }

    private void configureSpringJtaPlatform(Map<String, Object> vendorProperties,
                                            JtaTransactionManager jtaTransactionManager) {
        try {
            vendorProperties.put(JTA_PLATFORM, new SpringJtaPlatform(jtaTransactionManager));
        }
        catch (LinkageError ex) {
            // NoClassDefFoundError can happen if Hibernate 4.2 is used and some
            // containers (e.g. JBoss EAP 6) wrap it in the superclass LinkageError
            if (!isUsingJndi()) {
                throw new IllegalStateException(
                        "Unable to set Hibernate JTA platform, are you using the correct version of Hibernate?", ex);
            }
            // Assume that Hibernate will use JNDI
            if (logger.isDebugEnabled()) {
                logger.debug("Unable to set Hibernate JTA platform : " + ex.getMessage());
            }
        }
    }

    private boolean isUsingJndi() {
        try {
            return JndiLocatorDelegate.isDefaultJndiEnvironmentAvailable();
        }
        catch (Error ex) {
            return false;
        }
    }

    private Object getNoJtaPlatformManager() {
        for (String candidate : NO_JTA_PLATFORM_CLASSES) {
            try {
                return Class.forName(candidate).newInstance();
            }
            catch (Exception ex) {
                // Continue searching
            }
        }
        throw new IllegalStateException(
                "No available JtaPlatform candidates amongst " + Arrays.toString(NO_JTA_PLATFORM_CLASSES));
    }

    private static class NamingStrategiesHibernatePropertiesCustomizer implements HibernatePropertiesCustomizer {

        private final PhysicalNamingStrategy physicalNamingStrategy;

        private final ImplicitNamingStrategy implicitNamingStrategy;

        NamingStrategiesHibernatePropertiesCustomizer(PhysicalNamingStrategy physicalNamingStrategy,
                                                      ImplicitNamingStrategy implicitNamingStrategy) {
            this.physicalNamingStrategy = physicalNamingStrategy;
            this.implicitNamingStrategy = implicitNamingStrategy;
        }

        @Override
        public void customize(Map<String, Object> hibernateProperties) {
            if (this.physicalNamingStrategy != null) {
                hibernateProperties.put("hibernate.physical_naming_strategy", this.physicalNamingStrategy);
            }
            if (this.implicitNamingStrategy != null) {
                hibernateProperties.put("hibernate.implicit_naming_strategy", this.implicitNamingStrategy);
            }
        }

    }






}
