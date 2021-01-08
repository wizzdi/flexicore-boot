package com.wizzdi.flexicore.security.rest;

import com.flexicore.model.RoleToBaseclass;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.flexicore.security.SecurityContext;
import com.wizzdi.flexicore.security.request.RoleToBaseclassCreate;
import com.wizzdi.flexicore.security.request.RoleToBaseclassFilter;
import com.wizzdi.flexicore.security.request.RoleToBaseclassUpdate;
import com.wizzdi.flexicore.security.response.PaginationResponse;
import com.wizzdi.flexicore.security.service.RoleToBaseclassService;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/roleToBaseclass")
@Extension
public class RoleToBaseclassController implements Plugin {

	@Autowired
	private RoleToBaseclassService roleToBaseclassService;

	@PostMapping("/create")
	public RoleToBaseclass create(@RequestBody RoleToBaseclassCreate roleToBaseclassCreate, @RequestAttribute SecurityContext securityContext){
		roleToBaseclassService.validate(roleToBaseclassCreate,securityContext);
		return roleToBaseclassService.createRoleToBaseclass(roleToBaseclassCreate,securityContext);
	}

	@PostMapping("/getAll")
	public PaginationResponse<RoleToBaseclass> getAll(@RequestBody RoleToBaseclassFilter roleToBaseclassFilter, @RequestAttribute SecurityContext securityContext){
		roleToBaseclassService.validate(roleToBaseclassFilter,securityContext);
		return roleToBaseclassService.getAllRoleToBaseclass(roleToBaseclassFilter,securityContext);
	}

	@PutMapping("/update")
	public RoleToBaseclass update(@RequestBody RoleToBaseclassUpdate roleToBaseclassUpdate, @RequestAttribute SecurityContext securityContext){
		String id=roleToBaseclassUpdate.getId();
		RoleToBaseclass roleToBaseclass=id!=null?roleToBaseclassService.getByIdOrNull(id,RoleToBaseclass.class,securityContext):null;
		if(roleToBaseclass==null){
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"no security user with id "+id);
		}
		roleToBaseclassService.validate(roleToBaseclassUpdate,securityContext);
		return roleToBaseclassService.updateRoleToBaseclass(roleToBaseclassUpdate,securityContext);
	}
}
