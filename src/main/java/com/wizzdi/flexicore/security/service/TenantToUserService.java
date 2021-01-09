package com.wizzdi.flexicore.security.service;

import com.flexicore.model.Baseclass;
import com.flexicore.model.TenantToUser;
import com.flexicore.security.SecurityContextBase;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.security.data.TenantToUserRepository;
import com.wizzdi.flexicore.security.request.TenantToUserCreate;
import com.wizzdi.flexicore.security.request.TenantToUserFilter;
import com.wizzdi.flexicore.security.request.TenantToUserUpdate;
import com.wizzdi.flexicore.security.response.PaginationResponse;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Extension
@Component
public class TenantToUserService implements Plugin {

	@Autowired
	private BaselinkService baselinkService;
	@Autowired
	private TenantToUserRepository tenantToUserRepository;


	public TenantToUser createTenantToUser(TenantToUserCreate tenantToUserCreate, SecurityContextBase securityContext){
		TenantToUser tenantToUser= createTenantToUserNoMerge(tenantToUserCreate,securityContext);
		tenantToUserRepository.merge(tenantToUser);
		return tenantToUser;
	}

	public void merge(Object o){
		tenantToUserRepository.merge(o);
	}
	public void massMerge(List<Object> list){
		tenantToUserRepository.massMerge(list);
	}
	public <T extends Baseclass> List<T> listByIds(Class<T> c,Set<String> ids,  SecurityContextBase securityContext) {
		return tenantToUserRepository.listByIds(c, ids, securityContext);
	}

	public TenantToUser createTenantToUserNoMerge(TenantToUserCreate tenantToUserCreate, SecurityContextBase securityContext){
		TenantToUser tenantToUser=new TenantToUser(tenantToUserCreate.getName(),securityContext);
		updateTenantToUserNoMerge(tenantToUserCreate,tenantToUser);
		tenantToUserRepository.merge(tenantToUser);
		return tenantToUser;
	}

	public boolean updateTenantToUserNoMerge(TenantToUserCreate tenantToUserCreate, TenantToUser tenantToUser) {
		return baselinkService.updateBaselinkNoMerge(tenantToUserCreate,tenantToUser);
	}

	public TenantToUser updateTenantToUser(TenantToUserUpdate tenantToUserUpdate, SecurityContextBase securityContext){
		TenantToUser tenantToUser=tenantToUserUpdate.getTenantToUser();
		if(updateTenantToUserNoMerge(tenantToUserUpdate,tenantToUser)){
			tenantToUserRepository.merge(tenantToUser);
		}
		return tenantToUser;
	}

	public void validate(TenantToUserCreate tenantToUserCreate, SecurityContextBase securityContext) {
		baselinkService.validate(tenantToUserCreate,securityContext);
	}

	public void validate(TenantToUserFilter tenantToUserFilter, SecurityContextBase securityContext) {
		baselinkService.validate(tenantToUserFilter,securityContext);
	}

	public <T extends Baseclass> T getByIdOrNull(String id,Class<T> c, SecurityContextBase securityContext) {
		return tenantToUserRepository.getByIdOrNull(id,c,securityContext);
	}

	public PaginationResponse<TenantToUser> getAllTenantToUsers(TenantToUserFilter tenantToUserFilter, SecurityContextBase securityContext) {
		List<TenantToUser> list= listAllTenantToUsers(tenantToUserFilter, securityContext);
		long count=tenantToUserRepository.countAllTenantToUsers(tenantToUserFilter,securityContext);
		return new PaginationResponse<>(list,tenantToUserFilter,count);
	}

	public List<TenantToUser> listAllTenantToUsers(TenantToUserFilter tenantToUserFilter, SecurityContextBase securityContext) {
		return tenantToUserRepository.listAllTenantToUsers(tenantToUserFilter, securityContext);
	}
}
