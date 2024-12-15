package com.wizzdi.flexicore.security.rest;

import com.flexicore.annotations.IOperation;
import com.flexicore.annotations.OperationsInside;
import com.flexicore.model.SecurityUser;
import com.wizzdi.segmantix.model.SecurityContext;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.security.request.SecurityUserCreate;
import com.wizzdi.flexicore.security.request.SecurityUserFilter;
import com.wizzdi.flexicore.security.request.SecurityUserUpdate;
import com.wizzdi.flexicore.security.response.PaginationResponse;
import com.wizzdi.flexicore.security.service.SecurityUserService;
import com.wizzdi.flexicore.security.validation.Create;
import com.wizzdi.flexicore.security.validation.Update;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/securityUser")
@OperationsInside
@Extension
public class SecurityUserController implements Plugin {

    @Autowired
    private SecurityUserService securityUserService;

    @IOperation(Name = "creates security user", Description = "creates security user")
    @PostMapping("/create")
    public SecurityUser create(@RequestBody @Validated(Create.class) SecurityUserCreate securityUserCreate, @RequestAttribute SecurityContext securityContext) {

        return securityUserService.createSecurityUser(securityUserCreate, securityContext);
    }

    @IOperation(Name = "returns security user", Description = "returns security user")
    @PostMapping("/getAll")
    public PaginationResponse<SecurityUser> getAll(@RequestBody @Valid SecurityUserFilter securityUserFilter, @RequestAttribute SecurityContext securityContext) {

        return securityUserService.getAllSecurityUsers(securityUserFilter, securityContext);
    }

    @IOperation(Name = "updates security user", Description = "updates security user")
    @PutMapping("/update")
    public SecurityUser update(@RequestBody @Validated(Update.class) SecurityUserUpdate securityUserUpdate, @RequestAttribute SecurityContext securityContext) {

        return securityUserService.updateSecurityUser(securityUserUpdate, securityContext);
    }
}
