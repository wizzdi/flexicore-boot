package com.wizzdi.flexicore.boot.dynamic.invokers.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flexicore.model.Baseclass;
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
import org.pf4j.Extension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;

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
	private ObjectMapper objectMapper;


	public DynamicExecution createDynamicExecution(DynamicExecutionCreate dynamicExecutionCreate, SecurityContextBase securityContext) {
		List<Object> toMerge = new ArrayList<>();
		DynamicExecution dynamicExecution = createDynamicExecutionNoMerge(dynamicExecutionCreate, toMerge, securityContext);
		Baseclass security = new Baseclass(dynamicExecutionCreate.getName(), securityContext);
		dynamicExecution.setSecurity(security);
		toMerge.add(security);
		dynamicExecutionRepository.massMerge(toMerge);
		return dynamicExecution;
	}

	public void merge(Object o) {
		dynamicExecutionRepository.merge(o);
	}

	public void massMerge(List<Object> list) {
		dynamicExecutionRepository.massMerge(list);
	}


	public DynamicExecution createDynamicExecutionNoMerge(DynamicExecutionCreate dynamicExecutionCreate, List<Object> toMerge, SecurityContextBase securityContext) {
		DynamicExecution dynamicExecution = new DynamicExecution();
		dynamicExecution.setId(UUID.randomUUID().toString());
		updateDynamicExecutionNoMerge(dynamicExecutionCreate, toMerge, dynamicExecution);
		toMerge.add(dynamicExecution);
		return dynamicExecution;
	}

	public boolean updateDynamicExecutionNoMerge(DynamicExecutionCreate dynamicExecutionCreate, List<Object> toMerge, DynamicExecution dynamicExecution) {
		boolean update = false;
		if (dynamicExecutionCreate.getName() != null && !dynamicExecutionCreate.getName().equals(dynamicExecution.getName())) {
			dynamicExecution.setName(dynamicExecutionCreate.getName());
			update = true;
		}
		if (dynamicExecutionCreate.getDescription() != null && !dynamicExecutionCreate.getDescription().equals(dynamicExecution.getDescription())) {
			dynamicExecution.setDescription(dynamicExecutionCreate.getDescription());
			update = true;
		}
		if (dynamicExecutionCreate.getMethodName() != null && !dynamicExecutionCreate.getMethodName().equals(dynamicExecution.getMethodName())) {
			dynamicExecution.setMethodName(dynamicExecutionCreate.getMethodName());
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
	}

	public void validateCreate(DynamicExecutionCreate dynamicExecutionCreate, SecurityContextBase securityContext) {
		validate(dynamicExecutionCreate, securityContext);
		if (dynamicExecutionCreate.getMethodName() == null || dynamicExecutionCreate.getMethodName().isEmpty()) {
			throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "method name must be non null");
		}
		if (dynamicExecutionCreate.getServiceCanonicalNames() == null || dynamicExecutionCreate.getServiceCanonicalNames().isEmpty()) {
			throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "service canonical names must be non null");
		}

	}

	public void validate(DynamicExecutionFilter dynamicExecutionFilter, SecurityContextBase securityContext) {
	}


	public PaginationResponse<DynamicExecution> getAllDynamicExecutions(DynamicExecutionFilter dynamicExecutionFilter, SecurityContextBase securityContext) {
		List<DynamicExecution> list = listAllDynamicExecutions(dynamicExecutionFilter, securityContext);
		long count = dynamicExecutionRepository.countAllDynamicExecutions(dynamicExecutionFilter, securityContext);
		return new PaginationResponse<>(list, dynamicExecutionFilter.getPageSize(), count);
	}

	public List<DynamicExecution> listAllDynamicExecutions(DynamicExecutionFilter dynamicExecutionFilter, SecurityContextBase securityContext) {
		return dynamicExecutionRepository.listAllDynamicExecutions(dynamicExecutionFilter, securityContext);
	}

	public <T extends DynamicExecution> T getByIdOrNull(String id, Class<T> c, SecurityContextBase securityContext) {
		return dynamicExecutionRepository.getByIdOrNull(id, c, securityContext);
	}

	public <T extends DynamicExecution> List<T> listByIds(Set<String> ids, Class<T> c, SecurityContextBase securityContext) {
		return dynamicExecutionRepository.listByIds(ids, c, securityContext);
	}

	public void validate(DynamicExecutionExampleRequest dynamicExecutionExampleRequest, SecurityContextBase securityContext) {
		String id = dynamicExecutionExampleRequest.getId();
		DynamicExecution dynamicExecution = id != null ? getByIdOrNull(id, DynamicExecution.class, securityContext) : null;
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
					Class<?> returnType = PaginationResponse.class.equals(method.getReturnTypeClass()) ? invokerInfo.getHandlingType() : method.getReturnTypeClass();
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
		DynamicExecution dynamicExecution=dynamicExecutionId!=null?getByIdOrNull(dynamicExecutionId,DynamicExecution.class,securityContext):null;
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
