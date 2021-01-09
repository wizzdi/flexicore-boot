package com.wizzdi.flexicore.security.service;

import com.flexicore.model.Baseclass;
import com.flexicore.model.SecurityUser;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.security.data.SecurityUserRepository;
import com.flexicore.security.SecurityContextBase;
import com.wizzdi.flexicore.security.request.SecurityUserCreate;
import com.wizzdi.flexicore.security.request.SecurityUserFilter;
import com.wizzdi.flexicore.security.request.SecurityUserUpdate;
import com.wizzdi.flexicore.security.response.PaginationResponse;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Extension
@Component
public class SecurityUserService implements Plugin {

	@Autowired
	private SecurityEntityService securityEntityService;
	@Autowired
	private SecurityUserRepository securityUserRepository;


	public SecurityUser createSecurityUser(SecurityUserCreate securityUserCreate, SecurityContextBase securityContext){
		SecurityUser securityUser= createSecurityUserNoMerge(securityUserCreate,securityContext);
		securityUserRepository.merge(securityUser);
		return securityUser;
	}
	public void merge(Object o){
		securityUserRepository.merge(o);
	}
	public void massMerge(List<Object> list){
		securityUserRepository.massMerge(list);
	}

	public <T extends Baseclass> List<T> listByIds(Class<T> c,Set<String> ids,  SecurityContextBase securityContext) {
		return securityUserRepository.listByIds(c, ids, securityContext);
	}

	public SecurityUser createSecurityUserNoMerge(SecurityUserCreate securityUserCreate, SecurityContextBase securityContext){
		SecurityUser securityUser=new SecurityUser(securityUserCreate.getName(),securityContext);
		updateSecurityUserNoMerge(securityUserCreate,securityUser);
		securityUserRepository.merge(securityUser);
		return securityUser;
	}

	public boolean updateSecurityUserNoMerge(SecurityUserCreate securityUserCreate, SecurityUser securityUser) {
		return securityEntityService.updateNoMerge(securityUserCreate,securityUser);
	}

	public SecurityUser updateSecurityUser(SecurityUserUpdate securityUserUpdate, SecurityContextBase securityContext){
		SecurityUser securityUser=securityUserUpdate.getSecurityUser();
		if(updateSecurityUserNoMerge(securityUserUpdate,securityUser)){
			securityUserRepository.merge(securityUser);
		}
		return securityUser;
	}

	public void validate(SecurityUserCreate securityUserCreate, SecurityContextBase securityContext) {
		securityEntityService.validate(securityUserCreate,securityContext);
	}

	public void validate(SecurityUserFilter securityUserFilter, SecurityContextBase securityContext) {
		securityEntityService.validate(securityUserFilter,securityContext);
	}

	public <T extends Baseclass> T getByIdOrNull(String id,Class<T> c, SecurityContextBase securityContext) {
		return securityUserRepository.getByIdOrNull(id,c,securityContext);
	}

	public PaginationResponse<SecurityUser> getAllSecurityUsers(SecurityUserFilter securityUserFilter, SecurityContextBase securityContext) {
		List<SecurityUser> list= listAllSecurityUsers(securityUserFilter, securityContext);
		long count=securityUserRepository.countAllSecurityUsers(securityUserFilter,securityContext);
		return new PaginationResponse<>(list,securityUserFilter,count);
	}

	public List<SecurityUser> listAllSecurityUsers(SecurityUserFilter securityUserFilter, SecurityContextBase securityContext) {
		return securityUserRepository.listAllSecurityUsers(securityUserFilter, securityContext);
	}
}
