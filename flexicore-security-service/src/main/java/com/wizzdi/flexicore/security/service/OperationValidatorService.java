package com.wizzdi.flexicore.security.service;

import com.wizzdi.segmantix.model.Access;
import com.flexicore.model.SecurityOperation;
import com.flexicore.model.SecurityTenant;
import com.flexicore.model.SecurityUser;
import com.flexicore.model.TenantToBaseclass;
import com.wizzdi.segmantix.model.SecurityContext;

import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.security.data.SecurityOpCheckRepository;

import com.wizzdi.flexicore.security.request.TenantToBaseclassFilter;
import org.pf4j.Extension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Lazy
@Service
@Extension
public class OperationValidatorService implements Plugin {

    private static final Logger logger = LoggerFactory.getLogger(OperationValidatorService.class);

    String USER_TYPE = "USER";
    String ROLE_TYPE = "ROLE";
    String TENANT_TYPE = "TENANT";


    @Autowired
    private SecurityOpCheckRepository securityOpCheckRepository;
    @Autowired
    private TenantToBaseclassService tenantToBaseclassService;



    @Cacheable(value = "operationValidatorCache", key = "#key",cacheManager = "operationAccessControlCacheManager",unless = "#result==null")
    public Boolean getIsAllowedFromCache(String key) {
        return null;
    }

    @CachePut(value = "operationValidatorCache", key = "#key",cacheManager = "operationAccessControlCacheManager",unless = "#result==null")
    public Boolean updateIsAllowedCache(String key, Boolean value) {
        return value;
    }

    static String getAccessControlKey(String type, String opId, String securityEntityId, Access access){
        return type+"."+opId+"."+securityEntityId+"."+access.name();
    }

    public boolean checkIfAllowed(SecurityContext securityContext) {
        Access defaultaccess = securityContext.operation() instanceof SecurityOperation securityOperation&& securityOperation.getDefaultAccess() != null ? securityOperation.getDefaultAccess(): Access.allow;
        return checkIfAllowed((SecurityUser) securityContext.user(), securityContext.tenants().stream().map(f->(SecurityTenant)f).toList(), (SecurityOperation) securityContext.operation(), defaultaccess);
    }

    public boolean userAllowed(SecurityOperation operation, SecurityUser user) {
        return checkUser(operation, user, Access.allow);

    }

    public boolean userDenied(SecurityOperation operation, SecurityUser user) {
        return checkUser(operation, user, Access.deny);
    }

    public boolean roleAllowed(SecurityOperation operation, SecurityUser user) {
        Access access = Access.allow;
        String cacheKey= getAccessControlKey(ROLE_TYPE,operation.getId(),user.getId(),access);
        Boolean val= getIsAllowedFromCache(cacheKey);
        if(val!=null){
            return val;
        }
        val = securityOpCheckRepository.checkRole(operation, user, access);
        updateIsAllowedCache(cacheKey,val);

        return val;

    }

    public boolean roleDenied(SecurityOperation operation, SecurityUser user) {
        Access access = Access.deny;

        String cacheKey=getAccessControlKey(ROLE_TYPE,operation.getId(),user.getId(),access);
        Boolean val= getIsAllowedFromCache(cacheKey);
        if(val!=null){
            return val;
        }
        val = securityOpCheckRepository.checkRole(operation, user, access);
        updateIsAllowedCache(cacheKey,val);
        return val;
    }

    public boolean checkUser(SecurityOperation operation, SecurityUser user, Access access) {
        String cacheKey= getAccessControlKey(USER_TYPE,operation.getId(),user.getId(),access);
        Boolean val= getIsAllowedFromCache(cacheKey);
        if(val!=null){
            return val;
        }
        val = securityOpCheckRepository.checkUser(operation, user, access);
        updateIsAllowedCache(cacheKey,val);
        return val;
    }
    public boolean tenantAllowed(SecurityOperation operation, SecurityTenant tenant) {
        Access access= Access.allow;
        String cacheKey= getAccessControlKey(TENANT_TYPE,operation.getId(),tenant.getId(),access);
        Boolean val= getIsAllowedFromCache(cacheKey);
        if(val!=null){
            return val;
        }
        TenantToBaseclass link = tenantToBaseclassService.listAllTenantToBaseclasss(new TenantToBaseclassFilter().setTenants(Collections.singletonList(tenant)).setSecuredIds(Collections.singleton(operation.getId())).setAccesses(Collections.singleton(access)),null).stream().findFirst().orElse(null);
        val= link != null;
        updateIsAllowedCache(cacheKey,val);
        return val;
    }

    public boolean tenantDenied(SecurityOperation operation, SecurityTenant tenant) {
        Access access= Access.deny;
        String cacheKey= getAccessControlKey(TENANT_TYPE,operation.getId(),tenant.getId(),access);
        Boolean val= getIsAllowedFromCache(cacheKey);
        if(val!=null){
            return val;
        }
        TenantToBaseclass link = tenantToBaseclassService.listAllTenantToBaseclasss(new TenantToBaseclassFilter().setTenants(Collections.singletonList(tenant)).setSecuredIds(Collections.singleton(operation.getId())).setAccesses(Collections.singleton(access)),null).stream().findFirst().orElse(null);
        val= link != null;
        updateIsAllowedCache(cacheKey,val);
        return val;
    }


    
    public boolean checkIfAllowed(SecurityUser securityUser, List<SecurityTenant> tenants, SecurityOperation securityOperation, Access access) {

        if (userAllowed(securityOperation, securityUser)) {
            return true;
        } else {
            if (userDenied(securityOperation, securityUser)) {
                return false;
            }
            if (roleAllowed(securityOperation, securityUser)) {
                return true;
            } else {
                if (roleDenied(securityOperation, securityUser)) {

                    return false;
                } else {
                    for (SecurityTenant securityTenant : tenants) {
                        if (tenantAllowed(securityOperation, securityTenant)) {

                            return true;
                        }
                    }
                    boolean allDenied = true;
                    for (SecurityTenant securityTenant : tenants) {
                        allDenied = tenantDenied(securityOperation, securityTenant) && allDenied;

                    }
                    if (allDenied) {
                        return false;
                    } else {
                        return access == Access.allow;
                    }


                }
            }

        }
    }





}
