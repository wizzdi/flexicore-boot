package com.wizzdi.flexicore.security.data;

import com.flexicore.model.Baseclass;
import com.flexicore.model.TenantToBaseclass;
import com.flexicore.model.TenantToBaseclass_;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.flexicore.security.SecurityContextBase;
import com.wizzdi.flexicore.security.request.TenantToBaseclassFilter;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
@Extension
public class TenantToBaseclassRepository implements Plugin {
	@PersistenceContext
	private EntityManager em;

	@Autowired
	private SecurityLinkRepository securityLinkRepository;

	public List<TenantToBaseclass> listAllTenantToBaseclasss(TenantToBaseclassFilter tenantToBaseclassFilter, SecurityContextBase securityContext){
		CriteriaBuilder cb=em.getCriteriaBuilder();
		CriteriaQuery<TenantToBaseclass> q=cb.createQuery(TenantToBaseclass.class);
		Root<TenantToBaseclass> r=q.from(TenantToBaseclass.class);
		List<Predicate> predicates=new ArrayList<>();
		addTenantToBaseclassPredicates(tenantToBaseclassFilter,cb,q,r,predicates,securityContext);
		q.select(r).where(predicates.toArray(Predicate[]::new));
		TypedQuery<TenantToBaseclass> query = em.createQuery(q);
		BasicRepository.addPagination(tenantToBaseclassFilter,query);
		return query.getResultList();

	}

	public <T extends TenantToBaseclass> void addTenantToBaseclassPredicates(TenantToBaseclassFilter tenantToBaseclassFilter, CriteriaBuilder cb, CommonAbstractCriteria q, From<?,T> r, List<Predicate> predicates, SecurityContextBase securityContext) {
		securityLinkRepository.addSecurityLinkPredicates(tenantToBaseclassFilter,cb,q,r,predicates,securityContext);
		if(tenantToBaseclassFilter.getTenants()!=null&&!tenantToBaseclassFilter.getTenants().isEmpty()){
			predicates.add(r.get(TenantToBaseclass_.tenant).in(tenantToBaseclassFilter.getTenants()));
		}
	}

	public long countAllTenantToBaseclasss(TenantToBaseclassFilter tenantToBaseclassFilter, SecurityContextBase securityContext){
		CriteriaBuilder cb=em.getCriteriaBuilder();
		CriteriaQuery<Long> q=cb.createQuery(Long.class);
		Root<TenantToBaseclass> r=q.from(TenantToBaseclass.class);
		List<Predicate> predicates=new ArrayList<>();
		addTenantToBaseclassPredicates(tenantToBaseclassFilter,cb,q,r,predicates,securityContext);
		q.select(cb.count(r)).where(predicates.toArray(Predicate[]::new));
		TypedQuery<Long> query = em.createQuery(q);
		return query.getSingleResult();

	}

	@Transactional
	public <T> T merge(T o){
		return securityLinkRepository.merge(o);
	}

	@Transactional
	public void massMerge(List<Object> list){
		securityLinkRepository.massMerge(list);
	}

	public <T extends Baseclass> List<T> listByIds(Class<T> c,Set<String> ids,  SecurityContextBase securityContext) {
		return securityLinkRepository.listByIds(c, ids, securityContext);
	}

	public <T extends Baseclass> T getByIdOrNull(String id, Class<T> c, SecurityContextBase securityContext) {
		return securityLinkRepository.getByIdOrNull(id, c, securityContext);
	}
}
