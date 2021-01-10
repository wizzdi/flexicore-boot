package com.wizzdi.flexicore.security.service;

import com.flexicore.model.SecurityOperation;
import com.flexicore.model.Baseclass;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.security.data.SecurityOperationRepository;
import com.wizzdi.flexicore.security.request.SecurityOperationCreate;
import com.wizzdi.flexicore.security.request.SecurityOperationFilter;
import com.wizzdi.flexicore.security.request.SecurityOperationUpdate;
import com.flexicore.security.SecurityContextBase;
import com.wizzdi.flexicore.security.response.PaginationResponse;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Extension
@Component
public class SecurityOperationService implements Plugin {

	@Autowired
	private BaseclassService baseclassService;
	@Autowired
	private SecurityOperationRepository operationRepository;


	public SecurityOperation createOperation(SecurityOperationCreate operationCreate, SecurityContextBase securityContext){
		SecurityOperation operation= createOperationNoMerge(operationCreate,securityContext);
		operationRepository.merge(operation);
		return operation;
	}

	public void merge(Object o){
		operationRepository.merge(o);
	}
	public void massMerge(List<Object> list){
		operationRepository.massMerge(list);
	}

	public <T extends Baseclass> List<T> listByIds(Class<T> c,Set<String> ids,  SecurityContextBase securityContext) {
		return operationRepository.listByIds(c, ids, securityContext);
	}

	public SecurityOperation createOperationNoMerge(SecurityOperationCreate operationCreate, SecurityContextBase securityContext){
		SecurityOperation operation=new SecurityOperation(operationCreate.getName(),securityContext);
		updateOperationNoMerge(operationCreate,operation);
		operationRepository.merge(operation);
		return operation;
	}

	public boolean updateOperationNoMerge(SecurityOperationCreate operationCreate, SecurityOperation operation) {
		return baseclassService.updateBaseclassNoMerge(operationCreate,operation);
	}

	public SecurityOperation updateOperation(SecurityOperationUpdate operationUpdate, SecurityContextBase securityContext){
		SecurityOperation operation=operationUpdate.getOperation();
		if(updateOperationNoMerge(operationUpdate,operation)){
			operationRepository.merge(operation);
		}
		return operation;
	}

	public void validate(SecurityOperationCreate operationCreate, SecurityContextBase securityContext) {
		baseclassService.validate(operationCreate,securityContext);
	}

	public void validate(SecurityOperationFilter operationFilter, SecurityContextBase securityContext) {
	}

	public <T extends Baseclass> T getByIdOrNull(String id,Class<T> c, SecurityContextBase securityContext) {
		return operationRepository.getByIdOrNull(id,c,securityContext);
	}

	public PaginationResponse<SecurityOperation> getAllOperations(SecurityOperationFilter operationFilter, SecurityContextBase securityContext) {
		List<SecurityOperation> list= listAllOperations(operationFilter, securityContext);
		long count=operationRepository.countAllOperations(operationFilter,securityContext);
		return new PaginationResponse<>(list,operationFilter,count);
	}

	public List<SecurityOperation> listAllOperations(SecurityOperationFilter operationFilter, SecurityContextBase securityContext) {
		return operationRepository.listAllOperations(operationFilter, securityContext);
	}

	public <T extends Baseclass> List<T> findByIds(Class<T> c, Set<String> requested) {
		return operationRepository.findByIds(c, requested);
	}
}
