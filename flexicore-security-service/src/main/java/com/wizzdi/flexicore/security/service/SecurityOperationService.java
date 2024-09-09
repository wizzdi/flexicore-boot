package com.wizzdi.flexicore.security.service;

import com.flexicore.model.Basic;
import com.flexicore.model.SecurityOperation;
import com.flexicore.model.Baseclass;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.security.data.SecurityOperationRepository;
import com.wizzdi.flexicore.security.request.SecurityOperationCreate;
import com.wizzdi.flexicore.security.request.SecurityOperationFilter;
import com.wizzdi.flexicore.security.request.SecurityOperationUpdate;
import com.flexicore.security.SecurityContextBase;
import com.wizzdi.flexicore.security.response.PaginationResponse;
import jakarta.persistence.metamodel.SingularAttribute;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Extension
@Component
public class SecurityOperationService implements Plugin {

	@Autowired
	private BasicService basicService;
	@Autowired
	private SecurityOperationRepository operationRepository;


	public SecurityOperation createOperation(SecurityOperationCreate operationCreate, SecurityContextBase securityContext){
		SecurityOperation operation= createOperationNoMerge(operationCreate,securityContext);
		operationRepository.merge(operation);
		return operation;
	}




	public SecurityOperation createOperationNoMerge(SecurityOperationCreate operationCreate, SecurityContextBase securityContext){
		SecurityOperation operation=new SecurityOperation();
		operation.setId(UUID.randomUUID().toString());
		updateOperationNoMerge(operationCreate,operation);
		BaseclassService.createSecurityObjectNoMerge(operation,securityContext);
		return operation;
	}

	public boolean updateOperationNoMerge(SecurityOperationCreate operationCreate, SecurityOperation operation) {
		boolean update = basicService.updateBasicNoMerge(operationCreate, operation);
		if(operationCreate.getCategory()!=null&&!operationCreate.getCategory().equals(operation.getCategory())){
			operation.setCategory(operationCreate.getCategory());
			update=true;
		}
		return update;
	}

	public SecurityOperation updateOperation(SecurityOperationUpdate operationUpdate, SecurityContextBase securityContext){
		SecurityOperation operation=operationUpdate.getOperation();
		if(updateOperationNoMerge(operationUpdate,operation)){
			operationRepository.merge(operation);
		}
		return operation;
	}



	public PaginationResponse<SecurityOperation> getAllOperations(SecurityOperationFilter operationFilter, SecurityContextBase securityContext) {
		List<SecurityOperation> list= listAllOperations(operationFilter, securityContext);
		long count=operationRepository.countAllOperations(operationFilter,securityContext);
		return new PaginationResponse<>(list,operationFilter,count);
	}

	public List<SecurityOperation> listAllOperations(SecurityOperationFilter operationFilter, SecurityContextBase securityContext) {
		return operationRepository.listAllOperations(operationFilter, securityContext);
	}


	public <T> T merge(T base, boolean updateDate, boolean propagateEvents) {
		return operationRepository.merge(base, updateDate, propagateEvents);
	}


	public void massMerge(List<?> toMerge, boolean updatedate, boolean propagateEvents) {
		operationRepository.massMerge(toMerge, updatedate, propagateEvents);
	}

	public <T extends Baseclass> List<T> listByIds(Class<T> c, Set<String> ids, SecurityContextBase securityContext) {
		return operationRepository.listByIds(c, ids, securityContext);
	}

	public <T extends Baseclass> T getByIdOrNull(String id, Class<T> c, SecurityContextBase securityContext) {
		return operationRepository.getByIdOrNull(id, c, securityContext);
	}

	public <D extends Basic, E extends Baseclass, T extends D> T getByIdOrNull(String id, Class<T> c, SingularAttribute<D, E> baseclassAttribute, SecurityContextBase securityContext) {
		return operationRepository.getByIdOrNull(id, c, baseclassAttribute, securityContext);
	}

	public <D extends Basic, E extends Baseclass, T extends D> List<T> listByIds(Class<T> c, Set<String> ids, SingularAttribute<D, E> baseclassAttribute, SecurityContextBase securityContext) {
		return operationRepository.listByIds(c, ids, baseclassAttribute, securityContext);
	}

	public <D extends Basic, T extends D> List<T> findByIds(Class<T> c, Set<String> ids, SingularAttribute<D, String> idAttribute) {
		return operationRepository.findByIds(c, ids, idAttribute);
	}

	public <T extends Basic> List<T> findByIds(Class<T> c, Set<String> requested) {
		return operationRepository.findByIds(c, requested);
	}

	public <T> T findByIdOrNull(Class<T> type, String id) {
		return operationRepository.findByIdOrNull(type, id);
	}


	public <T> T merge(T base) {
		return operationRepository.merge(base);
	}


	public void massMerge(List<?> toMerge) {
		operationRepository.massMerge(toMerge);
	}
}
