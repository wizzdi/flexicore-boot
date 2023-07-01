package com.flexicore.service;

import com.flexicore.annotations.IOperation;
import com.flexicore.data.jsoncontainers.OperationInfo;
import com.flexicore.interfaces.FlexiCoreService;
import com.flexicore.model.Operation;
import com.flexicore.model.Tenant;
import com.flexicore.model.User;
import com.flexicore.request.PermissionSummaryRequest;
import com.flexicore.response.PermissionSummaryResponse;
import com.flexicore.security.SecurityContext;

import java.lang.reflect.Method;
import java.util.List;

public interface SecurityService extends FlexiCoreService {




    boolean checkIfAllowed(SecurityContext securityContext);

    boolean checkIfAllowed(User user, List<Tenant> tenants, Operation operation, IOperation.Access access);

    SecurityContext getSecurityContext(String authenticationkey, String operationId);

    OperationInfo getIOperation(Method method);

    /**
     *
     * @return #SecurityContext of the admin user
     */
    SecurityContext getAdminUserSecurityContext();

    SecurityContext getUserSecurityContextByEmail(String email);


    /**
     *
     * @param user user to return security context for
     * @return security context of the given user
     */
    SecurityContext getUserSecurityContext(User user);

    void refrehEntityManager();

    /**
     * validates #PermissionSummaryRequest
     * @param permissionSummaryRequest request
     * @param securityContext security context
     */
    void validate(PermissionSummaryRequest permissionSummaryRequest, SecurityContext securityContext);

    /**
     *  returns permission summary - that is if users have or dont have access to given objects
     * @param permissionSummaryRequest request
     * @param securityContext securitu context
     * @return response
     */
    PermissionSummaryResponse getPermissionsSummary(PermissionSummaryRequest permissionSummaryRequest, SecurityContext securityContext);
}
