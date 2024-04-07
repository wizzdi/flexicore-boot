package com.wizzdi.flexicore.security.service;

import com.flexicore.annotations.AnnotatedClazz;
import com.flexicore.annotations.IOperation;
import com.flexicore.annotations.OperationsInside;
import com.flexicore.annotations.rest.*;
import com.flexicore.model.*;
import com.flexicore.security.SecurityContextBase;
import com.wizzdi.flexicore.boot.base.init.FlexiCorePluginManager;
import com.wizzdi.flexicore.boot.base.init.PluginInit;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.security.interfaces.*;
import com.wizzdi.flexicore.security.request.*;
import com.wizzdi.flexicore.security.response.*;
import org.pf4j.Extension;
import org.pf4j.PluginWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ClassUtils;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Primary
@Component
@Extension
public class ClassScannerService implements Plugin {

    private static final Logger logger = LoggerFactory.getLogger(ClassScannerService.class);

    @Autowired
    private SecurityOperationService operationService;

    @Autowired
    private ClazzService clazzService;


    @Autowired
    private OperationToClazzService operationToClazzService;


    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    @Lazy
    private FlexiCorePluginManager pluginManager;


    /**
     * runs once per server start. synchronizes annotated methods with
     * (IOperation) in the database so roles can be built with proper access
     * rights
     */

    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    @ConditionalOnMissingBean
    public OperationsMethodScanner operationsMethodScanner(ObjectProvider<OperationAnnotationConverter> converters) {
        return f->scanOperationOnMethod(f,converters);
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
        return (securityOperationCreate, existing, relatedClazzes, toMerge, clazzes, securityContextBase) -> createOperation(securityOperationCreate, existing, relatedClazzes, toMerge, clazzes, securityContextBase);
    }

    private SecurityOperation createOperation(OperationScanContext operationScanContext, Map<String, SecurityOperation> existing, Map<String, Map<String, OperationToClazz>> relatedClazzes, List<Object> toMerge, Map<String, Clazz> clazzes, SecurityContextBase securityContextBase) {
        SecurityOperationCreate securityOperationCreate = operationScanContext.getSecurityOperationCreate();
        SecurityOperation securityOperation = existing.get(securityOperationCreate.getIdForCreate());
        if (securityOperation == null) {
            securityOperation = operationService.createOperationNoMerge(securityOperationCreate, securityContextBase);
            securityOperation.setId(securityOperationCreate.getIdForCreate());
            securityOperation = operationService.merge(securityOperation);
            existing.put(securityOperation.getId(), securityOperation);
            //toMerge.add(securityOperation);
        } else {
            if (operationService.updateOperationNoMerge(securityOperationCreate, securityOperation)) {
                securityOperation = operationService.merge(securityOperation);

                //toMerge.add(securityOperation);
            }
        }
        Class<?>[] relatedClasses = operationScanContext.getRelatedClasses();
        if (relatedClasses != null) {
            for (Class<?> relatedClass : relatedClasses) {
                String clazzId = Baseclass.generateUUIDFromStringCompt(relatedClass.getCanonicalName());
                Clazz clazz = clazzes.get(clazzId);
                Map<String, OperationToClazz> operationClazzes = relatedClazzes.computeIfAbsent(securityOperation.getId(), f -> new HashMap<>());
                OperationToClazz existingOperationToClazz = operationClazzes.get(clazzId);
                if (existingOperationToClazz == null) {
                    OperationToClazzCreate operationToClazzCreate = new OperationToClazzCreate()
                            .setClazz(clazz)
                            .setSecurityOperation(securityOperation);
                    existingOperationToClazz = operationToClazzService.createOperationToClazz(operationToClazzCreate, securityContextBase);
                    //toMerge.add(existingOperationToClazz);
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
    public SecurityContextBase adminSecurityContext(DefaultSecurityEntities defaultSecurityEntities, SecurityContextProvider securityContextProvider) {
        return securityContextProvider.getSecurityContext(defaultSecurityEntities.getSecurityUser());

    }

    @Bean
    @Qualifier("allOps")
    @ConditionalOnMissingBean
    public SecurityOperation allOps(Operations operations){
        return operations.getOperations().stream().filter(f->f.getId().equals(Baseclass.generateUUIDFromStringCompt(All.class.getCanonicalName()))).findFirst().orElseThrow(()->new RuntimeException("could not find all operation"));
    }

    @Bean
    @Qualifier("securityWildcard")
    @ConditionalOnMissingBean
    public Clazz securityWildcard(Clazzes clazzes){
        return clazzes.getClazzes().stream().filter(f->f.getId().equals(Baseclass.generateUUIDFromStringCompt(SecurityWildcard.class.getCanonicalName()))).findFirst().orElseThrow(()->new RuntimeException("could not find SecurityWildcard"));
    }

    @Bean
    public OperationGroups operationGroups(Operations operations, ObjectProvider<OperationGroupProvider> operationGroupProviders, OperationGroupService operationGroupService, OperationToGroupService operationToGroupService, @Qualifier("adminSecurityContext") SecurityContextBase adminSecurityContext) {
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
            List<SecurityOperation> operationList = operations.getOperations().stream().filter(f -> f.getCategory()!=null&&f.getCategory().equals(StandardSecurityOperationCategories.READ.name())).collect(Collectors.toList());
            return new OperationGroupContext(new OperationGroupCreate().setExternalId("ViewOperations").setName("View Operations").setDescription("Operations that are required for viewers: read."), operationList);
        };
    }

    @Bean
    public OperationGroupProvider managingOperations() {
        Set<String> categories=Set.of(StandardSecurityOperationCategories.READ,StandardSecurityOperationCategories.UPDATE,StandardSecurityOperationCategories.WRITE).stream().map(f->f.name()).collect(Collectors.toSet());
       return operations -> {
            List<SecurityOperation> operationList = operations.getOperations().stream().filter(f -> f.getCategory()!=null&&categories.contains(f.getCategory())).collect(Collectors.toList());
            return new OperationGroupContext(new OperationGroupCreate().setExternalId("ManagingOperations").setName("Managing Operations").setDescription("Operations that are required for managers: read , write and update."), operationList);
        };
       }

       @Bean
       public OperationGroupProvider administrativeOperations() {
           Set<String> categories=Set.of(StandardSecurityOperationCategories.READ,StandardSecurityOperationCategories.UPDATE,StandardSecurityOperationCategories.WRITE,StandardSecurityOperationCategories.DELETE).stream().map(f->f.name()).collect(Collectors.toSet());

           return operations -> {
               List<SecurityOperation> operationList = operations.getOperations().stream().filter(f -> f.getCategory()!=null&&categories.contains(f.getCategory())).collect(Collectors.toList());
               return new OperationGroupContext(new OperationGroupCreate().setExternalId("AdministrativeOperations").setName("Administrative Operations").setDescription("Operations that are required for administrators: read,write,update and delete."), operationList);
           };
       }




    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    @ConditionalOnMissingBean
    public Operations initializeOperations(@Qualifier("adminSecurityContext") SecurityContextBase securityContextBase, Clazzes clazzes, OperationsClassScanner operationsClassScanner, OperationBuilder operationBuilder, StandardOperationScanner standardOperationScanner) {

        Map<String, Clazz> clazzMap = clazzes.getClazzes().stream().collect(Collectors.toMap(f -> f.getId(), f -> f));

        List<PluginWrapper> startedPlugins = pluginManager.getStartedPlugins().stream().sorted(PluginInit.PLUGIN_COMPARATOR).collect(Collectors.toList());
        Set<Class<?>> operationClasses = new HashSet<>();
        operationClasses.addAll(pluginManager.getApplicationContext().getBeansWithAnnotation(OperationsInside.class).values().stream().map(f -> ClassUtils.getUserClass(f.getClass())).collect(Collectors.toSet()));
        for (PluginWrapper startedPlugin : startedPlugins) {
            ApplicationContext applicationContext = pluginManager.getApplicationContext(startedPlugin);
            operationClasses.addAll(applicationContext.getBeansWithAnnotation(OperationsInside.class).values().stream().map(f -> ClassUtils.getUserClass(f.getClass())).collect(Collectors.toSet()));

        }
        List<OperationScanContext> scannedOperations = new ArrayList<>();
        for (Class<?> annotated : operationClasses) {
            List<? extends OperationScanContext> scan = operationsClassScanner.scan(annotated);
            scannedOperations.addAll(scan);
        }
        scannedOperations.addAll(standardOperationScanner.getStandardOperations());
        Map<String, OperationScanContext> operationCreateMap = scannedOperations.stream().collect(Collectors.toMap(f -> f.getSecurityOperationCreate().getIdForCreate(), f -> f, (a, b) -> a));
        Map<String, SecurityOperation> existing = operationCreateMap.isEmpty() ? new HashMap<>() : operationService.findByIds(SecurityOperation.class, operationCreateMap.keySet()).stream().collect(Collectors.toMap(f -> f.getId(), f -> f, (a, b) -> a));
        Map<String, Map<String, OperationToClazz>> relatedClazzes = existing.isEmpty() ? new HashMap<>() : operationToClazzService.listAllOperationToClazz(new OperationToClazzFilter().setSecurityOperations(new ArrayList<>(existing.values())), null).stream().filter(f -> f.getOperation() != null && f.getClazz() != null).collect(Collectors.groupingBy(f -> f.getOperation().getId(), Collectors.toMap(f -> f.getClazz().getId(), f -> f, (a, b) -> a)));
        List<Object> toMerge = new ArrayList<>();

        for (OperationScanContext securityOperationCreate : operationCreateMap.values()) {
            SecurityOperation securityOperation = operationBuilder.upsertOperationNoMerge(securityOperationCreate, existing, relatedClazzes, toMerge, clazzMap, securityContextBase);
            securityOperation = operationService.merge(securityOperation);
        }

        //operationService.massMerge(toMerge);

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
                .setIdForCreate(Baseclass.generateUUIDFromStringCompt(standardAccess.getCanonicalName()))
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
        if (name.contains("read")) {
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

    private OperationScanContext scanOperationOnMethod(Method method, ObjectProvider<OperationAnnotationConverter> converters) {
        IOperation ioperation = converters.orderedStream().map(f -> f.getIOperation(method)).filter(Objects::nonNull).findFirst().orElse(null);
        if (ioperation != null) {
            Class<?>[] relatedClasses = ioperation.relatedClazzes();
            if (relatedClasses.length == 0 && method.getReturnType() != null && Basic.class.isAssignableFrom(method.getReturnType())) {
                relatedClasses =new Class<?>[]{method.getReturnType()};
            }
            String id = Baseclass.generateUUIDFromStringCompt(method.toString());
            return new OperationScanContext(new SecurityOperationCreate()
                    .setDefaultAccess(ioperation.access())
                    .setDescription(ioperation.Description())
                    .setName(ioperation.Name())
                    .setIdForCreate(id)
                    , relatedClasses);
        }
        return null;
    }

    private IOperation addRelatedClazz(IOperation ioperation, Class<? extends Baseclass>[] classes) {
        return new IOperation() {
            @Override
            public String Name() {
                return ioperation.Name();
            }

            @Override
            public String Description() {
                return ioperation.Description();
            }

            @Override
            public String Category() {
                return ioperation.Category();
            }

            @Override
            public boolean auditable() {
                return ioperation.auditable();
            }

            @Override
            public Class<? extends Baseclass>[] relatedClazzes() {
                return classes;
            }

            @Override
            public Access access() {
                return ioperation.access();
            }

            @Override
            public Class<? extends Annotation> annotationType() {
                return IOperation.class;
            }
        };
    }


    private SecurityOperation addOperation(IOperation ioperation, String id, List<Object> toMerge, Map<String, SecurityOperation> existing, SecurityContextBase securityContextBase) {
        SecurityOperation operation = existing.get(id);
        if (operation == null) {
            SecurityOperationCreate createOperationRequest = new SecurityOperationCreate()
                    .setDefaultAccess(ioperation.access())
                    .setDescription(ioperation.Description())
                    .setName(ioperation.Name());
            operation = operationService.createOperationNoMerge(createOperationRequest, securityContextBase);
            operation.setId(id);
            operation = operationService.merge(operation);

            logger.debug("Have created a new operation" + operation.toString());


        }


        return operation;
    }

    private void handleOperationRelatedClasses(SecurityOperation operation, Class<? extends Baseclass>[] related, List<Object> toMerge, Map<String, OperationToClazz> existing) {

        for (Class<? extends Baseclass> relatedClazz : related) {
            String linkId = Baseclass.generateUUIDFromStringCompt(operation.getId() + relatedClazz.getCanonicalName());
            OperationToClazz operationToClazz = existing.get(linkId);
            Clazz clazz = Baseclass.getClazzByName(relatedClazz.getCanonicalName());

            OperationToClazzCreate operationToClazzCreate=new OperationToClazzCreate()
                    .setClazz(clazz)
                    .setSecurityOperation(operation)
                    .setIdForCreate(linkId)
                    .setName("OperationToClazz");
            if (operationToClazz == null) {
                operationToClazzService.createOperationToClazz(operationToClazzCreate, null);



            }

        }
    }


    /**
     * Make sure that all classes annotated with {@code AnnotatedClazz} are registered in the database
     *
     * @return list of initialized classes
     */
    @Transactional
    @Bean
    @ConditionalOnMissingBean
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)

    public Clazzes initializeClazzes() {
        logger.info("Initializing classes");


        Set<Class<?>> entities = entityManager.getMetamodel().getEntities().stream().map(f -> f.getJavaType()).collect(Collectors.toSet());
        logger.debug("detected classes:  " + entities.parallelStream().map(e -> e.getCanonicalName()).collect(Collectors.joining(System.lineSeparator())));

        Set<String> ids = entities.parallelStream().map(f -> Baseclass.generateUUIDFromStringCompt(f.getCanonicalName())).collect(Collectors.toSet());
        ids.add(Baseclass.generateUUIDFromStringCompt(Clazz.class.getCanonicalName()));
        Map<String, Clazz> existing = new HashMap<>();
        for (List<String> part : partition(new ArrayList<>(ids), 50)) {
            if (!part.isEmpty()) {
                existing.putAll(clazzService.findByIds(Clazz.class, ids).parallelStream().collect(Collectors.toMap(f -> f.getId(), f -> f)));
            }
        }
        List<Object> toMerge = new ArrayList<>();
        // registering clazz before all
        handleEntityClass(Clazz.class, existing, toMerge);
        // registering the rest
        for (Class<?> annotated : entities) {
            if (!annotated.getCanonicalName().equalsIgnoreCase(Clazz.class.getCanonicalName())) {
                handleEntityClass(annotated, existing, toMerge);
            }
        }
        //clazzService.massMerge(toMerge);
        entities.add(Clazz.class);
        //createIndexes(entities);
        return new Clazzes(new ArrayList<>(existing.values()));


    }

    private void handleEntityClass(Class<?> claz, Map<String, Clazz> existing, List<Object> toMerge) {
        registerClazzes(claz, existing, toMerge);
    }


    private void registerClazzes(Class<?> claz, Map<String, Clazz> existing, List<Object> toMerge) {
        try {
            String classname = claz.getCanonicalName();
            AnnotatedClazz annotatedclazz = claz.getAnnotation(AnnotatedClazz.class);

            if (annotatedclazz == null) {
                annotatedclazz = generateAnnotatedClazz(claz);
            }
            String id = Baseclass.generateUUIDFromStringCompt(classname);


            Clazz clazz = existing.get(id);
            ClazzCreate clazzCreate=new ClazzCreate()
                    .setIdForCreate(id)
                    .setName(classname)
                    .setDescription(annotatedclazz.Description());
            if (clazz == null) {
               clazz=clazzService.createClazz(clazzCreate, null);
                existing.put(clazz.getId(), clazz);
                logger.debug("Have created a new class " + clazz.toString());


            } else {
                logger.debug("Clazz  allready exists: " + clazz);

            }
            Baseclass.addClazz(clazz);

        } catch (Exception e) {
            logger.error("failed registering clazz", e);
        }

    }




    private AnnotatedClazz generateAnnotatedClazz(Class<?> claz) {
        return new AnnotatedClazz() {

            @Override
            public String DisplayName() {
                return claz.getSimpleName();
            }

            @Override
            public Class<? extends Annotation> annotationType() {
                return AnnotatedClazz.class;
            }


            @Override
            public String Name() {
                return claz.getCanonicalName();
            }

            @Override
            public String Description() {
                return "Auto Generated Description";
            }

            @Override
            public String Category() {
                return "Auto Generated Category";
            }
        };
    }

    protected <T> T save(T Acd, boolean en) {

        return Acd;
    }

    public static <T> List<List<T>> partition(List<T> list, int size) {
        if (size <= 0) {
            throw new IllegalArgumentException("The size must be greater than 0");
        }
        return new ArrayList<>(IntStream.iterate(0, i -> i + size)
                .limit((long) Math.ceil((double) list.size() / size))
                .mapToObj(cur -> list.subList(cur, Math.min(cur + size, list.size())))
                .collect(Collectors.toList()));
    }

}
