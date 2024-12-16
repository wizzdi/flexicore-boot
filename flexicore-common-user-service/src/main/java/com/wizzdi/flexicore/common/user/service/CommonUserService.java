/*******************************************************************************
 *  Copyright (C) FlexiCore, Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Avishay Ben Natan And Asaf Ben Natan, October 2015
 ******************************************************************************/
/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.wizzdi.flexicore.common.user.service;


import com.flexicore.model.SecurityTenant;
import com.flexicore.model.TenantToUser;
import com.flexicore.model.User;
import com.wizzdi.flexicore.security.configuration.SecurityContext;
import com.wizzdi.flexicore.common.user.data.CommonUserRepository;
import com.wizzdi.flexicore.common.user.request.CommonUserCreate;
import com.wizzdi.flexicore.common.user.request.CommonUserFilter;
import com.wizzdi.flexicore.common.user.request.CommonUserUpdate;
import com.wizzdi.flexicore.security.request.TenantToUserCreate;
import com.wizzdi.flexicore.security.response.PaginationResponse;
import com.wizzdi.flexicore.security.service.BaseclassService;
import com.wizzdi.flexicore.security.service.SecurityUserService;
import com.wizzdi.flexicore.security.service.TenantToUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Component
public class CommonUserService  {

    private static final Logger logger = LoggerFactory.getLogger(CommonUserService.class);


    @Autowired
    private CommonUserRepository commonUserRepository;

    @Autowired
    @Lazy
    private SecurityUserService securityUserService;


    @Autowired
    @Lazy
    private TenantToUserService tenantToUserService;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Value("${flexicore.users.rootDirPath:/home/flexicore/users/}")
    private String usersRootHomeDir;


    
    public User createUser(CommonUserCreate commonUserCreate, SecurityContext securityContext) {
        List<Object> toMerge = new ArrayList<>();
        SecurityTenant securityTenant =  commonUserCreate.getTenant();
        User user = createUserNoMerge(commonUserCreate, securityContext);
        toMerge.add(user);
        TenantToUserCreate tenantToUserCreate = new TenantToUserCreate().setDefaultTenant(true).setUser(user).setTenant(securityTenant);
        TenantToUser tenantToUser = tenantToUserService.createTenantToUserNoMerge(tenantToUserCreate, securityContext);
        toMerge.add(tenantToUser);
        commonUserRepository.massMerge(toMerge);
        user.getTenants().add(tenantToUser);
        return user;
    }
    public User createUserPlain(CommonUserCreate commonUserCreate, SecurityContext securityContext) {
        User user = createUserNoMerge(commonUserCreate, securityContext);
        commonUserRepository.merge(user);
        return user;
    }


    
    public User createUserNoMerge(CommonUserCreate createUser, SecurityContext securityContext) {
        User user = new User();
        user.setId(UUID.randomUUID().toString());
        updateUserNoMerge(user, createUser);
        BaseclassService.createSecurityObjectNoMerge(user, securityContext);
        return user;
    }

    public String generateUserHomeDir(User user) {
        return new File(usersRootHomeDir, user.getName() + user.getId()).getAbsolutePath();
    }

    
    public boolean updateUserNoMerge(User user, CommonUserCreate createUser) {
        boolean update = securityUserService.updateSecurityUserNoMerge(createUser, user);
        if(user.getHomeDir()==null && createUser.getHomeDir()==null){
            createUser.setHomeDir(generateUserHomeDir(user));
        }

        if (createUser.getEmail() != null && !createUser.getEmail().equals(user.getEmail())) {
            user.setEmail(createUser.getEmail());
            update = true;
        }
        if (createUser.getHomeDir() != null && !createUser.getHomeDir().equals(user.getHomeDir())) {
            user.setHomeDir(createUser.getHomeDir());
            update = true;
        }

        if (createUser.getPhoneNumber() != null && !createUser.getPhoneNumber().equals(user.getPhoneNumber())) {
            user.setPhoneNumber(createUser.getPhoneNumber());
            update = true;
        }
        if (createUser.getTotpSalt() != null && !createUser.getTotpSalt().equals(user.getTotpSalt())) {
            user.setTotpSalt(createUser.getTotpSalt());
            update = true;
        }

        if (createUser.getUiConfiguration() != null && !createUser.getUiConfiguration().equals(user.getUiConfiguration())) {
            user.setUiConfiguration(createUser.getUiConfiguration());
            update = true;
        }


        if (createUser.getLastName() != null && !createUser.getLastName().equals(user.getLastName())) {
            user.setLastName(createUser.getLastName());
            update = true;
        }

        if (createUser.getDisabled() != null && createUser.getDisabled() != user.isDisabled()) {
            user.setDisabled(createUser.getDisabled());
            update = true;
        }

        if (createUser.getApprovingUser() != null && (user.getApprovingUser() == null || !createUser.getApprovingUser().getId().equals(user.getApprovingUser().getId()))) {
            user.setApprovingUser(createUser.getApprovingUser());
            update = true;
        }

        if (createUser.getDateApproved() != null && !createUser.getDateApproved().equals(user.getDateApproved())) {
            user.setDateApproved(createUser.getDateApproved());
            update = true;
        }


        if (createUser.getPassword() != null) {

            String hash = passwordEncoder.encode(createUser.getPassword());
            if (!hash.equals(user.getPassword())) {
                user.setPassword(hash);
                update = true;
            }
        }
        return update;
    }





    public User updateUser(CommonUserUpdate userUpdate, SecurityContext securityContext) {
        User user = userUpdate.getUser();
        if (updateUserNoMerge(user, userUpdate)) {
            commonUserRepository.merge(user);
        }
        return user;
    }

    
    public PaginationResponse<User> getAllUsers(CommonUserFilter commonUserFilter, SecurityContext securityContext) {
        List<User> list = listAllUsers(commonUserFilter, securityContext);
        long count = commonUserRepository.countAllUsers(commonUserFilter, securityContext);
        return new PaginationResponse<>(list, commonUserFilter, count);
    }

    
    public List<User> listAllUsers(CommonUserFilter commonUserFilter, SecurityContext securityContext) {
        return commonUserRepository.getAllUsers(commonUserFilter, securityContext);
    }


}
