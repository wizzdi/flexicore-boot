package com.flexicore.service;

import com.flexicore.data.jsoncontainers.PaginationResponse;
import com.flexicore.interfaces.FlexiCoreService;
import com.flexicore.model.Tenant;
import com.flexicore.request.TenantCreate;
import com.flexicore.request.TenantFilter;
import com.flexicore.request.TenantUpdate;
import com.flexicore.security.SecurityContext;

import java.util.List;

public interface TenantService extends FlexiCoreService {
    String TENANT_ADMINISTRATOR_NAME = "Tenant Administrator";


    PaginationResponse<Tenant> getTenants(TenantFilter tenantFilter, SecurityContext securityContext);

    List<Tenant> listAllTenants(TenantFilter tenantFilter, SecurityContext securityContext);

    /**
     * creates a tenant
     * @param tenantCreate object for creating a tenant
     * @param securityContext security context of the user to execute the action
     * @return
     */
    Tenant createTenant(TenantCreate tenantCreate, SecurityContext securityContext);

    /**
     * validates a tenantCreate object
     * @param tenantCreate Object used to create tenant
     * @param securityContext security context of the user to execute the action
     */
    void validate(TenantCreate tenantCreate, SecurityContext securityContext);

    /**
     * validates a tenantUpdate object
     * @param tenantUpdate Object used to update tenant
     * @param securityContext security context of the user to execute the action
     */
    void validateUpdate(TenantUpdate tenantUpdate, SecurityContext securityContext);


    /**
     * updates a tenant
     * @param tenantUpdate object to update tenant
     * @param securityContext security context of the user to execute the action
     * @return
     */
    Tenant updateTenant(TenantUpdate tenantUpdate, SecurityContext securityContext);
}
