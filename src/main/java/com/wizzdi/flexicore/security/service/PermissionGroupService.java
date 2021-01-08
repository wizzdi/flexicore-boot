package com.wizzdi.flexicore.security.service;

import com.flexicore.model.Baseclass;
import com.flexicore.model.PermissionGroup;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.security.data.PermissionGroupRepository;
import com.flexicore.security.SecurityContext;
import com.wizzdi.flexicore.security.request.PermissionGroupCreate;
import com.wizzdi.flexicore.security.request.PermissionGroupFilter;
import com.wizzdi.flexicore.security.request.PermissionGroupUpdate;
import com.wizzdi.flexicore.security.response.PaginationResponse;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Extension
@Component
public class PermissionGroupService implements Plugin {

	@Autowired
	private BaseclassService baseclassService;
	@Autowired
	private PermissionGroupRepository permissionGroupRepository;


	public PermissionGroup createPermissionGroup(PermissionGroupCreate permissionGroupCreate, SecurityContext securityContext){
		PermissionGroup permissionGroup= createPermissionGroupNoMerge(permissionGroupCreate,securityContext);
		permissionGroupRepository.merge(permissionGroup);
		return permissionGroup;
	}


	public PermissionGroup createPermissionGroupNoMerge(PermissionGroupCreate permissionGroupCreate, SecurityContext securityContext){
		PermissionGroup permissionGroup=new PermissionGroup(permissionGroupCreate.getName(),securityContext);
		updatePermissionGroupNoMerge(permissionGroupCreate,permissionGroup);
		permissionGroupRepository.merge(permissionGroup);
		return permissionGroup;
	}

	public boolean updatePermissionGroupNoMerge(PermissionGroupCreate permissionGroupCreate, PermissionGroup permissionGroup) {
		return baseclassService.updateBaseclassNoMerge(permissionGroupCreate,permissionGroup);
	}

	public PermissionGroup updatePermissionGroup(PermissionGroupUpdate permissionGroupUpdate, SecurityContext securityContext){
		PermissionGroup permissionGroup=permissionGroupUpdate.getPermissionGroup();
		if(updatePermissionGroupNoMerge(permissionGroupUpdate,permissionGroup)){
			permissionGroupRepository.merge(permissionGroup);
		}
		return permissionGroup;
	}

	public void validate(PermissionGroupCreate permissionGroupCreate, SecurityContext securityContext) {
		baseclassService.validate(permissionGroupCreate,securityContext);
	}

	public void validate(PermissionGroupFilter permissionGroupFilter, SecurityContext securityContext) {
	}

	public <T extends Baseclass> T getByIdOrNull(String id,Class<T> c, SecurityContext securityContext) {
		return permissionGroupRepository.getByIdOrNull(id,c,securityContext);
	}

	public PaginationResponse<PermissionGroup> getAllPermissionGroups(PermissionGroupFilter permissionGroupFilter, SecurityContext securityContext) {
		List<PermissionGroup> list= listAllPermissionGroups(permissionGroupFilter, securityContext);
		long count=permissionGroupRepository.countAllPermissionGroups(permissionGroupFilter,securityContext);
		return new PaginationResponse<>(list,permissionGroupFilter,count);
	}

	public List<PermissionGroup> listAllPermissionGroups(PermissionGroupFilter permissionGroupFilter, SecurityContext securityContext) {
		return permissionGroupRepository.listAllPermissionGroups(permissionGroupFilter, securityContext);
	}
}
