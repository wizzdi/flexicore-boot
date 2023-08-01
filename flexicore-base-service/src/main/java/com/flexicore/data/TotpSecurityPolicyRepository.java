package com.flexicore.data;

import com.flexicore.model.Baseclass;
import com.flexicore.model.Basic;
import com.flexicore.model.security.TotpSecurityPolicy;
import com.flexicore.request.TotpSecurityPolicyFilter;
import com.flexicore.security.SecurityContextBase;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.security.data.BaseclassRepository;
import com.wizzdi.flexicore.security.data.BasicRepository;
import com.wizzdi.flexicore.security.data.SecurityPolicyRepository;
import jakarta.persistence.metamodel.SingularAttribute;
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
public class TotpSecurityPolicyRepository implements Plugin {
	@PersistenceContext
	private EntityManager em;
	@Autowired
	private SecurityPolicyRepository securityPolicyRepository;


	public List<TotpSecurityPolicy> listAllSecurityPolicies(TotpSecurityPolicyFilter TotpSecurityPolicyFilter, SecurityContextBase securityContext) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<TotpSecurityPolicy> q = cb.createQuery(TotpSecurityPolicy.class);
		Root<TotpSecurityPolicy> r = q.from(TotpSecurityPolicy.class);
		List<Predicate> predicates = new ArrayList<>();
		addSecurityPolicyPredicates(TotpSecurityPolicyFilter, cb, q, r, predicates, securityContext);
		q.select(r).where(predicates.toArray(Predicate[]::new));
		TypedQuery<TotpSecurityPolicy> query = em.createQuery(q);
		BasicRepository.addPagination(TotpSecurityPolicyFilter, query);
		return query.getResultList();

	}

	public <T extends TotpSecurityPolicy> void addSecurityPolicyPredicates(TotpSecurityPolicyFilter securityPolicyFilter, CriteriaBuilder cb, CommonAbstractCriteria q, From<?, T> r, List<Predicate> predicates, SecurityContextBase securityContext) {
		securityPolicyRepository.addSecurityPolicyPredicates(securityPolicyFilter, cb, q, r, predicates, securityContext);


	}

	public long countAllSecurityPolicies(TotpSecurityPolicyFilter TotpSecurityPolicyFilter, SecurityContextBase securityContext) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Long> q = cb.createQuery(Long.class);
		Root<TotpSecurityPolicy> r = q.from(TotpSecurityPolicy.class);
		List<Predicate> predicates = new ArrayList<>();
		addSecurityPolicyPredicates(TotpSecurityPolicyFilter, cb, q, r, predicates, securityContext);
		q.select(cb.count(r)).where(predicates.toArray(Predicate[]::new));
		TypedQuery<Long> query = em.createQuery(q);
		return query.getSingleResult();

	}

	public <T extends Baseclass> List<T> listByIds(Class<T> c, Set<String> ids, SecurityContextBase securityContext) {
		return securityPolicyRepository.listByIds(c, ids, securityContext);
	}

	public <T extends Baseclass> T getByIdOrNull(String id, Class<T> c, SecurityContextBase securityContext) {
		return securityPolicyRepository.getByIdOrNull(id, c, securityContext);
	}

	public <D extends Basic, E extends Baseclass, T extends D> T getByIdOrNull(String id, Class<T> c, SingularAttribute<D, E> baseclassAttribute, SecurityContextBase securityContext) {
		return securityPolicyRepository.getByIdOrNull(id, c, baseclassAttribute, securityContext);
	}

	public <D extends Basic, E extends Baseclass, T extends D> List<T> listByIds(Class<T> c, Set<String> ids, SingularAttribute<D, E> baseclassAttribute, SecurityContextBase securityContext) {
		return securityPolicyRepository.listByIds(c, ids, baseclassAttribute, securityContext);
	}

	public <D extends Basic, T extends D> List<T> findByIds(Class<T> c, Set<String> ids, SingularAttribute<D, String> idAttribute) {
		return securityPolicyRepository.findByIds(c, ids, idAttribute);
	}

	public <T extends Basic> List<T> findByIds(Class<T> c, Set<String> requested) {
		return securityPolicyRepository.findByIds(c, requested);
	}

	public <T> T findByIdOrNull(Class<T> type, String id) {
		return securityPolicyRepository.findByIdOrNull(type, id);
	}

	@Transactional
	public void merge(Object base) {
		securityPolicyRepository.merge(base);
	}

	@Transactional
	public void massMerge(List<?> toMerge) {
		securityPolicyRepository.massMerge(toMerge);
	}
}
