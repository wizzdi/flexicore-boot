package com.wizzdi.flexicore.security.rest;

import com.flexicore.annotations.IOperation;
import com.flexicore.annotations.OperationsInside;
import com.flexicore.model.PermissionGroup;
import com.flexicore.security.SecurityContextBase;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.security.request.PermissionGroupCreate;
import com.wizzdi.flexicore.security.request.PermissionGroupFilter;
import com.wizzdi.flexicore.security.request.PermissionGroupUpdate;
import com.wizzdi.flexicore.security.response.PaginationResponse;
import com.wizzdi.flexicore.security.service.PermissionGroupService;
import com.wizzdi.flexicore.security.validation.Create;
import com.wizzdi.flexicore.security.validation.Update;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@OperationsInside
@RequestMapping("/permissionGroup")
@Extension
public class PermissionGroupController implements Plugin {

    @Autowired
    private PermissionGroupService permissionGroupService;

    @IOperation(Name = "returns PermissionGroup", Description = "returns PermissionGroup")
    @PostMapping("/create")
    public PermissionGroup create(@RequestBody @Validated(Create.class) PermissionGroupCreate permissionGroupCreate, @RequestAttribute SecurityContextBase securityContext) {

        return permissionGroupService.createPermissionGroup(permissionGroupCreate, securityContext);
    }

    @IOperation(Name = "returns PermissionGroup", Description = "returns PermissionGroup")
    @PostMapping("/getAll")
    public PaginationResponse<PermissionGroup> getAll(@RequestBody @Valid PermissionGroupFilter permissionGroupFilter, @RequestAttribute SecurityContextBase securityContext) {

        return permissionGroupService.getAllPermissionGroups(permissionGroupFilter, securityContext);
    }

    @IOperation(Name = "updates PermissionGroup", Description = "updates PermissionGroup")
    @PutMapping("/update")
    public PermissionGroup update(@RequestBody @Validated(Update.class) PermissionGroupUpdate permissionGroupUpdate, @RequestAttribute SecurityContextBase securityContext) {

        return permissionGroupService.updatePermissionGroup(permissionGroupUpdate, securityContext);
    }
}
