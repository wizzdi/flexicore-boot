package com.wizzdi.flexicore.security.rest;

import com.flexicore.annotations.IOperation;
import com.flexicore.annotations.OperationsInside;
import com.flexicore.model.PermissionGroupToBaseclass;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.flexicore.security.SecurityContextBase;
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
@OperationsInside
@RequestMapping("/permissionGroupToBaseclass")
@Extension
public class PermissionGroupToBaseclassController implements Plugin {

	@Autowired
	private PermissionGroupToBaseclassService permissionGroupToBaseclassService;

	@IOperation(Name = "creates PermissionGroupToBaseclass",Description = "creates PermissionGroupToBaseclass")
	@PostMapping("/create")
	public PermissionGroupToBaseclass create(@RequestHeader("authenticationKey") String authenticationKey,@RequestBody PermissionGroupToBaseclassCreate permissionGroupToBaseclassCreate, @RequestAttribute SecurityContextBase securityContext){
		permissionGroupToBaseclassService.validate(permissionGroupToBaseclassCreate,securityContext);
		return permissionGroupToBaseclassService.createPermissionGroupToBaseclass(permissionGroupToBaseclassCreate,securityContext);
	}

	@IOperation(Name = "returns PermissionGroupToBaseclass",Description = "returns PermissionGroupToBaseclass")
	@PostMapping("/getAll")
	public PaginationResponse<PermissionGroupToBaseclass> getAll(@RequestHeader("authenticationKey") String authenticationKey,@RequestBody PermissionGroupToBaseclassFilter permissionGroupToBaseclassFilter, @RequestAttribute SecurityContextBase securityContext){
		permissionGroupToBaseclassService.validate(permissionGroupToBaseclassFilter,securityContext);
		return permissionGroupToBaseclassService.getAllPermissionGroupToBaseclass(permissionGroupToBaseclassFilter,securityContext);
	}

	@IOperation(Name = "updates PermissionGroupToBaseclass",Description = "updates PermissionGroupToBaseclass")
	@PutMapping("/update")
	public PermissionGroupToBaseclass update(@RequestHeader("authenticationKey") String authenticationKey,@RequestBody PermissionGroupToBaseclassUpdate permissionGroupToBaseclassUpdate, @RequestAttribute SecurityContextBase securityContext){
		String id=permissionGroupToBaseclassUpdate.getId();
		PermissionGroupToBaseclass permissionGroupToBaseclass=id!=null?permissionGroupToBaseclassService.getByIdOrNull(id,PermissionGroupToBaseclass.class,securityContext):null;
		if(permissionGroupToBaseclass==null){
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"no security user with id "+id);
		}
		permissionGroupToBaseclassUpdate.setPermissionGroupToBaseclass(permissionGroupToBaseclass);
		permissionGroupToBaseclassService.validate(permissionGroupToBaseclassUpdate,securityContext);
		return permissionGroupToBaseclassService.updatePermissionGroupToBaseclass(permissionGroupToBaseclassUpdate,securityContext);
	}
}
