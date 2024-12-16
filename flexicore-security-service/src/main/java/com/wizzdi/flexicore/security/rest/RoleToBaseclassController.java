package com.wizzdi.flexicore.security.rest;

import com.flexicore.annotations.IOperation;
import com.flexicore.annotations.OperationsInside;
import com.flexicore.model.RoleToBaseclass;
import com.wizzdi.flexicore.security.configuration.SecurityContext;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.security.request.RoleToBaseclassCreate;
import com.wizzdi.flexicore.security.request.RoleToBaseclassFilter;
import com.wizzdi.flexicore.security.request.RoleToBaseclassUpdate;
import com.wizzdi.flexicore.security.response.PaginationResponse;
import com.wizzdi.flexicore.security.service.RoleToBaseclassService;
import com.wizzdi.flexicore.security.validation.Create;
import com.wizzdi.flexicore.security.validation.Update;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@OperationsInside
@RequestMapping("/roleToBaseclass")
@Extension
public class RoleToBaseclassController implements Plugin {

    @Autowired
    private RoleToBaseclassService roleToBaseclassService;

    @IOperation(Name = "creates RoleToBaseclass", Description = "creates RoleToBaseclass")
    @PostMapping("/create")
    public RoleToBaseclass create(@RequestBody @Validated(Create.class) RoleToBaseclassCreate roleToBaseclassCreate, @RequestAttribute SecurityContext securityContext) {

        return roleToBaseclassService.createRoleToBaseclass(roleToBaseclassCreate, securityContext);
    }

    @IOperation(Name = "returns RoleToBaseclass", Description = "returns RoleToBaseclass")
    @PostMapping("/getAll")
    public PaginationResponse<RoleToBaseclass> getAll(@RequestBody @Valid RoleToBaseclassFilter roleToBaseclassFilter, @RequestAttribute SecurityContext securityContext) {

        return roleToBaseclassService.getAllRoleToBaseclass(roleToBaseclassFilter, securityContext);
    }

    @IOperation(Name = "updates RoleToBaseclass", Description = "updates RoleToBaseclass")
    @PutMapping("/update")
    public RoleToBaseclass update(@RequestBody @Validated(Update.class) RoleToBaseclassUpdate roleToBaseclassUpdate, @RequestAttribute SecurityContext securityContext) {

        return roleToBaseclassService.updateRoleToBaseclass(roleToBaseclassUpdate, securityContext);
    }
}
