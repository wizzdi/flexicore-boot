package com.wizzdi.flexicore.security.service;

import com.flexicore.model.Tenant;
import com.flexicore.model.Baseclass;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.security.data.TenantRepository;
import com.flexicore.security.SecurityContext;
import com.wizzdi.flexicore.security.request.TenantCreate;
import com.wizzdi.flexicore.security.request.TenantFilter;
import com.wizzdi.flexicore.security.request.TenantUpdate;
import com.wizzdi.flexicore.security.response.PaginationResponse;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Extension
@Component
public class TenantService implements Plugin {

	@Autowired
	private SecurityEntityService securityEntityService;
	@Autowired
	private TenantRepository tenantRepository;


	public Tenant createTenant(TenantCreate tenantCreate, SecurityContext securityContext){
		Tenant tenant= createTenantNoMerge(tenantCreate,securityContext);
		tenantRepository.merge(tenant);
		return tenant;
	}


	public Tenant createTenantNoMerge(TenantCreate tenantCreate, SecurityContext securityContext){
		Tenant tenant=new Tenant(tenantCreate.getName(),securityContext);
		updateTenantNoMerge(tenantCreate,tenant);
		tenantRepository.merge(tenant);
		return tenant;
	}

	public boolean updateTenantNoMerge(TenantCreate tenantCreate, Tenant tenant) {
		return securityEntityService.updateNoMerge(tenantCreate,tenant);
	}

	public Tenant updateTenant(TenantUpdate tenantUpdate, SecurityContext securityContext){
		Tenant tenant=tenantUpdate.getTenant();
		if(updateTenantNoMerge(tenantUpdate,tenant)){
			tenantRepository.merge(tenant);
		}
		return tenant;
	}

	public void validate(TenantCreate tenantCreate, SecurityContext securityContext) {
		securityEntityService.validate(tenantCreate,securityContext);
	}

	public void validate(TenantFilter tenantFilter, SecurityContext securityContext) {
		securityEntityService.validate(tenantFilter,securityContext);
	}

	public <T extends Baseclass> T getByIdOrNull(String id,Class<T> c, SecurityContext securityContext) {
		return tenantRepository.getByIdOrNull(id,c,securityContext);
	}

	public PaginationResponse<Tenant> getAllTenants(TenantFilter tenantFilter, SecurityContext securityContext) {
		List<Tenant> list= listAllTenants(tenantFilter, securityContext);
		long count=tenantRepository.countAllTenants(tenantFilter,securityContext);
		return new PaginationResponse<>(list,tenantFilter,count);
	}

	public List<Tenant> listAllTenants(TenantFilter tenantFilter, SecurityContext securityContext) {
		return tenantRepository.listAllTenants(tenantFilter, securityContext);
	}
}
