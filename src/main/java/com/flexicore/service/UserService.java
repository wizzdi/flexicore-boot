package com.flexicore.service;

import com.flexicore.data.jsoncontainers.*;
import com.flexicore.exceptions.CheckYourCredentialsException;
import com.flexicore.exceptions.UserNotFoundException;
import com.flexicore.interfaces.FlexiCoreService;
import com.flexicore.model.*;
import com.flexicore.request.*;
import com.flexicore.response.AuthenticationResponse;
import com.flexicore.response.ImpersonateResponse;
import com.flexicore.response.UserProfile;
import com.flexicore.security.AuthenticationRequestHolder;
import com.flexicore.security.RunningUser;
import com.flexicore.security.SecurityContext;

import java.time.OffsetDateTime;
import java.util.List;

/**
 * service for creating user and user-related service
 * methods ending with "noMerge" creates the object but wont save it to the DB
 */
public interface UserService extends FlexiCoreService {
    <T extends Baseclass> List<T> getAllFiltered(QueryInformationHolder<T> queryInformationHolder);


    User findById(String id);

    /**
     * receives a list of entities to merge into the DB
     * @param toMerge
     */
    void massMerge(List<?> toMerge);

    /**
     * creates a User
     * @param userCreate object used to create a user
     * @param securityContext
     * @return
     */
    User createUser(UserCreate userCreate, SecurityContext securityContext);

    /**
     * creates a tenant to user link
     * @param tenantToUserCreate
     * @param securityContext
     * @return the tenantToUser link created
     */
    TenantToUser createTenantToUserNoMerge(TenantToUserCreate tenantToUserCreate, SecurityContext securityContext);

    /**
     * creates a User
     * @param createUser
     * @param securityContext
     * @return the created user
     */
    User createUserNoMerge(UserCreate createUser, SecurityContext securityContext);

    /**
     * updates a user
     * @param user
     * @param createUser
     * @return if anything was changed
     */
    boolean updateUserNoMerge(User user, UserCreate createUser);

    RunningUser getRunningUser(String authenticationKey);

    User getUser(String authenticationKey);

    User getUserByMail(String mail);

    User getUserByMail(String mail, SecurityContext securityContext);

    /**
     * returns the authenticationKey related user tenants
     * @param authenticationKey
     * @return
     */
    List<Tenant> getUserTenants(String authenticationKey);

    /**
     * returns user default tenant
     * @param authenticationKey
     * @return
     */
    Tenant getUserDefaultTenant(String authenticationKey);

    /**
     * logs in a user receiving an autheticationRequestHolder and an OPTIONAL user
     * this will validate the content of the bundle (password and mail/phonenumber)
     * @param bundle
     * @param user
     * @return
     * @throws UserNotFoundException
     * @throws CheckYourCredentialsException
     */
    RunningUser login(AuthenticationRequestHolder bundle, User user)
                                            throws UserNotFoundException, CheckYourCredentialsException;

    /**
     * logs in a user
     * @param user
     * @return
     */
    RunningUser registerUserIntoSystem(User user);

    /**
     * logs in a user  , authenticationKey will expire at the given expirationDate
     * @param user
     * @return
     */
    RunningUser registerUserIntoSystem(User user, OffsetDateTime expirationDate);

    /**
     * logs in a user receiving an autheticationRequestHolder and an OPTIONAL user
     * this will validate the content of the bundle (password and mail/phonenumber)
     * this wont return the authenticationKey for the user , the expected usage is calling authenticate and then calling registerUserIntoSystem
     * @param bundle
     * @param user
     * @return
     * @throws UserNotFoundException
     * @throws CheckYourCredentialsException
     */
    User authenticate(AuthenticationRequestHolder bundle, User user);

    User findUserByPhoneNumberOrNull(String phoneNumber, SecurityContext securityContext);


    /**
     * log outs a user
     * @param authenticationkey
     * @return
     */
    boolean logOut(String authenticationkey);


    /**
     * given a verification code ( received by RestPasswordResponse), sets the user password to the given one.
     * @param resetPasswordWithVerification
     * @return
     */
    ResetPasswordResponse resetPasswordWithVerification(ResetPasswordWithVerification resetPasswordWithVerification);

    /**
     * finishing verification
     * @param verifyMail
     * @return
     */
    VerifyMailResponse verifyMail(VerifyMail verifyMail);


    /**
     * flushes and clear all repositories used by UserService
     */
    void refrehEntityManager();

    /**
     * returns System admin user
     * @return
     */
    User getAdminUser();

    /**
     * validates user create object
     * @param userCreate
     * @param securityContext
     */
    void validateUserForCreate(UserCreate userCreate, SecurityContext securityContext);

    /**
     * validates user update object
     * @param userUpdate
     * @param securityContext
     */
    void validateUserUpdate(UserUpdate userUpdate, SecurityContext securityContext);

    /**
     * same as {@link #validateUserForCreate(UserCreate, SecurityContext)}
     * @param userCreate
     * @param securityContext
     */
    void validateUser(UserCreate userCreate, SecurityContext securityContext);

    /**
     * updates user , null properties on #UserUpdate will be ignored
     * @param userUpdate
     * @param securityContext
     * @return
     */
    User updateUser(UserUpdate userUpdate, SecurityContext securityContext);

    PaginationResponse<User> getAllUsers(UserFiltering userFiltering, SecurityContext securityContext);

    List<User> listAllUsers(UserFiltering userFiltering, SecurityContext securityContext);

    List<TenantToUser> getAllTenantToUsers(TenantToUserFilter userFiltering, SecurityContext securityContext);

    RoleToUser createRoleToUserNoMerge(RoleToUserCreate roleToUserCreate, SecurityContext securityContext);

    UserProfile getUserProfile(UserProfileRequest userProfileRequest, SecurityContext securityContext);
    ResetPasswordResponse resetPasswordViaMailPrepare(ResetUserPasswordRequest resetUserPasswordRequest);

        /**
         * validates user filtering object
         * @param userFiltering
         * @param securityContext
         */
    void validate(UserFiltering userFiltering, SecurityContext securityContext);

    /**
     * validates authetication request
     * @param authenticationRequest
     * @param securityContext
     */
    void validate(AuthenticationRequest authenticationRequest, SecurityContext securityContext);

    /**
     * logs in a user
     * @param authenticationRequest
     * @param securityContext
     * @return
     */
    AuthenticationResponse login(AuthenticationRequest authenticationRequest, SecurityContext securityContext);

    /**
     * validate impersonate request
     * @param impersonateRequest
     * @param securityContext
     */
    void validate(ImpersonateRequest impersonateRequest, SecurityContext securityContext);

    /**
     * given a securityContext a writeTenant(optional - one of users tenants) and a list of read tenant (subset of user tenants)
     * will return an authenticationKey that will limit all requests to objects to the given list of read tenants and will create any object under writeTenant
     * @param impersonateRequest
     * @param securityContext
     * @return
     */
    ImpersonateResponse impersonate(ImpersonateRequest impersonateRequest, SecurityContext securityContext);
}
