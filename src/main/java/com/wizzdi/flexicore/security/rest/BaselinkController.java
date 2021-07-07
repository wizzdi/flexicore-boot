package com.wizzdi.flexicore.security.rest;

import com.flexicore.annotations.IOperation;
import com.flexicore.annotations.OperationsInside;
import com.flexicore.model.Baselink;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.flexicore.security.SecurityContextBase;
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
@OperationsInside
@RequestMapping("/baselink")
@Extension
public class BaselinkController implements Plugin {

	@Autowired
	private BaselinkService baselinkService;

	@IOperation(Name = "creates Baselink",Description = "creates Baselink")
	@PostMapping("/create")
	public Baselink create(@RequestHeader("authenticationKey") String authenticationKey,@RequestBody BaselinkCreate baselinkCreate, @RequestAttribute SecurityContextBase securityContext){
		baselinkService.validate(baselinkCreate,securityContext);
		return baselinkService.createBaselink(baselinkCreate,securityContext);
	}

	@IOperation(Name = "returns Baselink",Description = "returns Baselink")
	@PostMapping("/getAll")
	public PaginationResponse<Baselink> getAll(@RequestHeader("authenticationKey") String authenticationKey,@RequestBody BaselinkFilter baselinkFilter, @RequestAttribute SecurityContextBase securityContext){
		baselinkService.validate(baselinkFilter,securityContext);
		return baselinkService.getAllBaselinks(baselinkFilter,securityContext);
	}

	@IOperation(Name = "updates Baselink",Description = "updates Baselink")
	@PutMapping("/update")
	public Baselink update(@RequestHeader("authenticationKey") String authenticationKey,@RequestBody BaselinkUpdate baselinkUpdate, @RequestAttribute SecurityContextBase securityContext){
		String id=baselinkUpdate.getId();
		Baselink baselink=id!=null?baselinkService.getByIdOrNull(id,Baselink.class,securityContext):null;
		if(baselink==null){
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"no security user with id "+id);
		}
		baselinkUpdate.setBaselink(baselink);
		baselinkService.validate(baselinkUpdate,securityContext);
		return baselinkService.updateBaselink(baselinkUpdate,securityContext);
	}
}
