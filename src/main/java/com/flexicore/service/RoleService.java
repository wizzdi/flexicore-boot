package com.flexicore.service;

import com.flexicore.data.jsoncontainers.PaginationResponse;
import com.flexicore.interfaces.FlexiCoreService;
import com.flexicore.model.*;
import com.flexicore.request.RoleCreate;
import com.flexicore.request.RoleFilter;
import com.flexicore.request.RoleToBaseclassCreate;
import com.flexicore.request.RoleUpdate;
import com.flexicore.security.SecurityContext;

import java.util.List;

public interface RoleService extends FlexiCoreService {
    @Deprecated
    Role createRole(String roleName, SecurityContext securityContext);

    /**
     *
     * @param roleCreate object used to create the role
     * @param securityContext security context of the user executing the action
     * @return
     */
    Role createRole(RoleCreate roleCreate, SecurityContext securityContext);

    /**
     *
     * @param roleCreate object used to create the role
     * @param securityContext security context of the user executing the action
     * @return
     */
    Role createRoleNoMerge(RoleCreate roleCreate, SecurityContext securityContext);

    Role findById(String id, SecurityContext securityContext);

    List<Role> getAllFiltered(QueryInformationHolder<Role> queryInformationHolder);

    List<Role> getAllUserRoles(QueryInformationHolder<Role> queryInformationHolder, User user);

    <T extends Baseclass>  T getById(String id, Class<T> c, List<String> batchString, SecurityContext securityContext);

    <T extends Baseclass> T getByIdOrNull(String id, Class<T> c, List<String> batchString, SecurityContext securityContext);

    void massMerge(List<?> toMerge);

    void merge(Object base);

    void refrehEntityManager();

    RoleToBaseclass createRoleToBaseclassNoMerge(RoleToBaseclassCreate roleToBaseclassCreate, SecurityContext securityContext);

    void validate(RoleFilter roleFilter, SecurityContext securityContext);

    void validate(RoleCreate roleCreate, SecurityContext securityContext);

    PaginationResponse<Role> getAllRoles(RoleFilter roleFilter, SecurityContext securityContext);

    /**
     *
     * @param roleFilter object used to filter the roles
     * @param securityContext security context of the user executing the action
     * @return
     */
    List<Role> listAllRoles(RoleFilter roleFilter, SecurityContext securityContext);

    /**
     * updates role
     * @param roleCreate object used to update the role
     * @param securityContext security context of the user executing the action
     * @return
     */
    Role updateRole(RoleUpdate roleCreate, SecurityContext securityContext);
}
