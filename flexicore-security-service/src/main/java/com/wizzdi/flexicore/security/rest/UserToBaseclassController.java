package com.wizzdi.flexicore.security.rest;

import com.flexicore.annotations.IOperation;
import com.flexicore.annotations.OperationsInside;
import com.flexicore.model.UserToBaseclass;
import com.wizzdi.segmantix.model.SecurityContext;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.security.request.UserToBaseclassCreate;
import com.wizzdi.flexicore.security.request.UserToBaseclassFilter;
import com.wizzdi.flexicore.security.request.UserToBaseclassUpdate;
import com.wizzdi.flexicore.security.response.PaginationResponse;
import com.wizzdi.flexicore.security.service.UserToBaseclassService;
import com.wizzdi.flexicore.security.validation.Create;
import com.wizzdi.flexicore.security.validation.Update;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@OperationsInside
@RequestMapping("/userToBaseclass")
@Extension
public class UserToBaseclassController implements Plugin {

    @Autowired
    private UserToBaseclassService userToBaseclassService;

    @IOperation(Name = "create user to baseclass", Description = "creates user to baseclass")
    @PostMapping("/create")
    public UserToBaseclass create(@RequestBody @Validated(Create.class) UserToBaseclassCreate userToBaseclassCreate, @RequestAttribute SecurityContext securityContext) {

        return userToBaseclassService.createUserToBaseclass(userToBaseclassCreate, securityContext);
    }

    @IOperation(Name = "returns user to baseclass", Description = "returns user to baseclass")

    @PostMapping("/getAll")
    public PaginationResponse<UserToBaseclass> getAll(@RequestBody @Valid UserToBaseclassFilter userToBaseclassFilter, @RequestAttribute SecurityContext securityContext) {

        return userToBaseclassService.getAllUserToBaseclass(userToBaseclassFilter, securityContext);
    }

    @IOperation(Name = "updates user to baseclass", Description = "updates user to baseclass")

    @PutMapping("/update")
    public UserToBaseclass update(@RequestBody @Validated(Update.class) UserToBaseclassUpdate userToBaseclassUpdate, @RequestAttribute SecurityContext securityContext) {

        return userToBaseclassService.updateUserToBaseclass(userToBaseclassUpdate, securityContext);
    }
}
