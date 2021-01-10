package com.wizzdi.flexicore.security.rest;

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
@RequestMapping("/Clazz")
@Extension
public class ClazzController implements Plugin {

	@Autowired
	private ClazzService ClazzService;

	@PostMapping("/create")
	public Clazz create(@RequestBody ClazzCreate ClazzCreate, @RequestAttribute SecurityContextBase securityContext){
		ClazzService.validate(ClazzCreate,securityContext);
		return ClazzService.createClazz(ClazzCreate,securityContext);
	}

	@PostMapping("/getAll")
	public PaginationResponse<Clazz> getAll(@RequestBody ClazzFilter ClazzFilter, @RequestAttribute SecurityContextBase securityContext){
		ClazzService.validate(ClazzFilter,securityContext);
		return ClazzService.getAllClazzs(ClazzFilter,securityContext);
	}

	@PutMapping("/update")
	public Clazz update(@RequestBody ClazzUpdate ClazzUpdate, @RequestAttribute SecurityContextBase securityContext){
		String id=ClazzUpdate.getId();
		Clazz Clazz=id!=null?ClazzService.getByIdOrNull(id,Clazz.class,securityContext):null;
		if(Clazz==null){
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"no security user with id "+id);
		}
		ClazzService.validate(ClazzUpdate,securityContext);
		return ClazzService.updateClazz(ClazzUpdate,securityContext);
	}
}
