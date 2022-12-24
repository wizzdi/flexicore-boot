package com.wizzdi.flexicore.security.rest;

import com.flexicore.annotations.IOperation;
import com.flexicore.annotations.OperationsInside;
import com.flexicore.model.Role;
import com.flexicore.security.SecurityContextBase;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.security.request.RoleCreate;
import com.wizzdi.flexicore.security.request.RoleFilter;
import com.wizzdi.flexicore.security.request.RoleUpdate;
import com.wizzdi.flexicore.security.response.PaginationResponse;
import com.wizzdi.flexicore.security.service.RoleService;
import com.wizzdi.flexicore.security.validation.Create;
import com.wizzdi.flexicore.security.validation.Update;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@OperationsInside
@RequestMapping("/role")
@Extension
public class RoleController implements Plugin {

    @Autowired
    private RoleService roleService;

    @IOperation(Name = "creates Role", Description = "creates Role")
    @PostMapping("/create")
    public Role create(@RequestBody @Validated(Create.class) RoleCreate roleCreate, @RequestAttribute SecurityContextBase securityContext) {

        return roleService.createRole(roleCreate, securityContext);
    }

    @IOperation(Name = "returns Role", Description = "returns Role")
    @PostMapping("/getAll")
    public PaginationResponse<Role> getAll(@RequestBody @Valid RoleFilter roleFilter, @RequestAttribute SecurityContextBase securityContext) {

        return roleService.getAllRoles(roleFilter, securityContext);
    }

    @IOperation(Name = "updates Role", Description = "updates Role")
    @PutMapping("/update")
    public Role update(@RequestBody @Validated(Update.class) RoleUpdate roleUpdate, @RequestAttribute SecurityContextBase securityContext) {

        return roleService.updateRole(roleUpdate, securityContext);
    }
}
