package com.wizzdi.flexicore.security.rest;

import com.flexicore.annotations.IOperation;
import com.flexicore.annotations.OperationsInside;
import com.flexicore.model.security.SecurityPolicy;
import com.flexicore.security.SecurityContextBase;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.security.request.SecurityPolicyCreate;
import com.wizzdi.flexicore.security.request.SecurityPolicyFilter;
import com.wizzdi.flexicore.security.request.SecurityPolicyUpdate;
import com.wizzdi.flexicore.security.response.PaginationResponse;
import com.wizzdi.flexicore.security.service.SecurityPolicyService;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@OperationsInside
@RequestMapping("/securityPolicy")
@Extension
public class SecurityPolicyController implements Plugin {

	@Autowired
	private SecurityPolicyService securityPolicyService;

	@IOperation(Name = "creates security policy",Description = "creates security policy")
	@PostMapping("/create")
	public SecurityPolicy create(@RequestHeader("authenticationKey") String authenticationKey,@RequestBody SecurityPolicyCreate securityPolicyCreate, @RequestAttribute SecurityContextBase securityContext){
		securityPolicyService.validate(securityPolicyCreate,securityContext);
		return securityPolicyService.createSecurityPolicy(securityPolicyCreate,securityContext);
	}

	@IOperation(Name = "returns security policy",Description = "returns security policy")
	@PostMapping("/getAll")
	public PaginationResponse<SecurityPolicy> getAll(@RequestHeader("authenticationKey") String authenticationKey,@RequestBody SecurityPolicyFilter securityPolicyFilter, @RequestAttribute SecurityContextBase securityContext){
		securityPolicyService.validate(securityPolicyFilter,securityContext);
		return securityPolicyService.getAllSecurityPolicies(securityPolicyFilter,securityContext);
	}

	@IOperation(Name = "updates security policy",Description = "updates security policy")
	@PutMapping("/update")
	public SecurityPolicy update(@RequestHeader("authenticationKey") String authenticationKey,@RequestBody SecurityPolicyUpdate securityPolicyUpdate, @RequestAttribute SecurityContextBase securityContext){
		String id=securityPolicyUpdate.getId();
		SecurityPolicy securityPolicy=id!=null? securityPolicyService.getSecurityPolicyByIdOrNull(id,SecurityPolicy.class,securityContext):null;
		if(securityPolicy==null){
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"no security user with id "+id);
		}
		securityPolicyUpdate.setSecurityPolicy(securityPolicy);
		securityPolicyService.validate(securityPolicyUpdate,securityContext);
		return securityPolicyService.updateSecurityPolicy(securityPolicyUpdate,securityContext);
	}
}
