package com.wizzdi.flexicore.security.rest;

import com.flexicore.annotations.IOperation;
import com.flexicore.annotations.OperationsInside;
import com.flexicore.model.SecurityOperation;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.security.request.SecurityOperationCreate;
import com.wizzdi.flexicore.security.request.SecurityOperationFilter;
import com.wizzdi.flexicore.security.request.SecurityOperationUpdate;
import com.flexicore.security.SecurityContextBase;
import com.wizzdi.flexicore.security.response.PaginationResponse;
import com.wizzdi.flexicore.security.service.SecurityOperationService;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@OperationsInside
@RequestMapping("/securityOperation")
@Extension
public class SecurityOperationController implements Plugin {

	@Autowired
	private SecurityOperationService operationService;

	@IOperation(Name = "creates security operation",Description = "creates security operation")
	@PostMapping("/create")
	public SecurityOperation create(@RequestHeader("authenticationKey") String authenticationKey,@RequestBody SecurityOperationCreate operationCreate, @RequestAttribute SecurityContextBase securityContext){
		operationService.validate(operationCreate,securityContext);
		return operationService.createOperation(operationCreate,securityContext);
	}

	@IOperation(Name = "returns security operation",Description = "returns security operation")
	@PostMapping("/getAll")
	public PaginationResponse<SecurityOperation> getAll(@RequestHeader("authenticationKey") String authenticationKey,@RequestBody SecurityOperationFilter operationFilter, @RequestAttribute SecurityContextBase securityContext){
		operationService.validate(operationFilter,securityContext);
		return operationService.getAllOperations(operationFilter,securityContext);
	}

	@IOperation(Name = "updates security operation",Description = "updates security operation")
	@PutMapping("/update")
	public SecurityOperation update(@RequestHeader("authenticationKey") String authenticationKey,@RequestBody SecurityOperationUpdate operationUpdate, @RequestAttribute SecurityContextBase securityContext){
		String id=operationUpdate.getId();
		SecurityOperation operation=id!=null?operationService.getByIdOrNull(id,SecurityOperation.class,securityContext):null;
		if(operation==null){
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"no security user with id "+id);
		}
		operationUpdate.setOperation(operation);
		operationService.validate(operationUpdate,securityContext);
		return operationService.updateOperation(operationUpdate,securityContext);
	}
}
