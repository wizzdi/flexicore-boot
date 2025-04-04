package com.wizzdi.flexicore.security.service;

import com.flexicore.model.Baseclass;
import com.flexicore.model.SecurityTenant;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.security.data.SecurityTenantRepository;
import com.wizzdi.flexicore.security.configuration.SecurityContext;
import com.wizzdi.flexicore.security.request.SecurityTenantCreate;
import com.wizzdi.flexicore.security.request.SecurityTenantFilter;
import com.wizzdi.flexicore.security.request.SecurityTenantUpdate;
import com.wizzdi.flexicore.security.response.PaginationResponse;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Extension
@Component
public class SecurityTenantService implements Plugin {

	@Autowired
	private SecurityEntityService securityEntityService;
	@Autowired
	private SecurityTenantRepository tenantRepository;


	public SecurityTenant createTenant(SecurityTenantCreate tenantCreate, SecurityContext securityContext){
		SecurityTenant tenant= createTenantNoMerge(tenantCreate,securityContext);
		tenantRepository.merge(tenant);
		return tenant;
	}

	public <T> T merge(T o){
		return tenantRepository.merge(o);
	}
	public void massMerge(List<Object> list){
		tenantRepository.massMerge(list);
	}

	public <T extends Baseclass> List<T> listByIds(Class<T> c, Set<String> ids, SecurityContext securityContext) {
		return tenantRepository.listByIds(c, ids, securityContext);
	}

	public SecurityTenant createTenantNoMerge(SecurityTenantCreate tenantCreate, SecurityContext securityContext){
		SecurityTenant tenant=new SecurityTenant();
		tenant.setId(UUID.randomUUID().toString());
		updateTenantNoMerge(tenantCreate,tenant);
		BaseclassService.createSecurityObjectNoMerge(tenant,securityContext);
		return tenant;
	}

	public boolean updateTenantNoMerge(SecurityTenantCreate tenantCreate, SecurityTenant tenant) {
		return securityEntityService.updateNoMerge(tenantCreate,tenant);
	}

	public SecurityTenant updateTenant(SecurityTenantUpdate tenantUpdate, SecurityContext securityContext){
		SecurityTenant tenant=tenantUpdate.getTenantToUpdate();
		if(updateTenantNoMerge(tenantUpdate,tenant)){
			tenantRepository.merge(tenant);
		}
		return tenant;
	}



	public <T extends Baseclass> T getByIdOrNull(String id, Class<T> c, SecurityContext securityContext) {
		return tenantRepository.getByIdOrNull(id,c,securityContext);
	}

	public PaginationResponse<SecurityTenant> getAllTenants(SecurityTenantFilter tenantFilter, SecurityContext securityContext) {
		List<SecurityTenant> list= listAllTenants(tenantFilter, securityContext);
		long count=tenantRepository.countAllTenants(tenantFilter,securityContext);
		return new PaginationResponse<>(list,tenantFilter,count);
	}

	public List<SecurityTenant> listAllTenants(SecurityTenantFilter tenantFilter, SecurityContext securityContext) {
		return tenantRepository.listAllTenants(tenantFilter, securityContext);
	}

	public <T extends Baseclass> List<T> findByIds(Class<T> c, Set<String> requested) {
		return tenantRepository.findByIds(c, requested);
	}

	public <T> T findByIdOrNull(Class<T> type, String id) {
		return tenantRepository.findByIdOrNull(type, id);
	}
}
