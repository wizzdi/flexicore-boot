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
package com.flexicore.service.impl;

import com.flexicore.data.BaselinkRepository;
import com.flexicore.data.TenantRepository;
import com.flexicore.data.UserRepository;
import com.flexicore.data.jsoncontainers.*;
import com.flexicore.events.LoginEvent;
import com.flexicore.exceptions.BadRequestCustomException;
import com.flexicore.exceptions.CheckYourCredentialsException;
import com.flexicore.exceptions.UserNotFoundException;
import com.flexicore.model.*;
import com.flexicore.model.security.PasswordSecurityPolicy;
import com.flexicore.model.security.SecurityPolicy;
import com.flexicore.request.*;
import com.flexicore.response.*;
import com.flexicore.security.AuthenticationRequestHolder;
import com.flexicore.security.RunningUser;
import com.flexicore.security.SecurityContext;
import com.flexicore.service.PasswordSecurityPolicyService;
import com.flexicore.service.TokenService;

import com.lambdaworks.crypto.SCryptUtil;
import com.wizzdi.flexicore.security.service.SecurityPolicyService;
import com.wizzdi.flexicore.security.service.SecurityUserService;
import org.pf4j.Extension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.core.Response;

import java.io.File;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Primary
@Component
@Extension
public class UserService implements com.flexicore.service.UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private java.util.logging.Logger utilLogger;

    @Autowired
    private UserRepository userrepository;

    @Autowired
    private TenantRepository tenantRepository;

    @Autowired
    private BaselinkRepository baselinkRepository;

    @Autowired
    private BaseclassNewService baseclassService;
    @Autowired
    private SecurityUserService securityUserService;

    @Autowired
    private TokenService tokenService;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    @Qualifier("systemAdminId")
    public static String systemAdminId() {
        return "UEKbB6XlQhKOtjziJoUQ8w";
    }

    @Value("${flexicore.login.maxFailedAttempts:-1}")
    private int maxFailedAttemptsAllowed;

    @Autowired
    @Qualifier("systemAdminId")
    private String systemAdminId;


    @Value("${flexicore.users.rootDirPath:/home/flexicore/users/}")
    private String usersRootHomeDir;
    @Value("${flexicore.users.cache.size:10}")
    private int usersMaxCacheSize;
    @Value("${flexicore.security.jwt.secondsValid:86400}")
    private long jwtSecondsValid;
    @Value("${flexicore.users.verificationLink.minutesValid:30}")
    private int verificationLinkValidInMin;

    @Value("${flexicore.security.password.scryptN:16384}")
    private int scryptN;
    @Value("${flexicore.security.password.scryptR:8}")
    private int scryptR;
    @Value("${flexicore.security.password.scryptP:1}")
    private int scryptP;

    @Autowired
    private PasswordSecurityPolicyService passwordSecurityPolicyService;
    @Autowired
    private SecurityPolicyService securityPolicyService;


    @Override
    public <T extends Baseclass> List<T> getAllFiltered(QueryInformationHolder<T> queryInformationHolder) {
        return userrepository.getAllFiltered(queryInformationHolder);
    }

    @Override
    public User findById(String id) {
        return userrepository.findById(id);
    }


    /**
     *
     */


    @Override
    public void massMerge(List<?> toMerge) {
        userrepository.massMerge(toMerge);
    }


    @Override
    public User createUser(UserCreate userCreate, SecurityContext securityContext) {
        List<Object> toMerge = new ArrayList<>();
        Tenant tenant = (Tenant) userCreate.getTenant();
        User user = createUserNoMerge(userCreate, securityContext);
        toMerge.add(user);
        TenantToUserCreate tenantToUserCreate = new TenantToUserCreate().setDefaultTenant(true).setUser(user).setTenant(tenant);
        TenantToUser tenantToUser = createTenantToUserNoMerge(tenantToUserCreate, securityContext);
        toMerge.add(tenantToUser);
        userrepository.massMerge(toMerge);
        user.getTenantToUsers().add(tenantToUser);
        return user;
    }


    @Override
    public TenantToUser createTenantToUserNoMerge(TenantToUserCreate tenantToUserCreate, SecurityContext securityContext) {
        TenantToUser tenantToUser = new TenantToUser("TenantToUser", securityContext);

        updateTenantToUserNoMerge(tenantToUserCreate, tenantToUser);
        return tenantToUser;
    }

    public boolean updateTenantToUserNoMerge(TenantToUserCreate tenantToUserCreate, TenantToUser tenantToUser) {
        boolean update = baseclassService.updateBaseclassNoMerge(tenantToUserCreate, tenantToUser);
        if (tenantToUserCreate.isDefaultTenant() != null && tenantToUserCreate.isDefaultTenant() != tenantToUser.isDefualtTennant()) {
            tenantToUser.setDefualtTennant(tenantToUserCreate.isDefaultTenant());
            update = true;
        }
        if (tenantToUserCreate.getTenant() != null && (tenantToUser.getLeftside() == null || !tenantToUserCreate.getTenant().getId().equals(tenantToUser.getLeftside().getId()))) {
            tenantToUser.setLeftside(tenantToUserCreate.getTenant());
            update = true;
        }

        if (tenantToUserCreate.getUser() != null && (tenantToUser.getRightside() == null || !tenantToUserCreate.getUser().getId().equals(tenantToUser.getRightside().getId()))) {
            tenantToUser.setUser(tenantToUserCreate.getUser());
            update = true;
        }
        return update;
    }

    @Override
    public User createUserNoMerge(UserCreate createUser, SecurityContext securityContext) {
        User user = new User(createUser.getName(), securityContext);
        updateUserNoMerge(user, createUser);
        return user;
    }

    public String generateUserHomeDir(User user) {
        return new File(usersRootHomeDir, user.getName() + user.getId()).getAbsolutePath();
    }

    @Override
    public boolean updateUserNoMerge(User user, UserCreate createUser) {
        boolean update = securityUserService.updateSecurityUserNoMerge(createUser, user);
        if (user.getHomeDir() == null && createUser.getHomeDir() == null) {
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


    @Override
    public RunningUser getRunningUser(String authenticationKey) {
        if (getBlackListedToken(authenticationKey) != null) {
            return null;
        }
        RunningUser runningUser = getLoggedInUser(authenticationKey);
        if (runningUser != null) {
            if (runningUser.getExpiresDate() != null && OffsetDateTime.now().isAfter(runningUser.getExpiresDate())) {
                removeLoggedUser(authenticationKey);
                return null;
            }
            return runningUser;
        }
        JWTClaims claims = tokenService.parseClaimsAndVerifyClaims(authenticationKey, utilLogger);
        if (claims != null) {
            String email = claims.getSubject();
            User user = userrepository.findByEmail(email);
            runningUser = createRunningUser(user, authenticationKey);
            runningUser.setExpiresDate(claims.getExpiration() != null ? claims.getExpiration().toInstant().atZone(ZoneId.of("UTC")).toOffsetDateTime() : null);
            Collection<String> readTenantsRaw = (Collection<String>) claims.get(tokenService.READ_TENANTS);
            String writeTenant = (String) claims.get(tokenService.WRITE_TENANT);
            Boolean totpVerified = (Boolean) claims.get(tokenService.TOTP_VERIFIED);
            Set<String> tenantIds = runningUser.getTenants().parallelStream().map(f -> f.getId()).collect(Collectors.toSet());
            if (writeTenant != null && tenantIds.contains(writeTenant)) {
                Tenant tenant = userrepository.findByIdOrNull(Tenant.class, writeTenant);
                runningUser.setDefaultTenant(tenant);
                runningUser.setImpersonated(true);
            }
            if (readTenantsRaw != null) {
                Set<String> readTenants = readTenantsRaw.parallelStream().collect(Collectors.toSet());
                List<Tenant> tenants = userrepository.findByIds(Tenant.class, readTenants).parallelStream().filter(f -> tenantIds.contains(f.getId())).collect(Collectors.toList());
                runningUser.setTenants(tenants);
                runningUser.setImpersonated(true);
            }
            if (totpVerified != null) {
                runningUser.setTotpVerified(totpVerified);
            }

            putLoggedInUser(authenticationKey, runningUser);
            return runningUser;
        }


        return null;
    }

    @Cacheable(cacheNames = "blacklistedTokens", key = "#authenticationKey", unless = "#result==null", cacheManager = "blacklistedTokensCacheManager")
    public String getBlackListedToken(String authenticationKey) {
        return null;
    }


    @Override
    public User getUser(String authenticationKey) {
        RunningUser runningUser = getRunningUser(authenticationKey);
        return runningUser != null ? runningUser.getUser() : null;
    }

    @Override
    public User getUserByMail(String mail) {
        return userrepository.findByEmail(mail);
    }


    @Override
    public User getUserByMail(String mail, SecurityContext securityContext) {
        List<User> users = userrepository.findByEmail(mail, securityContext);
        return users.isEmpty() ? null : users.get(0);
    }

    @Override
    public List<Tenant> getUserTenants(String authenticationKey) {
        RunningUser user = getRunningUser(authenticationKey);
        return user.getTenants();
    }


    @Override
    public Tenant getUserDefaultTenant(String authenticationKey) {
        RunningUser user = getRunningUser(authenticationKey);
        return user.getDefaultTenant();
    }


    @Cacheable(value = "loginAttempts", key = "#ip", cacheManager = "loginAttemptsCacheManager")
    public int getFailedLoginAttempts(String ip) {
        return 0;
    }

    @CachePut(value = "loginAttempts", key = "#ip", cacheManager = "loginAttemptsCacheManager")
    public int incrementFailedLoginAttempts(String ip, int failedAttempts) {
        return failedAttempts + 1;
    }

    /**
     * log into the system, if user!=null will not search for it.
     *
     * @param bundle bundle
     * @param user   user
     * @return logged in user
     */
    @Override
    public RunningUser login(AuthenticationRequestHolder bundle, User user)
            throws UserNotFoundException, CheckYourCredentialsException {
        boolean ok = false;
        int failedAttempts = maxFailedAttemptsAllowed > 0 ? getFailedLoginAttempts(bundle.getIp()) : 0;
        if (maxFailedAttemptsAllowed > 0) {
            if (failedAttempts > maxFailedAttemptsAllowed) {
                throw new BadRequestCustomException("Too Many Failed Login Attempts", LoginErrors.TOO_MANY_FAILED_ATTEMPTS.getCode());
            }
        }
        try {
            user = authenticate(bundle, user);
            ok = true;
        } finally {
            if (maxFailedAttemptsAllowed > 0 && !ok) {
                incrementFailedLoginAttempts(bundle.getIp(), failedAttempts);
            }
        }
        return registerUserIntoSystem(user);

    }

    @Override
    public RunningUser registerUserIntoSystem(User user) {
        OffsetDateTime expirationDate = OffsetDateTime.now().plusSeconds(jwtSecondsValid);
        return registerUserIntoSystem(user, expirationDate);
    }


    @Override
    public RunningUser registerUserIntoSystem(User user, OffsetDateTime expirationDate) {
        String jwtToken = tokenService.getJwtToken(user, expirationDate);
        RunningUser runningUser = createRunningUser(user, jwtToken);
        runningUser.setExpiresDate(expirationDate);
        putLoggedInUser(jwtToken, runningUser);
        return runningUser;

    }

    /**
     * authenticates a user by email and password or by phonenumber and password or by facebook user id and facebook token
     *
     * @param bundle bundle
     * @param user   user
     * @return user
     */
    @Override
    public User authenticate(AuthenticationRequestHolder bundle, User user) {
        long expiresAt = -1;
        if (user == null) {
            long start = System.currentTimeMillis();
            user = bundle.getMail() != null ? userrepository.findByEmail(bundle.getMail()) : (bundle.getPhoneNumber() != null ? findUserByPhoneNumberOrNull(bundle.getPhoneNumber(), null) : null);
            log.info("Time taken to find user by email is: " + (System.currentTimeMillis() - start));
            if (user == null) {
                if (bundle.getMail() != null) {
                    throw new UserNotFoundException("User for email: " + bundle.getMail() + " was not found");

                } else {
                    throw new UserNotFoundException("User for phone number : " + bundle.getPhoneNumber() + " was not found");

                }
            }
            if (!SCryptUtil.check(bundle.getPassword(), user.getPassword())) {
                throw (new CheckYourCredentialsException("Please check your credentials"));
            }
        }
        return user;
    }

    @Override
    public User findUserByPhoneNumberOrNull(String phoneNumber, SecurityContext securityContext) {
        List<User> users = userrepository.findUserByPhoneNumber(phoneNumber, securityContext);
        return users.isEmpty() ? null : users.get(0);
    }

    private boolean isScrypt(String password) {
        return password.length() > 40;
    }


    private RunningUser createRunningUser(User user, String jwtKey) {
        RunningUser running = new RunningUser(user, jwtKey);
        List<TenantToUser> tenantToUsers = tenantRepository.getAllTenants(user);
        List<RoleToUser> roleToUsers = listAllRoleToUsers(new RoleToUserFilter().setUsers(Collections.singletonList(user)), null);
        List<Role> allRoles = roleToUsers.stream().map(f -> f.getLeftside()).filter(distinctByKey(Role::getId)).collect(Collectors.toList());
        Map<String, List<Role>> roles = allRoles.stream().collect(Collectors.groupingBy(f -> f.getTenant().getId()));
        List<Tenant> tenants = tenantToUsers.parallelStream().map(f -> (Tenant) f.getLeftside()).collect(Collectors.toList());
        running.setTenants(tenants);
        running.setDefaultTenant(tenantToUsers.parallelStream().filter(f -> f.isDefualtTennant()).map(f -> (Tenant) f.getLeftside()).findFirst().orElse(null));
        running.setRoles(roles);
        running.setLoggedin(true);
        List<SecurityPolicy> securityPolicies = securityPolicyService.listAllSecurityPolicies(new PasswordSecurityPolicyFilter().setStartTime(OffsetDateTime.now()).setEnabled(true).setSecurityTenants(tenants.stream().map(f -> (SecurityTenant) f).collect(Collectors.toList())).setRoles(allRoles).setIncludeNoRole(true), null);
        running.setSecurityPolicies(securityPolicies);

        return running;
    }

    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }

    @Cacheable(value = "loggedUsers", key = "#authenticationkey", cacheManager = "loggedUsersCacheManager", unless = "#result == null")
    public RunningUser getLoggedInUser(String authenticationkey) {
        return null;
    }
    @CachePut(value = "loggedUsers", key = "#authenticationkey", cacheManager = "loggedUsersCacheManager", unless = "#result == null")
    public RunningUser putLoggedInUser(String authenticationkey,RunningUser runningUser) {
        return runningUser;
    }
    @CacheEvict(value = "loggedUsers", key = "#authenticationkey", cacheManager = "loggedUsersCacheManager")
    public void removeLoggedUser(String authenticationkey) {

    }

    @CachePut(value = "loggedUsers", key = "#authenticationkey", cacheManager = "loggedUsersCacheManager", unless = "#result == null")
    public String updateBlackList(String authenticationkey) {
        return authenticationkey;
    }

    @Override
    public boolean logOut(String authenticationkey) {
        RunningUser runningUser = getLoggedInUser(authenticationkey);
        if (runningUser != null) {
            if (runningUser.getExpiresDate() == null && OffsetDateTime.now().isBefore(runningUser.getExpiresDate())) {
                updateBlackList( authenticationkey);
                return true;
            }

        } else {
            JWTClaims claims = tokenService.parseClaimsAndVerifyClaims(authenticationkey, utilLogger);
            if (claims != null && (claims.getExpiration() == null || OffsetDateTime.now().isBefore(claims.getExpiration().toInstant().atZone(ZoneId.of("UTC")).toOffsetDateTime()))) {
                updateBlackList( authenticationkey);
                return true;
            }
        }
        return false;

    }


    public ResetPasswordResponse resetUserPassword(ResetUserPasswordRequest resetUserPasswordRequest, SecurityContext securityContext) {
        User user = resetUserPasswordRequest.getUser();
        UserUpdate userUpdate = new UserUpdate()
                .setUser(user)
                .setPassword(resetUserPasswordRequest.getPassword());
        updateUser(userUpdate, securityContext);
        return new ResetPasswordResponse();

    }


    @Override
    public ResetPasswordResponse resetPasswordViaMailPrepare(ResetUserPasswordRequest resetUserPasswordRequest) {
        User user = resetUserPasswordRequest.getUser();
        String verification = getVerificationToken();
        user.setForgotPasswordToken(verification);
        user.setForgotPasswordTokenValid(OffsetDateTime.now().plusMinutes(verificationLinkValidInMin));
        userrepository.merge(user);
        return new ResetPasswordResponse(verification);

    }

    private String getVerificationToken() {
        return Baseclass.getBase64ID().replaceAll("\\+", "0").replaceAll("/", "1");
    }


    @Override
    public ResetPasswordResponse resetPasswordWithVerification(ResetPasswordWithVerification resetPasswordWithVerification) {
        String verification = resetPasswordWithVerification.getVerification();

        List<User> users = userrepository.getUserByForgotPasswordVerificationToken(verification);
        User user = users.isEmpty() ? null : users.get(0);
        if (user == null) {
            throw new BadRequestCustomException("Invalid token", ResetPasswordResponse.INVALID_TOKEN);
        }
        if (user.getForgotPasswordTokenValid() == null || OffsetDateTime.now().isAfter(user.getForgotPasswordTokenValid())) {
            throw new BadRequestCustomException("Token has expired", ResetPasswordResponse.TOKEN_EXPIRED);
        }
        user.setPassword(hashPassword(resetPasswordWithVerification.getPassword()));
        user.setForgotPasswordTokenValid(null);
        user.setForgotPasswordToken(null);
        userrepository.merge(user);
        return new ResetPasswordResponse().setEmail(user.getEmail()).setPhoneNumber(user.getEmail());
    }

    @Override
    public VerifyMailResponse verifyMail(VerifyMail verifyMail) {
        String verification = verifyMail.getVerification();

        List<User> users = userrepository.getUserByEmailVerificationToken(verification);
        User user = users.isEmpty() ? null : users.get(0);
        if (user == null) {
            throw new BadRequestCustomException("Invalid token", VerifyMailResponse.INVALID_TOKEN);
        }
        if (user.getEmailVerificationTokenValid() == null || OffsetDateTime.now().isAfter(user.getEmailVerificationTokenValid())) {
            throw new BadRequestCustomException("Token has expired", VerifyMailResponse.TOKEN_EXPIRED);
        }
        user.setEmailVerificationToken(null);
        user.setEmailVerificationTokenValid(null);
        user.setLastVerificationDate(OffsetDateTime.now());
        userrepository.merge(user);
        return new VerifyMailResponse();

    }


    private VerifyMailResponse verifyMailStartPrepare(VerifyMailStart verifyMail, SecurityContext securityContext) {
        User user = verifyMail.getUser();
        user.setEmailVerificationTokenValid(OffsetDateTime.now().plusMinutes(5));
        String verification = getVerificationToken();
        user.setEmailVerificationToken(verification);
        return new VerifyMailResponse(verification);
    }

    @Override
    public void refrehEntityManager() {

        baselinkRepository.refrehEntityManager();
        tenantRepository.refrehEntityManager();
        userrepository.refrehEntityManager();
    }


    @Override
    public User getAdminUser() {
        return userrepository.findById(systemAdminId);
    }


    @Override
    public void validateUserForCreate(UserCreate userCreate, SecurityContext securityContext) {
        validateUser(userCreate, securityContext);
        User existing = userCreate.getEmail() != null ? userrepository.findByEmail(userCreate.getEmail()) : findUserByPhoneNumberOrNull(userCreate.getPhoneNumber(), null);
        if (existing != null) {
            String cause = userCreate.getEmail() != null ? "Email" : "PhoneNumber";
            throw new BadRequestException("Cannot create User " + cause + " is not unique");
        }

    }

    @Override
    public void validateUserUpdate(UserUpdate userUpdate, SecurityContext securityContext) {
        securityUserService.validate(userUpdate, securityContext);
        User user = userrepository.getByIdOrNull(userUpdate.getId(), User.class, null, securityContext);
        if (user == null) {
            throw new BadRequestException("No User With id " + userUpdate.getId());
        }
        userUpdate.setUser(user);
        User existing = userUpdate.getEmail() != null ? userrepository.findByEmail(userUpdate.getEmail()) : findUserByPhoneNumberOrNull(userUpdate.getPhoneNumber(), null);
        if (existing != null && !user.getId().equals(existing.getId())) {
            String cause = userUpdate.getEmail() != null ? "Email" : "PhoneNumber";
            throw new BadRequestException("Cannot Update User " + cause + " is not unique");
        }
        if (userUpdate.getPassword() != null) {
            List<SecurityTenant> userTenants = getAllTenantToUsers(new TenantToUserFilter().setUsers(Collections.singletonList(user)), null).stream().map(f -> f.getLeftside()).collect(Collectors.toList());
            List<Role> userRoles = listAllRoleToUsers(new RoleToUserFilter().setUsers(Collections.singletonList(user)), null).stream().map(f -> f.getLeftside()).collect(Collectors.toList());
            List<PasswordSecurityPolicy> passwordSecurityPolicies = passwordSecurityPolicyService.listAllSecurityPolicies(new PasswordSecurityPolicyFilter().setSecurityTenants(userTenants).setRoles(userRoles), securityContext);
            validatePasswordPolicies(userUpdate, passwordSecurityPolicies);

        }


    }

    @Override
    public void validateUser(UserCreate userCreate, SecurityContext securityContext) {
        securityUserService.validate(userCreate, securityContext);
        SecurityTenant tenant = userCreate.getTenant();
        if (tenant == null) {
            tenant = securityContext.getTenantToCreateIn() != null ? securityContext.getTenantToCreateIn() : (securityContext.getTenants().isEmpty() ? null : securityContext.getTenants().get(0));
        }
        if (tenant == null) {
            throw new BadRequestException("Could not determine tenant to create user in");
        }
        userCreate.setTenant(tenant);
        if (userCreate.getPhoneNumber() == null && userCreate.getEmail() == null) {
            throw new BadRequestException("Phone Number or Email Must Be Provided");
        }

        if (userCreate.getPassword() != null) {
            List<PasswordSecurityPolicy> passwordSecurityPolicies = passwordSecurityPolicyService.listAllSecurityPolicies(new PasswordSecurityPolicyFilter().setEnabled(true).setStartTime(OffsetDateTime.now()).setIncludeNoRole(true).setSecurityTenants(Collections.singletonList(tenant)), securityContext);
            validatePasswordPolicies(userCreate, passwordSecurityPolicies);
        }


    }

    private void validatePasswordPolicies(UserCreate userCreate, List<PasswordSecurityPolicy> passwordSecurityPolicies) {
        for (PasswordSecurityPolicy passwordSecurityPolicy : passwordSecurityPolicies) {
            List<PasswordPolicyError> policyErrors = passwordSecurityPolicyService.enforcePolicy(passwordSecurityPolicy, userCreate.getPassword());
            if (!policyErrors.isEmpty()) {
                PasswordSecurityPolicyErrorBody entity1 = new PasswordSecurityPolicyErrorBody(400, 0, "user password does not meet the security policy " + passwordSecurityPolicy.getName() + " errors: " + policyErrors);
                entity1.setErrorSet(policyErrors.stream().collect(Collectors.toSet()));
                entity1.setPasswordRequiredLength(passwordSecurityPolicy.getMinLength());
                Response.ResponseBuilder entity = Response.status(Response.Status.BAD_REQUEST).
                        entity(entity1);
                throw new BadRequestException(entity.build());
            }
        }
    }

    @Override
    public User updateUser(UserUpdate userUpdate, SecurityContext securityContext) {
        User user = userUpdate.getUser();
        if (updateUserNoMerge(user, userUpdate)) {
            userrepository.merge(user);
        }
        return user;
    }

    @Override
    public PaginationResponse<User> getAllUsers(UserFiltering userFiltering, SecurityContext securityContext) {
        List<User> list = listAllUsers(userFiltering, securityContext);
        long count = userrepository.countAllUsers(userFiltering, securityContext);
        return new PaginationResponse<>(list, userFiltering, count);
    }

    @Override
    public List<User> listAllUsers(UserFiltering userFiltering, SecurityContext securityContext) {
        return userrepository.getAllUsers(userFiltering, securityContext);
    }

    @Override
    public List<TenantToUser> getAllTenantToUsers(TenantToUserFilter userFiltering, SecurityContext securityContext) {
        return userrepository.getAllTenantToUsers(userFiltering, securityContext);
    }

    public PaginationResponse<TenantToUser> listAllTenantToUsers(TenantToUserFilter tenantToUserFilter, SecurityContext securityContext) {
        List<TenantToUser> tenantToUsers = getAllTenantToUsers(tenantToUserFilter, securityContext);
        long count = userrepository.countAllTenantToUsers(tenantToUserFilter, securityContext);
        return new PaginationResponse<>(tenantToUsers, tenantToUserFilter, count);
    }

    @Override
    public RoleToUser createRoleToUserNoMerge(RoleToUserCreate roleToUserCreate, SecurityContext securityContext) {
        RoleToUser roleToUser = new RoleToUser("roleLink", securityContext);
        roleToUser.setRole(roleToUserCreate.getRole());
        roleToUser.setUser(roleToUserCreate.getUser());
        return roleToUser;
    }

    public boolean updateRoleToUserNoMerge(RoleToUserCreate roleToUserCreate, RoleToUser roleToUser) {
        boolean update = baseclassService.updateBaseclassNoMerge(roleToUserCreate, roleToUser);
        if (roleToUserCreate.getRole() != null && (roleToUser.getLeftside() == null || !roleToUserCreate.getRole().getId().equals(roleToUser.getLeftside().getId()))) {
            roleToUser.setRole(roleToUserCreate.getRole());
            update = true;
        }
        if (roleToUserCreate.getUser() != null && (roleToUser.getRightside() == null || !roleToUserCreate.getUser().getId().equals(roleToUser.getRightside().getId()))) {
            roleToUser.setUser(roleToUserCreate.getUser());
            update = true;
        }
        return update;
    }

    @Override
    public UserProfile getUserProfile(UserProfileRequest userProfileRequest, SecurityContext securityContext) {
        Tenant tenantToCreateIn = securityContext.getTenantToCreateIn();
        Tenant tenant = tenantToCreateIn == null && !securityContext.getTenants().isEmpty() ? securityContext.getTenants().get(0) : tenantToCreateIn;
        return new UserProfile().setUser(securityContext.getUser()).setTenant(tenant).setTenants(securityContext.getTenants());
    }

    @Override
    public void validate(UserFiltering userFiltering, SecurityContext securityContext) {

        Set<String> tenantIds = userFiltering.getUserTenantsIds();
        Map<String, Tenant> map = tenantIds.isEmpty() ? new HashMap<>() : userrepository.listByIds(Tenant.class, tenantIds, securityContext).parallelStream().collect(Collectors.toMap(f -> f.getId(), f -> f));
        tenantIds.removeAll(map.keySet());
        if (!tenantIds.isEmpty()) {
            throw new BadRequestException("No Tenant with ids " + tenantIds);
        }
        userFiltering.setUserTenants(new ArrayList<>(map.values()));

    }

    @Override
    public void validate(AuthenticationRequest authenticationRequest, SecurityContext securityContext) {

        if (authenticationRequest.getEmail() == null && authenticationRequest.getPhoneNumber() == null) {
            throw new BadRequestCustomException("Email Or Phone Must be Provided", LoginErrors.IDENTIFIER_NOT_PROVIDED.getCode());
        }
        if (authenticationRequest.getPassword() == null) {
            throw new BadRequestCustomException("Password must be provided", LoginErrors.PASSWORD_NOT_PROVIDED.getCode());
        }
        UserFiltering userFiltering = new UserFiltering();
        if (authenticationRequest.getEmail() != null) {
            userFiltering.setEmails(Collections.singleton(authenticationRequest.getEmail()));
        } else {
            if (authenticationRequest.getPhoneNumber() != null) {
                userFiltering.setPhoneNumbers(Collections.singleton(authenticationRequest.getPhoneNumber()));
            }
        }


        List<User> user = listAllUsers(userFiltering, securityContext);
        if (user.isEmpty()) {
            throw new CheckYourCredentialsException("Please check your credentials");
        }
        authenticationRequest.setUser(user.get(0));
    }

    @Override
    public AuthenticationResponse login(AuthenticationRequest authenticationRequest, SecurityContext securityContext) {
        User user = authenticationRequest.getUser();
        if (!SCryptUtil.check(authenticationRequest.getPassword(), user.getPassword())) {
            throw (new CheckYourCredentialsException("Please check your credentials"));
        }
        OffsetDateTime expirationDate = OffsetDateTime.now().plusSeconds(authenticationRequest.getSecondsValid() != 0 ? authenticationRequest.getSecondsValid() : jwtSecondsValid);
        String jwtToken = tokenService.getJwtToken(user, expirationDate);
        if (!user.isTotpEnabled()) {
            applicationEventPublisher.publishEvent(new LoginEvent(user));
        }
        return new AuthenticationResponse()
                .setAuthenticationKey(jwtToken)
                .setTokenExpirationDate(expirationDate)
                .setUserId(user.getId())
                .setTotp(user.isTotpEnabled());

    }


    @Override
    public void validate(ImpersonateRequest impersonateRequest, SecurityContext securityContext) {

        String creationTenantId = impersonateRequest.getCreationTenantId();
        Tenant creationTenant = securityContext.getTenants().stream().filter(f -> f.getId().equals(creationTenantId)).findFirst().orElseThrow(() -> new BadRequestException("no tenant with id " + creationTenantId));
        if (creationTenant == null) {
            throw new BadRequestException("no tenant with id " + creationTenantId);
        }
        impersonateRequest.setCreationTenant(creationTenant);

        Set<String> readTenantIds = impersonateRequest.getReadTenantsIds();
        Map<String, Tenant> tenantMap = securityContext.getTenants().stream().filter(f -> readTenantIds.contains(f.getId())).collect(Collectors.toMap(f -> f.getId(), f -> f));
        readTenantIds.removeAll(tenantMap.keySet());
        if (!readTenantIds.isEmpty()) {
            throw new BadRequestException("No Tenants with ids " + readTenantIds);
        }
        impersonateRequest.setReadTenants(new ArrayList<>(tenantMap.values()));
    }

    @Override
    public ImpersonateResponse impersonate(ImpersonateRequest impersonateRequest, SecurityContext securityContext) {
        User user = securityContext.getUser();
        boolean totpVerified = securityContext.isTotpVerified();
        OffsetDateTime expirationDate = OffsetDateTime.now().plusSeconds(jwtSecondsValid);
        String writeTenant = impersonateRequest.getCreationTenant().getId();
        Set<String> readTenants = impersonateRequest.getReadTenants().parallelStream().map(f -> f.getId()).collect(Collectors.toSet());
        String jwtToken = tokenService.getJwtToken(user, expirationDate, writeTenant, readTenants, totpVerified);
        return new ImpersonateResponse().setAuthenticationKey(jwtToken);

    }

    public List<RoleToUser> listAllRoleToUsers(RoleToUserFilter roleToUserFilter, SecurityContext securityContext) {
        return userrepository.listAllRoleToUsers(roleToUserFilter, securityContext);

    }

    public void validate(TenantToUserCreate tenantToUserCreate, SecurityContext securityContext) {
        String tenantId = tenantToUserCreate.getTenantId();
        Tenant tenant = tenantId != null ? tenantRepository.getByIdOrNull(tenantId, Tenant.class, null, securityContext) : null;
        if (tenantId != null && tenant == null) {
            throw new BadRequestException("No Tenant With Id " + tenantId);
        }
        tenantToUserCreate.setTenant(tenant);

        String userId = tenantToUserCreate.getUserId();
        User user = userId != null ? tenantRepository.getByIdOrNull(userId, User.class, null, securityContext) : null;
        if (userId != null && user == null) {
            throw new BadRequestException("No User With Id " + userId);
        }
        tenantToUserCreate.setUser(user);
    }

    public <T extends Baseclass> T getByIdOrNull(String id, Class<T> c, List<String> batchString, SecurityContext securityContext) {
        return userrepository.getByIdOrNull(id, c, batchString, securityContext);
    }

    public TenantToUser createTenantToUser(TenantToUserCreate tenantToUserCreate, SecurityContext securityContext) {
        TenantToUser tenantToUser = createTenantToUserNoMerge(tenantToUserCreate, securityContext);
        tenantRepository.merge(tenantToUser);
        return tenantToUser;
    }

    public TenantToUser updateTenantToUser(TenantToUserUpdate tenantToUserUpdate, SecurityContext securityContext) {
        TenantToUser tenantToUser = tenantToUserUpdate.getTenantToUser();
        if (updateTenantToUserNoMerge(tenantToUserUpdate, tenantToUser)) {
            tenantRepository.merge(tenantToUser);
        }
        return tenantToUser;
    }

    @Transactional
    public void merge(Object base) {
        userrepository.merge(base);
    }
}
