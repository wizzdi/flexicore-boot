package com.wizzdi.flexicore.security.service;

import com.flexicore.model.Baseclass;
import com.flexicore.model.Role;
import com.flexicore.model.RoleToUser;
import com.flexicore.model.SecurityUser;
import com.flexicore.security.SecurityContextBase;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.security.data.RoleToUserRepository;
import com.wizzdi.flexicore.security.request.RoleToUserCreate;
import com.wizzdi.flexicore.security.request.RoleToUserFilter;
import com.wizzdi.flexicore.security.request.RoleToUserUpdate;
import com.wizzdi.flexicore.security.response.PaginationResponse;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;

import java.util.*;
import java.util.stream.Collectors;

@Extension
@Component
public class RoleToUserService implements Plugin {

	@Autowired
	private BaselinkService baselinkService;
	@Autowired
	private RoleToUserRepository roleToUserRepository;


	public RoleToUser createRoleToUser(RoleToUserCreate roleToUserCreate, SecurityContextBase securityContext){
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

	public <T extends Baseclass> List<T> listByIds(Class<T> c,Set<String> ids,  SecurityContextBase securityContext) {
		return roleToUserRepository.listByIds(c, ids, securityContext);
	}


	public RoleToUser createRoleToUserNoMerge(RoleToUserCreate roleToUserCreate, SecurityContextBase securityContext){
		RoleToUser roleToUser=new RoleToUser(roleToUserCreate.getName(),securityContext);
		updateRoleToUserNoMerge(roleToUserCreate,roleToUser);
		roleToUserRepository.merge(roleToUser);
		return roleToUser;
	}

	public boolean updateRoleToUserNoMerge(RoleToUserCreate roleToUserCreate, RoleToUser roleToUser) {
		boolean update = baselinkService.updateBaselinkNoMerge(roleToUserCreate, roleToUser);
		if(roleToUserCreate.getSecurityUser()!=null&&(roleToUser.getRightside()==null||!roleToUserCreate.getSecurityUser().getId().equals(roleToUser.getRightside().getId()))){
			roleToUser.setRightside(roleToUserCreate.getSecurityUser());
			update=true;
		}
		if(roleToUserCreate.getRole()!=null&&(roleToUser.getLeftside()==null||!roleToUserCreate.getRole().getId().equals(roleToUser.getLeftside().getId()))){
			roleToUser.setRole(roleToUserCreate.getRole());
			update=true;
		}
		return update;
	}

	public RoleToUser updateRoleToUser(RoleToUserUpdate roleToUserUpdate, SecurityContextBase securityContext){
		RoleToUser roleToUser=roleToUserUpdate.getRoleToUser();
		if(updateRoleToUserNoMerge(roleToUserUpdate,roleToUser)){
			roleToUserRepository.merge(roleToUser);
		}
		return roleToUser;
	}

	public void validate(RoleToUserCreate roleToUserCreate, SecurityContextBase securityContext) {
		baselinkService.validate(roleToUserCreate,securityContext);
	}

	public void validate(RoleToUserFilter roleToUserFilter, SecurityContextBase securityContext) {
		baselinkService.validate(roleToUserFilter,securityContext);
		Set<String> roleIds=roleToUserFilter.getRolesIds();
		Map<String, Role> roleMap=roleIds.isEmpty()?new HashMap<>():listByIds(Role.class,roleIds,securityContext).stream().collect(Collectors.toMap(f->f.getId(),f->f));
		roleIds.removeAll(roleMap.keySet());
		if(!roleIds.isEmpty()){
			throw new HttpClientErrorException(HttpStatus.BAD_REQUEST,"no roles with ids "+roleIds);
		}
		roleToUserFilter.setRoles(new ArrayList<>(roleMap.values()));

		Set<String> usersIds=roleToUserFilter.getUsersIds();
		Map<String, SecurityUser> userMap=usersIds.isEmpty()?new HashMap<>():listByIds(SecurityUser.class,usersIds,securityContext).stream().collect(Collectors.toMap(f->f.getId(), f->f));
		usersIds.removeAll(userMap.keySet());
		if(!usersIds.isEmpty()){
			throw new HttpClientErrorException(HttpStatus.BAD_REQUEST,"no security users with ids "+usersIds);
		}
		roleToUserFilter.setSecurityUsers(new ArrayList<>(userMap.values()));
	}

	public <T extends Baseclass> T getByIdOrNull(String id,Class<T> c, SecurityContextBase securityContext) {
		return roleToUserRepository.getByIdOrNull(id,c,securityContext);
	}

	public PaginationResponse<RoleToUser> getAllRoleToUsers(RoleToUserFilter roleToUserFilter, SecurityContextBase securityContext) {
		List<RoleToUser> list= listAllRoleToUsers(roleToUserFilter, securityContext);
		long count=roleToUserRepository.countAllRoleToUsers(roleToUserFilter,securityContext);
		return new PaginationResponse<>(list,roleToUserFilter,count);
	}

	public List<RoleToUser> listAllRoleToUsers(RoleToUserFilter roleToUserFilter, SecurityContextBase securityContext) {
		return roleToUserRepository.listAllRoleToUsers(roleToUserFilter, securityContext);
	}

	public <T extends Baseclass> List<T> findByIds(Class<T> c, Set<String> requested) {
		return roleToUserRepository.findByIds(c, requested);
	}

	public <T> T findByIdOrNull(Class<T> type, String id) {
		return roleToUserRepository.findByIdOrNull(type, id);
	}
}
