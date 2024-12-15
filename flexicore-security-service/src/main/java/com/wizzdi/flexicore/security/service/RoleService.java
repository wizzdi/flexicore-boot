package com.wizzdi.flexicore.security.service;

import com.flexicore.model.Baseclass;
import com.flexicore.model.Role;
import com.flexicore.model.SecurityTenant;
import com.wizzdi.segmantix.model.SecurityContext;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.security.data.RoleRepository;
import com.wizzdi.flexicore.security.request.RoleCreate;
import com.wizzdi.flexicore.security.request.RoleFilter;
import com.wizzdi.flexicore.security.request.RoleUpdate;
import com.wizzdi.flexicore.security.response.PaginationResponse;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

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

	public <T> T merge(T o){
		return roleRepository.merge(o);
	}
	public void massMerge(List<Object> list){
		roleRepository.massMerge(list);
	}

	public <T extends Baseclass> List<T> listByIds(Class<T> c, Set<String> ids, SecurityContext securityContext) {
		return roleRepository.listByIds(c, ids, securityContext);
	}


	public Role createRoleNoMerge(RoleCreate roleCreate, SecurityContext securityContext){
		Role role=new Role();
		role.setId(UUID.randomUUID().toString());
		updateRoleNoMerge(roleCreate,role);
		BaseclassService.createSecurityObjectNoMerge(role,securityContext);
		return role;
	}

	public boolean updateRoleNoMerge(RoleCreate roleCreate, Role role) {
		boolean update = securityEntityService.updateNoMerge(roleCreate, role);
		SecurityTenant currentTenant=Optional.of(role).map(f->f.getTenant()).orElse(null);
		if(roleCreate.getTenant()!=null&&(currentTenant==null||!roleCreate.getTenant().getId().equals(currentTenant.getId()))){
			role.setTenant(roleCreate.getTenant());
			update=true;
		}
		return update;
	}

	public Role updateRole(RoleUpdate roleUpdate, SecurityContext securityContext){
		Role role=roleUpdate.getRole();
		if(updateRoleNoMerge(roleUpdate,role)){
			roleRepository.merge(role);
		}
		return role;
	}



	public <T extends Baseclass> T getByIdOrNull(String id, Class<T> c, SecurityContext securityContext) {
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

	public <T extends Baseclass> List<T> findByIds(Class<T> c, Set<String> requested) {
		return roleRepository.findByIds(c, requested);
	}

	public <T> T findByIdOrNull(Class<T> type, String id) {
		return roleRepository.findByIdOrNull(type, id);
	}
}
