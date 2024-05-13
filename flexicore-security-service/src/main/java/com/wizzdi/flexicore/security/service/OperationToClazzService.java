package com.wizzdi.flexicore.security.service;

import com.flexicore.model.Baseclass;
import com.flexicore.model.OperationToClazz;
import com.flexicore.security.SecurityContextBase;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.security.data.OperationToClazzRepository;
import com.wizzdi.flexicore.security.request.OperationToClazzCreate;
import com.wizzdi.flexicore.security.request.OperationToClazzFilter;
import com.wizzdi.flexicore.security.request.OperationToClazzUpdate;
import com.wizzdi.flexicore.security.response.PaginationResponse;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Extension
@Component
public class OperationToClazzService implements Plugin {

	@Autowired
	private BasicService basicService;
	@Autowired
	private OperationToClazzRepository operationToClazzRepository;


	public OperationToClazz createOperationToClazz(OperationToClazzCreate operationToClazzCreate, SecurityContextBase securityContext){
		OperationToClazz operationToClazz= createOperationToClazzNoMerge(operationToClazzCreate,securityContext);
		operationToClazzRepository.merge(operationToClazz);
		return operationToClazz;
	}
	public <T> T merge(T o){
		return operationToClazzRepository.merge(o);
	}
	public void massMerge(List<Object> list){
		operationToClazzRepository.massMerge(list);
	}

	public <T extends Baseclass> List<T> listByIds(Class<T> c,Set<String> ids,  SecurityContextBase securityContext) {
		return operationToClazzRepository.listByIds(c, ids, securityContext);
	}

	public OperationToClazz createOperationToClazzNoMerge(OperationToClazzCreate operationToClazzCreate, SecurityContextBase securityContext){
		OperationToClazz operationToClazz=new OperationToClazz();
		operationToClazz.setId(UUID.randomUUID().toString());
		updateOperationToClazzNoMerge(operationToClazzCreate,operationToClazz);
		return operationToClazz;
	}

	public boolean updateOperationToClazzNoMerge(OperationToClazzCreate operationToClazzCreate, OperationToClazz operationToClazz) {
		boolean update= basicService.updateBasicNoMerge(operationToClazzCreate,operationToClazz);
		if(operationToClazzCreate.getClazz()!=null&&(operationToClazz.getClazz()==null||!operationToClazzCreate.getClazz().getId().equals(operationToClazz.getClazz().getId()))){
			operationToClazz.setClazz(operationToClazzCreate.getClazz());
			update=true;
		}
		if(operationToClazzCreate.getSecurityOperation()!=null&&(operationToClazz.getOperation()==null||!operationToClazzCreate.getSecurityOperation().getId().equals(operationToClazz.getOperation().getId()))){
			operationToClazz.setOperation(operationToClazzCreate.getSecurityOperation());
			update=true;
		}
		return update;
	}

	public OperationToClazz updateOperationToClazz(OperationToClazzUpdate operationToClazzUpdate, SecurityContextBase securityContext){
		OperationToClazz operationToClazz=operationToClazzUpdate.getOperationToClazz();
		if(updateOperationToClazzNoMerge(operationToClazzUpdate,operationToClazz)){
			operationToClazzRepository.merge(operationToClazz);
		}
		return operationToClazz;
	}

	@Deprecated
	public void validate(OperationToClazzCreate operationToClazzCreate, SecurityContextBase securityContext) {
		basicService.validate(operationToClazzCreate,securityContext);
	}

	@Deprecated
	public void validate(OperationToClazzFilter operationToClazzFilter, SecurityContextBase securityContext) {
		basicService.validate(operationToClazzFilter,securityContext);
	}

	public <T extends Baseclass> T getByIdOrNull(String id,Class<T> c, SecurityContextBase securityContext) {
		return operationToClazzRepository.getByIdOrNull(id,c,securityContext);
	}

	public PaginationResponse<OperationToClazz> getAllOperationToClazz(OperationToClazzFilter operationToClazzFilter, SecurityContextBase securityContext) {
		List<OperationToClazz> list= listAllOperationToClazz(operationToClazzFilter, securityContext);
		long count=operationToClazzRepository.countAllOperationToClazzs(operationToClazzFilter,securityContext);
		return new PaginationResponse<>(list,operationToClazzFilter,count);
	}

	public List<OperationToClazz> listAllOperationToClazz(OperationToClazzFilter operationToClazzFilter, SecurityContextBase securityContext) {
		return operationToClazzRepository.listAllOperationToClazzs(operationToClazzFilter, securityContext);
	}
}
