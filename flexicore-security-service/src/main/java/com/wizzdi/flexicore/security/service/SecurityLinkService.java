package com.wizzdi.flexicore.security.service;

import com.flexicore.model.Baseclass;
import com.flexicore.model.SecurityLink;
import com.flexicore.security.SecurityContextBase;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.security.data.SecurityLinkRepository;
import com.wizzdi.flexicore.security.request.*;
import com.wizzdi.flexicore.security.response.PaginationResponse;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Extension
@Component
public class SecurityLinkService implements Plugin {

	@Autowired
	private BasicService basicService;
	@Autowired
	private SecurityLinkRepository securityLinkRepository;
	@Autowired
	private SecurityTenantService securityTenantService;
	@Autowired
	private RoleService roleService;



	public void merge(Object o){
		securityLinkRepository.merge(o);
	}
	public void massMerge(List<Object> list){
		securityLinkRepository.massMerge(list);
	}

	public <T extends Baseclass> List<T> listByIds(Class<T> c,Set<String> ids,  SecurityContextBase securityContext) {
		return securityLinkRepository.listByIds(c, ids, securityContext);
	}



	public boolean updateSecurityLinkNoMerge(SecurityLinkCreate securityLinkCreate, SecurityLink securityLink) {
		boolean updated = basicService.updateBasicNoMerge(securityLinkCreate, securityLink);
		if(securityLinkCreate.getAccess()!=null&&!securityLinkCreate.getAccess().equals(securityLink.getAccess())){
			securityLink.setAccess(securityLinkCreate.getAccess());
			updated=true;
		}
		if(securityLinkCreate.getOperation()!=null&&(securityLink.getOperation()==null||!securityLinkCreate.getOperation().getId().equals(securityLink.getOperation().getId()))){
			securityLink.setOperation(securityLinkCreate.getOperation());
			updated=true;
		}
		if(securityLinkCreate.getOperationGroup()!=null&&(securityLink.getOperationGroup()==null||!securityLinkCreate.getOperationGroup().getId().equals(securityLink.getOperationGroup().getId()))){
			securityLink.setOperationGroup(securityLinkCreate.getOperationGroup());
			updated=true;
		}
		if(securityLinkCreate.getBaseclass()!=null&&(securityLink.getBaseclass()==null || !securityLinkCreate.getBaseclass().getId().equals(securityLink.getBaseclass().getId()))){
			securityLink.setBaseclass(securityLinkCreate.getBaseclass());
			updated=true;
		}

		if(securityLinkCreate.getPermissionGroup()!=null&&(securityLink.getPermissionGroup()==null || !securityLinkCreate.getPermissionGroup().getId().equals(securityLink.getPermissionGroup().getId()))){
			securityLink.setPermissionGroup(securityLinkCreate.getPermissionGroup());
			updated=true;
		}

		if(securityLinkCreate.getClazz()!=null&&(securityLink.getClazz()==null || !securityLinkCreate.getClazz().getId().equals(securityLink.getClazz().getId()))){
			securityLink.setClazz(securityLinkCreate.getClazz());
			updated=true;
		}
		return updated;
	}

	public SecurityLink updateSecurityLink(SecurityLinkUpdate securityLinkUpdate, SecurityContextBase securityContext){
		SecurityLink securityLink=securityLinkUpdate.getSecurityLink();
		if(updateSecurityLinkNoMerge(securityLinkUpdate,securityLink)){
			securityLinkRepository.merge(securityLink);
		}
		return securityLink;
	}


	public <T extends Baseclass> T getByIdOrNull(String id,Class<T> c, SecurityContextBase securityContext) {
		return securityLinkRepository.getByIdOrNull(id,c,securityContext);
	}

	public PaginationResponse<SecurityLink> getAllSecurityLinks(SecurityLinkFilter securityLinkFilter, SecurityContextBase securityContext) {
		List<SecurityLink> list= listAllSecurityLinks(securityLinkFilter, securityContext);
		long count=securityLinkRepository.countAllSecurityLinks(securityLinkFilter,securityContext);
		return new PaginationResponse<>(list,securityLinkFilter,count);
	}

	public List<SecurityLink> listAllSecurityLinks(SecurityLinkFilter securityLinkFilter, SecurityContextBase securityContext) {
		return securityLinkRepository.listAllSecurityLinks(securityLinkFilter, securityContext);
	}

	public void setRelevant(SecurityLinkFilter securityLinkFilter, SecurityContextBase securityContext) {
		if(securityLinkFilter.getRelevantUsers()!=null&&!securityLinkFilter.getRelevantUsers().isEmpty()){
			securityLinkFilter.setRelevantRoles(roleService.listAllRoles(new RoleFilter().setUsers(securityLinkFilter.getRelevantUsers()),securityContext));
			securityLinkFilter.setRelevantTenants(securityTenantService.listAllTenants(new SecurityTenantFilter().setUsers(securityLinkFilter.getRelevantUsers()),securityContext));
		}
	}
}
