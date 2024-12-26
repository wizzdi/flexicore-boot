package com.wizzdi.flexicore.security.rest;

import com.flexicore.annotations.IOperation;
import com.flexicore.annotations.OperationsInside;
import com.flexicore.model.Basic;
import com.flexicore.model.Clazz;
import com.flexicore.model.PermissionGroupToBaseclass;
import com.wizzdi.flexicore.security.configuration.SecurityContext;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.security.request.BaseclassFilter;
import com.wizzdi.flexicore.security.request.BasicPropertiesFilter;
import com.wizzdi.flexicore.security.request.PermissionGroupToBaseclassCreate;
import com.wizzdi.flexicore.security.request.PermissionGroupToBaseclassFilter;
import com.wizzdi.flexicore.security.request.PermissionGroupToBaseclassUpdate;
import com.wizzdi.flexicore.security.response.PaginationResponse;
import com.wizzdi.flexicore.security.service.BaseclassService;
import com.wizzdi.flexicore.security.service.PermissionGroupToBaseclassService;
import com.wizzdi.flexicore.security.validation.Create;
import com.wizzdi.flexicore.security.validation.Update;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.Set;

@RestController
@OperationsInside
@RequestMapping("/permissionGroupToBaseclass")
@Extension
public class PermissionGroupToBaseclassController implements Plugin {

    @Autowired
    private PermissionGroupToBaseclassService permissionGroupToBaseclassService;
    @Autowired
    private BaseclassService baseclassService;

    @IOperation(Name = "creates PermissionGroupToBaseclass", Description = "creates PermissionGroupToBaseclass")
    @PostMapping("/create")
    public PermissionGroupToBaseclass create(@RequestBody @Validated(Create.class) PermissionGroupToBaseclassCreate permissionGroupToBaseclassCreate, @RequestAttribute SecurityContext securityContext) {


            String securedId = permissionGroupToBaseclassCreate.getSecuredId();
            Clazz securedType = permissionGroupToBaseclassCreate.getSecuredType();
            String name = securedType.name();
            Object secured = baseclassService.listAllBaseclass(new BaseclassFilter().setClazzes(Collections.singletonList(securedType)).setBasicPropertiesFilter(new BasicPropertiesFilter().setOnlyIds(Set.of(securedId))), securityContext).stream().findFirst().orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "no instance of type %s and id %s".formatted(name, securedId)));
            if(secured instanceof Basic basic){
                if(permissionGroupToBaseclassCreate.getName()==null){
                    permissionGroupToBaseclassCreate.setName(basic.getName());
                }
                if(permissionGroupToBaseclassCreate.getSecuredCreationDate()==null){
                   permissionGroupToBaseclassCreate.setSecuredCreationDate(basic.getCreationDate());
                }
            }
        return permissionGroupToBaseclassService.createPermissionGroupToBaseclass(permissionGroupToBaseclassCreate, securityContext);
    }

    @IOperation(Name = "returns PermissionGroupToBaseclass", Description = "returns PermissionGroupToBaseclass")
    @PostMapping("/getAll")
    public PaginationResponse<PermissionGroupToBaseclass> getAll(@RequestBody @Valid PermissionGroupToBaseclassFilter permissionGroupToBaseclassFilter, @RequestAttribute SecurityContext securityContext) {

        return permissionGroupToBaseclassService.getAllPermissionGroupToBaseclass(permissionGroupToBaseclassFilter, securityContext);
    }

    @IOperation(Name = "updates PermissionGroupToBaseclass", Description = "updates PermissionGroupToBaseclass")
    @PutMapping("/update")
    public PermissionGroupToBaseclass update(@RequestBody @Validated(Update.class) PermissionGroupToBaseclassUpdate permissionGroupToBaseclassUpdate, @RequestAttribute SecurityContext securityContext) {

        return permissionGroupToBaseclassService.updatePermissionGroupToBaseclass(permissionGroupToBaseclassUpdate, securityContext);
    }
}
