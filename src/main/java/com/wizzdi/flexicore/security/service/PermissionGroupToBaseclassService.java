package com.wizzdi.flexicore.security.service;

import com.flexicore.model.PermissionGroupToBaseclass;
import com.flexicore.model.Baseclass;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.security.data.PermissionGroupToBaseclassRepository;
import com.flexicore.security.SecurityContextBase;
import com.wizzdi.flexicore.security.request.PermissionGroupToBaseclassCreate;
import com.wizzdi.flexicore.security.request.PermissionGroupToBaseclassFilter;
import com.wizzdi.flexicore.security.request.PermissionGroupToBaseclassUpdate;
import com.wizzdi.flexicore.security.response.PaginationResponse;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Extension
@Component
public class PermissionGroupToBaseclassService implements Plugin {

	@Autowired
	private BaselinkService baselinkService;
	@Autowired
	private PermissionGroupToBaseclassRepository permissionGroupToBaseclassRepository;


	public PermissionGroupToBaseclass createPermissionGroupToBaseclass(PermissionGroupToBaseclassCreate permissionGroupToBaseclassCreate, SecurityContextBase securityContext){
		PermissionGroupToBaseclass permissionGroupToBaseclass= createPermissionGroupToBaseclassNoMerge(permissionGroupToBaseclassCreate,securityContext);
		permissionGroupToBaseclassRepository.merge(permissionGroupToBaseclass);
		return permissionGroupToBaseclass;
	}
	public void merge(Object o){
		permissionGroupToBaseclassRepository.merge(o);
	}
	public void massMerge(List<Object> list){
		permissionGroupToBaseclassRepository.massMerge(list);
	}

	public <T extends Baseclass> List<T> listByIds(Class<T> c,Set<String> ids,  SecurityContextBase securityContext) {
		return permissionGroupToBaseclassRepository.listByIds(c, ids, securityContext);
	}

	public PermissionGroupToBaseclass createPermissionGroupToBaseclassNoMerge(PermissionGroupToBaseclassCreate permissionGroupToBaseclassCreate, SecurityContextBase securityContext){
		PermissionGroupToBaseclass permissionGroupToBaseclass=new PermissionGroupToBaseclass(permissionGroupToBaseclassCreate.getName(),securityContext);
		updatePermissionGroupToBaseclassNoMerge(permissionGroupToBaseclassCreate,permissionGroupToBaseclass);
		permissionGroupToBaseclassRepository.merge(permissionGroupToBaseclass);
		return permissionGroupToBaseclass;
	}

	public boolean updatePermissionGroupToBaseclassNoMerge(PermissionGroupToBaseclassCreate permissionGroupToBaseclassCreate, PermissionGroupToBaseclass permissionGroupToBaseclass) {
		return baselinkService.updateBaselinkNoMerge(permissionGroupToBaseclassCreate,permissionGroupToBaseclass);
	}

	public PermissionGroupToBaseclass updatePermissionGroupToBaseclass(PermissionGroupToBaseclassUpdate permissionGroupToBaseclassUpdate, SecurityContextBase securityContext){
		PermissionGroupToBaseclass permissionGroupToBaseclass=permissionGroupToBaseclassUpdate.getPermissionGroupToBaseclass();
		if(updatePermissionGroupToBaseclassNoMerge(permissionGroupToBaseclassUpdate,permissionGroupToBaseclass)){
			permissionGroupToBaseclassRepository.merge(permissionGroupToBaseclass);
		}
		return permissionGroupToBaseclass;
	}

	public void validate(PermissionGroupToBaseclassCreate permissionGroupToBaseclassCreate, SecurityContextBase securityContext) {
		baselinkService.validate(permissionGroupToBaseclassCreate,securityContext);
	}

	public void validate(PermissionGroupToBaseclassFilter permissionGroupToBaseclassFilter, SecurityContextBase securityContext) {
		baselinkService.validate(permissionGroupToBaseclassFilter,securityContext);
	}

	public <T extends Baseclass> T getByIdOrNull(String id,Class<T> c, SecurityContextBase securityContext) {
		return permissionGroupToBaseclassRepository.getByIdOrNull(id,c,securityContext);
	}

	public PaginationResponse<PermissionGroupToBaseclass> getAllPermissionGroupToBaseclass(PermissionGroupToBaseclassFilter permissionGroupToBaseclassFilter, SecurityContextBase securityContext) {
		List<PermissionGroupToBaseclass> list= listAllPermissionGroupToBaseclass(permissionGroupToBaseclassFilter, securityContext);
		long count=permissionGroupToBaseclassRepository.countAllPermissionGroupToBaseclasss(permissionGroupToBaseclassFilter,securityContext);
		return new PaginationResponse<>(list,permissionGroupToBaseclassFilter,count);
	}

	public List<PermissionGroupToBaseclass> listAllPermissionGroupToBaseclass(PermissionGroupToBaseclassFilter permissionGroupToBaseclassFilter, SecurityContextBase securityContext) {
		return permissionGroupToBaseclassRepository.listAllPermissionGroupToBaseclasss(permissionGroupToBaseclassFilter, securityContext);
	}
}
