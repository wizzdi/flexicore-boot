package com.wizzdi.flexicore.security.rest;

import com.flexicore.annotations.IOperation;
import com.flexicore.annotations.OperationsInside;
import com.flexicore.model.Clazz;
import com.flexicore.security.SecurityContextBase;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.security.request.ClazzCreate;
import com.wizzdi.flexicore.security.request.ClazzFilter;
import com.wizzdi.flexicore.security.request.ClazzUpdate;
import com.wizzdi.flexicore.security.response.PaginationResponse;
import com.wizzdi.flexicore.security.service.ClazzService;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@OperationsInside
@RequestMapping("/clazz")
@Extension
public class ClazzController implements Plugin {

	@Autowired
	private ClazzService ClazzService;

	@IOperation(Name = "creates Clazz",Description = "creates Clazz")
	@PostMapping("/create")
	public Clazz create(@RequestHeader("authenticationKey") String authenticationKey,@RequestBody ClazzCreate ClazzCreate, @RequestAttribute SecurityContextBase securityContext){
		ClazzService.validate(ClazzCreate,securityContext);
		return ClazzService.createClazz(ClazzCreate,securityContext);
	}

	@IOperation(Name = "returns Clazz",Description = "returns Clazz")
	@PostMapping("/getAll")
	public PaginationResponse<Clazz> getAll(@RequestHeader("authenticationKey") String authenticationKey,@RequestBody ClazzFilter ClazzFilter, @RequestAttribute SecurityContextBase securityContext){
		ClazzService.validate(ClazzFilter,securityContext);
		return ClazzService.getAllClazzs(ClazzFilter,securityContext);
	}

	@IOperation(Name = "updates Clazz",Description = "updates Clazz")
	@PutMapping("/update")
	public Clazz update(@RequestHeader("authenticationKey") String authenticationKey,@RequestBody ClazzUpdate clazzUpdate, @RequestAttribute SecurityContextBase securityContext){
		String id=clazzUpdate.getId();
		Clazz clazz=id!=null?ClazzService.getByIdOrNull(id,Clazz.class,securityContext):null;
		if(clazz==null){
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"no security user with id "+id);
		}
		clazzUpdate.setClazz(clazz);
		ClazzService.validate(clazzUpdate,securityContext);
		return ClazzService.updateClazz(clazzUpdate,securityContext);
	}
}
