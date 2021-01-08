package com.wizzdi.flexicore.security.service;

import com.flexicore.model.PermissionGroupToBaseclass;
import com.flexicore.model.Baseclass;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.security.data.PermissionGroupToBaseclassRepository;
import com.flexicore.security.SecurityContext;
import com.wizzdi.flexicore.security.request.PermissionGroupToBaseclassCreate;
import com.wizzdi.flexicore.security.request.PermissionGroupToBaseclassFilter;
import com.wizzdi.flexicore.security.request.PermissionGroupToBaseclassUpdate;
import com.wizzdi.flexicore.security.response.PaginationResponse;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Extension
@Component
public class PermissionGroupToBaseclassService implements Plugin {

	@Autowired
	private BaselinkService baselinkService;
	@Autowired
	private PermissionGroupToBaseclassRepository permissionGroupToBaseclassRepository;


	public PermissionGroupToBaseclass createPermissionGroupToBaseclass(PermissionGroupToBaseclassCreate permissionGroupToBaseclassCreate, SecurityContext securityContext){
		PermissionGroupToBaseclass permissionGroupToBaseclass= createPermissionGroupToBaseclassNoMerge(permissionGroupToBaseclassCreate,securityContext);
		permissionGroupToBaseclassRepository.merge(permissionGroupToBaseclass);
		return permissionGroupToBaseclass;
	}


	public PermissionGroupToBaseclass createPermissionGroupToBaseclassNoMerge(PermissionGroupToBaseclassCreate permissionGroupToBaseclassCreate, SecurityContext securityContext){
		PermissionGroupToBaseclass permissionGroupToBaseclass=new PermissionGroupToBaseclass(permissionGroupToBaseclassCreate.getName(),securityContext);
		updatePermissionGroupToBaseclassNoMerge(permissionGroupToBaseclassCreate,permissionGroupToBaseclass);
		permissionGroupToBaseclassRepository.merge(permissionGroupToBaseclass);
		return permissionGroupToBaseclass;
	}

	public boolean updatePermissionGroupToBaseclassNoMerge(PermissionGroupToBaseclassCreate permissionGroupToBaseclassCreate, PermissionGroupToBaseclass permissionGroupToBaseclass) {
		return baselinkService.updateBaselinkNoMerge(permissionGroupToBaseclassCreate,permissionGroupToBaseclass);
	}

	public PermissionGroupToBaseclass updatePermissionGroupToBaseclass(PermissionGroupToBaseclassUpdate permissionGroupToBaseclassUpdate, SecurityContext securityContext){
		PermissionGroupToBaseclass permissionGroupToBaseclass=permissionGroupToBaseclassUpdate.getPermissionGroupToBaseclass();
		if(updatePermissionGroupToBaseclassNoMerge(permissionGroupToBaseclassUpdate,permissionGroupToBaseclass)){
			permissionGroupToBaseclassRepository.merge(permissionGroupToBaseclass);
		}
		return permissionGroupToBaseclass;
	}

	public void validate(PermissionGroupToBaseclassCreate permissionGroupToBaseclassCreate, SecurityContext securityContext) {
		baselinkService.validate(permissionGroupToBaseclassCreate,securityContext);
	}

	public void validate(PermissionGroupToBaseclassFilter permissionGroupToBaseclassFilter, SecurityContext securityContext) {
		baselinkService.validate(permissionGroupToBaseclassFilter,securityContext);
	}

	public <T extends Baseclass> T getByIdOrNull(String id,Class<T> c, SecurityContext securityContext) {
		return permissionGroupToBaseclassRepository.getByIdOrNull(id,c,securityContext);
	}

	public PaginationResponse<PermissionGroupToBaseclass> getAllPermissionGroupToBaseclass(PermissionGroupToBaseclassFilter permissionGroupToBaseclassFilter, SecurityContext securityContext) {
		List<PermissionGroupToBaseclass> list= listAllPermissionGroupToBaseclass(permissionGroupToBaseclassFilter, securityContext);
		long count=permissionGroupToBaseclassRepository.countAllPermissionGroupToBaseclasss(permissionGroupToBaseclassFilter,securityContext);
		return new PaginationResponse<>(list,permissionGroupToBaseclassFilter,count);
	}

	public List<PermissionGroupToBaseclass> listAllPermissionGroupToBaseclass(PermissionGroupToBaseclassFilter permissionGroupToBaseclassFilter, SecurityContext securityContext) {
		return permissionGroupToBaseclassRepository.listAllPermissionGroupToBaseclasss(permissionGroupToBaseclassFilter, securityContext);
	}
}
