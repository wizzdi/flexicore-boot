/*******************************************************************************
 *  Copyright (C) FlexiCore, Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Avishay Ben Natan And Asaf Ben Natan, October 2015
 ******************************************************************************/
package com.wizzdi.flexicore.common.user.rest;

import com.flexicore.annotations.IOperation;
import com.flexicore.annotations.IOperation.Access;
import com.flexicore.annotations.OperationsInside;
import com.flexicore.model.User;
import com.flexicore.security.SecurityContextBase;
import com.wizzdi.flexicore.common.user.request.CommonUserCreate;
import com.wizzdi.flexicore.common.user.request.CommonUserFilter;
import com.wizzdi.flexicore.common.user.request.CommonUserUpdate;
import com.wizzdi.flexicore.common.user.service.CommonUserService;
import com.wizzdi.flexicore.security.response.PaginationResponse;
import com.wizzdi.flexicore.security.validation.Create;
import com.wizzdi.flexicore.security.validation.Update;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@OperationsInside
@Tag(name = "Common User")
@RequestMapping("/commonUser")
public class CommonUserController {


    @Autowired
    private CommonUserService commonUserService;
   private static final Logger logger = LoggerFactory.getLogger(CommonUserController.class);




    @PostMapping("/getAllUsers")
    @IOperation(access = Access.allow, Name = "getAllUsers", Description = "lists Users", relatedClazzes = {User.class})
    public PaginationResponse<User> getAllUsers(@RequestBody @Valid CommonUserFilter commonUserFilter,
                                                @RequestAttribute SecurityContextBase securityContext) {
        return commonUserService.getAllUsers(commonUserFilter, securityContext);

    }


    @PostMapping("/createUser")
    @IOperation(access = Access.allow, Name = "Creates User", Description = "Creates User", relatedClazzes = {User.class})
    public User createUser( @RequestBody @Validated(Create.class) CommonUserCreate commonUserCreate,
                            @RequestAttribute SecurityContextBase securityContext) {
        return commonUserService.createUser(commonUserCreate, securityContext);

    }

    @PutMapping("/updateUser")
    @IOperation(access = Access.allow, Name = "Updates User", Description = "Updates User", relatedClazzes = {User.class})
    public User updateUser(@RequestBody @Validated(Update.class) CommonUserUpdate userUpdate,
                           @RequestAttribute SecurityContextBase securityContext) {
        return commonUserService.updateUser(userUpdate, securityContext);

    }


}
