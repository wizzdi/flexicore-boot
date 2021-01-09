package com.wizzdi.flexicore.security.rest;

import com.flexicore.model.PermissionGroup;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.flexicore.security.SecurityContextBase;
import com.wizzdi.flexicore.security.request.PermissionGroupCreate;
import com.wizzdi.flexicore.security.request.PermissionGroupFilter;
import com.wizzdi.flexicore.security.request.PermissionGroupUpdate;
import com.wizzdi.flexicore.security.response.PaginationResponse;
import com.wizzdi.flexicore.security.service.PermissionGroupService;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/permissionGroup")
@Extension
public class PermissionGroupController implements Plugin {

	@Autowired
	private PermissionGroupService permissionGroupService;

	@PostMapping("/create")
	public PermissionGroup create(@RequestBody PermissionGroupCreate permissionGroupCreate, @RequestAttribute SecurityContextBase securityContext){
		permissionGroupService.validate(permissionGroupCreate,securityContext);
		return permissionGroupService.createPermissionGroup(permissionGroupCreate,securityContext);
	}

	@PostMapping("/getAll")
	public PaginationResponse<PermissionGroup> getAll(@RequestBody PermissionGroupFilter permissionGroupFilter, @RequestAttribute SecurityContextBase securityContext){
		permissionGroupService.validate(permissionGroupFilter,securityContext);
		return permissionGroupService.getAllPermissionGroups(permissionGroupFilter,securityContext);
	}

	@PutMapping("/update")
	public PermissionGroup update(@RequestBody PermissionGroupUpdate permissionGroupUpdate, @RequestAttribute SecurityContextBase securityContext){
		String id=permissionGroupUpdate.getId();
		PermissionGroup permissionGroup=id!=null?permissionGroupService.getByIdOrNull(id,PermissionGroup.class,securityContext):null;
		if(permissionGroup==null){
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"no security user with id "+id);
		}
		permissionGroupService.validate(permissionGroupUpdate,securityContext);
		return permissionGroupService.updatePermissionGroup(permissionGroupUpdate,securityContext);
	}
}
