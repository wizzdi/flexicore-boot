package com.wizzdi.flexicore.security.rest;

import com.flexicore.annotations.IOperation;
import com.flexicore.annotations.OperationsInside;
import com.flexicore.model.SecurityTenant;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.flexicore.security.SecurityContextBase;
import com.wizzdi.flexicore.security.request.SecurityTenantCreate;
import com.wizzdi.flexicore.security.request.SecurityTenantFilter;
import com.wizzdi.flexicore.security.request.SecurityTenantUpdate;
import com.wizzdi.flexicore.security.response.PaginationResponse;
import com.wizzdi.flexicore.security.service.SecurityTenantService;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@OperationsInside
@RequestMapping("/securityTenant")
@Extension
public class SecurityTenantController implements Plugin {

	@Autowired
	private SecurityTenantService tenantService;

	@IOperation(Name = "creates security tenant",Description = "creates security tenant")
	@PostMapping("/create")
	public SecurityTenant create(@RequestBody SecurityTenantCreate tenantCreate, @RequestAttribute SecurityContextBase securityContext){
		tenantService.validate(tenantCreate,securityContext);
		return tenantService.createTenant(tenantCreate,securityContext);
	}

	@IOperation(Name = "returns security tenant",Description = "returns security tenant")
	@PostMapping("/getAll")
	public PaginationResponse<SecurityTenant> getAll(@RequestBody SecurityTenantFilter tenantFilter, @RequestAttribute SecurityContextBase securityContext){
		tenantService.validate(tenantFilter,securityContext);
		return tenantService.getAllTenants(tenantFilter,securityContext);
	}

	@IOperation(Name = "updates security tenant",Description = "updates security tenant")
	@PutMapping("/update")
	public SecurityTenant update(@RequestBody SecurityTenantUpdate tenantUpdate, @RequestAttribute SecurityContextBase securityContext){
		String id=tenantUpdate.getId();
		SecurityTenant tenant=id!=null?tenantService.getByIdOrNull(id,SecurityTenant.class,securityContext):null;
		if(tenant==null){
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"no security user with id "+id);
		}
		tenantUpdate.setTenantToUpdate(tenant);
		tenantService.validate(tenantUpdate,securityContext);
		return tenantService.updateTenant(tenantUpdate,securityContext);
	}
}
