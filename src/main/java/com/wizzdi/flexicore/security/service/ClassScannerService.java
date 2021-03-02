package com.wizzdi.flexicore.security.service;

import com.flexicore.annotations.AnnotatedClazz;
import com.flexicore.annotations.IOperation;
import com.flexicore.annotations.OperationsInside;
import com.flexicore.annotations.rest.*;
import com.flexicore.model.*;
import com.flexicore.security.SecurityContextBase;
import com.google.common.collect.Lists;
import com.wizzdi.flexicore.boot.base.init.FlexiCorePluginManager;
import com.wizzdi.flexicore.boot.base.init.PluginInit;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.security.interfaces.OperationBuilder;
import com.wizzdi.flexicore.security.interfaces.OperationsClassScanner;
import com.wizzdi.flexicore.security.interfaces.OperationsMethodScanner;
import com.wizzdi.flexicore.security.interfaces.StandardOperationScanner;
import com.wizzdi.flexicore.security.request.*;
import com.wizzdi.flexicore.security.response.*;
import org.pf4j.Extension;
import org.pf4j.PluginWrapper;
import org.reflections.Reflections;
import org.reflections.serializers.JsonSerializer;
import org.reflections.util.FilterBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
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

import javax.persistence.EntityManager;
import javax.persistence.ManyToOne;
import javax.persistence.PersistenceContext;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Primary
@Component
@Extension
public class ClassScannerService implements Plugin {

	private static final Logger logger = LoggerFactory.getLogger(ClassScannerService.class);
	private static final String DEFAULT_TENANT_ID = "jgV8M9d0Qd6owkPPFrbWIQ";
	private static final String TENANT_TO_USER_ID = "Xk5siBx+TyWv+G6V+XuSdw";
	private static final String SUPER_ADMIN_ROLE_ID = "HzFnw-nVR0Olq6WBvwKcQg";
	private static final String SUPER_ADMIN_TO_ADMIN_ID = "EbVFgr+YS3ezYUblzceVGA";
	@Autowired
	private SecurityOperationService operationService;

	@Autowired
	private ClazzService clazzService;

	@Autowired
	private SecurityTenantService tenantService;

	@Autowired
	private TenantToUserService tenantToUserService;
	@Autowired
	private RoleService roleService;

	@Autowired
	private RoleToUserService roleToUserService;

	@Autowired
	private OperationToClazzService operationToClazzService;

	@Autowired
	private SecurityUserService userService;

	@PersistenceContext
	private EntityManager entityManager;
	@Autowired
	@Lazy
	private FlexiCorePluginManager pluginManager;

	private Reflections reflections;
	@Autowired
	@Qualifier("systemAdminId")
	private String systemAdminId;




	/**
	 * runs once per server start. synchronizes annotated methods with
	 * (IOperation) in the database so roles can be built with proper access
	 * rights
	 */

	@Bean
	@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
	@ConditionalOnMissingBean
	public OperationsMethodScanner operationsMethodScanner() {
		return this::scanOperationOnMethod;
	}

	@Bean
	@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
	@ConditionalOnMissingBean
	public OperationsClassScanner operationsClassScanner(OperationsMethodScanner operationsMethodScanner) {
		return c -> scanOperationsInClass(c,operationsMethodScanner);
	}

	@Bean
	@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
	@ConditionalOnMissingBean
	public OperationBuilder securityOperationBuilder() {
		return (securityOperationCreate, existing, relatedClazzes, toMerge, clazzes, securityContextBase) -> createOperation(securityOperationCreate, existing,relatedClazzes,toMerge,clazzes, securityContextBase);
	}

	private SecurityOperation createOperation(OperationScanContext operationScanContext, Map<String, SecurityOperation> existing, Map<String, Map<String, OperationToClazz>> relatedClazzes, List<Object> toMerge, Map<String, Clazz> clazzes, SecurityContextBase<?, ?, ?, ?> securityContextBase) {
		SecurityOperationCreate securityOperationCreate = operationScanContext.getSecurityOperationCreate();
		SecurityOperation securityOperation=existing.get(securityOperationCreate.getIdForCreate());
		if(securityOperation==null){
			securityOperation=operationService.createOperationNoMerge(securityOperationCreate,securityContextBase);
			securityOperation.setId(securityOperationCreate.getIdForCreate());
			existing.put(securityOperation.getId(),securityOperation);
			toMerge.add(securityOperation);
		}
		else{
			if(operationService.updateOperationNoMerge(securityOperationCreate,securityOperation)){
				toMerge.add(securityOperation);
			}
		}
		Class<?>[] relatedClasses = operationScanContext.getRelatedClasses();
		if(relatedClasses !=null){
			for (Class<?> relatedClass : relatedClasses) {
				String clazzId = Baseclass.generateUUIDFromString(relatedClass.getCanonicalName());
				Clazz clazz=clazzes.get(clazzId);
				Map<String,OperationToClazz> operationClazzes=relatedClazzes.computeIfAbsent(securityOperation.getId(),f->new HashMap<>());
				OperationToClazz existingOperationToClazz=operationClazzes.get(clazzId);
				if(existingOperationToClazz==null){
					OperationToClazzCreate operationToClazzCreate=new OperationToClazzCreate()
							.setClazz(clazz)
							.setSecurityOperation(securityOperation);
					existingOperationToClazz = operationToClazzService.createOperationToClazzNoMerge(operationToClazzCreate, securityContextBase);
					toMerge.add(existingOperationToClazz);
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
	public SecurityContextBase<?,?,?,?> adminSecurityContext(DefaultSecurityEntities defaultSecurityEntities){
		return new SecurityContextBase<>()
				.setTenants(Collections.singletonList(defaultSecurityEntities.getSecurityTenant()))
				.setTenantToCreateIn(defaultSecurityEntities.getSecurityTenant())
				.setRoleMap(Stream.of(defaultSecurityEntities.getRole()).collect(Collectors.groupingBy(f -> f.getTenant().getId())))
				.setUser(defaultSecurityEntities.getSecurityUser());

	}

	@Bean
	@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
	@ConditionalOnMissingBean
	public Operations initializeOperations(@Qualifier("adminSecurityContext") SecurityContextBase<?,?,?,?> securityContextBase,Clazzes clazzes, OperationsClassScanner operationsClassScanner, OperationBuilder operationBuilder,StandardOperationScanner standardOperationScanner) {

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
		Map<String,OperationScanContext> operationCreateMap=scannedOperations.stream().collect(Collectors.toMap(f->f.getSecurityOperationCreate().getIdForCreate(),f->f,(a,b)->a));
		Map<String,SecurityOperation> existing=operationCreateMap.isEmpty()?new HashMap<>():operationService.findByIds(SecurityOperation.class,operationCreateMap.keySet()).stream().collect(Collectors.toMap(f->f.getId(),f->f,(a,b)->a));
		Map<String,Map<String,OperationToClazz>> relatedClazzes=existing.isEmpty()?new HashMap<>():operationToClazzService.listAllOperationToClazz(new OperationToClazzFilter().setSecurityOperations(new ArrayList<>(existing.values())),null).stream().filter(f->f.getLeftside()!=null&&f.getRightside()!=null).collect(Collectors.groupingBy(f->f.getLeftside().getId(),Collectors.toMap(f->f.getRightside().getId(),f->f,(a,b)->a)));
		List<Object> toMerge = new ArrayList<>();

		for (OperationScanContext securityOperationCreate : operationCreateMap.values()) {
			toMerge.add(operationBuilder.upsertOperationNoMerge(securityOperationCreate,existing,relatedClazzes,toMerge, clazzMap, securityContextBase));
		}

		operationService.massMerge(toMerge);

		return new Operations(new ArrayList<>(existing.values()));
	}

	@Bean
	@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
	@ConditionalOnMissingBean
	public StandardOperationScanner standardOperationScanner() {
		return ()->Arrays.asList(Delete.class, Read.class, Update.class, Write.class, All.class).stream().map(this::standardAccess).collect(Collectors.toList());
	}

	private OperationScanContext standardAccess(Class<?> standardAccess) {
		IOperation ioperation=standardAccess.getDeclaredAnnotation(IOperation.class);
		return new OperationScanContext(new SecurityOperationCreate()
				.setDefaultaccess(ioperation.access())
				.setSystemObject(true)
				.setDescription(ioperation.Description())
				.setName(ioperation.Name())
				.setIdForCreate(Baseclass.generateUUIDFromString(standardAccess.getCanonicalName()))
				,null);
	}


	public List<OperationScanContext> scanOperationsInClass(Class<?> clazz, OperationsMethodScanner operationsMethodScanner) {
		List<OperationScanContext> ops = new ArrayList<>();
		for (Method method : clazz.getMethods()) {
			if (method.isBridge()) {
				continue;
			}
			OperationScanContext securityScanContext = operationsMethodScanner.scanOperationOnMethod(method);
			if(securityScanContext!=null){
				ops.add(securityScanContext);
			}

		}
		return ops;


	}

	private OperationScanContext scanOperationOnMethod(Method method) {
		IOperation ioperation = AnnotatedElementUtils.findMergedAnnotation(method,IOperation.class);
		if (ioperation != null) {
			Class<? extends Baseclass>[] relatedClasses = ioperation.relatedClazzes();
			if (relatedClasses.length == 0 && method.getReturnType() != null && Baseclass.class.isAssignableFrom(method.getReturnType())) {
				relatedClasses =  (Class<? extends Baseclass>[]) new Class<?>[]{method.getReturnType()};
			}
			String id = Baseclass.generateUUIDFromString(method.toString());
			return new OperationScanContext(new SecurityOperationCreate()
					.setDefaultaccess(ioperation.access())
					.setSystemObject(true)
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
					.setDefaultaccess(ioperation.access())
					.setDescription(ioperation.Description())
					.setName(ioperation.Name());
			operation = operationService.createOperationNoMerge(createOperationRequest, securityContextBase);
			operation.setId(id);
			toMerge.add(operation);

			logger.debug("Have created a new operation" + operation.toString());


		} else {
			if (!operation.isSystemObject()) {
				operation.setSystemObject(true);
				toMerge.add(operation);
			}
			logger.debug("operation already exists: " + operation);

		}


		return operation;
	}

	private void handleOperationRelatedClasses(SecurityOperation operation, Class<? extends Baseclass>[] related, List<Object> toMerge, Map<String, OperationToClazz> existing) {

		for (Class<? extends Baseclass> relatedClazz : related) {
			String linkId = Baseclass.generateUUIDFromString(operation.getId() + relatedClazz.getCanonicalName());
			OperationToClazz operationToClazz = existing.get(linkId);
			if (operationToClazz == null) {
				try {
					operationToClazz = new OperationToClazz("OperationToClazz", null);
					operationToClazz.setOperation(operation);
					operationToClazz.setClazz(Baseclass.getClazzByName(relatedClazz.getCanonicalName()));
					operationToClazz.setId(linkId);
					operationToClazz.setSystemObject(true);
					toMerge.add(operationToClazz);
				} catch (Exception e) {
					logger.info("[registerClazzRelatedOperationsInclass] Error while creating operation: " + e.getMessage());

				}

			} else {
				if (!operationToClazz.isSystemObject()) {
					operationToClazz.setSystemObject(true);
					toMerge.add(operationToClazz);
				}
			}

		}
	}

	private void initReflections() {
		if (reflections != null) {
			return;
		}
		try {
          /*  reflections=new org.reflections.Reflections(new org.reflections.util.ConfigurationBuilder()
                    .filterInputsBy(new org.reflections.util.FilterBuilder().exclude("/test/java/*"))
                    .setUrls(org.reflections.util.ClasspathHelper.forPackage("com.flexicore"))
                    .setScanners(new org.reflections.scanners.TypeAnnotationsScanner()., new org.reflections.scanners.SubTypesScanner(false), new org.reflections.scanners.MethodAnnotationsScanner()));*/
			reflections = Reflections.collect("META-INF/reflections/", new FilterBuilder().include(".*-reflections.json"), new JsonSerializer());


		} catch (Exception e) {
			logger.error("failed initializing reflections", e);
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


		Set<Class<?>> entities =entityManager.getMetamodel().getEntities().stream().map(f->f.getJavaType()).collect(Collectors.toSet());
		logger.debug("detected classes:  " + entities.parallelStream().map(e -> e.getCanonicalName()).collect(Collectors.joining(System.lineSeparator())));

		Set<String> ids = entities.parallelStream().map(f -> Baseclass.generateUUIDFromString(f.getCanonicalName())).collect(Collectors.toSet());
		ids.add(Baseclass.generateUUIDFromString(Clazz.class.getCanonicalName()));
		ids.add(Baseclass.generateUUIDFromString(ClazzLink.class.getCanonicalName()));
		Map<String, Clazz> existing = new HashMap<>();
		for (List<String> part : Lists.partition(new ArrayList<>(ids), 50)) {
			if (!part.isEmpty()) {
				existing.putAll(clazzService.findByIds(Clazz.class, ids).parallelStream().collect(Collectors.toMap(f -> f.getId(), f -> f)));
			}
		}
		List<Object> toMerge = new ArrayList<>();
		// registering clazz before all
		handleEntityClass(Clazz.class, existing, toMerge);
		handleEntityClass(ClazzLink.class, existing, toMerge);
		// registering the rest
		for (Class<?> annotated : entities) {
			if (!annotated.getCanonicalName().equalsIgnoreCase(Clazz.class.getCanonicalName())) {
				handleEntityClass(annotated, existing, toMerge);
			}
		}
		clazzService.massMerge(toMerge);
		entities.add(Clazz.class);
		entities.add(ClazzLink.class);
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
			String ID = Baseclass.generateUUIDFromString(classname);


			Clazz clazz = existing.get(ID);
			if (clazz == null) {
				clazz = Baselink.class.isAssignableFrom(claz) ? createClazzLink(claz, existing, toMerge) : new Clazz(classname, null);
				clazz.setId(ID);
				clazz.setDescription(annotatedclazz.Description());
				clazz.setSystemObject(true);
				clazz.setClazz(null);
				toMerge.add(clazz);
				existing.put(clazz.getId(), clazz);
				logger.debug("Have created a new class " + clazz.toString());


			} else {
				logger.debug("Clazz  allready exists: " + clazz);

			}
			Baseclass.addClazz(clazz);

		}
		catch (Exception e){
			logger.error("failed registering clazz",e);
		}

	}

	private ClazzLink createClazzLink(Class<?> claz, Map<String, Clazz> existing, List<Object> toMerge) {
		//handle the case where a ClazzLink is needed
		String classname = claz.getCanonicalName();
		ClazzLink clazzLink = new ClazzLink(classname, null);
		Class<?>[] params = new Class[0];
		try {
			Method l = claz.getDeclaredMethod("getLeftside", params);
			Method r = claz.getDeclaredMethod("getRightside", params);
			Clazz valueClazz = Baseclass.getClazzByName(Baseclass.class.getCanonicalName());
			if (valueClazz == null) {
				handleEntityClass(Baseclass.class, existing, toMerge);
				valueClazz = Baseclass.getClazzByName(Baseclass.class.getCanonicalName());
			}
			try {
				Method v = claz.getDeclaredMethod("getValue", params);
				ManyToOne mtO = v.getAnnotation(ManyToOne.class);
				Class<?> cv = mtO.targetEntity();
				handleEntityClass(cv, existing, toMerge);
				valueClazz = Baseclass.getClazzByName(cv.getCanonicalName());
			} catch (NoSuchMethodException e) {
				logger.info("there is not spesific decleration for value for: " + claz.getCanonicalName());

			}
			clazzLink.setValue(valueClazz);
			if (l.isAnnotationPresent(ManyToOne.class)) {
				ManyToOne mtO = l.getAnnotation(ManyToOne.class);
				Class<?> cl = mtO.targetEntity();
				handleEntityClass(cl, existing, toMerge);
				Clazz lclazz = Baseclass.getClazzByName(cl.getCanonicalName());
				clazzLink.setLeft(lclazz);

			}
			if (r.isAnnotationPresent(ManyToOne.class)) {
				ManyToOne mtO = r.getAnnotation(ManyToOne.class);
				Class<?> cr = mtO.targetEntity();
				handleEntityClass(cr, existing, toMerge);
				Clazz rclazz = Baseclass.getClazzByName(cr.getCanonicalName());
				clazzLink.setRight(rclazz);

			}
		} catch (Exception e) {
			logger.error("failed setting clazzlink properties", e);
		}
		return clazzLink;
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


	/**
	 * creates all defaults instances, these are defined by the {@link AnnotatedClazz}
	 */
	@ConditionalOnMissingBean
	@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
	@Bean
	public DefaultSecurityEntities createDefaultObjects(Clazzes clazzes) {
		List<Object> toMerge = new ArrayList<>();
		TenantAndUserInit tenantAndUserInit = createAdminAndDefaultTenant(toMerge);
		SecurityTenant defaultTenant = tenantAndUserInit.getDefaultTenant();
		SecurityUser admin = tenantAndUserInit.getAdmin();

		TenantToUserCreate tenantToUserCreate = new TenantToUserCreate().setDefaultTenant(true).setSecurityUser(admin).setTenant(defaultTenant);
		TenantToUser tenantToUser = tenantToUserService.findByIdOrNull(TenantToUser.class, TENANT_TO_USER_ID);
		if (tenantToUser == null) {
			logger.debug("Creating SecurityTenant To SecurityUser link");
			tenantToUser = tenantToUserService.createTenantToUserNoMerge(tenantToUserCreate, null);
			tenantToUser.setCreator(admin);
			tenantToUser.setId(TENANT_TO_USER_ID);
			toMerge.add(tenantToUser);
		} else {
			if (tenantToUserService.updateTenantToUserNoMerge(tenantToUserCreate, tenantToUser)) {
				toMerge.add(tenantToUser);
				logger.debug("Updated SecurityTenant To SecurityUser");
			}
		}
		RoleCreate roleCreate = new RoleCreate()
				.setTenant(defaultTenant)
				.setName("Super Administrators")
				.setDescription("Role for Super Administrators of the system");
		Role superAdminRole = roleService.findByIdOrNull(Role.class, SUPER_ADMIN_ROLE_ID);
		if (superAdminRole == null) {
			logger.debug("Creating Super Admin role");
			superAdminRole = roleService.createRoleNoMerge(roleCreate, null);
			superAdminRole.setTenant(defaultTenant);
			superAdminRole.setCreator(admin);
			superAdminRole.setId(SUPER_ADMIN_ROLE_ID);
			toMerge.add(superAdminRole);
		}
		RoleToUserCreate roleToUserCreate = new RoleToUserCreate().setRole(superAdminRole).setSecurityUser(admin).setTenant(defaultTenant);
		RoleToUser roleToUser = roleToUserService.findByIdOrNull(RoleToUser.class, SUPER_ADMIN_TO_ADMIN_ID);
		if (roleToUser == null) {
			logger.debug("Creating Role To SecurityUser Link");
			roleToUser = roleToUserService.createRoleToUserNoMerge(roleToUserCreate, null);
			roleToUser.setCreator(admin);
			roleToUser.setId(SUPER_ADMIN_TO_ADMIN_ID);
			toMerge.add(roleToUser);
		} else {
			if (roleToUserService.updateRoleToUserNoMerge(roleToUserCreate, roleToUser)) {
				toMerge.add(roleToUser);
				logger.debug("Updated Role To SecurityUser Link");
			}
		}
		roleToUserService.massMerge(toMerge);
		return new DefaultSecurityEntities(tenantAndUserInit.getAdmin(), tenantAndUserInit.getDefaultTenant(), superAdminRole, tenantToUser, roleToUser);

	}


	public TenantAndUserInit createAdminAndDefaultTenant(List<Object> toMerge) {
		boolean tenantUpdated = false;
		SecurityTenantCreate tenantCreate = new SecurityTenantCreate()
				.setName("Default SecurityTenant")
				.setDescription("Default SecurityTenant");
		SecurityTenant defaultTenant = tenantService.findByIdOrNull(SecurityTenant.class, DEFAULT_TENANT_ID);
		if (defaultTenant == null) {
			logger.debug("Creating Default SecurityTenant");
			defaultTenant = tenantService.createTenantNoMerge(tenantCreate, null);
			defaultTenant.setId(DEFAULT_TENANT_ID);
			defaultTenant.setTenant(defaultTenant);
			toMerge.add(defaultTenant);
		} else {
			if (defaultTenant.getTenant() == null) {
				defaultTenant.setTenant(defaultTenant);
				tenantUpdated = true;
			}
		}
		SecurityUserCreate userCreate = new SecurityUserCreate()
				.setTenant(defaultTenant)
				.setName("Admin");
		SecurityUser admin = userService.findByIdOrNull(SecurityUser.class, systemAdminId);
		if (admin == null) {
			logger.debug("Creating Admin SecurityUser");
			admin = userService.createSecurityUserNoMerge(userCreate, null);
			admin.setCreator(admin);
			admin.setId(systemAdminId);
			toMerge.add(admin);
		} else {
			if (admin.getCreator() == null) {
				admin.setCreator(admin);
				toMerge.add(admin);
			}
		}

		if (defaultTenant.getCreator() == null) {
			defaultTenant.setCreator(admin);
			tenantUpdated = true;
		}
		if (tenantUpdated) {
			toMerge.add(defaultTenant);
		}
		return new TenantAndUserInit(admin, defaultTenant);
	}


	private static class TenantAndUserInit {
		private final SecurityTenant defaultTenant;
		private final SecurityUser admin;


		public TenantAndUserInit(SecurityUser admin, SecurityTenant defaultTenant) {
			this.defaultTenant = defaultTenant;
			this.admin = admin;
		}

		public SecurityTenant getDefaultTenant() {
			return defaultTenant;
		}

		public SecurityUser getAdmin() {
			return admin;
		}


	}
}
