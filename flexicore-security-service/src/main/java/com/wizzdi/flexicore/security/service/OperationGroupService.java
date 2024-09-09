package com.wizzdi.flexicore.security.service;

import com.flexicore.model.Baseclass;
import com.flexicore.model.Basic;
import com.flexicore.model.OperationGroup;
import com.flexicore.security.SecurityContextBase;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.security.data.OperationGroupRepository;
import com.wizzdi.flexicore.security.request.OperationGroupCreate;
import com.wizzdi.flexicore.security.request.OperationGroupFilter;
import com.wizzdi.flexicore.security.request.OperationGroupUpdate;
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
public class OperationGroupService implements Plugin {

	@Autowired
	private BasicService basicService;
	@Autowired
	private OperationGroupRepository operationRepository;


	public OperationGroup createOperationGroup(OperationGroupCreate operationCreate, SecurityContextBase securityContext){
		OperationGroup operation= createOperationGroupNoMerge(operationCreate,securityContext);
		operationRepository.merge(operation);
		return operation;
	}




	public OperationGroup createOperationGroupNoMerge(OperationGroupCreate operationCreate, SecurityContextBase securityContext){
		OperationGroup operation=new OperationGroup();
		operation.setId(UUID.randomUUID().toString());
		updateOperationGroupNoMerge(operationCreate,operation);
		BaseclassService.createSecurityObjectNoMerge(operation,securityContext);
		return operation;
	}

	public boolean updateOperationGroupNoMerge(OperationGroupCreate operationCreate, OperationGroup operation) {
		boolean update = basicService.updateBasicNoMerge(operationCreate, operation);
		if(operationCreate.getExternalId()!=null&&!operationCreate.getExternalId().equals(operation.getExternalId())){
			operation.setExternalId(operationCreate.getExternalId());
			update=true;
		}
		return update;
	}

	public OperationGroup updateOperationGroup(OperationGroupUpdate operationUpdate, SecurityContextBase securityContext){
		OperationGroup operation=operationUpdate.getOperation();
		if(updateOperationGroupNoMerge(operationUpdate,operation)){
			operationRepository.merge(operation);
		}
		return operation;
	}

	public OperationGroup updateOperationGroup(OperationGroupCreate operationUpdate, OperationGroup operationGroup){
		if(updateOperationGroupNoMerge(operationUpdate,operationGroup)){
			operationRepository.merge(operationGroup);
		}
		return operationGroup;
	}



	public PaginationResponse<OperationGroup> getAllOperationGroups(OperationGroupFilter operationFilter, SecurityContextBase securityContext) {
		List<OperationGroup> list= listAllOperationGroups(operationFilter, securityContext);
		long count=operationRepository.countAllOperationGroups(operationFilter,securityContext);
		return new PaginationResponse<>(list,operationFilter,count);
	}

	public List<OperationGroup> listAllOperationGroups(OperationGroupFilter operationFilter, SecurityContextBase securityContext) {
		return operationRepository.listAllOperationGroups(operationFilter, securityContext);
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
