package com.flexicore.data;

import com.flexicore.model.Baseclass;
import com.flexicore.model.security.TotpSecurityPolicy;
import com.flexicore.request.TotpSecurityPolicyFilter;
import com.flexicore.security.SecurityContextBase;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.security.data.BaseclassRepository;
import com.wizzdi.flexicore.security.data.SecurityPolicyRepository;
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
public class TotpSecurityPolicyRepository implements Plugin {
	@PersistenceContext
	private EntityManager em;
	@Autowired
	private BaseclassRepository baseclassRepository;
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
		BaseclassRepository.addPagination(TotpSecurityPolicyFilter, query);
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
		BaseclassRepository.addPagination(TotpSecurityPolicyFilter, query);
		return query.getFirstResult();

	}

	@Transactional
	public void merge(Object o) {
		em.merge(o);
	}

	@Transactional
	public void massMerge(List<Object> list) {
		for (Object o : list) {
			em.merge(o);
		}
	}

	public <T extends Baseclass> List<T> listByIds(Class<T> c, Set<String> ids, SecurityContextBase securityContext) {
		return baseclassRepository.listByIds(c, ids, securityContext);
	}

	public <T extends Baseclass> T getByIdOrNull(String id, Class<T> c, SecurityContextBase securityContext) {
		return baseclassRepository.getByIdOrNull(id, c, securityContext);
	}

	public <T extends Baseclass> List<T> findByIds(Class<T> c, Set<String> requested) {
		return baseclassRepository.findByIds(c, requested);
	}

	public <T extends TotpSecurityPolicy> T getSecurityPolicyByIdOrNull(String id, Class<T> c, SecurityContextBase securityContext) {
		return securityPolicyRepository.getSecurityPolicyByIdOrNull(id, c, securityContext);
	}
}
