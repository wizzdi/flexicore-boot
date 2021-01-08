package com.wizzdi.flexicore.security.service;

import com.flexicore.model.Baseclass;
import com.flexicore.model.TenantToBaseClassPremission;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.security.data.TenantToBaseclassPermissionRepository;
import com.flexicore.security.SecurityContext;
import com.wizzdi.flexicore.security.request.TenantToBaseclassPermissionCreate;
import com.wizzdi.flexicore.security.request.TenantToBaseclassPermissionFilter;
import com.wizzdi.flexicore.security.request.TenantToBaseclassPermissionUpdate;
import com.wizzdi.flexicore.security.response.PaginationResponse;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Extension
@Component
public class TenantToBaseclassPermissionService implements Plugin {

	@Autowired
	private SecurityLinkService securityLinkService;
	@Autowired
	private TenantToBaseclassPermissionRepository tenantToBaseclassPermissionRepository;


	public TenantToBaseClassPremission createTenantToBaseclassPermission(TenantToBaseclassPermissionCreate tenantToBaseclassPermissionCreate, SecurityContext securityContext){
		TenantToBaseClassPremission tenantToBaseclassPermission= createTenantToBaseclassPermissionNoMerge(tenantToBaseclassPermissionCreate,securityContext);
		tenantToBaseclassPermissionRepository.merge(tenantToBaseclassPermission);
		return tenantToBaseclassPermission;
	}


	public TenantToBaseClassPremission createTenantToBaseclassPermissionNoMerge(TenantToBaseclassPermissionCreate tenantToBaseclassPermissionCreate, SecurityContext securityContext){
		TenantToBaseClassPremission tenantToBaseclassPermission=new TenantToBaseClassPremission(tenantToBaseclassPermissionCreate.getName(),securityContext);
		updateTenantToBaseclassPermissionNoMerge(tenantToBaseclassPermissionCreate,tenantToBaseclassPermission);
		tenantToBaseclassPermissionRepository.merge(tenantToBaseclassPermission);
		return tenantToBaseclassPermission;
	}

	public boolean updateTenantToBaseclassPermissionNoMerge(TenantToBaseclassPermissionCreate tenantToBaseclassPermissionCreate, TenantToBaseClassPremission tenantToBaseclassPermission) {
		return securityLinkService.updateSecurityLinkNoMerge(tenantToBaseclassPermissionCreate,tenantToBaseclassPermission);
	}

	public TenantToBaseClassPremission updateTenantToBaseclassPermission(TenantToBaseclassPermissionUpdate tenantToBaseclassPermissionUpdate, SecurityContext securityContext){
		TenantToBaseClassPremission tenantToBaseclassPermission=tenantToBaseclassPermissionUpdate.getTenantToBaseclassPermission();
		if(updateTenantToBaseclassPermissionNoMerge(tenantToBaseclassPermissionUpdate,tenantToBaseclassPermission)){
			tenantToBaseclassPermissionRepository.merge(tenantToBaseclassPermission);
		}
		return tenantToBaseclassPermission;
	}

	public void validate(TenantToBaseclassPermissionCreate tenantToBaseclassPermissionCreate, SecurityContext securityContext) {
		securityLinkService.validate(tenantToBaseclassPermissionCreate,securityContext);
	}

	public void validate(TenantToBaseclassPermissionFilter tenantToBaseclassPermissionFilter, SecurityContext securityContext) {
		securityLinkService.validate(tenantToBaseclassPermissionFilter,securityContext);
	}

	public <T extends Baseclass> T getByIdOrNull(String id,Class<T> c, SecurityContext securityContext) {
		return tenantToBaseclassPermissionRepository.getByIdOrNull(id,c,securityContext);
	}

	public PaginationResponse<TenantToBaseClassPremission> getAllTenantToBaseclassPermissions(TenantToBaseclassPermissionFilter tenantToBaseclassPermissionFilter, SecurityContext securityContext) {
		List<TenantToBaseClassPremission> list= listAllTenantToBaseclassPermissions(tenantToBaseclassPermissionFilter, securityContext);
		long count=tenantToBaseclassPermissionRepository.countAllTenantToBaseclassPermissions(tenantToBaseclassPermissionFilter,securityContext);
		return new PaginationResponse<>(list,tenantToBaseclassPermissionFilter,count);
	}

	public List<TenantToBaseClassPremission> listAllTenantToBaseclassPermissions(TenantToBaseclassPermissionFilter tenantToBaseclassPermissionFilter, SecurityContext securityContext) {
		return tenantToBaseclassPermissionRepository.listAllTenantToBaseclassPermissions(tenantToBaseclassPermissionFilter, securityContext);
	}
}
