package com.wizzdi.flexicore.security.rest;

import com.flexicore.model.UserToBaseClass;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.security.request.UserToBaseclassCreate;
import com.wizzdi.flexicore.security.request.UserToBaseclassFilter;
import com.wizzdi.flexicore.security.request.UserToBaseclassUpdate;
import com.flexicore.security.SecurityContext;
import com.wizzdi.flexicore.security.response.PaginationResponse;
import com.wizzdi.flexicore.security.service.UserToBaseclassService;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/userToBaseclass")
@Extension
public class UserToBaseclassController implements Plugin {

	@Autowired
	private UserToBaseclassService userToBaseclassService;

	@PostMapping("/create")
	public UserToBaseClass create(@RequestBody UserToBaseclassCreate userToBaseclassCreate, @RequestAttribute SecurityContext securityContext){
		userToBaseclassService.validate(userToBaseclassCreate,securityContext);
		return userToBaseclassService.createUserToBaseclass(userToBaseclassCreate,securityContext);
	}

	@PostMapping("/getAll")
	public PaginationResponse<UserToBaseClass> getAll(@RequestBody UserToBaseclassFilter userToBaseclassFilter, @RequestAttribute SecurityContext securityContext){
		userToBaseclassService.validate(userToBaseclassFilter,securityContext);
		return userToBaseclassService.getAllUserToBaseclass(userToBaseclassFilter,securityContext);
	}

	@PutMapping("/update")
	public UserToBaseClass update(@RequestBody UserToBaseclassUpdate userToBaseclassUpdate, @RequestAttribute SecurityContext securityContext){
		String id=userToBaseclassUpdate.getId();
		UserToBaseClass userToBaseclass=id!=null?userToBaseclassService.getByIdOrNull(id,UserToBaseClass.class,securityContext):null;
		if(userToBaseclass==null){
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"no security user with id "+id);
		}
		userToBaseclassService.validate(userToBaseclassUpdate,securityContext);
		return userToBaseclassService.updateUserToBaseclass(userToBaseclassUpdate,securityContext);
	}
}
