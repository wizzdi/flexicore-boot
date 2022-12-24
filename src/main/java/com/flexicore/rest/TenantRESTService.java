/*******************************************************************************
 *  Copyright (C) FlexiCore, Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Avishay Ben Natan And Asaf Ben Natan, October 2015
 ******************************************************************************/
package com.flexicore.rest;

import com.flexicore.annotations.IOperation;
import com.flexicore.annotations.IOperation.Access;
import com.flexicore.annotations.OperationsInside;
import com.flexicore.annotations.Protected;
import com.flexicore.data.jsoncontainers.PaginationResponse;
import com.flexicore.interfaces.RESTService;
import com.flexicore.model.Tenant;
import com.flexicore.model.User;
import com.flexicore.request.TenantCreate;
import com.flexicore.request.TenantFilter;
import com.flexicore.request.TenantUpdate;
import com.flexicore.security.SecurityContext;
import com.flexicore.service.impl.TenantService;
import com.flexicore.service.impl.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import java.util.List;


@Path("/tenant")
@RequestScoped
@Component
@OperationsInside
@Protected
@Tag(name = "Core")
@Tag(name = "Tenants")
@Extension
public class TenantRESTService implements RESTService {



    @Autowired
    private TenantService tenantService;

    @Autowired
    private UserService userService;



    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @IOperation(access = Access.allow, Name = "getTenants", Description = "returns all tenants", relatedClazzes = {Tenant.class})
    public List<Tenant> getTenants(@HeaderParam("authenticationkey") String authenticationkey,
                                   TenantFilter filteringInformationHolder,
                                   @HeaderParam("pagesize") @DefaultValue(value = "-1") Integer pagesize,
                                   @HeaderParam("currentpage") @DefaultValue(value = "-1") Integer currentpage,
                                   @Context SecurityContext securityContext) {
        filteringInformationHolder.setPageSize(pagesize);
        filteringInformationHolder.setCurrentPage(currentpage);
        return tenantService.getTenants(filteringInformationHolder, securityContext).getList();


    }

    @POST
    @Path("getAllTenants")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @IOperation(access = Access.allow, Name = "getTenants", Description = "returns all tenants", relatedClazzes = {Tenant.class})
    public PaginationResponse<Tenant> getAllTenants(@HeaderParam("authenticationkey") String authenticationkey,
                                                    TenantFilter filteringInformationHolder,
                                                    @Context SecurityContext securityContext) {

        return tenantService.getTenants(filteringInformationHolder, securityContext);


    }

    @POST
    @Path("createTenant")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @IOperation(access = Access.allow, Name = "createTenant", Description = "creates tenant", relatedClazzes = {Tenant.class, User.class})
    public Tenant createTenant(@HeaderParam("authenticationkey") String authenticationkey,
                               TenantCreate tenantCreate,
                               @Context SecurityContext securityContext) {
        tenantService.validate(tenantCreate,securityContext);
        return tenantService.createTenant(tenantCreate, securityContext);


    }

    @POST
    @Path("updateTenant")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @IOperation(access = Access.allow, Name = "updateTenant", Description = "update tenant", relatedClazzes = {Tenant.class, User.class})
    public Tenant updateTenant(@HeaderParam("authenticationkey") String authenticationkey,
                               TenantUpdate tenantCreate,
                               @Context SecurityContext securityContext) {
        tenantService.validateUpdate(tenantCreate,securityContext);
        return tenantService.updateTenant(tenantCreate, securityContext);


    }

}
