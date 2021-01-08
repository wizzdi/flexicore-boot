package com.wizzdi.flexicore.security.rest;

import com.flexicore.model.TenantToBaseClassPremission;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.flexicore.security.SecurityContext;
import com.wizzdi.flexicore.security.request.TenantToBaseclassPermissionCreate;
import com.wizzdi.flexicore.security.request.TenantToBaseclassPermissionFilter;
import com.wizzdi.flexicore.security.request.TenantToBaseclassPermissionUpdate;
import com.wizzdi.flexicore.security.response.PaginationResponse;
import com.wizzdi.flexicore.security.service.TenantToBaseclassPermissionService;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/tenantToBaseclassPermission")
@Extension
public class TenantToBaseclassPermissionController implements Plugin {

	@Autowired
	private TenantToBaseclassPermissionService tenantToBaseclassPermissionService;

	@PostMapping("/create")
	public TenantToBaseClassPremission create(@RequestBody TenantToBaseclassPermissionCreate tenantToBaseclassPermissionCreate, @RequestAttribute SecurityContext securityContext){
		tenantToBaseclassPermissionService.validate(tenantToBaseclassPermissionCreate,securityContext);
		return tenantToBaseclassPermissionService.createTenantToBaseclassPermission(tenantToBaseclassPermissionCreate,securityContext);
	}

	@PostMapping("/getAll")
	public PaginationResponse<TenantToBaseClassPremission> getAll(@RequestBody TenantToBaseclassPermissionFilter tenantToBaseclassPermissionFilter, @RequestAttribute SecurityContext securityContext){
		tenantToBaseclassPermissionService.validate(tenantToBaseclassPermissionFilter,securityContext);
		return tenantToBaseclassPermissionService.getAllTenantToBaseclassPermissions(tenantToBaseclassPermissionFilter,securityContext);
	}

	@PutMapping("/update")
	public TenantToBaseClassPremission update(@RequestBody TenantToBaseclassPermissionUpdate tenantToBaseclassPermissionUpdate, @RequestAttribute SecurityContext securityContext){
		String id=tenantToBaseclassPermissionUpdate.getId();
		TenantToBaseClassPremission tenantToBaseclassPermission=id!=null?tenantToBaseclassPermissionService.getByIdOrNull(id,TenantToBaseClassPremission.class,securityContext):null;
		if(tenantToBaseclassPermission==null){
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"no security user with id "+id);
		}
		tenantToBaseclassPermissionService.validate(tenantToBaseclassPermissionUpdate,securityContext);
		return tenantToBaseclassPermissionService.updateTenantToBaseclassPermission(tenantToBaseclassPermissionUpdate,securityContext);
	}
}
