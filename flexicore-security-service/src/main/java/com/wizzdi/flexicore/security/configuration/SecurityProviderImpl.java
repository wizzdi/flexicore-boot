package com.wizzdi.flexicore.security.configuration;

import com.flexicore.model.RoleToBaseclass;
import com.flexicore.model.RoleToBaseclass_;
import com.flexicore.model.SecurityLink;
import com.flexicore.model.SecurityLink_;
import com.flexicore.model.TenantToBaseclass;
import com.flexicore.model.TenantToBaseclass_;
import com.flexicore.model.UserToBaseclass;
import com.flexicore.model.UserToBaseclass_;
import com.wizzdi.segmantix.api.model.IRole;
import com.wizzdi.segmantix.api.model.ISecurityLink;
import com.wizzdi.segmantix.api.model.ISecurityContext;
import com.wizzdi.segmantix.api.model.ITenant;
import com.wizzdi.segmantix.api.model.IUser;
import com.wizzdi.segmantix.api.service.SecurityLinkProvider;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SecurityProviderImpl implements SecurityLinkProvider {
    private static final Logger logger= LoggerFactory.getLogger(SecurityProviderImpl.class);

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<ISecurityLink> getSecurityLinks(ISecurityContext securityContext) {
        logger.info("fetching links");
        IUser user=securityContext.user();
        List<? extends IRole> roles=securityContext.roles();
        List<? extends ITenant> tenants=securityContext.tenants();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<SecurityLink> q = cb.createQuery(SecurityLink.class);
        Root<SecurityLink> r = q.from(SecurityLink.class);
        Root<UserToBaseclass> userR = cb.treat(r, UserToBaseclass.class);
        Root<RoleToBaseclass> role = cb.treat(r, RoleToBaseclass.class);
        Root<TenantToBaseclass> tenant = cb.treat(r, TenantToBaseclass.class);
        q.select(r).where(
                cb.isFalse(r.get(SecurityLink_.softDelete)),
                cb.or(
                        userR.get(UserToBaseclass_.user).in(user),
                        roles.isEmpty()?cb.or():role.get(RoleToBaseclass_.role).in(roles),
                        tenants.isEmpty()?cb.or():tenant.get(TenantToBaseclass_.tenant).in(tenants)
                ));
        return new ArrayList<>(em.createQuery(q).getResultList());
    }
}
