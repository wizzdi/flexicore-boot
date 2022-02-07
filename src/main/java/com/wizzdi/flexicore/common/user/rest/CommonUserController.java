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
import com.wizzdi.flexicore.common.user.request.CommonUserCreate;
import com.wizzdi.flexicore.common.user.request.CommonUserFilter;
import com.wizzdi.flexicore.common.user.request.CommonUserUpdate;
import com.flexicore.security.SecurityContextBase;
import com.wizzdi.flexicore.common.user.service.CommonUserService;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.security.response.PaginationResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.pf4j.Extension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;


@Component
@OperationsInside
@Tag(name = "Common User")
@Extension
@RequestMapping("/commonUser")
public class CommonUserController implements Plugin {


    @Autowired
    private CommonUserService commonUserService;
   private static final Logger logger = LoggerFactory.getLogger(CommonUserController.class);




    @PostMapping("/getAllUsers")
    @IOperation(access = Access.allow, Name = "getAllUsers", Description = "lists Users", relatedClazzes = {User.class})
    public PaginationResponse<User> getAllUsers(@RequestHeader("authenticationkey") String authenticationkey
            , @RequestBody CommonUserFilter commonUserFilter, @RequestAttribute SecurityContextBase securityContextBase) {
        commonUserService.validate(commonUserFilter, securityContextBase);
        return commonUserService.getAllUsers(commonUserFilter, securityContextBase);

    }


    @PostMapping("/createUser")
    @IOperation(access = Access.allow, Name = "Creates User", Description = "Creates User", relatedClazzes = {User.class})
    public User createUser(@RequestHeader("authenticationkey") String authenticationkey
            , @RequestBody CommonUserCreate commonUserCreate, @RequestAttribute SecurityContextBase securityContextBase) {
        commonUserService.validateUserForCreate(commonUserCreate, securityContextBase);
        return commonUserService.createUser(commonUserCreate, securityContextBase);

    }

    @PutMapping("/updateUser")
    @IOperation(access = Access.allow, Name = "Updates User", Description = "Updates User", relatedClazzes = {User.class})
    public User updateUser(@RequestHeader("authenticationkey") String authenticationkey
            , @RequestBody CommonUserUpdate userUpdate, @RequestAttribute SecurityContextBase securityContextBase) {
        commonUserService.validateUserUpdate(userUpdate, securityContextBase);
        return commonUserService.updateUser(userUpdate, securityContextBase);

    }


}
