package com.flexicore.service;

import com.flexicore.data.jsoncontainers.CreatePermissionGroupLinkRequest;
import com.flexicore.data.jsoncontainers.CreatePermissionGroupRequest;
import com.flexicore.data.jsoncontainers.PaginationResponse;
import com.flexicore.interfaces.FlexiCoreService;
import com.flexicore.model.Baseclass;
import com.flexicore.model.PermissionGroup;
import com.flexicore.model.PermissionGroupToBaseclass;
import com.flexicore.request.PermissionGroupCopy;
import com.flexicore.request.PermissionGroupsFilter;
import com.flexicore.request.UpdatePermissionGroup;
import com.flexicore.security.SecurityContext;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

public interface PermissionGroupService extends FlexiCoreService {
    <T extends Baseclass> T getByIdOrNull(String id, Class<T> c, List<String> batchString, SecurityContext securityContext);

    <T extends Baseclass> List<T> listByIds(Class<T> c, Set<String> ids, SecurityContext securityContext);

    List<PermissionGroup> listPermissionGroups(PermissionGroupsFilter permissionGroupsFilter, SecurityContext securityContext);

    PaginationResponse<PermissionGroup> getAllPermissionGroups(PermissionGroupsFilter permissionGroupsFilter, SecurityContext securityContext);

    List<PermissionGroupToBaseclass> connectPermissionGroupsToBaseclasses(CreatePermissionGroupLinkRequest createPermissionGroupLinkRequest, SecurityContext securityContext);

    List<PermissionGroupToBaseclass> getExistingPermissionGroupsLinks(List<PermissionGroup> permissionGroup, List<Baseclass> baseclasses);

    void validate(PermissionGroupsFilter permissionGroupsFilter, SecurityContext securityContext);

    void validate(PermissionGroupCopy permissionGroupCopy, SecurityContext securityContext);

    PermissionGroup createPermissionGroupNoMerge(CreatePermissionGroupRequest createPermissionGroupRequest, SecurityContext securityContext);

    /**
     * updates a permission group
     * @param permissionGroup existing permission group
     * @param createPermissionGroupRequest object used to update the permission group
     * @return true if the permission group was actually updated
     */
    boolean updatePermissionGroupNoMerge(PermissionGroup permissionGroup, CreatePermissionGroupRequest createPermissionGroupRequest);

    /**
     * creates a permission group
     * @param createPermissionGroupRequest object used to create a permission group
     * @param securityContext security context of the user executing the action
     * @return
     */
    PermissionGroup createPermissionGroup(CreatePermissionGroupRequest createPermissionGroupRequest, SecurityContext securityContext);

    @Transactional
    void merge(Object base);

    @Transactional
    void massMerge(List<?> toMerge);

    /**
     * object used to update the permission group
     * @param updatePermissionGroup object ussed to update the permission group
     * @param securityContext security context of the user executing the action
     * @return updated permission group
     */
    PermissionGroup updatePermissionGroup(UpdatePermissionGroup updatePermissionGroup, SecurityContext securityContext);

    /**
     * copies permission group with all its content , this will NOT copy any security links this group is participating in
     * @param permissionGroupCopy object describing the permission group to copied
     * @param securityContext security context of the user executing the action
     * @return permission group
     */
    PermissionGroup copyPermissionGroup(PermissionGroupCopy permissionGroupCopy, SecurityContext securityContext);
}
