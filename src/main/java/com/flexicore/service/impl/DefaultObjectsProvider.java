package com.flexicore.service.impl;

import com.flexicore.annotations.AnnotatedClazz;
import com.flexicore.annotations.OperationsInside;
import com.flexicore.data.BaselinkRepository;
import com.flexicore.data.ClazzRegistration;
import com.flexicore.data.ClazzRepository;
import com.flexicore.interfaces.FlexiCoreService;
import com.flexicore.interfaces.dynamic.InvokerInfo;
import com.flexicore.interfaces.dynamic.InvokerMethodInfo;
import com.flexicore.model.*;
import com.flexicore.model.dynamic.DynamicInvoker;
import com.flexicore.request.*;
import com.flexicore.response.SwaggerTags;
import com.flexicore.security.SecurityContext;
import com.flexicore.service.PasswordGenerator;
import com.wizzdi.flexicore.boot.base.init.FlexiCorePluginManager;
import com.wizzdi.flexicore.boot.base.init.PluginInit;
import com.wizzdi.flexicore.boot.jpa.service.EntitiesHolder;
import com.wizzdi.flexicore.security.response.Clazzes;
import com.wizzdi.flexicore.security.response.DefaultSecurityEntities;
import com.wizzdi.flexicore.security.response.Operations;
import com.wizzdi.flexicore.security.service.OperationToClazzService;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.io.FileUtils;
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
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Primary
@Component
@Extension
public class DefaultObjectsProvider implements FlexiCoreService {

	private static final String DEFAULT_TENANT_ID = "jgV8M9d0Qd6owkPPFrbWIQ";
	private static final String TENANT_TO_USER_ID = "Xk5siBx+TyWv+G6V+XuSdw";
	private static final String SUPER_ADMIN_ROLE_ID = "HzFnw-nVR0Olq6WBvwKcQg";
	private static final String SUPER_ADMIN_TO_ADMIN_ID = "EbVFgr+YS3ezYUblzceVGA";
	public static final Pattern CAMEL_CASE_PATTERN = Pattern.compile("(?=[A-Z][a-z])");
	@Autowired
	OperationService operationService;
	@Autowired
	ClazzRegistration clazzRegistration;
	@Autowired
	ClazzRepository clazzrepository;

	@Autowired
	BaselinkRepository baselinkrepository;
	private static final Logger logger = LoggerFactory.getLogger(DefaultObjectsProvider.class);

	@Autowired
	private SecurityService securityService;

	@Autowired
	private TenantService tenantService;

	@Autowired
	private RoleService roleService;

	@Autowired
	private UserService userService;
	@Autowired
	private DynamicInvokersService dynamicInvokersService;
	@Autowired
	private EntitiesHolder entitiesHolder;

	@Autowired
	private OperationToClazzService operationToClazzService;

	private Reflections reflections;
	@Value("${flexicore.users.firstRunPath:/home/flexicore/firstRun.txt}")
	private String firstRunFilePath;
	@Value("${flexicore.users.adminEmail:admin@flexicore.com}")
	private String adminEmail;
	@Autowired
	@Qualifier("systemAdminId")
	private String systemAdminId;

	@Autowired
    @Lazy
    private FlexiCorePluginManager pluginManager;


	public void initializeInvokers() {
		SecurityContext securityContext = securityService.getAdminUserSecurityContext();
		Map<String, DynamicInvoker> invokersMap = dynamicInvokersService.getAllInvokers(new InvokersFilter(), securityContext).getList()
				.parallelStream().collect(Collectors.toMap(f -> f.getCanonicalName(), f -> f));
		InvokersOperationFilter invokersOperationFilter = new InvokersOperationFilter()
				.setInvokers(new ArrayList<>(invokersMap.values()));
		List<Operation> invokerOperations = invokersMap.isEmpty() ? new ArrayList<>() : dynamicInvokersService.getInvokerOperations(invokersOperationFilter, securityContext);
		Map<String, Map<String, Operation>> operationsForInvokers = invokerOperations
				.parallelStream().filter(f -> f.getDynamicInvoker() != null).collect(Collectors.groupingBy(f -> f.getDynamicInvoker().getId(), Collectors.toMap(f -> f.getId(), f -> f)));
		Set<String> operationIds = invokerOperations.parallelStream().map(f -> f.getId()).collect(Collectors.toSet());
		Map<String, OperationToClazz> relatedClazzMap = operationService.getRelatedClasses(operationIds).parallelStream().collect(Collectors.toMap(f -> f.getId(), f -> f));

		initReflections();
		Set<Class<?>> invokers = reflections.getTypesAnnotatedWith(InvokerInfo.class, true);
		List<Object> toMergeInvokers = new ArrayList<>();
		List<Object> toMergeOperations = new ArrayList<>();

		for (Class<?> invoker : invokers) {
			registerInvoker(invoker, invokersMap, operationsForInvokers, relatedClazzMap, toMergeInvokers, toMergeOperations, securityContext);
		}
		dynamicInvokersService.massMerge(toMergeInvokers);
		operationService.massMerge(toMergeOperations);

	}

	public void registerInvoker(Class<?> invoker, SecurityContext securityContext) {
		List<Object> toMergeInvokers = new ArrayList<>();
		List<Object> toMergeOperations = new ArrayList<>();
		Map<String, DynamicInvoker> invokersMap = dynamicInvokersService.getAllInvokers(new InvokersFilter(), securityContext).getList()
				.parallelStream().collect(Collectors.toMap(f -> f.getCanonicalName(), f -> f));
		InvokersOperationFilter invokersOperationFilter = new InvokersOperationFilter()
				.setInvokers(new ArrayList<>(invokersMap.values()));
		List<Operation> invokerOperations = dynamicInvokersService.getInvokerOperations(invokersOperationFilter, securityContext);
		Map<String, Map<String, Operation>> invokerOperationsMap = invokerOperations
				.parallelStream().filter(f -> f.getDynamicInvoker() != null).collect(Collectors.groupingBy(f -> f.getDynamicInvoker().getId(), Collectors.toMap(f -> f.getId(), f -> f)));
		Map<String, OperationToClazz> relatedClazzMap = operationService.getRelatedClasses(invokerOperations.parallelStream().map(f -> f.getId()).collect(Collectors.toSet()))
				.parallelStream().collect(Collectors.toMap(f -> f.getId(), f -> f));
		registerInvoker(invoker, invokersMap, invokerOperationsMap, relatedClazzMap, toMergeInvokers, toMergeOperations, securityContext);
		dynamicInvokersService.massMerge(toMergeInvokers);
		operationService.massMerge(toMergeOperations);


	}


	private void registerInvoker(Class<?> invokerClass, Map<String, DynamicInvoker> invokersMap, Map<String, Map<String, Operation>> operationsForInvokers, Map<String, OperationToClazz> related, List<Object> toMergeInvokers, List<Object> toMergeOperations, SecurityContext securityContext) {
		DynamicInvoker dynamicInvoker = invokersMap.get(invokerClass.getCanonicalName());
		InvokerInfo invokerInfo = invokerClass.getDeclaredAnnotation(InvokerInfo.class);
		if (dynamicInvoker == null) {
			CreateInvokerRequest createInvokerRequest = new CreateInvokerRequest()
					.setCanonicalName(invokerClass.getCanonicalName())
					.setDescription(invokerInfo.description())
					.setDisplayName(invokerInfo.displayName().isEmpty() ? invokerClass.getSimpleName() : invokerInfo.displayName());
			dynamicInvoker = dynamicInvokersService.createInvokerNoMerge(createInvokerRequest, securityContext);
			toMergeInvokers.add(dynamicInvoker);
			invokersMap.put(dynamicInvoker.getCanonicalName(), dynamicInvoker);
			logger.info("created invoker " + dynamicInvoker.getCanonicalName());
		} else {
			UpdateInvokerRequest createInvokerRequest = new UpdateInvokerRequest()
					.setCanonicalName(invokerClass.getCanonicalName())
					.setDescription(invokerInfo.description())
					.setDisplayName(invokerInfo.displayName().isEmpty() ? invokerClass.getSimpleName() : invokerInfo.displayName())
					.setInvoker(dynamicInvoker);
			if (dynamicInvokersService.updateInvokerNoMerge(createInvokerRequest, createInvokerRequest.getInvoker())) {
				toMergeInvokers.add(dynamicInvoker);
				logger.debug("updated invoker " + dynamicInvoker.getCanonicalName());
			} else {
				logger.debug("invoker " + dynamicInvoker.getCanonicalName() + " already exists");

			}

		}
		Map<String, Operation> operationMap = operationsForInvokers.getOrDefault(dynamicInvoker.getId(), new HashMap<>());
		for (Method method : invokerClass.getDeclaredMethods()) {
			if (method.isBridge()) {
				continue;
			}
			InvokerMethodInfo invokerMethodInfo = AnnotatedElementUtils.findMergedAnnotation(method,InvokerMethodInfo.class);
			if (invokerMethodInfo != null) {
				String operationId = Baseclass.generateUUIDFromString(method.toString());


				Operation operation = operationMap.get(operationId);
				if (operation == null) {
					OperationCreate createOperationRequest = new OperationCreate()
							.setDynamicInvoker(dynamicInvoker)
							.setDefaultaccess(invokerMethodInfo.access())
							.setName(invokerMethodInfo.displayName().isEmpty() ? method.getName() : invokerMethodInfo.displayName())
							.setDescription(invokerMethodInfo.description());

					operation = operationService.createOperationNoMerge(createOperationRequest, securityContext);
					operation.setId(operationId);

					toMergeOperations.add(operation);
					operationMap.put(operation.getId(), operation);
					logger.debug("created operation " + operation.getName() + "(" + operationId + ") for invoker " + dynamicInvoker.getCanonicalName());

				} else {
					UpdateOperationRequest updateOperationRequest = new UpdateOperationRequest()
							.setName(invokerMethodInfo.displayName().isEmpty() ? method.getName() : invokerMethodInfo.displayName())
							.setDescription(invokerMethodInfo.description())
							.setAccess(invokerMethodInfo.access())
							.setDynamicInvoker(dynamicInvoker)
							.setId(operationId)
							.setOperation(operation);
					if (operationService.updateOperationNoMerge(updateOperationRequest, updateOperationRequest.getOperation())) {
						toMergeOperations.add(operation);
						logger.debug("updated operation " + operation.getName() + "(" + operationId + ") for invoker " + dynamicInvoker.getCanonicalName());
					} else {
						logger.debug("operation " + operation.getName() + "(" + operationId + ") for invoker " + dynamicInvoker.getCanonicalName() + " already exists");

					}
				}

				operationService.handleOperationRelatedClassesNoMerge(operation, invokerMethodInfo.relatedClasses(), related, toMergeOperations);

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


	@Bean
	@Qualifier("adminSecurityContext")
	@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
	@ConditionalOnMissingBean
	public SecurityContext adminSecurityContext(DefaultSecurityEntities defaultSecurityEntities) {
		return new SecurityContext(Collections.singletonList((Tenant) defaultSecurityEntities.getSecurityTenant()), (User) defaultSecurityEntities.getSecurityUser(), null, (Tenant) defaultSecurityEntities.getSecurityTenant())
				.setRoleMap(Stream.of(defaultSecurityEntities.getRole()).collect(Collectors.groupingBy(f -> f.getTenant().getId())));

	}


	/**
	 * creates all defaults instances, these are defined by the {@link AnnotatedClazz}
	 */
	@SuppressWarnings("unused")
	@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
	@Bean
	public DefaultSecurityEntities createDefaultObjects(Clazzes clazzes) {
		List<Object> toMerge = new ArrayList<>();
		TenantAndUserInit tenantAndUserInit = createAdminAndDefaultTenant(toMerge);
		Tenant defaultTenant = tenantAndUserInit.getDefaultTenant();
		User admin = tenantAndUserInit.getAdmin();

		TenantToUserCreate tenantToUserCreate = new TenantToUserCreate().setDefaultTenant(true).setUser(admin).setTenant(defaultTenant);
		TenantToUser tenantToUser = baselinkrepository.findByIdOrNull(TenantToUser.class, TENANT_TO_USER_ID);
		if (tenantToUser == null) {
			logger.debug("Creating Tenant To User link");
			tenantToUser = userService.createTenantToUserNoMerge(tenantToUserCreate, null);
			tenantToUser.setCreator(admin);
			tenantToUser.setId(TENANT_TO_USER_ID);
			userService.merge(tenantToUser);
		} else {
			if (userService.updateTenantToUserNoMerge(tenantToUserCreate, tenantToUser)) {
				userService.merge(tenantToUser);
				logger.debug("Updated Tenant To User");
			}
		}
		RoleCreate roleCreate = new RoleCreate()
				.setName("Super Administrators")
				.setDescription("Role for Super Administrators of the system")
				.setTenant(defaultTenant);
		Role superAdminRole = baselinkrepository.findByIdOrNull(Role.class, SUPER_ADMIN_ROLE_ID);
		if (superAdminRole == null) {
			logger.debug("Creating Super Admin role");
			superAdminRole = roleService.createRoleNoMerge(roleCreate, null);
			superAdminRole.setCreator(admin);
			superAdminRole.setId(SUPER_ADMIN_ROLE_ID);
			roleService.merge(superAdminRole);
		}
		RoleToUserCreate roleToUserCreate = new RoleToUserCreate().setRole(superAdminRole).setUser(admin).setTenant(defaultTenant);
		RoleToUser roleToUser = baselinkrepository.findByIdOrNull(RoleToUser.class, SUPER_ADMIN_TO_ADMIN_ID);
		if (roleToUser == null) {
			logger.debug("Creating Role To User Link");
			roleToUser = userService.createRoleToUserNoMerge(roleToUserCreate, null);
			roleToUser.setTenant(defaultTenant);
			roleToUser.setCreator(admin);
			roleToUser.setId(SUPER_ADMIN_TO_ADMIN_ID);
			userService.merge(roleToUser);
		} else {
			if (userService.updateRoleToUserNoMerge(roleToUserCreate, roleToUser)) {
				userService.merge(roleToUser);
				logger.debug("Updated Role To User Link");
			}
		}
		return new DefaultSecurityEntities(admin, defaultTenant, superAdminRole, tenantToUser, roleToUser);

	}

	private String readFromFirstRunFile() {
		File file = new File(firstRunFilePath);
		if (!file.getParentFile().exists()) {
			if (!file.getParentFile().mkdirs()) {
				System.out.println("cannot create first run file parent dir");
			}

		}
		List<String> lines = null;
		try {
			lines = FileUtils.readLines(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return lines != null && !lines.isEmpty() ? lines.get(0).trim() : null;

	}

	private void writeToFirstRunFile(String pass) {
		File file = new File(firstRunFilePath);
		if (!file.getParentFile().exists()) {
			if (!file.getParentFile().mkdirs()) {
				System.out.println("cannot create first run file parent dir");
			}

		}
		List<String> lines = new ArrayList<>();
		lines.add(pass);
		try {
			FileUtils.writeLines(file, lines);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	@Bean
	@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
	public SwaggerTags createSwaggerTags(Operations operations,SecurityContext securityContext) {

        List<PluginWrapper> startedPlugins = pluginManager.getStartedPlugins().stream().sorted(PluginInit.PLUGIN_COMPARATOR).collect(Collectors.toList());
        Set<Class<?>> tagClasses = new HashSet<>();
        tagClasses.addAll(pluginManager.getApplicationContext().getBeansWithAnnotation(OpenAPIDefinition.class).values().stream().map(f -> ClassUtils.getUserClass(f.getClass())).collect(Collectors.toSet()));
        tagClasses.addAll(pluginManager.getApplicationContext().getBeansWithAnnotation(Tag.class).values().stream().map(f -> ClassUtils.getUserClass(f.getClass())).collect(Collectors.toSet()));
		tagClasses.addAll(pluginManager.getApplicationContext().getBeansWithAnnotation(RestController.class).values().stream().map(f -> ClassUtils.getUserClass(f.getClass())).collect(Collectors.toSet()));

        for (PluginWrapper startedPlugin : startedPlugins) {
            ApplicationContext applicationContext = pluginManager.getApplicationContext(startedPlugin);
            tagClasses.addAll(applicationContext.getBeansWithAnnotation(OpenAPIDefinition.class).values().stream().map(f -> ClassUtils.getUserClass(f.getClass())).collect(Collectors.toSet()));
            tagClasses.addAll(applicationContext.getBeansWithAnnotation(Tag.class).values().stream().map(f -> ClassUtils.getUserClass(f.getClass())).collect(Collectors.toSet()));
			tagClasses.addAll(applicationContext.getBeansWithAnnotation(RestController.class).values().stream().map(f -> ClassUtils.getUserClass(f.getClass())).collect(Collectors.toSet()));


        }
        List<Tag> tagsToCreate=new ArrayList<>();

		for (Class<?> annotated : tagClasses) {
			addSwaggerTags(annotated, securityContext,tagsToCreate);
		}
        Map<String,Tag> tagMap= tagsToCreate.stream().collect(Collectors.toMap(f-> Baseclass.generateUUIDFromString(f.name()),f->f,(a,b)->a));
        Map<String,DocumentationTag> existing=tagMap.isEmpty()?new HashMap<>():baselinkrepository.findByIds(DocumentationTag.class,tagMap.keySet()).stream().collect(Collectors.toMap(f->f.getId(),f->f));
        List<DocumentationTag> toRet=new ArrayList<>();
        for (Map.Entry<String, Tag> stringTagEntry : tagMap.entrySet()) {
            toRet.add(addTag(securityContext,stringTagEntry.getValue(),stringTagEntry.getKey(),existing));
        }
        return new SwaggerTags(toRet);

	}

	public void addSwaggerTags(Class<?> annotated, SecurityContext securityContext,List<Tag> tags) {
		OpenAPIDefinition def = AnnotatedElementUtils.findMergedAnnotation(annotated,OpenAPIDefinition.class);
		if (def != null) {
			tags.addAll(Arrays.stream(def.tags()).collect(Collectors.toList()));
		}
		RestController restController = AnnotatedElementUtils.findMergedAnnotation(annotated,RestController.class);
		if (restController != null) {
			String name=toHypedName(annotated.getSimpleName());
			tags.add(getTag(name));
		}
		tags.addAll(Arrays.stream(annotated.getAnnotationsByType(Tag.class)).collect(Collectors.toList()));
	}

	public String toHypedName(String camelCase){
		final Matcher matcher = CAMEL_CASE_PATTERN.matcher(camelCase);
		String s = matcher.replaceAll("-").toLowerCase();
		if(s.startsWith("-")){
			s=s.replaceFirst("-","");
		}
		return s;
	}

	private Tag getTag(String name) {
		return new Tag(){
			@Override
			public String name() {
				return name;
			}

			@Override
			public String description() {
				return name;
			}

			@Override
			public ExternalDocumentation externalDocs() {
				return null;
			}

			@Override
			public io.swagger.v3.oas.annotations.extensions.Extension[] extensions() {
				return new io.swagger.v3.oas.annotations.extensions.Extension[0];
			}

			@Override
			public Class<? extends Annotation> annotationType() {
				return Tag.class;
			}
		};
	}

	private DocumentationTag addTag(SecurityContext securityContext, Tag tag, String id, Map<String, DocumentationTag> existing) {
		DocumentationTag doc = existing.get(id);
		if (doc == null) {

			doc = new DocumentationTag(tag.name(), securityContext);
			doc.setSystemObject(true);
			doc.setId(id);
			doc.setDescription(doc.getDescription());
			baselinkrepository.merge(doc);
			logger.debug("found new tag: " + tag.name());
			existing.put(id,doc);
		} else {
			logger.debug("tag: " + tag.name() + " already exist in the database");
		}
		return doc;
	}


	public TenantAndUserInit createAdminAndDefaultTenant(List<Object> toMerge) {
		boolean tenantUpdated = false;
		TenantCreate tenantCreate = new TenantCreate()
				.setName("Default Tenant")
				.setDescription("Default Tenant");
		Tenant defaultTenant = baselinkrepository.findByIdOrNull(Tenant.class, DEFAULT_TENANT_ID);
		if (defaultTenant == null) {
			logger.debug("Creating Default Tenant");
			defaultTenant = tenantService.createTenantNoMerge(tenantCreate, null);
			defaultTenant.setId(DEFAULT_TENANT_ID);
			defaultTenant.setTenant(defaultTenant);
			tenantService.merge(defaultTenant);
		} else {
			if (defaultTenant.getTenant() == null) {
				tenantService.merge(defaultTenant);
				tenantUpdated = true;
			}
		}
		String pass = readFromFirstRunFile();
		if (pass == null) {
			pass = PasswordGenerator.generateRandom(8);
			writeToFirstRunFile(pass);
		}
		UserCreate userCreate = new UserCreate()
				.setEmail(adminEmail)
				.setPassword(pass)
				.setLastName("Admin")
				.setTenant(defaultTenant)
				.setName("Admin");
		User admin = baselinkrepository.findByIdOrNull(User.class, systemAdminId);
		if (admin == null) {
			logger.debug("Creating Admin User");
			admin = userService.createUserNoMerge(userCreate, null);
			admin.setCreator(admin);
			admin.setId(systemAdminId);
			userService.merge(admin);
		} else {
			if (admin.getCreator() == null) {
				userService.merge(admin);
				toMerge.add(admin);
			}
		}

		if (defaultTenant.getCreator() == null) {
			defaultTenant.setCreator(admin);
			tenantUpdated = true;
		}
		if (tenantUpdated) {
			tenantService.merge(defaultTenant);
		}
		if(admin.getTenant()==null){
			admin.setTenant(defaultTenant);
			userService.merge(admin);
		}
		return new TenantAndUserInit(admin, defaultTenant);
	}


	private static class TenantAndUserInit {
		private final Tenant defaultTenant;
		private final User admin;


		public TenantAndUserInit(User admin, Tenant defaultTenant) {
			this.defaultTenant = defaultTenant;
			this.admin = admin;
		}

		public Tenant getDefaultTenant() {
			return defaultTenant;
		}

		public User getAdmin() {
			return admin;
		}


	}
}
