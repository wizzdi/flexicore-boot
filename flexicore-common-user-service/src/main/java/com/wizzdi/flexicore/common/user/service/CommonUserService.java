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


import com.wizzdi.flexicore.common.user.data.CommonUserRepository;
import com.flexicore.model.SecurityTenant;
import com.flexicore.model.TenantToUser;
import com.flexicore.model.User;
import com.wizzdi.flexicore.common.user.request.CommonUserCreate;
import com.wizzdi.flexicore.common.user.request.CommonUserFilter;
import com.wizzdi.flexicore.common.user.request.CommonUserUpdate;
import com.flexicore.security.SecurityContextBase;
import com.lambdaworks.crypto.SCryptUtil;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.security.request.TenantToUserCreate;
import com.wizzdi.flexicore.security.response.PaginationResponse;
import com.wizzdi.flexicore.security.service.SecurityUserService;
import com.wizzdi.flexicore.security.service.TenantToUserService;
import org.pf4j.Extension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;


@Component
@Extension
public class CommonUserService implements Plugin {

    private static final Logger logger = LoggerFactory.getLogger(CommonUserService.class);


    @Autowired
    private CommonUserRepository commonUserRepository;

    @Autowired
    private SecurityUserService securityUserService;


    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    private TenantToUserService tenantToUserService;

    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    @Qualifier("systemAdminId")
    public static String systemAdminId(){
        return "UEKbB6XlQhKOtjziJoUQ8w";
    }

    @Autowired
    @Qualifier("systemAdminId")
    private String systemAdminId;


    @Value("${flexicore.users.rootDirPath:/home/flexicore/users/}")
    private String usersRootHomeDir;


    @Value("${flexicore.security.password.scryptN:16384}")
    private int scryptN;
    @Value("${flexicore.security.password.scryptR:8}")
    private int scryptR;
    @Value("${flexicore.security.password.scryptP:1}")
    private int scryptP;



    
    public User createUser(CommonUserCreate commonUserCreate, SecurityContextBase securityContextBase) {
        List<Object> toMerge = new ArrayList<>();
        SecurityTenant securityTenant =  commonUserCreate.getTenant();
        User user = createUserNoMerge(commonUserCreate, securityContextBase);
        toMerge.add(user);
        TenantToUserCreate tenantToUserCreate = new TenantToUserCreate().setDefaultTenant(true).setSecurityUser(user).setTenant(securityTenant);
        TenantToUser tenantToUser = tenantToUserService.createTenantToUserNoMerge(tenantToUserCreate, securityContextBase);
        toMerge.add(tenantToUser);
        commonUserRepository.massMerge(toMerge);
        user.getTenantToUsers().add(tenantToUser);
        return user;
    }



    
    public User createUserNoMerge(CommonUserCreate createUser, SecurityContextBase securityContextBase) {
        User user = new User(createUser.getName(), securityContextBase);
        updateUserNoMerge(user, createUser);
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


        if (createUser.getLastName() != null && !createUser.getLastName().equals(user.getSurName())) {
            user.setSurName(createUser.getLastName());
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

            String hash = hashPassword(createUser.getPassword());
            if (!hash.equals(user.getPassword())) {
                user.setPassword(hash);
                update = true;
            }
        }
        return update;
    }

    private String hashPassword(String plain) {
        return SCryptUtil.scrypt(plain, scryptN, scryptR, scryptP);
    }


    public void validateUserForCreate(CommonUserCreate commonUserCreate, SecurityContextBase securityContextBase) {
        validateUser(commonUserCreate, securityContextBase);
        CommonUserFilter commonUserFilter = new CommonUserFilter();
        if(commonUserCreate.getEmail()!=null){
            commonUserFilter.setEmails(Collections.singleton(commonUserCreate.getEmail()));
        }
        else{
            commonUserFilter.setPhoneNumbers(Collections.singleton(commonUserCreate.getPhoneNumber()));

        }
        List<User> existing =listAllUsers(commonUserFilter,null);
        if (!existing.isEmpty()) {
            String cause = commonUserCreate.getEmail() != null ? "Email" : "PhoneNumber";
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Cannot create User " + cause + " is not unique");
        }

    }

    
    public void validateUserUpdate(CommonUserUpdate userUpdate, SecurityContextBase securityContextBase) {
        securityUserService.validate(userUpdate, securityContextBase);
        User user = commonUserRepository.getByIdOrNull(userUpdate.getId(), User.class, null);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"No User With id " + userUpdate.getId());
        }
        userUpdate.setUser(user);
        if(userUpdate.getEmail()!=null||userUpdate.getPassword()!=null){
            CommonUserFilter commonUserFilter = new CommonUserFilter();
            if(userUpdate.getEmail()!=null){
                commonUserFilter.setEmails(Collections.singleton(userUpdate.getEmail()));
            }
            else{
                commonUserFilter.setPhoneNumbers(Collections.singleton(userUpdate.getPhoneNumber()));

            }
            List<User> existing =listAllUsers(commonUserFilter,null);
            if (existing.size()==1 && !user.getId().equals(existing.get(0).getId())) {
                String cause = userUpdate.getEmail() != null ? "Email" : "PhoneNumber";
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Cannot Update User " + cause + " is not unique");
            }
        }


    }

    
    public void validateUser(CommonUserCreate commonUserCreate, SecurityContextBase securityContextBase) {
        securityUserService.validate(commonUserCreate, securityContextBase);
        SecurityTenant securityTenant = commonUserCreate.getTenant();
        if (securityTenant == null) {
            securityTenant = securityContextBase.getTenantToCreateIn() != null ? securityContextBase.getTenantToCreateIn() : (securityContextBase.getTenants().isEmpty() ? null : (SecurityTenant) securityContextBase.getTenants().get(0));
        }
        if (securityTenant == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Could not determine securityTenant to create user in");
        }
        commonUserCreate.setTenant(securityTenant);
        if (commonUserCreate.getPhoneNumber() == null && commonUserCreate.getEmail() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Phone Number or Email Must Be Provided");
        }




    }

    public User updateUser(CommonUserUpdate userUpdate, SecurityContextBase securityContextBase) {
        User user = userUpdate.getUser();
        if (updateUserNoMerge(user, userUpdate)) {
            commonUserRepository.merge(user);
        }
        return user;
    }

    
    public PaginationResponse<User> getAllUsers(CommonUserFilter commonUserFilter, SecurityContextBase securityContextBase) {
        List<User> list = listAllUsers(commonUserFilter, securityContextBase);
        long count = commonUserRepository.countAllUsers(commonUserFilter, securityContextBase);
        return new PaginationResponse<>(list, commonUserFilter, count);
    }

    
    public List<User> listAllUsers(CommonUserFilter commonUserFilter, SecurityContextBase securityContextBase) {
        return commonUserRepository.getAllUsers(commonUserFilter, securityContextBase);
    }

    public void validate(CommonUserFilter commonUserFilter, SecurityContextBase securityContextBase) {

        Set<String> securityTenantIds = commonUserFilter.getUserSecurityTenantsIds();
        Map<String, SecurityTenant> map = securityTenantIds.isEmpty() ? new HashMap<>() : commonUserRepository.listByIds(SecurityTenant.class, securityTenantIds, securityContextBase).parallelStream().collect(Collectors.toMap(f -> f.getId(), f -> f));
        securityTenantIds.removeAll(map.keySet());
        if (!securityTenantIds.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"No SecurityTenant with ids " + securityTenantIds);
        }
        commonUserFilter.setUserSecurityTenants(new ArrayList<>(map.values()));

    }

}
