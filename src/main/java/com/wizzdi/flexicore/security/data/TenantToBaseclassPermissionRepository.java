package com.wizzdi.flexicore.security.data;

import com.flexicore.model.Baseclass;
import com.flexicore.model.Baseclass_;
import com.flexicore.model.TenantToBaseClassPremission;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.flexicore.security.SecurityContextBase;
import com.wizzdi.flexicore.security.request.TenantToBaseclassPermissionFilter;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
@Extension
public class TenantToBaseclassPermissionRepository implements Plugin {
	@PersistenceContext
	private EntityManager em;
	@Autowired
	private BaseclassRepository baseclassRepository;
	@Autowired
	private SecurityLinkRepository securityLinkRepository;

	public List<TenantToBaseClassPremission> listAllTenantToBaseclassPermissions(TenantToBaseclassPermissionFilter tenantToBaseclassPermissionFilter, SecurityContextBase securityContext){
		CriteriaBuilder cb=em.getCriteriaBuilder();
		CriteriaQuery<TenantToBaseClassPremission> q=cb.createQuery(TenantToBaseClassPremission.class);
		Root<TenantToBaseClassPremission> r=q.from(TenantToBaseClassPremission.class);
		List<Predicate> predicates=new ArrayList<>();
		addTenantToBaseclassPermissionPredicates(tenantToBaseclassPermissionFilter,cb,q,r,predicates,securityContext);
		q.select(r).where(predicates.toArray(Predicate[]::new));
		TypedQuery<TenantToBaseClassPremission> query = em.createQuery(q);
		BaseclassRepository.addPagination(tenantToBaseclassPermissionFilter,query);
		return query.getResultList();

	}

	public <T extends TenantToBaseClassPremission> void addTenantToBaseclassPermissionPredicates(TenantToBaseclassPermissionFilter tenantToBaseclassPermissionFilter, CriteriaBuilder cb, CommonAbstractCriteria q, From<?,T> r, List<Predicate> predicates, SecurityContextBase securityContext) {
		securityLinkRepository.addSecurityLinkPredicates(tenantToBaseclassPermissionFilter,cb,q,r,predicates,securityContext);
	}

	public long countAllTenantToBaseclassPermissions(TenantToBaseclassPermissionFilter tenantToBaseclassPermissionFilter, SecurityContextBase securityContext){
		CriteriaBuilder cb=em.getCriteriaBuilder();
		CriteriaQuery<Long> q=cb.createQuery(Long.class);
		Root<TenantToBaseClassPremission> r=q.from(TenantToBaseClassPremission.class);
		List<Predicate> predicates=new ArrayList<>();
		addTenantToBaseclassPermissionPredicates(tenantToBaseclassPermissionFilter,cb,q,r,predicates,securityContext);
		q.select(cb.count(r)).where(predicates.toArray(Predicate[]::new));
		TypedQuery<Long> query = em.createQuery(q);
		return query.getSingleResult();

	}

	@Transactional
	public <T> T merge(T o){
		return baseclassRepository.merge(o);
	}

	@Transactional
	public void massMerge(List<Object> list){
		baseclassRepository.massMerge(list);
	}

	public <T extends Baseclass> List<T> listByIds(Class<T> c,Set<String> ids,  SecurityContextBase securityContext) {
		return baseclassRepository.listByIds(c, ids, securityContext);
	}

	public <T extends Baseclass> T getByIdOrNull(String id, Class<T> c, SecurityContextBase securityContext) {
		return baseclassRepository.getByIdOrNull(id, c, securityContext);
	}
}
