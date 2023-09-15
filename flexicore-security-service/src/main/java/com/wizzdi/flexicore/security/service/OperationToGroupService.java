package com.wizzdi.flexicore.security.service;

import com.flexicore.model.Baseclass;
import com.flexicore.model.Basic;
import com.flexicore.model.OperationToGroup;
import com.flexicore.security.SecurityContextBase;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.security.data.OperationToGroupRepository;
import com.wizzdi.flexicore.security.request.OperationToGroupCreate;
import com.wizzdi.flexicore.security.request.OperationToGroupFilter;
import com.wizzdi.flexicore.security.request.OperationToGroupUpdate;
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
public class OperationToGroupService implements Plugin {

	@Autowired
	private BasicService basicService;
	@Autowired
	private OperationToGroupRepository operationRepository;


	public OperationToGroup createOperationToGroup(OperationToGroupCreate operationToGroupCreate, SecurityContextBase securityContext){
		OperationToGroup operation= createOperationToGroupNoMerge(operationToGroupCreate,securityContext);
		operationRepository.merge(operation);
		return operation;
	}




	public OperationToGroup createOperationToGroupNoMerge(OperationToGroupCreate operationToGroupCreate, SecurityContextBase securityContext){
		OperationToGroup operation=new OperationToGroup();
		operation.setId(UUID.randomUUID().toString());
		updateOperationToGroupNoMerge(operationToGroupCreate,operation);
		BaseclassService.createSecurityObjectNoMerge(operation,securityContext);
		return operation;
	}

	public boolean updateOperationToGroupNoMerge(OperationToGroupCreate operationToGroupCreate, OperationToGroup operationToGroup) {
		boolean update = basicService.updateBasicNoMerge(operationToGroupCreate, operationToGroup);
		if(operationToGroupCreate.getOperationGroup()!=null&&(operationToGroup.getOperationGroup()==null||!operationToGroupCreate.getOperationGroup().getId().equals(operationToGroup.getOperationGroup().getId()))){
			operationToGroup.setOperationGroup(operationToGroupCreate.getOperationGroup());
			update=true;
		}
		if(operationToGroupCreate.getOperation()!=null&&(operationToGroup.getOperation()==null||!operationToGroupCreate.getOperation().getId().equals(operationToGroup.getOperation().getId()))){
			operationToGroup.setOperation(operationToGroupCreate.getOperation());
			update=true;
		}
		return update;
	}

	public OperationToGroup updateOperationToGroup(OperationToGroupUpdate operationToGroupUpdate, SecurityContextBase securityContext){
		OperationToGroup operation=operationToGroupUpdate.getOperationToGroup();
		if(updateOperationToGroupNoMerge(operationToGroupUpdate,operation)){
			operationRepository.merge(operation);
		}
		return operation;
	}

	public OperationToGroup updateOperationToGroup(OperationToGroupCreate operationToGroupUpdate, OperationToGroup operationToGroup){
		if(updateOperationToGroupNoMerge(operationToGroupUpdate,operationToGroup)){
			operationRepository.merge(operationToGroup);
		}
		return operationToGroup;
	}



	public PaginationResponse<OperationToGroup> getAllOperationToGroups(OperationToGroupFilter operationToGroupFilter, SecurityContextBase securityContext) {
		List<OperationToGroup> list= listAllOperationToGroups(operationToGroupFilter, securityContext);
		long count=operationRepository.countAllOperationToGroups(operationToGroupFilter,securityContext);
		return new PaginationResponse<>(list,operationToGroupFilter,count);
	}

	public List<OperationToGroup> listAllOperationToGroups(OperationToGroupFilter operationToGroupFilter, SecurityContextBase securityContext) {
		return operationRepository.listAllOperationToGroups(operationToGroupFilter, securityContext);
	}

	@Transactional
	public <T> T merge(T base, boolean updateDate, boolean propagateEvents) {
		return operationRepository.merge(base, updateDate, propagateEvents);
	}

	@Transactional
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

	@Transactional
	public <T> T merge(T base) {
		return operationRepository.merge(base);
	}

	@Transactional
	public void massMerge(List<?> toMerge) {
		operationRepository.massMerge(toMerge);
	}
}
