package com.wizzdi.flexicore.security.rest;

import com.flexicore.model.TenantToUser;
import com.flexicore.security.SecurityContextBase;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.security.request.TenantToUserCreate;
import com.wizzdi.flexicore.security.request.TenantToUserFilter;
import com.wizzdi.flexicore.security.request.TenantToUserUpdate;
import com.wizzdi.flexicore.security.response.PaginationResponse;
import com.wizzdi.flexicore.security.service.TenantToUserService;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/tenantToUser")
@Extension
public class TenantToUserController implements Plugin {

	@Autowired
	private TenantToUserService tenantToUserService;

	@PostMapping("/create")
	public TenantToUser create(@RequestBody TenantToUserCreate tenantToUserCreate, @RequestAttribute SecurityContextBase securityContext){
		tenantToUserService.validate(tenantToUserCreate,securityContext);
		return tenantToUserService.createTenantToUser(tenantToUserCreate,securityContext);
	}

	@PostMapping("/getAll")
	public PaginationResponse<TenantToUser> getAll(@RequestBody TenantToUserFilter tenantToUserFilter, @RequestAttribute SecurityContextBase securityContext){
		tenantToUserService.validate(tenantToUserFilter,securityContext);
		return tenantToUserService.getAllTenantToUsers(tenantToUserFilter,securityContext);
	}

	@PutMapping("/update")
	public TenantToUser update(@RequestBody TenantToUserUpdate tenantToUserUpdate, @RequestAttribute SecurityContextBase securityContext){
		String id=tenantToUserUpdate.getId();
		TenantToUser tenantToUser=id!=null?tenantToUserService.getByIdOrNull(id,TenantToUser.class,securityContext):null;
		if(tenantToUser==null){
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"no security user with id "+id);
		}
		tenantToUserService.validate(tenantToUserUpdate,securityContext);
		return tenantToUserService.updateTenantToUser(tenantToUserUpdate,securityContext);
	}
}
