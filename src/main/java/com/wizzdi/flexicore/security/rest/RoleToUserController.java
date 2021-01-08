package com.wizzdi.flexicore.security.rest;

import com.flexicore.model.RoleToUser;
import com.flexicore.security.SecurityContext;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.security.request.RoleToUserCreate;
import com.wizzdi.flexicore.security.request.RoleToUserFilter;
import com.wizzdi.flexicore.security.request.RoleToUserUpdate;
import com.wizzdi.flexicore.security.response.PaginationResponse;
import com.wizzdi.flexicore.security.service.RoleToUserService;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/roleToUser")
@Extension
public class RoleToUserController implements Plugin {

	@Autowired
	private RoleToUserService roleToUserService;

	@PostMapping("/create")
	public RoleToUser create(@RequestBody RoleToUserCreate roleToUserCreate, @RequestAttribute SecurityContext securityContext){
		roleToUserService.validate(roleToUserCreate,securityContext);
		return roleToUserService.createRoleToUser(roleToUserCreate,securityContext);
	}

	@PostMapping("/getAll")
	public PaginationResponse<RoleToUser> getAll(@RequestBody RoleToUserFilter roleToUserFilter, @RequestAttribute SecurityContext securityContext){
		roleToUserService.validate(roleToUserFilter,securityContext);
		return roleToUserService.getAllRoleToUsers(roleToUserFilter,securityContext);
	}

	@PutMapping("/update")
	public RoleToUser update(@RequestBody RoleToUserUpdate roleToUserUpdate, @RequestAttribute SecurityContext securityContext){
		String id=roleToUserUpdate.getId();
		RoleToUser roleToUser=id!=null?roleToUserService.getByIdOrNull(id,RoleToUser.class,securityContext):null;
		if(roleToUser==null){
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"no security user with id "+id);
		}
		roleToUserService.validate(roleToUserUpdate,securityContext);
		return roleToUserService.updateRoleToUser(roleToUserUpdate,securityContext);
	}
}
