package com.wizzdi.flexicore.security.service;

import com.flexicore.model.Baseclass;
import com.flexicore.model.PermissionGroup;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.security.data.PermissionGroupRepository;
import com.flexicore.security.SecurityContextBase;
import com.wizzdi.flexicore.security.request.PermissionGroupCreate;
import com.wizzdi.flexicore.security.request.PermissionGroupFilter;
import com.wizzdi.flexicore.security.request.PermissionGroupUpdate;
import com.wizzdi.flexicore.security.response.PaginationResponse;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Extension
@Component
public class PermissionGroupService implements Plugin {

	@Autowired
	private BaseclassService baseclassService;
	@Autowired
	private PermissionGroupRepository permissionGroupRepository;


	public PermissionGroup createPermissionGroup(PermissionGroupCreate permissionGroupCreate, SecurityContextBase securityContext){
		PermissionGroup permissionGroup= createPermissionGroupNoMerge(permissionGroupCreate,securityContext);
		permissionGroupRepository.merge(permissionGroup);
		return permissionGroup;
	}

	public void merge(Object o){
		permissionGroupRepository.merge(o);
	}
	public void massMerge(List<Object> list){
		permissionGroupRepository.massMerge(list);
	}

	public <T extends Baseclass> List<T> listByIds(Class<T> c,Set<String> ids,  SecurityContextBase securityContext) {
		return permissionGroupRepository.listByIds(c, ids, securityContext);
	}

	public PermissionGroup createPermissionGroupNoMerge(PermissionGroupCreate permissionGroupCreate, SecurityContextBase securityContext){
		PermissionGroup permissionGroup=new PermissionGroup(permissionGroupCreate.getName(),securityContext);
		updatePermissionGroupNoMerge(permissionGroupCreate,permissionGroup);
		permissionGroupRepository.merge(permissionGroup);
		return permissionGroup;
	}

	public boolean updatePermissionGroupNoMerge(PermissionGroupCreate permissionGroupCreate, PermissionGroup permissionGroup) {
		boolean update = baseclassService.updateBaseclassNoMerge(permissionGroupCreate, permissionGroup);
		if(permissionGroupCreate.getExternalId()!=null&&!permissionGroupCreate.getExternalId().equals(permissionGroup.getExternalId())){
			permissionGroup.setExternalId(permissionGroupCreate.getExternalId());
			update=true;
		}
		return update;
	}

	public PermissionGroup updatePermissionGroup(PermissionGroupUpdate permissionGroupUpdate, SecurityContextBase securityContext){
		PermissionGroup permissionGroup=permissionGroupUpdate.getPermissionGroup();
		if(updatePermissionGroupNoMerge(permissionGroupUpdate,permissionGroup)){
			permissionGroupRepository.merge(permissionGroup);
		}
		return permissionGroup;
	}

	@Deprecated
	public void validate(PermissionGroupCreate permissionGroupCreate, SecurityContextBase securityContext) {
		baseclassService.validate(permissionGroupCreate,securityContext);
	}

	@Deprecated
	public void validate(PermissionGroupFilter permissionGroupFilter, SecurityContextBase securityContext) {
	}

	public <T extends Baseclass> T getByIdOrNull(String id,Class<T> c, SecurityContextBase securityContext) {
		return permissionGroupRepository.getByIdOrNull(id,c,securityContext);
	}

	public PaginationResponse<PermissionGroup> getAllPermissionGroups(PermissionGroupFilter permissionGroupFilter, SecurityContextBase securityContext) {
		List<PermissionGroup> list= listAllPermissionGroups(permissionGroupFilter, securityContext);
		long count=permissionGroupRepository.countAllPermissionGroups(permissionGroupFilter,securityContext);
		return new PaginationResponse<>(list,permissionGroupFilter,count);
	}

	public List<PermissionGroup> listAllPermissionGroups(PermissionGroupFilter permissionGroupFilter, SecurityContextBase securityContext) {
		return permissionGroupRepository.listAllPermissionGroups(permissionGroupFilter, securityContext);
	}
}
