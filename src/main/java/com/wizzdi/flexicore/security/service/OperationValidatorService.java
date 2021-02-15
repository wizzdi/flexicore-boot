package com.wizzdi.flexicore.security.service;

import com.flexicore.annotations.IOperation;
import com.flexicore.annotations.IOperation.Access;
import com.flexicore.model.Baselink;
import com.flexicore.model.SecurityOperation;
import com.flexicore.model.SecurityTenant;
import com.flexicore.model.SecurityUser;
import com.flexicore.security.SecurityContextBase;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.security.data.SecurityRepository;
import com.wizzdi.flexicore.security.request.BaselinkFilter;
import org.pf4j.Extension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Lazy
@Service
@Extension
public class OperationValidatorService implements Plugin {

    private static final Logger logger = LoggerFactory.getLogger(OperationValidatorService.class);
    private static final Cache<String,Boolean> accessControlCache= CacheBuilder.newBuilder().maximumSize(100).expireAfterWrite(15, TimeUnit.MINUTES).build();

    String USER_TYPE = "USER";
    String ROLE_TYPE = "ROLE";
    String TENANT_TYPE = "TENANT";

    @Autowired
    private BaselinkService baselinkService;

    @Autowired
    private SecurityRepository securityRepository;




    static String getAccessControlKey(String type, String opId, String securityEntityId, IOperation.Access access){
        return type+"."+opId+"."+securityEntityId+"."+access.name();
    }

    public boolean checkIfAllowed(SecurityContextBase securityContextBase) {
        Access defaultaccess = securityContextBase.getOperation() != null && securityContextBase.getOperation().getDefaultaccess() != null ? securityContextBase.getOperation().getDefaultaccess() : Access.allow;
        return checkIfAllowed(securityContextBase.getUser(), securityContextBase.getTenants(), securityContextBase.getOperation(), defaultaccess);
    }

    public boolean userAllowed(SecurityOperation operation, SecurityUser user) {
        return checkUser(operation, user, IOperation.Access.allow);

    }

    public boolean userDenied(SecurityOperation operation, SecurityUser user) {
        return checkUser(operation, user, IOperation.Access.deny);
    }

    public boolean roleAllowed(SecurityOperation operation, SecurityUser user) {
        IOperation.Access access = IOperation.Access.allow;
        String cacheKey= getAccessControlKey(ROLE_TYPE,operation.getId(),user.getId(),access);
        Boolean val=accessControlCache.getIfPresent(cacheKey);
        if(val!=null){
            return val;
        }
        val = securityRepository.checkRole(operation, user, access);
        accessControlCache.put(cacheKey,val);

        return val;

    }

    public boolean roleDenied(SecurityOperation operation, SecurityUser user) {
        IOperation.Access access = IOperation.Access.deny;

        String cacheKey=getAccessControlKey(ROLE_TYPE,operation.getId(),user.getId(),access);
        Boolean val=accessControlCache.getIfPresent(cacheKey);
        if(val!=null){
            return val;
        }
        val = securityRepository.checkRole(operation, user, access);
        accessControlCache.put(cacheKey,val);
        return val;
    }

    public boolean checkUser(SecurityOperation operation, SecurityUser user, IOperation.Access access) {
        String cacheKey= getAccessControlKey(USER_TYPE,operation.getId(),user.getId(),access);
        Boolean val=accessControlCache.getIfPresent(cacheKey);
        if(val!=null){
            return val;
        }
        val = securityRepository.checkUser(operation, user, access);
        accessControlCache.put(cacheKey,val);
        return val;
    }
    public boolean tenantAllowed(SecurityOperation operation, SecurityTenant tenant) {
        IOperation.Access access=IOperation.Access.allow;
        String cacheKey= getAccessControlKey(TENANT_TYPE,operation.getId(),tenant.getId(),access);
        Boolean val=accessControlCache.getIfPresent(cacheKey);
        if(val!=null){
            return val;
        }
        Baselink link = baselinkService.listAllBaselinks(new BaselinkFilter().setLeftside(Collections.singletonList(tenant)).setRightside(Collections.singletonList(operation)).setSimpleValues(Collections.singleton(access.name())),null).stream().findFirst().orElse(null);
        val= link != null;
        accessControlCache.put(cacheKey,val);
        return val;
    }

    public boolean tenantDenied(SecurityOperation operation, SecurityTenant tenant) {
        IOperation.Access access=IOperation.Access.deny;
        String cacheKey= getAccessControlKey(TENANT_TYPE,operation.getId(),tenant.getId(),access);
        Boolean val=accessControlCache.getIfPresent(cacheKey);
        if(val!=null){
            return val;
        }
        Baselink link = baselinkService.listAllBaselinks(new BaselinkFilter().setLeftside(Collections.singletonList(tenant)).setRightside(Collections.singletonList(operation)).setSimpleValues(Collections.singleton(access.name())),null).stream().findFirst().orElse(null);
        val= link != null;
        accessControlCache.put(cacheKey,val);
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
