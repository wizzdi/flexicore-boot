package com.wizzdi.flexicore.security.service;

import com.wizzdi.flexicore.security.configuration.SecurityContext;
import com.flexicore.annotations.IOperation;
import com.flexicore.annotations.OperationsInside;
import com.flexicore.annotations.rest.*;
import com.flexicore.model.*;
import com.wizzdi.flexicore.boot.base.init.FlexiCorePluginManager;
import com.wizzdi.flexicore.boot.base.init.PluginInit;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.security.interfaces.*;
import com.wizzdi.flexicore.security.request.*;
import com.wizzdi.flexicore.security.response.*;
import org.pf4j.Extension;
import org.pf4j.PluginWrapper;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

@Primary
@Component
@Extension
public class ClassScannerService implements Plugin {

    private static final Logger logger = LoggerFactory.getLogger(ClassScannerService.class);


    @Autowired
    private ClazzService clazzService;


    @Autowired
    private OperationToClazzService operationToClazzService;


    @Autowired
    @Qualifier("pluginManagerUnInjected")
    private FlexiCorePluginManager pluginManagerUnInjected;


    /**
     * runs once per server start. synchronizes annotated methods with
     * (IOperation) in the database so roles can be built with proper access
     * rights
     */

    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    @ConditionalOnMissingBean
    public OperationsMethodScanner operationsMethodScanner(ObjectProvider<OperationAnnotationConverter> converters) {
        List<OperationAnnotationConverter> convertersList = converters.orderedStream().toList();
        return f->scanOperationOnMethod(f,convertersList);
    }

    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    @ConditionalOnMissingBean
    public OperationsClassScanner operationsClassScanner(OperationsMethodScanner operationsMethodScanner) {
        return c -> scanOperationsInClass(c, operationsMethodScanner);
    }

    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    @ConditionalOnMissingBean
    public OperationBuilder securityOperationBuilder() {
        return (securityOperationCreate, existing, relatedClazzes, toMerge, clazzes) -> createOperationNoMerge(securityOperationCreate, existing, relatedClazzes, toMerge, clazzes);
    }

    private SecurityOperation createOperationNoMerge(OperationScanContext operationScanContext, Map<String, SecurityOperation> existing, Map<String, Map<String, OperationToClazz>> relatedClazzes, List<Object> toMerge, Map<String, Clazz> clazzes ) {
        SecurityOperationCreate securityOperationCreate = operationScanContext.getSecurityOperationCreate();
        SecurityOperation securityOperation = existing.get(securityOperationCreate.getIdForCreate());
        Class<?>[] relatedClasses = operationScanContext.getRelatedClasses();
        if (securityOperation == null) {
            securityOperation = SecurityOperationService.getSecurityOperation(securityOperationCreate);
            existing.put(securityOperation.getId(), securityOperation);
        }
        if(relatedClasses!=null){
            for (Class<?> relatedClass : relatedClasses) {
                String clazzId =relatedClass.getSimpleName();
                Clazz clazz = clazzes.get(clazzId);
                if(clazz== null){
                    logger.warn("could not find clazz for class: {} required for operation {}({})", relatedClass.getCanonicalName(),operationScanContext.getSecurityOperationCreate().getName(),operationScanContext.getSecurityOperationCreate().getIdForCreate());
                    continue;
                }
                Map<String, OperationToClazz> operationClazzes = relatedClazzes.computeIfAbsent(securityOperation.getId(), f -> new HashMap<>());
                OperationToClazz existingOperationToClazz = operationClazzes.get(clazzId);
                if (existingOperationToClazz == null) {
                    OperationToClazzCreate operationToClazzCreate = new OperationToClazzCreate()
                            .setType(clazz)
                            .setSecurityOperation(securityOperation);
                    existingOperationToClazz = operationToClazzService.addOperationToClazz(operationToClazzCreate);
                    operationClazzes.put(clazzId, existingOperationToClazz);
                }
            }
        }
        return securityOperation;
    }

    @Bean
    @Qualifier("adminSecurityContext")
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    @ConditionalOnMissingBean
    public SecurityContext adminSecurityContext(DefaultSecurityEntities defaultSecurityEntities, SecurityContextProvider securityContextProvider) {
        return securityContextProvider.getSecurityContext(defaultSecurityEntities.getSecurityUser());

    }

    @Bean
    @Qualifier("allOps")
    @ConditionalOnMissingBean
    public SecurityOperation allOps(Operations operations){
        return operations.getOperations().stream().filter(f->f.getId().equals(ClazzService.getClazzId(All.class))).findFirst().orElseThrow(()->new RuntimeException("could not find all operation"));
    }

    @Bean
    @Qualifier("securityWildcard")
    @ConditionalOnMissingBean
    public Clazz securityWildcard(Clazzes clazzes){
        return clazzes.getClazzes().stream().filter(f->f.name().equals(SecurityWildcard.class.getSimpleName())).findFirst().orElseThrow(()->new RuntimeException("could not find SecurityWildcard"));
    }

    @Bean
    public OperationGroups operationGroups(Operations operations, ObjectProvider<OperationGroupProvider> operationGroupProviders, OperationGroupService operationGroupService, OperationToGroupService operationToGroupService, @Qualifier("adminSecurityContext") SecurityContext adminSecurityContext) {
        Map<String, OperationGroupContext> contexts = operationGroupProviders.stream().map(f -> f.getOperationGroupContext(operations)).filter(f -> f.operationGroupCreate().getExternalId() != null).collect(Collectors.toMap(f -> f.operationGroupCreate().getExternalId(), f -> f, (a, b) -> a));
        Map<String, OperationGroup> existing = contexts.isEmpty() ? new HashMap<>() : operationGroupService.listAllOperationGroups(new OperationGroupFilter().setExternalIds(contexts.keySet()), null).stream().collect(Collectors.toMap(f -> f.getExternalId(), f -> f, (a, b) -> a));
        List<OperationGroup> existingList = new ArrayList<>(existing.values());
        Map<String, Map<String, OperationToGroup>> existingLinks = existingList.isEmpty() ? new HashMap<>() : operationToGroupService.listAllOperationToGroups(new OperationToGroupFilter().setOperationGroups(existingList), null).stream().filter(f -> f.getOperation() != null).collect(Collectors.groupingBy(f -> f.getOperationGroup().getId(), Collectors.toMap(f -> f.getOperation().getId(), f -> f, (a, b) -> a)));
        List<OperationGroup> operationGroups = new ArrayList<>();
        for (OperationGroupContext value : contexts.values()) {
            OperationGroupCreate operationGroupCreate = new OperationGroupCreate()
                    .setExternalId(value.operationGroupCreate().getExternalId())
                    .setName(value.operationGroupCreate().getName())
                    .setDescription(value.operationGroupCreate().getDescription());
            OperationGroup operationGroup = Optional.ofNullable(existing.get(value.operationGroupCreate().getExternalId()))
                    .map(f -> operationGroupService.updateOperationGroup(operationGroupCreate, f))
                    .orElseGet(() -> operationGroupService.createOperationGroup(operationGroupCreate, adminSecurityContext));
            operationGroups.add(operationGroup);
            Map<String, OperationToGroup> linkMap = existingLinks.computeIfAbsent(operationGroup.getId(), f -> new HashMap<>());
            for (SecurityOperation operation : value.operations()) {
                OperationToGroupCreate operationToGroupCreate = new OperationToGroupCreate()
                        .setOperationGroup(operationGroup)
                        .setOperation(operation);
                OperationToGroup operationToGroup = Optional.ofNullable(linkMap.get(operation.getId()))
                        .map(f -> operationToGroupService.updateOperationToGroup(operationToGroupCreate, f))
                        .orElseGet(() -> operationToGroupService.createOperationToGroup(operationToGroupCreate, adminSecurityContext));
                linkMap.put(operation.getId(), operationToGroup);
            }

        }
        return new OperationGroups(operationGroups);

    }

    @Bean
    public OperationGroupProvider viewOperations() {
        return operations -> {
            List<SecurityOperation> operationList = operations.getOperations().stream().filter(f -> f.category()!=null&&f.category().equals(StandardSecurityOperationCategories.READ.name())).collect(Collectors.toList());
            return new OperationGroupContext(new OperationGroupCreate().setExternalId("ViewOperations").setName("View Operations").setDescription("Operations that are required for viewers: read."), operationList);
        };
    }

    @Bean
    public OperationGroupProvider managingOperations() {
        Set<String> categories=Set.of(StandardSecurityOperationCategories.READ,StandardSecurityOperationCategories.UPDATE,StandardSecurityOperationCategories.WRITE).stream().map(f->f.name()).collect(Collectors.toSet());
       return operations -> {
            List<SecurityOperation> operationList = operations.getOperations().stream().filter(f -> f.category()!=null&&categories.contains(f.category())).collect(Collectors.toList());
            return new OperationGroupContext(new OperationGroupCreate().setExternalId("ManagingOperations").setName("Managing Operations").setDescription("Operations that are required for managers: read , write and update."), operationList);
        };
       }

       @Bean
       public OperationGroupProvider administrativeOperations() {
           Set<String> categories=Set.of(StandardSecurityOperationCategories.READ,StandardSecurityOperationCategories.UPDATE,StandardSecurityOperationCategories.WRITE,StandardSecurityOperationCategories.DELETE).stream().map(f->f.name()).collect(Collectors.toSet());

           return operations -> {
               List<SecurityOperation> operationList = operations.getOperations().stream().filter(f -> f.category()!=null&&categories.contains(f.category())).collect(Collectors.toList());
               return new OperationGroupContext(new OperationGroupCreate().setExternalId("AdministrativeOperations").setName("Administrative Operations").setDescription("Operations that are required for administrators: read,write,update and delete."), operationList);
           };
       }




    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    @ConditionalOnMissingBean
    public Operations initializeOperations(Clazzes clazzes, OperationsClassScanner operationsClassScanner, OperationBuilder operationBuilder, StandardOperationScanner standardOperationScanner, Reflections reflections) {

        Map<String, Clazz> clazzMap = clazzes.getClazzes().stream().collect(Collectors.toMap(f -> f.name(), f -> f));

        List<PluginWrapper> startedPlugins = pluginManagerUnInjected.getStartedPlugins().stream().sorted(PluginInit.PLUGIN_COMPARATOR).collect(Collectors.toList());
        Set<Class<?>> operationClasses = new HashSet<>(reflections.getTypesAnnotatedWith(OperationsInside.class).stream().map(f -> ClassUtils.getUserClass(f)).collect(Collectors.toSet()));
        for (PluginWrapper startedPlugin : startedPlugins) {
            List<? extends Class<?>> pluginOperations = pluginManagerUnInjected.getExtensionClasses(startedPlugin.getPluginId()).stream().map(f->ClassUtils.getUserClass(f)).filter(f->f.isAnnotationPresent(OperationsInside.class)).toList();
            operationClasses.addAll(pluginOperations);

        }
        List<OperationScanContext> scannedOperations = new ArrayList<>();
        for (Class<?> annotated : operationClasses) {
            List<? extends OperationScanContext> scan = operationsClassScanner.scan(annotated);
            scannedOperations.addAll(scan);
        }
        scannedOperations.addAll(standardOperationScanner.getStandardOperations());
        Map<String, OperationScanContext> operationCreateMap = scannedOperations.stream().collect(Collectors.toMap(f -> f.getSecurityOperationCreate().getIdForCreate(), f -> f, (a, b) -> a));
        Map<String, SecurityOperation> existing = new HashMap<>();
        Map<String, Map<String, OperationToClazz>> relatedClazzes = new HashMap<>();
        List<Object> toMerge = new ArrayList<>();

        for (OperationScanContext securityOperationCreate : operationCreateMap.values()) {
            SecurityOperation securityOperation = operationBuilder.upsertOperationNoMerge(securityOperationCreate, existing, relatedClazzes, toMerge, clazzMap);
        }

        return new Operations(new ArrayList<>(existing.values()));
    }

    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    @ConditionalOnMissingBean
    public StandardOperationScanner standardOperationScanner() {
        return () -> Arrays.asList(Delete.class, Read.class, Update.class, Write.class, All.class).stream().map(this::standardAccess).collect(Collectors.toList());
    }

    private OperationScanContext standardAccess(Class<?> standardAccess) {
        IOperation ioperation = standardAccess.getDeclaredAnnotation(IOperation.class);
        return new OperationScanContext(new SecurityOperationCreate()
                .setCategory(ioperation.Category().isEmpty() ? detectCategory(ioperation) : ioperation.Category())
                .setDefaultAccess(ioperation.access())
                .setDescription(ioperation.Description())
                .setName(ioperation.Name())
                .setIdForCreate(ClazzService.getClazzId(standardAccess))
                , null);
    }

    private String detectCategory(IOperation ioperation) {
        String name = ioperation.Name();
        if (name.contains("create")) {
            return StandardSecurityOperationCategories.WRITE.name();
        }
        if (name.contains("update")) {
            return StandardSecurityOperationCategories.UPDATE.name();
        }
        if (name.contains("delete")) {
            return StandardSecurityOperationCategories.DELETE.name();
        }
        if (name.contains("get")) {
            return StandardSecurityOperationCategories.READ.name();
        }
        return StandardSecurityOperationCategories.WRITE.name();
    }


    public List<OperationScanContext> scanOperationsInClass(Class<?> clazz, OperationsMethodScanner operationsMethodScanner) {
        List<OperationScanContext> ops = new ArrayList<>();
        for (Method method : clazz.getMethods()) {
            if (method.isBridge()) {
                continue;
            }
            OperationScanContext securityScanContext = operationsMethodScanner.scanOperationOnMethod(method);
            if (securityScanContext != null) {
                ops.add(securityScanContext);
            }

        }
        return ops;


    }

    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    private OperationAnnotationConverter ioperationConverter() {
        return method -> AnnotatedElementUtils.findMergedAnnotation(method, IOperation.class);
    }

    private OperationScanContext scanOperationOnMethod(Method method, List<OperationAnnotationConverter> converters) {
        IOperation ioperation = converters.stream().map(f -> f.getIOperation(method)).filter(Objects::nonNull).findFirst().orElse(null);
        if (ioperation != null) {
            Class<?>[] relatedClasses = ioperation.relatedClazzes();
            if (relatedClasses.length == 0 && method.getReturnType() != null && Basic.class.isAssignableFrom(method.getReturnType())) {
                relatedClasses =new Class<?>[]{method.getReturnType()};
            }
            String id = ClazzService.getIdFromString(method.toString());
            return new OperationScanContext(new SecurityOperationCreate()
                    .setCategory(ioperation.Category().isEmpty() ? detectCategory(ioperation) : ioperation.Category())
                    .setDefaultAccess(ioperation.access())
                    .setDescription(ioperation.Description())
                    .setName(ioperation.Name())
                    .setIdForCreate(id)
                    , relatedClasses);
        }
        return null;
    }







    /**
     * Make sure that all classes annotated with {@code AnnotatedClazz} are registered in the database
     *
     * @return list of initialized classes
     */
    @Bean
    @ConditionalOnMissingBean
    public Clazzes initializeClazzes() {
        return new Clazzes(clazzService.listAllClazzs(new ClazzFilter()));
    }
}
