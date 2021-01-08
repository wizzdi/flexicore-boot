package com.wizzdi.flexicore.security.service;

import com.flexicore.model.Operation;
import com.flexicore.model.Baseclass;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.security.data.OperationRepository;
import com.wizzdi.flexicore.security.request.OperationCreate;
import com.wizzdi.flexicore.security.request.OperationFilter;
import com.wizzdi.flexicore.security.request.OperationUpdate;
import com.flexicore.security.SecurityContext;
import com.wizzdi.flexicore.security.response.PaginationResponse;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Extension
@Component
public class OperationService implements Plugin {

	@Autowired
	private BaseclassService baseclassService;
	@Autowired
	private OperationRepository operationRepository;


	public Operation createOperation(OperationCreate operationCreate, SecurityContext securityContext){
		Operation operation= createOperationNoMerge(operationCreate,securityContext);
		operationRepository.merge(operation);
		return operation;
	}


	public Operation createOperationNoMerge(OperationCreate operationCreate, SecurityContext securityContext){
		Operation operation=new Operation(operationCreate.getName(),securityContext);
		updateOperationNoMerge(operationCreate,operation);
		operationRepository.merge(operation);
		return operation;
	}

	public boolean updateOperationNoMerge(OperationCreate operationCreate, Operation operation) {
		return baseclassService.updateBaseclassNoMerge(operationCreate,operation);
	}

	public Operation updateOperation(OperationUpdate operationUpdate, SecurityContext securityContext){
		Operation operation=operationUpdate.getOperation();
		if(updateOperationNoMerge(operationUpdate,operation)){
			operationRepository.merge(operation);
		}
		return operation;
	}

	public void validate(OperationCreate operationCreate, SecurityContext securityContext) {
		baseclassService.validate(operationCreate,securityContext);
	}

	public void validate(OperationFilter operationFilter, SecurityContext securityContext) {
	}

	public <T extends Baseclass> T getByIdOrNull(String id,Class<T> c, SecurityContext securityContext) {
		return operationRepository.getByIdOrNull(id,c,securityContext);
	}

	public PaginationResponse<Operation> getAllOperations(OperationFilter operationFilter, SecurityContext securityContext) {
		List<Operation> list= listAllOperations(operationFilter, securityContext);
		long count=operationRepository.countAllOperations(operationFilter,securityContext);
		return new PaginationResponse<>(list,operationFilter,count);
	}

	public List<Operation> listAllOperations(OperationFilter operationFilter, SecurityContext securityContext) {
		return operationRepository.listAllOperations(operationFilter, securityContext);
	}
}
