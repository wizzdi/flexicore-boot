package com.wizzdi.flexicore.security.configuration;

import com.flexicore.model.PermissionGroup;
import com.flexicore.model.PermissionGroupToBaseclass;
import com.flexicore.model.PermissionGroupToBaseclass_;
import com.wizzdi.segmantix.api.model.IInstanceGroup;
import com.wizzdi.segmantix.api.model.IInstanceGroupLink;
import com.wizzdi.segmantix.api.service.InstanceGroupLinkProvider;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class InstanceGroupLinkProviderImpl implements InstanceGroupLinkProvider {

    @PersistenceContext
    private  EntityManager em;


    @Override
    public List<IInstanceGroupLink> getInstanceGroupLinks(List<IInstanceGroup> instanceGroups) {
        List<PermissionGroup> permissionGroups=instanceGroups.stream().map(f->(PermissionGroup)f).toList();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<PermissionGroupToBaseclass> q = cb.createQuery(PermissionGroupToBaseclass.class);
        Root<PermissionGroupToBaseclass> r = q.from(PermissionGroupToBaseclass.class);
        q.select(r).where(r.get(PermissionGroupToBaseclass_.permissionGroup).in(permissionGroups));
        return new ArrayList<>(em.createQuery(q).getResultList());
    }
}
