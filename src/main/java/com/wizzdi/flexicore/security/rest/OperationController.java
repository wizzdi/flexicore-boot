package com.wizzdi.flexicore.security.rest;

import com.flexicore.model.Operation;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.security.request.OperationCreate;
import com.wizzdi.flexicore.security.request.OperationFilter;
import com.wizzdi.flexicore.security.request.OperationUpdate;
import com.flexicore.security.SecurityContext;
import com.wizzdi.flexicore.security.response.PaginationResponse;
import com.wizzdi.flexicore.security.service.OperationService;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/operation")
@Extension
public class OperationController implements Plugin {

	@Autowired
	private OperationService operationService;

	@PostMapping("/create")
	public Operation create(@RequestBody OperationCreate operationCreate, @RequestAttribute SecurityContext securityContext){
		operationService.validate(operationCreate,securityContext);
		return operationService.createOperation(operationCreate,securityContext);
	}

	@PostMapping("/getAll")
	public PaginationResponse<Operation> getAll(@RequestBody OperationFilter operationFilter, @RequestAttribute SecurityContext securityContext){
		operationService.validate(operationFilter,securityContext);
		return operationService.getAllOperations(operationFilter,securityContext);
	}

	@PutMapping("/update")
	public Operation update(@RequestBody OperationUpdate operationUpdate, @RequestAttribute SecurityContext securityContext){
		String id=operationUpdate.getId();
		Operation operation=id!=null?operationService.getByIdOrNull(id,Operation.class,securityContext):null;
		if(operation==null){
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"no security user with id "+id);
		}
		operationService.validate(operationUpdate,securityContext);
		return operationService.updateOperation(operationUpdate,securityContext);
	}
}
