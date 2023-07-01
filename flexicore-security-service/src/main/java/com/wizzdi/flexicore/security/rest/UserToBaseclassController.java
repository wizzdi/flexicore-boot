package com.wizzdi.flexicore.security.rest;

import com.flexicore.annotations.IOperation;
import com.flexicore.annotations.OperationsInside;
import com.flexicore.model.UserToBaseClass;
import com.flexicore.security.SecurityContextBase;
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
    public UserToBaseClass create(@RequestBody @Validated(Create.class) UserToBaseclassCreate userToBaseclassCreate, @RequestAttribute SecurityContextBase securityContext) {

        return userToBaseclassService.createUserToBaseclass(userToBaseclassCreate, securityContext);
    }

    @IOperation(Name = "returns user to baseclass", Description = "returns user to baseclass")

    @PostMapping("/getAll")
    public PaginationResponse<UserToBaseClass> getAll(@RequestBody @Valid UserToBaseclassFilter userToBaseclassFilter, @RequestAttribute SecurityContextBase securityContext) {

        return userToBaseclassService.getAllUserToBaseclass(userToBaseclassFilter, securityContext);
    }

    @IOperation(Name = "updates user to baseclass", Description = "updates user to baseclass")

    @PutMapping("/update")
    public UserToBaseClass update(@RequestBody @Validated(Update.class) UserToBaseclassUpdate userToBaseclassUpdate, @RequestAttribute SecurityContextBase securityContext) {

        return userToBaseclassService.updateUserToBaseclass(userToBaseclassUpdate, securityContext);
    }
}
