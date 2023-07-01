package com.wizzdi.flexicore.security.rest;

import com.flexicore.annotations.IOperation;
import com.flexicore.annotations.OperationsInside;
import com.flexicore.model.RoleToUser;
import com.flexicore.security.SecurityContextBase;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.security.request.RoleToUserCreate;
import com.wizzdi.flexicore.security.request.RoleToUserFilter;
import com.wizzdi.flexicore.security.request.RoleToUserUpdate;
import com.wizzdi.flexicore.security.response.PaginationResponse;
import com.wizzdi.flexicore.security.service.RoleToUserService;
import com.wizzdi.flexicore.security.validation.Create;
import com.wizzdi.flexicore.security.validation.Update;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@OperationsInside
@RequestMapping("/roleToUser")
@Extension
public class RoleToUserController implements Plugin {

    @Autowired
    private RoleToUserService roleToUserService;

    @IOperation(Name = "creates RoleToUser", Description = "creates RoleToUser")
    @PostMapping("/create")
    public RoleToUser create(@RequestBody @Validated(Create.class) RoleToUserCreate roleToUserCreate, @RequestAttribute SecurityContextBase securityContext) {

        return roleToUserService.createRoleToUser(roleToUserCreate, securityContext);
    }

    @IOperation(Name = "returns RoleToUser", Description = "returns RoleToUser")
    @PostMapping("/getAll")
    public PaginationResponse<RoleToUser> getAll(@RequestBody @Valid RoleToUserFilter roleToUserFilter, @RequestAttribute SecurityContextBase securityContext) {

        return roleToUserService.getAllRoleToUsers(roleToUserFilter, securityContext);
    }

    @IOperation(Name = "updates RoleToUser", Description = "updates RoleToUser")
    @PutMapping("/update")
    public RoleToUser update(@RequestBody @Validated(Update.class) RoleToUserUpdate roleToUserUpdate, @RequestAttribute SecurityContextBase securityContext) {

        return roleToUserService.updateRoleToUser(roleToUserUpdate, securityContext);
    }
}
