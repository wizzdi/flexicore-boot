package com.wizzdi.flexicore.security.rest;

import com.flexicore.model.Tenant;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.flexicore.security.SecurityContext;
import com.wizzdi.flexicore.security.request.TenantCreate;
import com.wizzdi.flexicore.security.request.TenantFilter;
import com.wizzdi.flexicore.security.request.TenantUpdate;
import com.wizzdi.flexicore.security.response.PaginationResponse;
import com.wizzdi.flexicore.security.service.TenantService;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/tenant")
@Extension
public class TenantController implements Plugin {

	@Autowired
	private TenantService tenantService;

	@PostMapping("/create")
	public Tenant create(@RequestBody TenantCreate tenantCreate, @RequestAttribute SecurityContext securityContext){
		tenantService.validate(tenantCreate,securityContext);
		return tenantService.createTenant(tenantCreate,securityContext);
	}

	@PostMapping("/getAll")
	public PaginationResponse<Tenant> getAll(@RequestBody TenantFilter tenantFilter, @RequestAttribute SecurityContext securityContext){
		tenantService.validate(tenantFilter,securityContext);
		return tenantService.getAllTenants(tenantFilter,securityContext);
	}

	@PutMapping("/update")
	public Tenant update(@RequestBody TenantUpdate tenantUpdate, @RequestAttribute SecurityContext securityContext){
		String id=tenantUpdate.getId();
		Tenant tenant=id!=null?tenantService.getByIdOrNull(id,Tenant.class,securityContext):null;
		if(tenant==null){
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"no security user with id "+id);
		}
		tenantService.validate(tenantUpdate,securityContext);
		return tenantService.updateTenant(tenantUpdate,securityContext);
	}
}
