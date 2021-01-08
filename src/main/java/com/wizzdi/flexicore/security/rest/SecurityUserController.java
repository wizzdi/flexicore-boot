package com.wizzdi.flexicore.security.rest;

import com.flexicore.model.SecurityUser;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.flexicore.security.SecurityContext;
import com.wizzdi.flexicore.security.request.SecurityUserCreate;
import com.wizzdi.flexicore.security.request.SecurityUserFilter;
import com.wizzdi.flexicore.security.request.SecurityUserUpdate;
import com.wizzdi.flexicore.security.response.PaginationResponse;
import com.wizzdi.flexicore.security.service.SecurityUserService;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/securityUser")
@Extension
public class SecurityUserController implements Plugin {

	@Autowired
	private SecurityUserService securityUserService;

	@PostMapping("/create")
	public SecurityUser create(@RequestBody SecurityUserCreate securityUserCreate, @RequestAttribute SecurityContext securityContext){
		securityUserService.validate(securityUserCreate,securityContext);
		return securityUserService.createSecurityUser(securityUserCreate,securityContext);
	}

	@PostMapping("/getAll")
	public PaginationResponse<SecurityUser> getAll(@RequestBody SecurityUserFilter securityUserFilter, @RequestAttribute SecurityContext securityContext){
		securityUserService.validate(securityUserFilter,securityContext);
		return securityUserService.getAllSecurityUsers(securityUserFilter,securityContext);
	}

	@PutMapping("/update")
	public SecurityUser update(@RequestBody SecurityUserUpdate securityUserUpdate, @RequestAttribute SecurityContext securityContext){
		String id=securityUserUpdate.getId();
		SecurityUser securityUser=id!=null?securityUserService.getByIdOrNull(id,SecurityUser.class,securityContext):null;
		if(securityUser==null){
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"no security user with id "+id);
		}
		securityUserService.validate(securityUserUpdate,securityContext);
		return securityUserService.updateSecurityUser(securityUserUpdate,securityContext);
	}
}
