package com.wizzdi.flexicore.security.service;

import com.flexicore.model.SecurityTenant;
import com.flexicore.model.Baseclass;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.security.data.SecurityTenantRepository;
import com.flexicore.security.SecurityContextBase;
import com.wizzdi.flexicore.security.request.SecurityTenantCreate;
import com.wizzdi.flexicore.security.request.SecurityTenantFilter;
import com.wizzdi.flexicore.security.request.SecurityTenantUpdate;
import com.wizzdi.flexicore.security.response.PaginationResponse;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Extension
@Component
public class SecurityTenantService implements Plugin {

	@Autowired
	private SecurityEntityService securityEntityService;
	@Autowired
	private SecurityTenantRepository tenantRepository;


	public SecurityTenant createTenant(SecurityTenantCreate tenantCreate, SecurityContextBase securityContext){
		SecurityTenant tenant= createTenantNoMerge(tenantCreate,securityContext);
		tenantRepository.merge(tenant);
		return tenant;
	}

	public void merge(Object o){
		tenantRepository.merge(o);
	}
	public void massMerge(List<Object> list){
		tenantRepository.massMerge(list);
	}

	public <T extends Baseclass> List<T> listByIds(Class<T> c,Set<String> ids,  SecurityContextBase securityContext) {
		return tenantRepository.listByIds(c, ids, securityContext);
	}

	public SecurityTenant createTenantNoMerge(SecurityTenantCreate tenantCreate, SecurityContextBase securityContext){
		SecurityTenant tenant=new SecurityTenant(tenantCreate.getName(),securityContext);
		updateTenantNoMerge(tenantCreate,tenant);
		tenantRepository.merge(tenant);
		return tenant;
	}

	public boolean updateTenantNoMerge(SecurityTenantCreate tenantCreate, SecurityTenant tenant) {
		return securityEntityService.updateNoMerge(tenantCreate,tenant);
	}

	public SecurityTenant updateTenant(SecurityTenantUpdate tenantUpdate, SecurityContextBase securityContext){
		SecurityTenant tenant=tenantUpdate.getTenantToUpdate();
		if(updateTenantNoMerge(tenantUpdate,tenant)){
			tenantRepository.merge(tenant);
		}
		return tenant;
	}

	public void validate(SecurityTenantCreate tenantCreate, SecurityContextBase securityContext) {
		securityEntityService.validate(tenantCreate,securityContext);
	}

	public void validate(SecurityTenantFilter tenantFilter, SecurityContextBase securityContext) {
		securityEntityService.validate(tenantFilter,securityContext);
	}

	public <T extends Baseclass> T getByIdOrNull(String id,Class<T> c, SecurityContextBase securityContext) {
		return tenantRepository.getByIdOrNull(id,c,securityContext);
	}

	public PaginationResponse<SecurityTenant> getAllTenants(SecurityTenantFilter tenantFilter, SecurityContextBase securityContext) {
		List<SecurityTenant> list= listAllTenants(tenantFilter, securityContext);
		long count=tenantRepository.countAllTenants(tenantFilter,securityContext);
		return new PaginationResponse<>(list,tenantFilter,count);
	}

	public List<SecurityTenant> listAllTenants(SecurityTenantFilter tenantFilter, SecurityContextBase securityContext) {
		return tenantRepository.listAllTenants(tenantFilter, securityContext);
	}

	public <T extends Baseclass> List<T> findByIds(Class<T> c, Set<String> requested) {
		return tenantRepository.findByIds(c, requested);
	}

	public <T> T findByIdOrNull(Class<T> type, String id) {
		return tenantRepository.findByIdOrNull(type, id);
	}
}
