package com.wizzdi.flexicore.security.rest;

import com.flexicore.model.Role;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.flexicore.security.SecurityContext;
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
@RequestMapping("/role")
@Extension
public class RoleController implements Plugin {

	@Autowired
	private RoleService roleService;

	@PostMapping("/create")
	public Role create(@RequestBody RoleCreate roleCreate, @RequestAttribute SecurityContext securityContext){
		roleService.validate(roleCreate,securityContext);
		return roleService.createRole(roleCreate,securityContext);
	}

	@PostMapping("/getAll")
	public PaginationResponse<Role> getAll(@RequestBody RoleFilter roleFilter, @RequestAttribute SecurityContext securityContext){
		roleService.validate(roleFilter,securityContext);
		return roleService.getAllRoles(roleFilter,securityContext);
	}

	@PutMapping("/update")
	public Role update(@RequestBody RoleUpdate roleUpdate, @RequestAttribute SecurityContext securityContext){
		String id=roleUpdate.getId();
		Role role=id!=null?roleService.getByIdOrNull(id,Role.class,securityContext):null;
		if(role==null){
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"no security user with id "+id);
		}
		roleService.validate(roleUpdate,securityContext);
		return roleService.updateRole(roleUpdate,securityContext);
	}
}
