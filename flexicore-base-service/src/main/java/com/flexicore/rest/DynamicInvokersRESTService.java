/*******************************************************************************
 *  Copyright (C) FlexiCore, Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Avishay Ben Natan And Asaf Ben Natan, October 2015
 ******************************************************************************/
package com.flexicore.rest;

import com.flexicore.annotations.OperationsInside;
import com.flexicore.annotations.Protected;
import com.flexicore.data.jsoncontainers.PaginationResponse;
import com.flexicore.interfaces.RESTService;
import com.wizzdi.flexicore.file.model.FileResource;
import com.flexicore.model.dynamic.DynamicInvoker;
import com.flexicore.request.*;
import com.flexicore.security.SecurityContext;
import com.flexicore.service.impl.BaseclassService;
import com.flexicore.service.impl.DynamicInvokersService;
import com.wizzdi.flexicore.boot.dynamic.invokers.model.DynamicExecution;
import com.wizzdi.flexicore.boot.dynamic.invokers.request.*;
import com.wizzdi.flexicore.boot.dynamic.invokers.response.InvokerInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Path("/dynamicInvokers")
@RequestScoped
@Component
@Extension
@OperationsInside
@Protected
@Tag(name = "DynamicInvokers")
@Tag(name = "Core")
public class DynamicInvokersRESTService implements RESTService {
	@Autowired
	private DynamicInvokersService service;

	@Autowired
	private BaseclassService baseclassService;


	@POST
	@Produces("application/json")
	@Operation(summary = "getAllInvokers", description = "lists all Invokers")
	@Path("getAllInvokers")
	public PaginationResponse<InvokerInfo> getAllInvokers(
			@HeaderParam("authenticationKey") String authenticationKey,
			DynamicInvokerFilter equipmentInvokersFilter,
			@Context SecurityContext securityContext) {

		return service.getAllInvokersInfo(equipmentInvokersFilter, securityContext);
	}


	@POST
	@Produces("application/json")
	@Operation(summary = "getDynamicExecutionReturnExample", description = "getDynamicExecutionReturnExample")
	@Path("getDynamicExecutionReturnExample")
	public Object getDynamicExecutionReturnExample(
			@HeaderParam("authenticationKey") String authenticationKey,
			DynamicExecutionExampleRequest dynamicExecutionExampleRequest,
			@Context SecurityContext securityContext) {

		return baseclassService.getExample(new GetClassInfo().setClassName(dynamicExecutionExampleRequest.getId()));
	}


	@POST
	@Produces("application/json")
	@Operation(summary = "getAllInvokers", description = "lists all Invokers protected")
	@Path("getAllInvokersProtected")
	public PaginationResponse<InvokerInfo> getAllInvokersProtected(
			@HeaderParam("authenticationKey") String authenticationKey,
			DynamicInvokerFilter equipmentInvokersFilter,
			@Context SecurityContext securityContext) {

		return service.getAllInvokersInfo(equipmentInvokersFilter, securityContext);
	}

	@POST
	@Produces("application/json")
	@Operation(summary = "getAllInvokerOperations", description = "lists all Invoker operations")
	@Path("getAllInvokerOperations")
	public PaginationResponse<com.flexicore.model.Operation> getAllInvokerOperations(
			@HeaderParam("authenticationKey") String authenticationKey,
			InvokersOperationFilter invokersOperationFilter,
			@Context SecurityContext securityContext) {
		List<DynamicInvoker> invokers=!invokersOperationFilter.getInvokerIds().isEmpty()?service.listByIds(DynamicInvoker.class,invokersOperationFilter.getInvokerIds(),securityContext):new ArrayList<>();
		invokersOperationFilter.getInvokerIds().removeAll(invokers.parallelStream().map(f->f.getId()).collect(Collectors.toList()));
		if(!invokersOperationFilter.getInvokerIds().isEmpty()){
			throw new BadRequestException("No Invokers with ids "+invokersOperationFilter.getInvokerIds());
		}
		invokersOperationFilter.setInvokers(invokers);

		return service.getInvokerOperationsPagination(invokersOperationFilter, securityContext);
	}


	@POST
	@Produces("application/json")
	@Operation(summary = "getAllDynamicExecutions", description = "getAllDynamicExecutions")
	@Path("getAllDynamicExecutions")
	public PaginationResponse<DynamicExecution> getAllDynamicExecutions(
			@HeaderParam("authenticationKey") String authenticationKey,
			DynamicExecutionFilter dynamicExecutionFilter,
			@Context SecurityContext securityContext) {

		service.validate(dynamicExecutionFilter,securityContext);

		return service.getAllDynamicExecutions(dynamicExecutionFilter, securityContext);
	}

	@POST
	@Produces("application/json")
	@Operation(summary = "exportDynamicExecutionResultToCSV", description = "exportDynamicExecutionResultToCSV")
	@Path("exportDynamicExecutionResultToCSV")
	public FileResource exportDynamicExecutionResultToCSV(
			@HeaderParam("authenticationKey") String authenticationKey,
			ExportDynamicExecution exportDynamicExecution,
			@Context SecurityContext securityContext) {
		service.validateExportDynamicExecution(exportDynamicExecution,securityContext);

		return service.exportDynamicExecutionResultToCSV(exportDynamicExecution, securityContext);
	}


	@POST
	@Produces("application/json")
	@Operation(summary = "exportDynamicInvokerToCSV", description = "exportDynamicInvokerToCSV")
	@Path("exportDynamicInvokerToCSV")
	public FileResource exportDynamicInvokerToCSV(
			@HeaderParam("authenticationKey") String authenticationKey,
			ExportDynamicInvoker exportDynamicInvoker,
			@Context SecurityContext securityContext) {
		service.validateExportDynamicInvoker(exportDynamicInvoker,securityContext);

		return service.exportDynamicInvokerToCSV(exportDynamicInvoker, securityContext);
	}


	@POST
	@Produces("application/json")
	@Operation(summary = "createDynamicExecution", description = "createDynamicExecution")
	@Path("createDynamicExecution")
	public DynamicExecution createDynamicExecution(
			@HeaderParam("authenticationKey") String authenticationKey,
			CreateDynamicExecution createDynamicExecution,
			@Context SecurityContext securityContext) {


		return service.createDynamicExecution(createDynamicExecution, securityContext);
	}

	@PUT
	@Produces("application/json")
	@Operation(summary = "updateDynamicExecution", description = "updateDynamicExecution")
	@Path("updateDynamicExecution")
	public DynamicExecution updateDynamicExecution(
			@HeaderParam("authenticationKey") String authenticationKey,
			UpdateDynamicExecution updateDynamicExecution,
			@Context SecurityContext securityContext) {
		String id=updateDynamicExecution.getId();
		DynamicExecution dynamicExecution=id!=null?service.getDynamicExectionByIdOrNull(id,DynamicExecution.class,securityContext):null;
		if(dynamicExecution==null){
			throw new BadRequestException("No Dynamic Execution with id "+id);
		}
		updateDynamicExecution.setDynamicExecution(dynamicExecution);

		return service.updateDynamicExecution(updateDynamicExecution, securityContext);
	}


	@POST
	@Produces("application/json")
	@Operation(summary = "executeInvoker", description = "executeInvoker")
	@Path("executeInvoker")
	public ExecuteInvokersResponse executeInvoker(
			@HeaderParam("authenticationKey") String authenticationKey,
			ExecuteInvokerRequest executeInvokerRequest,
			@Context SecurityContext securityContext) {

		return service.executeInvoker(executeInvokerRequest, securityContext);
	}

	@POST
	@Produces("application/json")
	@Operation(summary = "executeDynamicExecution", description = "executeDynamicExecution")
	@Path("executeDynamicExecution")
	public ExecuteInvokersResponse executeDynamicExecution(
			@HeaderParam("authenticationKey") String authenticationKey,
			ExecuteDynamicExecution executeDynamicExecution,
			@Context SecurityContext securityContext) {
	service.validate(executeDynamicExecution,securityContext);
		return service.executeDynamicExecution(executeDynamicExecution, securityContext);
	}

}
