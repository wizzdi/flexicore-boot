package com.flexicore.rest;

import com.flexicore.annotations.OperationsInside;
import com.flexicore.annotations.ProtectedREST;
import com.flexicore.model.security.PasswordSecurityPolicy;
import com.flexicore.model.security.SecurityPolicy;
import com.flexicore.request.PasswordSecurityPolicyCreate;
import com.flexicore.request.PasswordSecurityPolicyFilter;
import com.flexicore.request.PasswordSecurityPolicyUpdate;
import com.flexicore.security.SecurityContextBase;
import com.flexicore.service.PasswordSecurityPolicyService;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;

import com.wizzdi.flexicore.security.response.PaginationResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/passwordSecurityPolicy")
@Extension
@Tag(name = "passwordSecurityPolicy")
@ProtectedREST
@OperationsInside
public class PasswordSecurityPolicyController implements Plugin {

	@Autowired
	private PasswordSecurityPolicyService passwordSecurityPolicyService;

	@PostMapping("/create")
	@Operation(description = "creates Security Policy",summary = "creates Security Policy")
	public PasswordSecurityPolicy create(@RequestHeader(value = "authenticationKey",required = false)String key, @RequestBody PasswordSecurityPolicyCreate securityPolicyCreate, @RequestAttribute SecurityContextBase securityContext){
		passwordSecurityPolicyService.validate(securityPolicyCreate,securityContext);
		return passwordSecurityPolicyService.createSecurityPolicy(securityPolicyCreate,securityContext);
	}

	@PostMapping("/getAll")
	@Operation(description = "returns Security Policies",summary = "returns Security Policies")

	public PaginationResponse<PasswordSecurityPolicy> getAll(@RequestHeader(value = "authenticationKey",required = false)String key,@RequestBody PasswordSecurityPolicyFilter securityPolicyFilter,  @RequestAttribute SecurityContextBase securityContext){
		passwordSecurityPolicyService.validate(securityPolicyFilter,securityContext);
		return passwordSecurityPolicyService.getAllSecurityPolicies(securityPolicyFilter,securityContext);
	}

	@PutMapping("/update")
	@Operation(description = "updates Security Policies",summary = "updates Security Policies")

	public PasswordSecurityPolicy update(@RequestHeader(value = "authenticationKey",required = false)String key,@RequestBody PasswordSecurityPolicyUpdate securityPolicyUpdate, @RequestAttribute SecurityContextBase securityContext){
		String id=securityPolicyUpdate.getId();
		PasswordSecurityPolicy passwordSecurityPolicy=id!=null? passwordSecurityPolicyService.getSecurityPolicyByIdOrNull(id,PasswordSecurityPolicy.class,securityContext):null;
		if(passwordSecurityPolicy==null){
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"no PasswordSecurityPolicy user with id "+id);
		}
		securityPolicyUpdate.setPasswordSecurityPolicy(passwordSecurityPolicy);
		passwordSecurityPolicyService.validate(securityPolicyUpdate,securityContext);
		return passwordSecurityPolicyService.updateSecurityPolicy(securityPolicyUpdate,securityContext);
	}
}
