package com.wizzdi.flexicore.security.rest;

import com.flexicore.annotations.IOperation;
import com.flexicore.annotations.OperationsInside;
import com.flexicore.model.security.SecurityPolicy;
import com.wizzdi.flexicore.security.configuration.SecurityContext;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.security.request.SecurityPolicyCreate;
import com.wizzdi.flexicore.security.request.SecurityPolicyFilter;
import com.wizzdi.flexicore.security.request.SecurityPolicyUpdate;
import com.wizzdi.flexicore.security.response.PaginationResponse;
import com.wizzdi.flexicore.security.service.SecurityPolicyService;
import com.wizzdi.flexicore.security.validation.Create;
import com.wizzdi.flexicore.security.validation.Update;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@OperationsInside
@RequestMapping("/securityPolicy")
@Extension
public class SecurityPolicyController implements Plugin {

    @Autowired
    private SecurityPolicyService securityPolicyService;

    @IOperation(Name = "creates security policy", Description = "creates security policy")
    @PostMapping("/create")
    public SecurityPolicy create(@RequestBody @Validated(Create.class) SecurityPolicyCreate securityPolicyCreate, @RequestAttribute SecurityContext securityContext) {

        return securityPolicyService.createSecurityPolicy(securityPolicyCreate, securityContext);
    }

    @IOperation(Name = "returns security policy", Description = "returns security policy")
    @PostMapping("/getAll")
    public PaginationResponse<SecurityPolicy> getAll(@RequestBody @Valid SecurityPolicyFilter securityPolicyFilter, @RequestAttribute SecurityContext securityContext) {

        return securityPolicyService.getAllSecurityPolicies(securityPolicyFilter, securityContext);
    }

    @IOperation(Name = "updates security policy", Description = "updates security policy")
    @PutMapping("/update")
    public SecurityPolicy update(@RequestBody @Validated(Update.class) SecurityPolicyUpdate securityPolicyUpdate, @RequestAttribute SecurityContext securityContext) {

        return securityPolicyService.updateSecurityPolicy(securityPolicyUpdate, securityContext);
    }
}
