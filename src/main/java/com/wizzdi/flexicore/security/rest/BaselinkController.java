package com.wizzdi.flexicore.security.rest;

import com.flexicore.model.Baselink;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.flexicore.security.SecurityContext;
import com.wizzdi.flexicore.security.request.BaselinkCreate;
import com.wizzdi.flexicore.security.request.BaselinkFilter;
import com.wizzdi.flexicore.security.request.BaselinkUpdate;
import com.wizzdi.flexicore.security.response.PaginationResponse;
import com.wizzdi.flexicore.security.service.BaselinkService;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/baselink")
@Extension
public class BaselinkController implements Plugin {

	@Autowired
	private BaselinkService baselinkService;

	@PostMapping("/create")
	public Baselink create(@RequestBody BaselinkCreate baselinkCreate, @RequestAttribute SecurityContext securityContext){
		baselinkService.validate(baselinkCreate,securityContext);
		return baselinkService.createBaselink(baselinkCreate,securityContext);
	}

	@PostMapping("/getAll")
	public PaginationResponse<Baselink> getAll(@RequestBody BaselinkFilter baselinkFilter, @RequestAttribute SecurityContext securityContext){
		baselinkService.validate(baselinkFilter,securityContext);
		return baselinkService.getAllBaselinks(baselinkFilter,securityContext);
	}

	@PutMapping("/update")
	public Baselink update(@RequestBody BaselinkUpdate baselinkUpdate, @RequestAttribute SecurityContext securityContext){
		String id=baselinkUpdate.getId();
		Baselink baselink=id!=null?baselinkService.getByIdOrNull(id,Baselink.class,securityContext):null;
		if(baselink==null){
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"no security user with id "+id);
		}
		baselinkService.validate(baselinkUpdate,securityContext);
		return baselinkService.updateBaselink(baselinkUpdate,securityContext);
	}
}
