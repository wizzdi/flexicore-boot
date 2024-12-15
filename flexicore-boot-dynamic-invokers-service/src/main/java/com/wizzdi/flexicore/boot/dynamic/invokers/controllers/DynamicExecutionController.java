package com.wizzdi.flexicore.boot.dynamic.invokers.controllers;

import com.flexicore.annotations.IOperation;
import com.flexicore.annotations.OperationsInside;
import com.wizzdi.segmantix.model.SecurityContext;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.boot.dynamic.invokers.model.DynamicExecution;
import com.wizzdi.flexicore.boot.dynamic.invokers.request.*;
import com.wizzdi.flexicore.boot.dynamic.invokers.service.DynamicExecutionService;
import com.wizzdi.flexicore.security.response.PaginationResponse;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@OperationsInside
@RequestMapping("/dynamicExecution")
@Extension
public class DynamicExecutionController implements Plugin {

	@Autowired
	private DynamicExecutionService dynamicExecutionService;

	@IOperation(Name = "creates dynamicExecution",Description = "creates dynamicExecution")
	@PostMapping("/create")
	public DynamicExecution create(
			@RequestBody DynamicExecutionCreate dynamicExecutionCreate, @RequestAttribute SecurityContext securityContext){
		dynamicExecutionService.validateCreate(dynamicExecutionCreate,securityContext);
		return dynamicExecutionService.createDynamicExecution(dynamicExecutionCreate,securityContext);
	}

	@IOperation(Name = "returns dynamicExecution",Description = "returns dynamicExecution")
	@PostMapping("/getAll")
	public PaginationResponse<DynamicExecution> getAll(
			@RequestBody DynamicExecutionFilter dynamicExecutionFilter, @RequestAttribute SecurityContext securityContext){
		dynamicExecutionService.validate(dynamicExecutionFilter,securityContext);
		return dynamicExecutionService.getAllDynamicExecutions(dynamicExecutionFilter,securityContext);
	}

	@IOperation(Name = "returns example for the dynamic execution",Description = "returns example for the dynamic execution")
	@PostMapping("/getDynamicExecutionReturnExample")
	public Object getDynamicExecutionReturnExample(
			@RequestBody DynamicExecutionExampleRequest dynamicExecutionExampleRequest, @RequestAttribute SecurityContext securityContext){
		dynamicExecutionService.validate(dynamicExecutionExampleRequest,securityContext);
		return dynamicExecutionService.getExample(dynamicExecutionExampleRequest.getClazz());
	}

	@IOperation(Name = "updates dynamicExecution",Description = "updates dynamicExecution")
	@PutMapping("/update")
	public DynamicExecution update(
			@RequestBody DynamicExecutionUpdate dynamicExecutionUpdate, @RequestAttribute SecurityContext securityContext){
		String id=dynamicExecutionUpdate.getId();
		DynamicExecution dynamicExecution=id!=null?dynamicExecutionService.getByIdOrNull(id,DynamicExecution.class, securityContext):null;
		if(dynamicExecution==null){
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"no Dynamic Execution with id "+id);
		}
		dynamicExecutionUpdate.setDynamicExecution(dynamicExecution);
		return dynamicExecutionService.updateDynamicExecution(dynamicExecutionUpdate,securityContext);
	}

	@IOperation(Name = "executes DynamicExecution",Description = "executes DynamicExecution")
	@PostMapping("/executeDynamicExecution")
	public ExecuteInvokersResponse executeDynamicExecution(
			@RequestBody ExecuteDynamicExecution executeDynamicExecution, @RequestAttribute SecurityContext securityContext){
		dynamicExecutionService.validate(executeDynamicExecution,securityContext);
		return dynamicExecutionService.executeDynamicExecution(executeDynamicExecution,securityContext);
	}
}
