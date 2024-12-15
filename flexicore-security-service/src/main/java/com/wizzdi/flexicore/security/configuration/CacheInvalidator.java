package com.wizzdi.flexicore.security.configuration;

import com.flexicore.model.OperationToGroup;
import com.flexicore.model.RoleToBaseclass;
import com.flexicore.model.SecurityLink;
import com.flexicore.model.TenantToBaseclass;
import com.flexicore.model.UserToBaseclass;
import com.wizzdi.flexicore.security.events.BasicCreated;
import com.wizzdi.flexicore.security.events.BasicUpdated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class CacheInvalidator {
    private static final Logger logger= LoggerFactory.getLogger(CacheInvalidator.class);
    @Autowired
    private Cache operationToOperationGroupCache;
    @Autowired
    private Cache dataAccessControlCache;


    @EventListener
    public void invalidateUserCache(BasicCreated<UserToBaseclass> securityLink) {
        SecurityLink link = securityLink.getBaseclass();
        invalidateCache(link);
    }

    @EventListener
    public void invalidateRoleCache(BasicCreated<RoleToBaseclass> securityLink) {
        SecurityLink link = securityLink.getBaseclass();
        invalidateCache(link);
    }

    @EventListener
    public void invalidateTenantCache(BasicCreated<TenantToBaseclass> securityLink) {
        SecurityLink link = securityLink.getBaseclass();
        invalidateCache(link);
    }

    @EventListener
    public void invalidateUserCache(BasicUpdated<UserToBaseclass> securityLink) {
        SecurityLink link = securityLink.getBaseclass();
        invalidateCache(link);
    }

    @EventListener
    public void invalidateRoleCache(BasicUpdated<RoleToBaseclass> securityLink) {
        SecurityLink link = securityLink.getBaseclass();
        invalidateCache(link);
    }

    @EventListener
    public void invalidateTenantCache(BasicUpdated<TenantToBaseclass> securityLink) {
        SecurityLink link = securityLink.getBaseclass();
        invalidateCache(link);
    }

    @EventListener
    public void invalidateOperationGroupCache(BasicUpdated<OperationToGroup> operationToGroup){
        invalidateCache(operationToGroup.getBaseclass());
    }

    @EventListener
    public void invalidateOperationGroupCache(BasicCreated<OperationToGroup> operationToGroup){
        invalidateCache(operationToGroup.getBaseclass());
    }

    private void invalidateCache(OperationToGroup operationToGroup) {
        if(operationToGroup.getOperation()==null){
            return;
        }
        operationToOperationGroupCache.evict(operationToGroup.getOperation().getId());
    }


    private void invalidateCache(SecurityLink link) {
        if (link.getSecurityEntity() != null) {
            dataAccessControlCache.evict(link.getSecurityEntity().getId());
            logger.debug("evicted security entity " + link.getSecurityEntity().getId());
        }
    }
}
