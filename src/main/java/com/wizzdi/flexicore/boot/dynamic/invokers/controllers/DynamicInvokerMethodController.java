package com.wizzdi.flexicore.boot.dynamic.invokers.controllers;

import com.flexicore.annotations.IOperation;
import com.flexicore.annotations.OperationsInside;
import com.flexicore.security.SecurityContextBase;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.boot.dynamic.invokers.request.DynamicInvokerFilter;
import com.wizzdi.flexicore.boot.dynamic.invokers.request.DynamicInvokerMethodFilter;
import com.wizzdi.flexicore.boot.dynamic.invokers.request.ExecuteInvokerRequest;
import com.wizzdi.flexicore.boot.dynamic.invokers.request.ExecuteInvokersResponse;
import com.wizzdi.flexicore.boot.dynamic.invokers.response.InvokerHolder;
import com.wizzdi.flexicore.boot.dynamic.invokers.response.InvokerInfo;
import com.wizzdi.flexicore.boot.dynamic.invokers.response.InvokerMethodHolder;
import com.wizzdi.flexicore.boot.dynamic.invokers.service.DynamicInvokerService;
import com.wizzdi.flexicore.security.response.PaginationResponse;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@OperationsInside
@RequestMapping("/dynamicInvokerMethod")
@Extension
public class DynamicInvokerMethodController implements Plugin {

	@Autowired
	private DynamicInvokerService dynamicInvokerService;




	@IOperation(Name = "returns dynamicInvokerMethod holders",Description = "returns dynamicInvokerMethod holders")
	@PostMapping("/getAllInvokerMethodHolders")
	public PaginationResponse<InvokerMethodHolder> getAllInvokerMethodHolders(@RequestHeader("authenticationKey") String authenticationKey,
			@RequestBody DynamicInvokerMethodFilter dynamicInvokerFilter, @RequestAttribute SecurityContextBase securityContext){
		dynamicInvokerService.validate(dynamicInvokerFilter,securityContext);
		return dynamicInvokerService.getAllInvokerMethodHolders(dynamicInvokerFilter,securityContext);
	}




}
