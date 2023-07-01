package com.flexicore.rest;

import com.flexicore.annotations.OperationsInside;
import com.flexicore.annotations.ProtectedREST;
import com.flexicore.model.security.TotpSecurityPolicy;
import com.flexicore.model.security.SecurityPolicy;
import com.flexicore.request.TotpSecurityPolicyCreate;
import com.flexicore.request.TotpSecurityPolicyFilter;
import com.flexicore.request.TotpSecurityPolicyUpdate;
import com.flexicore.security.SecurityContextBase;
import com.flexicore.service.TotpSecurityPolicyService;
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
@RequestMapping("/totpSecurityPolicy")
@Extension
@Tag(name = "totpSecurityPolicy")
@ProtectedREST
@OperationsInside
public class TotpSecurityPolicyController implements Plugin {

	@Autowired
	private TotpSecurityPolicyService totpSecurityPolicyService;

	@PostMapping("/create")
	@Operation(description = "creates Security Policy",summary = "creates Security Policy")
	public SecurityPolicy create(@RequestHeader(value = "authenticationKey",required = false)String key, @RequestBody TotpSecurityPolicyCreate securityPolicyCreate, @RequestAttribute SecurityContextBase securityContext){
		totpSecurityPolicyService.validate(securityPolicyCreate,securityContext);
		return totpSecurityPolicyService.createSecurityPolicy(securityPolicyCreate,securityContext);
	}

	@PostMapping("/getAll")
	@Operation(description = "returns Security Policies",summary = "returns Security Policies")

	public PaginationResponse<TotpSecurityPolicy> getAll(@RequestHeader(value = "authenticationKey",required = false)String key,@RequestBody TotpSecurityPolicyFilter securityPolicyFilter,  @RequestAttribute SecurityContextBase securityContext){
		totpSecurityPolicyService.validate(securityPolicyFilter,securityContext);
		return totpSecurityPolicyService.getAllSecurityPolicies(securityPolicyFilter,securityContext);
	}

	@PutMapping("/update")
	@Operation(description = "updates Security Policies",summary = "updates Security Policies")

	public TotpSecurityPolicy update(@RequestHeader(value = "authenticationKey",required = false)String key,@RequestBody TotpSecurityPolicyUpdate securityPolicyUpdate, @RequestAttribute SecurityContextBase securityContext){
		String id=securityPolicyUpdate.getId();
		TotpSecurityPolicy totpSecurityPolicy=id!=null? totpSecurityPolicyService.getSecurityPolicyByIdOrNull(id,TotpSecurityPolicy.class,securityContext):null;
		if(totpSecurityPolicy==null){
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"no TotpSecurityPolicy user with id "+id);
		}
		securityPolicyUpdate.setTotpSecurityPolicy(totpSecurityPolicy);
		totpSecurityPolicyService.validate(securityPolicyUpdate,securityContext);
		return totpSecurityPolicyService.updateSecurityPolicy(securityPolicyUpdate,securityContext);
	}
}
