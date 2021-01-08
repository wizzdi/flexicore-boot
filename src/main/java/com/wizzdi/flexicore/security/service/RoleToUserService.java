package com.wizzdi.flexicore.security.service;

import com.flexicore.model.Baseclass;
import com.flexicore.model.RoleToUser;
import com.flexicore.security.SecurityContext;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.security.data.RoleToUserRepository;
import com.wizzdi.flexicore.security.request.RoleToUserCreate;
import com.wizzdi.flexicore.security.request.RoleToUserFilter;
import com.wizzdi.flexicore.security.request.RoleToUserUpdate;
import com.wizzdi.flexicore.security.response.PaginationResponse;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Extension
@Component
public class RoleToUserService implements Plugin {

	@Autowired
	private BaselinkService baselinkService;
	@Autowired
	private RoleToUserRepository roleToUserRepository;


	public RoleToUser createRoleToUser(RoleToUserCreate roleToUserCreate, SecurityContext securityContext){
		RoleToUser roleToUser= createRoleToUserNoMerge(roleToUserCreate,securityContext);
		roleToUserRepository.merge(roleToUser);
		return roleToUser;
	}


	public RoleToUser createRoleToUserNoMerge(RoleToUserCreate roleToUserCreate, SecurityContext securityContext){
		RoleToUser roleToUser=new RoleToUser(roleToUserCreate.getName(),securityContext);
		updateRoleToUserNoMerge(roleToUserCreate,roleToUser);
		roleToUserRepository.merge(roleToUser);
		return roleToUser;
	}

	public boolean updateRoleToUserNoMerge(RoleToUserCreate roleToUserCreate, RoleToUser roleToUser) {
		return baselinkService.updateBaselinkNoMerge(roleToUserCreate,roleToUser);
	}

	public RoleToUser updateRoleToUser(RoleToUserUpdate roleToUserUpdate, SecurityContext securityContext){
		RoleToUser roleToUser=roleToUserUpdate.getRoleToUser();
		if(updateRoleToUserNoMerge(roleToUserUpdate,roleToUser)){
			roleToUserRepository.merge(roleToUser);
		}
		return roleToUser;
	}

	public void validate(RoleToUserCreate roleToUserCreate, SecurityContext securityContext) {
		baselinkService.validate(roleToUserCreate,securityContext);
	}

	public void validate(RoleToUserFilter roleToUserFilter, SecurityContext securityContext) {
		baselinkService.validate(roleToUserFilter,securityContext);
	}

	public <T extends Baseclass> T getByIdOrNull(String id,Class<T> c, SecurityContext securityContext) {
		return roleToUserRepository.getByIdOrNull(id,c,securityContext);
	}

	public PaginationResponse<RoleToUser> getAllRoleToUsers(RoleToUserFilter roleToUserFilter, SecurityContext securityContext) {
		List<RoleToUser> list= listAllRoleToUsers(roleToUserFilter, securityContext);
		long count=roleToUserRepository.countAllRoleToUsers(roleToUserFilter,securityContext);
		return new PaginationResponse<>(list,roleToUserFilter,count);
	}

	public List<RoleToUser> listAllRoleToUsers(RoleToUserFilter roleToUserFilter, SecurityContext securityContext) {
		return roleToUserRepository.listAllRoleToUsers(roleToUserFilter, securityContext);
	}
}
