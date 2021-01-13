package com.wizzdi.flexicore.security.rest;

import com.flexicore.annotations.IOperation;
import com.flexicore.annotations.OperationsInside;
import com.flexicore.model.Role;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.flexicore.security.SecurityContextBase;
import com.wizzdi.flexicore.security.request.RoleCreate;
import com.wizzdi.flexicore.security.request.RoleFilter;
import com.wizzdi.flexicore.security.request.RoleUpdate;
import com.wizzdi.flexicore.security.response.PaginationResponse;
import com.wizzdi.flexicore.security.service.RoleService;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@OperationsInside
@RequestMapping("/role")
@Extension
public class RoleController implements Plugin {

	@Autowired
	private RoleService roleService;

	@IOperation(Name = "creates Role",Description = "creates Role")
	@PostMapping("/create")
	public Role create(@RequestBody RoleCreate roleCreate, @RequestAttribute SecurityContextBase securityContext){
		roleService.validate(roleCreate,securityContext);
		return roleService.createRole(roleCreate,securityContext);
	}

	@IOperation(Name = "returns Role",Description = "returns Role")
	@PostMapping("/getAll")
	public PaginationResponse<Role> getAll(@RequestBody RoleFilter roleFilter, @RequestAttribute SecurityContextBase securityContext){
		roleService.validate(roleFilter,securityContext);
		return roleService.getAllRoles(roleFilter,securityContext);
	}

	@IOperation(Name = "updates Role",Description = "updates Role")
	@PutMapping("/update")
	public Role update(@RequestBody RoleUpdate roleUpdate, @RequestAttribute SecurityContextBase securityContext){
		String id=roleUpdate.getId();
		Role role=id!=null?roleService.getByIdOrNull(id,Role.class,securityContext):null;
		if(role==null){
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"no security user with id "+id);
		}
		roleUpdate.setRole(role);
		roleService.validate(roleUpdate,securityContext);
		return roleService.updateRole(roleUpdate,securityContext);
	}
}
