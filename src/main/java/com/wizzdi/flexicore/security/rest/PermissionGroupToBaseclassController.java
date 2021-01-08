package com.wizzdi.flexicore.security.rest;

import com.flexicore.model.PermissionGroupToBaseclass;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.flexicore.security.SecurityContext;
import com.wizzdi.flexicore.security.request.PermissionGroupToBaseclassCreate;
import com.wizzdi.flexicore.security.request.PermissionGroupToBaseclassFilter;
import com.wizzdi.flexicore.security.request.PermissionGroupToBaseclassUpdate;
import com.wizzdi.flexicore.security.response.PaginationResponse;
import com.wizzdi.flexicore.security.service.PermissionGroupToBaseclassService;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/permissionGroupToBaseclass")
@Extension
public class PermissionGroupToBaseclassController implements Plugin {

	@Autowired
	private PermissionGroupToBaseclassService permissionGroupToBaseclassService;

	@PostMapping("/create")
	public PermissionGroupToBaseclass create(@RequestBody PermissionGroupToBaseclassCreate permissionGroupToBaseclassCreate, @RequestAttribute SecurityContext securityContext){
		permissionGroupToBaseclassService.validate(permissionGroupToBaseclassCreate,securityContext);
		return permissionGroupToBaseclassService.createPermissionGroupToBaseclass(permissionGroupToBaseclassCreate,securityContext);
	}

	@PostMapping("/getAll")
	public PaginationResponse<PermissionGroupToBaseclass> getAll(@RequestBody PermissionGroupToBaseclassFilter permissionGroupToBaseclassFilter, @RequestAttribute SecurityContext securityContext){
		permissionGroupToBaseclassService.validate(permissionGroupToBaseclassFilter,securityContext);
		return permissionGroupToBaseclassService.getAllPermissionGroupToBaseclass(permissionGroupToBaseclassFilter,securityContext);
	}

	@PutMapping("/update")
	public PermissionGroupToBaseclass update(@RequestBody PermissionGroupToBaseclassUpdate permissionGroupToBaseclassUpdate, @RequestAttribute SecurityContext securityContext){
		String id=permissionGroupToBaseclassUpdate.getId();
		PermissionGroupToBaseclass permissionGroupToBaseclass=id!=null?permissionGroupToBaseclassService.getByIdOrNull(id,PermissionGroupToBaseclass.class,securityContext):null;
		if(permissionGroupToBaseclass==null){
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"no security user with id "+id);
		}
		permissionGroupToBaseclassService.validate(permissionGroupToBaseclassUpdate,securityContext);
		return permissionGroupToBaseclassService.updatePermissionGroupToBaseclass(permissionGroupToBaseclassUpdate,securityContext);
	}
}
