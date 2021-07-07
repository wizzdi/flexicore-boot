package com.wizzdi.flexicore.security.rest;

import com.flexicore.annotations.IOperation;
import com.flexicore.annotations.OperationsInside;
import com.flexicore.model.TenantToBaseClassPremission;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.flexicore.security.SecurityContextBase;
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
@OperationsInside
@RequestMapping("/tenantToBaseclassPermission")
@Extension
public class TenantToBaseclassPermissionController implements Plugin {

	@Autowired
	private TenantToBaseclassPermissionService tenantToBaseclassPermissionService;

	@IOperation(Name = "creates tenant to baseclass",Description = "creates tenant to baseclass")
	@PostMapping("/create")
	public TenantToBaseClassPremission create(@RequestHeader("authenticationKey") String authenticationKey,@RequestBody TenantToBaseclassPermissionCreate tenantToBaseclassPermissionCreate, @RequestAttribute SecurityContextBase securityContext){
		tenantToBaseclassPermissionService.validate(tenantToBaseclassPermissionCreate,securityContext);
		return tenantToBaseclassPermissionService.createTenantToBaseclassPermission(tenantToBaseclassPermissionCreate,securityContext);
	}

	@IOperation(Name = "returns tenant to baseclass",Description = "returns tenant to baseclass")
	@PostMapping("/getAll")
	public PaginationResponse<TenantToBaseClassPremission> getAll(@RequestHeader("authenticationKey") String authenticationKey,@RequestBody TenantToBaseclassPermissionFilter tenantToBaseclassPermissionFilter, @RequestAttribute SecurityContextBase securityContext){
		tenantToBaseclassPermissionService.validate(tenantToBaseclassPermissionFilter,securityContext);
		return tenantToBaseclassPermissionService.getAllTenantToBaseclassPermissions(tenantToBaseclassPermissionFilter,securityContext);
	}

	@IOperation(Name = "update tenant to baseclass",Description = "update tenant to baseclass")
	@PutMapping("/update")
	public TenantToBaseClassPremission update(@RequestHeader("authenticationKey") String authenticationKey,@RequestBody TenantToBaseclassPermissionUpdate tenantToBaseclassPermissionUpdate, @RequestAttribute SecurityContextBase securityContext){
		String id=tenantToBaseclassPermissionUpdate.getId();
		TenantToBaseClassPremission tenantToBaseclassPermission=id!=null?tenantToBaseclassPermissionService.getByIdOrNull(id,TenantToBaseClassPremission.class,securityContext):null;
		if(tenantToBaseclassPermission==null){
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"no security user with id "+id);
		}
		tenantToBaseclassPermissionUpdate.setTenantToBaseclassPermission(tenantToBaseclassPermission);
		tenantToBaseclassPermissionService.validate(tenantToBaseclassPermissionUpdate,securityContext);
		return tenantToBaseclassPermissionService.updateTenantToBaseclassPermission(tenantToBaseclassPermissionUpdate,securityContext);
	}
}
