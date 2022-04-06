package com.wizzdi.flexicore.security.service;

import com.flexicore.model.RoleToBaseclass;
import com.flexicore.model.Baseclass;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.security.data.RoleToBaseclassRepository;
import com.flexicore.security.SecurityContextBase;
import com.wizzdi.flexicore.security.request.RoleToBaseclassCreate;
import com.wizzdi.flexicore.security.request.RoleToBaseclassFilter;
import com.wizzdi.flexicore.security.request.RoleToBaseclassUpdate;
import com.wizzdi.flexicore.security.response.PaginationResponse;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Extension
@Component
public class RoleToBaseclassService implements Plugin {

	@Autowired
	private SecurityLinkService securityLinkService;
	@Autowired
	private RoleToBaseclassRepository roleToBaseclassRepository;


	public RoleToBaseclass createRoleToBaseclass(RoleToBaseclassCreate roleToBaseclassCreate, SecurityContextBase securityContext){
		RoleToBaseclass roleToBaseclass= createRoleToBaseclassNoMerge(roleToBaseclassCreate,securityContext);
		roleToBaseclassRepository.merge(roleToBaseclass);
		return roleToBaseclass;
	}
	public void merge(Object o){
		roleToBaseclassRepository.merge(o);
	}
	public void massMerge(List<Object> list){
		roleToBaseclassRepository.massMerge(list);
	}

	public <T extends Baseclass> List<T> listByIds(Class<T> c,Set<String> ids,  SecurityContextBase securityContext) {
		return roleToBaseclassRepository.listByIds(c, ids, securityContext);
	}


	public RoleToBaseclass createRoleToBaseclassNoMerge(RoleToBaseclassCreate roleToBaseclassCreate, SecurityContextBase securityContext){
		RoleToBaseclass roleToBaseclass=new RoleToBaseclass(roleToBaseclassCreate.getName(),securityContext);
		updateRoleToBaseclassNoMerge(roleToBaseclassCreate,roleToBaseclass);
		roleToBaseclassRepository.merge(roleToBaseclass);
		return roleToBaseclass;
	}

	public boolean updateRoleToBaseclassNoMerge(RoleToBaseclassCreate roleToBaseclassCreate, RoleToBaseclass roleToBaseclass) {
		boolean update = securityLinkService.updateSecurityLinkNoMerge(roleToBaseclassCreate, roleToBaseclass);
		if(roleToBaseclassCreate.getBaseclass()!=null&&(roleToBaseclass.getRightside()==null||!roleToBaseclassCreate.getBaseclass().getId().equals(roleToBaseclass.getRightside().getId()))){
			roleToBaseclass.setRightside(roleToBaseclassCreate.getBaseclass());
			update=true;
		}
		if(roleToBaseclassCreate.getRole()!=null&&(roleToBaseclass.getLeftside()==null||!roleToBaseclassCreate.getRole().getId().equals(roleToBaseclass.getLeftside().getId()))){
			roleToBaseclass.setLeftside(roleToBaseclassCreate.getRole());
			update=true;
		}
		return update;
	}

	public RoleToBaseclass updateRoleToBaseclass(RoleToBaseclassUpdate roleToBaseclassUpdate, SecurityContextBase securityContext){
		RoleToBaseclass roleToBaseclass=roleToBaseclassUpdate.getRoleToBaseclass();
		if(updateRoleToBaseclassNoMerge(roleToBaseclassUpdate,roleToBaseclass)){
			roleToBaseclassRepository.merge(roleToBaseclass);
		}
		return roleToBaseclass;
	}

	@Deprecated
	public void validate(RoleToBaseclassCreate roleToBaseclassCreate, SecurityContextBase securityContext) {
		securityLinkService.validate(roleToBaseclassCreate,securityContext);
	}

	@Deprecated
	public void validate(RoleToBaseclassFilter roleToBaseclassFilter, SecurityContextBase securityContext) {
		securityLinkService.validate(roleToBaseclassFilter,securityContext);
	}

	public <T extends Baseclass> T getByIdOrNull(String id,Class<T> c, SecurityContextBase securityContext) {
		return roleToBaseclassRepository.getByIdOrNull(id,c,securityContext);
	}

	public PaginationResponse<RoleToBaseclass> getAllRoleToBaseclass(RoleToBaseclassFilter roleToBaseclassFilter, SecurityContextBase securityContext) {
		List<RoleToBaseclass> list= listAllRoleToBaseclasss(roleToBaseclassFilter, securityContext);
		long count=roleToBaseclassRepository.countAllRoleToBaseclasss(roleToBaseclassFilter,securityContext);
		return new PaginationResponse<>(list,roleToBaseclassFilter,count);
	}

	public List<RoleToBaseclass> listAllRoleToBaseclasss(RoleToBaseclassFilter roleToBaseclassFilter, SecurityContextBase securityContext) {
		return roleToBaseclassRepository.listAllRoleToBaseclasss(roleToBaseclassFilter, securityContext);
	}
}
