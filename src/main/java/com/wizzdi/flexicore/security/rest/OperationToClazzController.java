package com.wizzdi.flexicore.security.rest;

import com.flexicore.model.OperationToClazz;
import com.flexicore.security.SecurityContextBase;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.security.request.OperationToClazzCreate;
import com.wizzdi.flexicore.security.request.OperationToClazzFilter;
import com.wizzdi.flexicore.security.request.OperationToClazzUpdate;
import com.wizzdi.flexicore.security.response.PaginationResponse;
import com.wizzdi.flexicore.security.service.OperationToClazzService;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/operationToClazz")
@Extension
public class OperationToClazzController implements Plugin {

	@Autowired
	private OperationToClazzService operationToClazzService;

	@PostMapping("/create")
	public OperationToClazz create(@RequestBody OperationToClazzCreate operationToClazzCreate, @RequestAttribute SecurityContextBase securityContext){
		operationToClazzService.validate(operationToClazzCreate,securityContext);
		return operationToClazzService.createOperationToClazz(operationToClazzCreate,securityContext);
	}

	@PostMapping("/getAll")
	public PaginationResponse<OperationToClazz> getAll(@RequestBody OperationToClazzFilter operationToClazzFilter, @RequestAttribute SecurityContextBase securityContext){
		operationToClazzService.validate(operationToClazzFilter,securityContext);
		return operationToClazzService.getAllOperationToClazz(operationToClazzFilter,securityContext);
	}

	@PutMapping("/update")
	public OperationToClazz update(@RequestBody OperationToClazzUpdate operationToClazzUpdate, @RequestAttribute SecurityContextBase securityContext){
		String id=operationToClazzUpdate.getId();
		OperationToClazz operationToClazz=id!=null?operationToClazzService.getByIdOrNull(id,OperationToClazz.class,securityContext):null;
		if(operationToClazz==null){
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"no security user with id "+id);
		}
		operationToClazzService.validate(operationToClazzUpdate,securityContext);
		return operationToClazzService.updateOperationToClazz(operationToClazzUpdate,securityContext);
	}
}
