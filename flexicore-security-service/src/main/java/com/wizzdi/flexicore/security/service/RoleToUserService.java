package com.wizzdi.flexicore.security.service;

import com.flexicore.model.Baseclass;
import com.flexicore.model.RoleToUser;
import com.wizzdi.flexicore.security.configuration.SecurityContext;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.security.data.RoleToUserRepository;
import com.wizzdi.flexicore.security.request.RoleToUserCreate;
import com.wizzdi.flexicore.security.request.RoleToUserFilter;
import com.wizzdi.flexicore.security.request.RoleToUserUpdate;
import com.wizzdi.flexicore.security.response.PaginationResponse;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Extension
@Component
public class RoleToUserService implements Plugin {

	@Autowired
	private BasicService basicService;
	@Autowired
	private RoleToUserRepository roleToUserRepository;


	public RoleToUser createRoleToUser(RoleToUserCreate roleToUserCreate, SecurityContext securityContext){
		RoleToUser roleToUser= createRoleToUserNoMerge(roleToUserCreate,securityContext);
		roleToUserRepository.merge(roleToUser);
		return roleToUser;
	}
	public <T> T merge(T o){
		return roleToUserRepository.merge(o);
	}
	public void massMerge(List<Object> list){
		roleToUserRepository.massMerge(list);
	}

	public <T extends Baseclass> List<T> listByIds(Class<T> c, Set<String> ids, SecurityContext securityContext) {
		return roleToUserRepository.listByIds(c, ids, securityContext);
	}


	public RoleToUser createRoleToUserNoMerge(RoleToUserCreate roleToUserCreate, SecurityContext securityContext){
		RoleToUser roleToUser=new RoleToUser();
		roleToUser.setId(UUID.randomUUID().toString());
		updateRoleToUserNoMerge(roleToUserCreate,roleToUser);
		BaseclassService.createSecurityObjectNoMerge(roleToUser,securityContext);
		return roleToUser;
	}

	public boolean updateRoleToUserNoMerge(RoleToUserCreate roleToUserCreate, RoleToUser roleToUser) {
		boolean update = basicService.updateBasicNoMerge(roleToUserCreate, roleToUser);
		if(roleToUserCreate.getSecurityUser()!=null&&(roleToUser.getUser()==null||!roleToUserCreate.getSecurityUser().getId().equals(roleToUser.getUser().getId()))){
			roleToUser.setUser(roleToUserCreate.getSecurityUser());
			update=true;
		}
		if(roleToUserCreate.getRole()!=null&&(roleToUser.getRole()==null||!roleToUserCreate.getRole().getId().equals(roleToUser.getRole().getId()))){
			roleToUser.setRole(roleToUserCreate.getRole());
			update=true;
		}
		return update;
	}

	public RoleToUser updateRoleToUser(RoleToUserUpdate roleToUserUpdate, SecurityContext securityContext){
		RoleToUser roleToUser=roleToUserUpdate.getRoleToUser();
		if(updateRoleToUserNoMerge(roleToUserUpdate,roleToUser)){
			roleToUserRepository.merge(roleToUser);
		}
		return roleToUser;
	}



	public <T extends Baseclass> T getByIdOrNull(String id, Class<T> c, SecurityContext securityContext) {
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

	public <T extends Baseclass> List<T> findByIds(Class<T> c, Set<String> requested) {
		return roleToUserRepository.findByIds(c, requested);
	}

	public <T> T findByIdOrNull(Class<T> type, String id) {
		return roleToUserRepository.findByIdOrNull(type, id);
	}
}
