package com.wizzdi.flexicore.security.service;

import com.flexicore.model.Baseclass;
import com.flexicore.model.Role;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.security.data.RoleRepository;
import com.flexicore.security.SecurityContext;
import com.wizzdi.flexicore.security.request.RoleCreate;
import com.wizzdi.flexicore.security.request.RoleFilter;
import com.wizzdi.flexicore.security.request.RoleUpdate;
import com.wizzdi.flexicore.security.response.PaginationResponse;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Extension
@Component
public class RoleService implements Plugin {

	@Autowired
	private SecurityEntityService securityEntityService;
	@Autowired
	private RoleRepository roleRepository;


	public Role createRole(RoleCreate roleCreate, SecurityContext securityContext){
		Role role= createRoleNoMerge(roleCreate,securityContext);
		roleRepository.merge(role);
		return role;
	}


	public Role createRoleNoMerge(RoleCreate roleCreate, SecurityContext securityContext){
		Role role=new Role(roleCreate.getName(),securityContext);
		updateRoleNoMerge(roleCreate,role);
		roleRepository.merge(role);
		return role;
	}

	public boolean updateRoleNoMerge(RoleCreate roleCreate, Role role) {
		return securityEntityService.updateNoMerge(roleCreate,role);
	}

	public Role updateRole(RoleUpdate roleUpdate, SecurityContext securityContext){
		Role role=roleUpdate.getRole();
		if(updateRoleNoMerge(roleUpdate,role)){
			roleRepository.merge(role);
		}
		return role;
	}

	public void validate(RoleCreate roleCreate, SecurityContext securityContext) {
		securityEntityService.validate(roleCreate,securityContext);
	}

	public void validate(RoleFilter roleFilter, SecurityContext securityContext) {
		securityEntityService.validate(roleFilter,securityContext);
	}

	public <T extends Baseclass> T getByIdOrNull(String id,Class<T> c, SecurityContext securityContext) {
		return roleRepository.getByIdOrNull(id,c,securityContext);
	}

	public PaginationResponse<Role> getAllRoles(RoleFilter roleFilter, SecurityContext securityContext) {
		List<Role> list= listAllRoles(roleFilter, securityContext);
		long count=roleRepository.countAllRoles(roleFilter,securityContext);
		return new PaginationResponse<>(list,roleFilter,count);
	}

	public List<Role> listAllRoles(RoleFilter roleFilter, SecurityContext securityContext) {
		return roleRepository.listAllRoles(roleFilter, securityContext);
	}
}
