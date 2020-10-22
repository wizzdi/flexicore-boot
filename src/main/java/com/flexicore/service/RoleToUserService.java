package com.flexicore.service;

import com.flexicore.data.jsoncontainers.PaginationResponse;
import com.flexicore.interfaces.FlexiCoreService;
import com.flexicore.model.Baseclass;
import com.flexicore.model.RoleToUser;
import com.flexicore.request.RoleToUserCreate;
import com.flexicore.request.RoleToUserFilter;
import com.flexicore.request.RoleToUserUpdate;
import com.flexicore.security.SecurityContext;

import java.util.List;

public interface RoleToUserService extends FlexiCoreService {

    /**
     *
     * @param roleToUserCreate object used to create the roleToUser
     * @param securityContext security context of the user executing the action
     * @return
     */
    RoleToUser createRoleToUser(RoleToUserCreate roleToUserCreate, SecurityContext securityContext);

    /**
     *
     * @param roleToUserCreate object used to create the roleToUser
     * @param securityContext security context of the user executing the action
     * @return
     */
    RoleToUser createRoleToUserNoMerge(RoleToUserCreate roleToUserCreate, SecurityContext securityContext);


    <T extends Baseclass> T getByIdOrNull(String id, Class<T> c, List<String> batchString, SecurityContext securityContext);

    void massMerge(List<?> toMerge);

    void merge(Object base);

    void validate(RoleToUserFilter roleToUserFilter, SecurityContext securityContext);

    void validate(RoleToUserCreate roleToUserCreate, SecurityContext securityContext);

    PaginationResponse<RoleToUser> getAllRoleToUsers(RoleToUserFilter roleToUserFilter, SecurityContext securityContext);

    /**
     *
     * @param roleToUserFilter object used to filter the roleToUsers
     * @param securityContext security context of the user executing the action
     * @return
     */
    List<RoleToUser> listAllRoleToUsers(RoleToUserFilter roleToUserFilter, SecurityContext securityContext);

    /**
     * updates roleToUser
     * @param roleToUserCreate object used to update the roleToUser
     * @param securityContext security context of the user executing the action
     * @return
     */
    RoleToUser updateRoleToUser(RoleToUserUpdate roleToUserCreate, SecurityContext securityContext);
}
