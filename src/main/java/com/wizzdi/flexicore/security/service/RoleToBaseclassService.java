package com.wizzdi.flexicore.security.service;

import com.flexicore.model.RoleToBaseclass;
import com.flexicore.model.Baseclass;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.security.data.RoleToBaseclassRepository;
import com.flexicore.security.SecurityContext;
import com.wizzdi.flexicore.security.request.RoleToBaseclassCreate;
import com.wizzdi.flexicore.security.request.RoleToBaseclassFilter;
import com.wizzdi.flexicore.security.request.RoleToBaseclassUpdate;
import com.wizzdi.flexicore.security.response.PaginationResponse;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Extension
@Component
public class RoleToBaseclassService implements Plugin {

	@Autowired
	private SecurityLinkService securityLinkService;
	@Autowired
	private RoleToBaseclassRepository roleToBaseclassRepository;


	public RoleToBaseclass createRoleToBaseclass(RoleToBaseclassCreate roleToBaseclassCreate, SecurityContext securityContext){
		RoleToBaseclass roleToBaseclass= createRoleToBaseclassNoMerge(roleToBaseclassCreate,securityContext);
		roleToBaseclassRepository.merge(roleToBaseclass);
		return roleToBaseclass;
	}


	public RoleToBaseclass createRoleToBaseclassNoMerge(RoleToBaseclassCreate roleToBaseclassCreate, SecurityContext securityContext){
		RoleToBaseclass roleToBaseclass=new RoleToBaseclass(roleToBaseclassCreate.getName(),securityContext);
		updateRoleToBaseclassNoMerge(roleToBaseclassCreate,roleToBaseclass);
		roleToBaseclassRepository.merge(roleToBaseclass);
		return roleToBaseclass;
	}

	public boolean updateRoleToBaseclassNoMerge(RoleToBaseclassCreate roleToBaseclassCreate, RoleToBaseclass roleToBaseclass) {
		return securityLinkService.updateSecurityLinkNoMerge(roleToBaseclassCreate,roleToBaseclass);
	}

	public RoleToBaseclass updateRoleToBaseclass(RoleToBaseclassUpdate roleToBaseclassUpdate, SecurityContext securityContext){
		RoleToBaseclass roleToBaseclass=roleToBaseclassUpdate.getRoleToBaseclass();
		if(updateRoleToBaseclassNoMerge(roleToBaseclassUpdate,roleToBaseclass)){
			roleToBaseclassRepository.merge(roleToBaseclass);
		}
		return roleToBaseclass;
	}

	public void validate(RoleToBaseclassCreate roleToBaseclassCreate, SecurityContext securityContext) {
		securityLinkService.validate(roleToBaseclassCreate,securityContext);
	}

	public void validate(RoleToBaseclassFilter roleToBaseclassFilter, SecurityContext securityContext) {
		securityLinkService.validate(roleToBaseclassFilter,securityContext);
	}

	public <T extends Baseclass> T getByIdOrNull(String id,Class<T> c, SecurityContext securityContext) {
		return roleToBaseclassRepository.getByIdOrNull(id,c,securityContext);
	}

	public PaginationResponse<RoleToBaseclass> getAllRoleToBaseclass(RoleToBaseclassFilter roleToBaseclassFilter, SecurityContext securityContext) {
		List<RoleToBaseclass> list= listAllRoleToBaseclasss(roleToBaseclassFilter, securityContext);
		long count=roleToBaseclassRepository.countAllRoleToBaseclasss(roleToBaseclassFilter,securityContext);
		return new PaginationResponse<>(list,roleToBaseclassFilter,count);
	}

	public List<RoleToBaseclass> listAllRoleToBaseclasss(RoleToBaseclassFilter roleToBaseclassFilter, SecurityContext securityContext) {
		return roleToBaseclassRepository.listAllRoleToBaseclasss(roleToBaseclassFilter, securityContext);
	}
}
