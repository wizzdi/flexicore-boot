package com.wizzdi.flexicore.boot.dynamic.invokers.controllers;

import com.flexicore.annotations.IOperation;
import com.flexicore.annotations.OperationsInside;
import com.wizzdi.segmantix.model.SecurityContext;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.boot.dynamic.invokers.request.DynamicInvokerFilter;
import com.wizzdi.flexicore.boot.dynamic.invokers.request.ExecuteInvokerRequest;
import com.wizzdi.flexicore.boot.dynamic.invokers.request.ExecuteInvokersResponse;
import com.wizzdi.flexicore.boot.dynamic.invokers.response.InvokerHolder;
import com.wizzdi.flexicore.boot.dynamic.invokers.response.InvokerInfo;
import com.wizzdi.flexicore.boot.dynamic.invokers.service.DynamicInvokerService;
import com.wizzdi.flexicore.security.response.PaginationResponse;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@OperationsInside
@RequestMapping("/dynamicInvoker")
@Extension
public class DynamicInvokersController implements Plugin {

	@Autowired
	private DynamicInvokerService dynamicInvokerService;



	@IOperation(Name = "returns dynamicInvoker",Description = "returns dynamicInvoker")
	@PostMapping("/getAll")
	public PaginationResponse<InvokerInfo> getAll(
			@RequestBody DynamicInvokerFilter dynamicInvokerFilter, @RequestAttribute SecurityContext securityContext){
		dynamicInvokerService.validate(dynamicInvokerFilter,securityContext);
		return dynamicInvokerService.getAllDynamicInvokers(dynamicInvokerFilter,securityContext);
	}

	@IOperation(Name = "executes dynamicInvoker",Description = "executes dynamicInvoker")
	@PostMapping("/executeInvoker")
	public ExecuteInvokersResponse executeInvoker(
			@RequestBody ExecuteInvokerRequest executeInvokerRequest, @RequestAttribute SecurityContext securityContext){
		return dynamicInvokerService.executeInvoker(executeInvokerRequest,securityContext);
	}

	@IOperation(Name = "returns dynamicInvoker holders",Description = "returns dynamicInvoker holders")
	@PostMapping("/getAllInvokerHolders")
	public PaginationResponse<InvokerHolder> getAllInvokerHolders(
			@RequestBody DynamicInvokerFilter dynamicInvokerFilter, @RequestAttribute SecurityContext securityContext){
		dynamicInvokerService.validate(dynamicInvokerFilter,securityContext);
		return dynamicInvokerService.getAllDynamicInvokerHolders(dynamicInvokerFilter,securityContext);
	}




}
