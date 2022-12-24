package com.wizzdi.flexicore.boot.dynamic.invokers.service;

import com.flexicore.model.Baseclass;
import com.flexicore.model.Basic;
import com.flexicore.model.SecuredBasic_;
import com.flexicore.security.SecurityContextBase;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.boot.dynamic.invokers.data.DynamicExecutionRepository;
import com.wizzdi.flexicore.boot.dynamic.invokers.interfaces.ExecutionContext;
import com.wizzdi.flexicore.boot.dynamic.invokers.model.DynamicExecution;
import com.wizzdi.flexicore.boot.dynamic.invokers.model.ServiceCanonicalName;
import com.wizzdi.flexicore.boot.dynamic.invokers.request.*;
import com.wizzdi.flexicore.boot.dynamic.invokers.response.InvokerInfo;
import com.wizzdi.flexicore.boot.dynamic.invokers.response.InvokerMethodInfo;
import com.wizzdi.flexicore.security.response.PaginationResponse;
import com.wizzdi.flexicore.security.service.BaseclassService;
import com.wizzdi.flexicore.security.service.BasicService;
import org.pf4j.Extension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;

import jakarta.persistence.metamodel.SingularAttribute;
import java.util.*;
import java.util.stream.Collectors;

@Extension
@Component
public class DynamicExecutionService implements Plugin {

	private static final Logger logger= LoggerFactory.getLogger(DynamicExecutionService.class);

	@Autowired
	private DynamicExecutionRepository dynamicExecutionRepository;

	@Autowired
	private DynamicInvokerService dynamicInvokerService;
	@Autowired
	private ExampleService exampleService;
	@Autowired
	private BasicService basicService;


	public DynamicExecution createDynamicExecution(DynamicExecutionCreate dynamicExecutionCreate, SecurityContextBase securityContext) {
		List<Object> toMerge = new ArrayList<>();
		DynamicExecution dynamicExecution = createDynamicExecutionNoMerge(dynamicExecutionCreate, toMerge, securityContext);
		dynamicExecutionRepository.massMerge(toMerge);
		return dynamicExecution;
	}

	@Transactional
	public void merge(Object base) {
		dynamicExecutionRepository.merge(base);
	}

	@Transactional
	public void massMerge(List<?> toMerge) {
		dynamicExecutionRepository.massMerge(toMerge);
	}

	public DynamicExecution createDynamicExecutionNoMerge(DynamicExecutionCreate dynamicExecutionCreate, List<Object> toMerge, SecurityContextBase securityContext) {
		DynamicExecution dynamicExecution = new DynamicExecution();
		dynamicExecution.setId(UUID.randomUUID().toString());
		updateDynamicExecutionNoMerge(dynamicExecutionCreate, toMerge, dynamicExecution);
		BaseclassService.createSecurityObjectNoMerge(dynamicExecution,securityContext);
		toMerge.add(dynamicExecution);
		return dynamicExecution;
	}

	public boolean updateDynamicExecutionNoMerge(DynamicExecutionCreate dynamicExecutionCreate, List<Object> toMerge, DynamicExecution dynamicExecution) {
		boolean update = basicService.updateBasicNoMerge(dynamicExecutionCreate,dynamicExecution);
		if (dynamicExecutionCreate.getMethodName() != null && !dynamicExecutionCreate.getMethodName().equals(dynamicExecution.getMethodName())) {
			dynamicExecution.setMethodName(dynamicExecutionCreate.getMethodName());
			update = true;
		}

		if (dynamicExecutionCreate.getCategory() != null && !dynamicExecutionCreate.getCategory().equals(dynamicExecution.getCategory())) {
			dynamicExecution.setCategory(dynamicExecutionCreate.getCategory());
			update = true;
		}
		if (dynamicExecutionCreate.getExecutionParametersHolder() != null&&!dynamicExecutionCreate.getExecutionParametersHolder().equals(dynamicExecution.getExecutionParametersHolder()) ) {
			dynamicExecution.setExecutionParametersHolder(dynamicExecutionCreate.getExecutionParametersHolder());
			update = true;
		}
		if (dynamicExecutionCreate.getServiceCanonicalNames() != null) {
			Set<String> ids = dynamicExecutionRepository.getAllServiceCanonicalNames(dynamicExecution).parallelStream().map(f -> f.getServiceCanonicalName()).collect(Collectors.toSet());
			dynamicExecutionCreate.getServiceCanonicalNames().removeAll(ids);
			if (!dynamicExecutionCreate.getServiceCanonicalNames().isEmpty()) {
				List<ServiceCanonicalName> list = dynamicExecutionCreate.getServiceCanonicalNames().parallelStream()
						.map(f -> new ServiceCanonicalName()
								.setId(Baseclass.getBase64ID())
								.setDynamicExecution(dynamicExecution)
								.setServiceCanonicalName(f)
						)
						.collect(Collectors.toList());
				dynamicExecution.getServiceCanonicalNames().addAll(list);
				toMerge.addAll(list);
				update = true;
			}


		}
		return update;
	}

	public DynamicExecution updateDynamicExecution(DynamicExecutionUpdate dynamicExecutionUpdate, SecurityContextBase securityContext) {
		DynamicExecution dynamicExecution = dynamicExecutionUpdate.getDynamicExecution();
		List<Object> toMerge = new ArrayList<>();
		if (updateDynamicExecutionNoMerge(dynamicExecutionUpdate, toMerge, dynamicExecution)) {
			toMerge.add(dynamicExecution);
			dynamicExecutionRepository.massMerge(toMerge);
		}
		return dynamicExecution;
	}

	public void validate(DynamicExecutionCreate dynamicExecutionCreate, SecurityContextBase securityContext) {
		basicService.validate(dynamicExecutionCreate,securityContext);
	}

	public void validateCreate(DynamicExecutionCreate dynamicExecutionCreate, SecurityContextBase securityContext) {
		validate(dynamicExecutionCreate, securityContext);
		String methodName = dynamicExecutionCreate.getMethodName();
		if (methodName == null || methodName.isEmpty()) {
			throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "method name must be non null");
		}
		Set<String> invokerNames = dynamicExecutionCreate.getServiceCanonicalNames();
		if (invokerNames == null || invokerNames.isEmpty()) {
			throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "service canonical names must be non null");
		}
		List<InvokerInfo> list = dynamicInvokerService.listAllDynamicInvokers(new DynamicInvokerFilter().setInvokerTypes(invokerNames), null);
		List<InvokerMethodInfo> invokerMethodInfos = new ArrayList<>();
		for (InvokerInfo invokerInfo : list) {
			for (InvokerMethodInfo method : invokerInfo.getMethods()) {
				if (methodName.equals(method.getName())) {
					invokerMethodInfos.add(method);
				}
			}
		}
		if (invokerMethodInfos.isEmpty()) {
			throw new HttpClientErrorException(HttpStatus.BAD_REQUEST,"No method " + methodName + " for invokers " + invokerNames);
		}
		if(dynamicExecutionCreate.getCategory()==null){
			invokerMethodInfos.stream().map(f->f.getCategories()).flatMap(Set::stream).findFirst().ifPresent(f->dynamicExecutionCreate.setCategory(f));
		}

	}

	public void validate(DynamicExecutionFilter dynamicExecutionFilter, SecurityContextBase securityContext) {
		if(dynamicExecutionFilter.getBasicPropertiesFilter()!=null){
			basicService.validate(dynamicExecutionFilter.getBasicPropertiesFilter(),securityContext);
		}
		if(dynamicExecutionFilter.getDynamicInvokerMethodFilter()!=null){
			dynamicInvokerService.validate(dynamicExecutionFilter.getDynamicInvokerMethodFilter(),securityContext);
		}
	}


	public PaginationResponse<DynamicExecution> getAllDynamicExecutions(DynamicExecutionFilter dynamicExecutionFilter, SecurityContextBase securityContext) {
		List<DynamicExecution> list = listAllDynamicExecutions(dynamicExecutionFilter, securityContext);
		long count = dynamicExecutionRepository.countAllDynamicExecutions(dynamicExecutionFilter, securityContext);
		return new PaginationResponse<>(list, dynamicExecutionFilter.getPageSize(), count);
	}

	public List<DynamicExecution> listAllDynamicExecutions(DynamicExecutionFilter dynamicExecutionFilter, SecurityContextBase securityContext) {
		return dynamicExecutionRepository.listAllDynamicExecutions(dynamicExecutionFilter, securityContext);
	}

	public <T extends Baseclass> List<T> listByIds(Class<T> c, Set<String> ids, SecurityContextBase securityContext) {
		return dynamicExecutionRepository.listByIds(c, ids, securityContext);
	}

	public <T extends Baseclass> T getByIdOrNull(String id, Class<T> c, SecurityContextBase securityContext) {
		return dynamicExecutionRepository.getByIdOrNull(id, c, securityContext);
	}

	public <D extends Basic, E extends Baseclass, T extends D> T getByIdOrNull(String id, Class<T> c, SingularAttribute<D, E> baseclassAttribute, SecurityContextBase securityContext) {
		return dynamicExecutionRepository.getByIdOrNull(id, c, baseclassAttribute, securityContext);
	}

	public <D extends Basic, E extends Baseclass, T extends D> List<T> listByIds(Class<T> c, Set<String> ids, SingularAttribute<D, E> baseclassAttribute, SecurityContextBase securityContext) {
		return dynamicExecutionRepository.listByIds(c, ids, baseclassAttribute, securityContext);
	}

	public <D extends Basic, T extends D> List<T> findByIds(Class<T> c, Set<String> ids, SingularAttribute<D, String> idAttribute) {
		return dynamicExecutionRepository.findByIds(c, ids, idAttribute);
	}

	public <T extends Basic> List<T> findByIds(Class<T> c, Set<String> requested) {
		return dynamicExecutionRepository.findByIds(c, requested);
	}

	public <T> T findByIdOrNull(Class<T> type, String id) {
		return dynamicExecutionRepository.findByIdOrNull(type, id);
	}

	public void validate(DynamicExecutionExampleRequest dynamicExecutionExampleRequest, SecurityContextBase securityContext) {
		String id = dynamicExecutionExampleRequest.getId();
		DynamicExecution dynamicExecution = id != null ? getByIdOrNull(id, DynamicExecution.class, SecuredBasic_.security, securityContext) : null;
		if (dynamicExecution == null) {
			throw new HttpClientErrorException(HttpStatus.BAD_REQUEST,"No DynamicExectuion with id " + id);
		}
		String methodName = dynamicExecution.getMethodName();
		if (methodName == null || methodName.isEmpty()) {
			throw new HttpClientErrorException(HttpStatus.BAD_REQUEST,"No method name for dynamic execution");
		}
		List<ServiceCanonicalName> serviceCanonicalNames = dynamicExecutionRepository.getAllServiceCanonicalNames(dynamicExecution);
		if (serviceCanonicalNames.isEmpty()) {
			throw new HttpClientErrorException(HttpStatus.BAD_REQUEST,"No Invoker Canonical Names with dynamic execution " + id);
		}

		Set<String> invokerNames = serviceCanonicalNames.parallelStream().map(f -> f.getServiceCanonicalName()).collect(Collectors.toSet());
		List<InvokerInfo> list = dynamicInvokerService.listAllDynamicInvokers(new DynamicInvokerFilter().setInvokerTypes(invokerNames), null);
		Set<Class<?>> returnTypes = new HashSet<>();
		for (InvokerInfo invokerInfo : list) {
			for (InvokerMethodInfo method : invokerInfo.getMethods()) {
				if (methodName.equals(method.getName())) {
					Class<?> returnType = method.getReturnTypeClass()!=null&&PaginationResponse.class.isAssignableFrom(method.getReturnTypeClass()) ? invokerInfo.getHandlingType() : method.getReturnTypeClass();
					returnTypes.add(returnType);
				}
			}
		}
		if (returnTypes.isEmpty()) {
			throw new HttpClientErrorException(HttpStatus.BAD_REQUEST,"No method " + methodName + " for invokers " + invokerNames);
		}
		Class<?> common=getCommonClass(returnTypes);
		dynamicExecutionExampleRequest.setClazz(common);

	}

	private Class<?> getCommonClass(Set<Class<?>> returnTypes) {
		Set<Class<?>> init=null;
		for (Class<?> returnType : returnTypes) {
			Set<Class<?>> superClasses=getSuperClasses(returnType);
			if(init==null){
				init=superClasses;
			}
			else{
				init.retainAll(superClasses);
			}
		}
		return init!=null?init.stream().max(Comparator.comparing(f->getSuperClasses(f).size())).orElse(null):null;
	}

	private Set<Class<?>> getSuperClasses(Class<?> c){
		Set<Class<?>> set=new HashSet<>();
		for(Class<?> i=c;c.getSuperclass()!=null ; c=c.getSuperclass()){
			set.add(i);
		}
		return set;
	}

	public Object getExample(Class<?> c) {
		return exampleService.getExample(c);
	}

	public ExecuteInvokerRequest getExecuteInvokerRequest(DynamicExecution dynamicExecution, SecurityContextBase securityContext) {
		return getExecuteInvokerRequest(dynamicExecution, null, securityContext);
	}


	public ExecuteInvokerRequest getExecuteInvokerRequest(DynamicExecution dynamicExecution, ExecutionContext executionContext, SecurityContextBase securityContext) {
		Set<String> invokerNames = dynamicExecutionRepository.getAllServiceCanonicalNames(dynamicExecution).parallelStream().map(f -> f.getServiceCanonicalName()).collect(Collectors.toSet());
		return new ExecuteInvokerRequest()
				.setLastExecuted(dynamicExecution.getLastExecuted())
				.setInvokerNames(invokerNames)
				.setInvokerMethodName(dynamicExecution.getMethodName())
				.setExecutionContext(executionContext)
				.setExecutionParametersHolder( dynamicExecution.getExecutionParametersHolder());
	}

	public void validate(ExecuteDynamicExecution executeDynamicExecution, SecurityContextBase securityContext) {
		String dynamicExecutionId=executeDynamicExecution.getDynamicExecutionId();
		DynamicExecution dynamicExecution=dynamicExecutionId!=null?getByIdOrNull(dynamicExecutionId,DynamicExecution.class,SecuredBasic_.security,securityContext):null;
		if(dynamicExecution==null){
			throw new HttpClientErrorException(HttpStatus.BAD_REQUEST,"no dynamic execution with id "+dynamicExecutionId);
		}
		executeDynamicExecution.setDynamicExecution(dynamicExecution);
	}

	public ExecuteInvokersResponse executeDynamicExecution(ExecuteDynamicExecution executeDynamicExecution, SecurityContextBase securityContext) {
		ExecuteInvokerRequest executeInvokerRequest = getExecuteInvokerRequest(executeDynamicExecution.getDynamicExecution(), securityContext);
		return dynamicInvokerService.executeInvoker(executeInvokerRequest,securityContext);
	}
}
