package com.wizzdi.flexicore.boot.dynamic.invokers.data;

import com.flexicore.model.Baseclass;
import com.flexicore.model.security.SecurityPolicy;
import com.flexicore.model.security.SecurityPolicy_;
import com.flexicore.security.SecurityContextBase;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.boot.dynamic.invokers.model.DynamicExecution;
import com.wizzdi.flexicore.boot.dynamic.invokers.model.DynamicExecution_;
import com.wizzdi.flexicore.boot.dynamic.invokers.model.ServiceCanonicalName;
import com.wizzdi.flexicore.boot.dynamic.invokers.model.ServiceCanonicalName_;
import com.wizzdi.flexicore.boot.dynamic.invokers.request.DynamicExecutionFilter;
import com.wizzdi.flexicore.security.data.BaseclassRepository;
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
public class DynamicExecutionRepository implements Plugin {
	@PersistenceContext
	private EntityManager em;
	@Autowired
	private BaseclassRepository baseclassRepository;

	public List<DynamicExecution> listAllDynamicExecutions(DynamicExecutionFilter dynamicExecutionFilter, SecurityContextBase securityContext) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<DynamicExecution> q = cb.createQuery(DynamicExecution.class);
		Root<DynamicExecution> r = q.from(DynamicExecution.class);
		List<Predicate> predicates = new ArrayList<>();
		addDynamicExecutionPredicates(dynamicExecutionFilter, cb, q, r, predicates, securityContext);
		q.select(r).where(predicates.toArray(Predicate[]::new));
		TypedQuery<DynamicExecution> query = em.createQuery(q);
		BaseclassRepository.addPagination(dynamicExecutionFilter, query);
		return query.getResultList();

	}


	public <T extends DynamicExecution> void addDynamicExecutionPredicates(DynamicExecutionFilter dynamicExecutionFilter, CriteriaBuilder cb, CommonAbstractCriteria q, From<?,T> r, List<Predicate> predicates, SecurityContextBase securityContext) {
		if(dynamicExecutionFilter.getNameLike()!=null){
			predicates.add(cb.like(r.get(DynamicExecution_.name),dynamicExecutionFilter.getNameLike()));
		}
		if(dynamicExecutionFilter.getCanonicalNames()!=null&&!dynamicExecutionFilter.getCanonicalNames().isEmpty()){
			Join<T, ServiceCanonicalName> join=r.join(DynamicExecution_.serviceCanonicalNames);
			predicates.add(join.get(ServiceCanonicalName_.serviceCanonicalName).in(dynamicExecutionFilter.getCanonicalNames()));
		}
		if(dynamicExecutionFilter.getMethodNames()!=null&&!dynamicExecutionFilter.getMethodNames().isEmpty()){
			predicates.add(r.get(DynamicExecution_.methodName).in(dynamicExecutionFilter.getMethodNames()));
		}
		if(securityContext!=null){
			Join<T,Baseclass> join=r.join(DynamicExecution_.security);
			baseclassRepository.addBaseclassPredicates(cb,q,join,predicates,securityContext);
		}
	}

	public long countAllDynamicExecutions(DynamicExecutionFilter dynamicExecutionFilter, SecurityContextBase securityContext) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Long> q = cb.createQuery(Long.class);
		Root<DynamicExecution> r = q.from(DynamicExecution.class);
		List<Predicate> predicates = new ArrayList<>();
		addDynamicExecutionPredicates(dynamicExecutionFilter, cb, q, r, predicates, securityContext);
		q.select(cb.count(r)).where(predicates.toArray(Predicate[]::new));
		TypedQuery<Long> query = em.createQuery(q);
		BaseclassRepository.addPagination(dynamicExecutionFilter, query);
		return query.getFirstResult();

	}

	public List<ServiceCanonicalName> getAllServiceCanonicalNames(DynamicExecution dynamicExecution) {

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<ServiceCanonicalName> q = cb.createQuery(ServiceCanonicalName.class);
		Root<ServiceCanonicalName> r = q.from(ServiceCanonicalName.class);
		q.select(r).where(cb.equal(r.get(ServiceCanonicalName_.dynamicExecution),dynamicExecution));
		return em.createQuery(q).getResultList();
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

	public <T extends DynamicExecution> List<T> listByIds(Set<String> ids, Class<T> c, SecurityContextBase securityContext) {

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<T> q = cb.createQuery(c);
		Root<T> r = q.from(c);
		List<Predicate> predicates = new ArrayList<>();
		predicates.add(r.get(DynamicExecution_.id).in(ids));
		if (securityContext != null) {
			Join<T, Baseclass> join = r.join(DynamicExecution_.security);
			baseclassRepository.addBaseclassPredicates(cb, q, join, predicates, securityContext);
		}
		q.select(r).where(predicates.toArray(Predicate[]::new));
		TypedQuery<T> query = em.createQuery(q);
		return query.getResultList();
	}

	public <T extends DynamicExecution> T getByIdOrNull(String id, Class<T> c, SecurityContextBase securityContext) {

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<T> q = cb.createQuery(c);
		Root<T> r = q.from(c);
		List<Predicate> predicates = new ArrayList<>();
		predicates.add(cb.equal(r.get(DynamicExecution_.id), id));
		if (securityContext != null) {
			Join<T, Baseclass> join = r.join(DynamicExecution_.security);
			baseclassRepository.addBaseclassPredicates(cb, q, join, predicates, securityContext);
		}
		q.select(r).where(predicates.toArray(Predicate[]::new));
		TypedQuery<T> query = em.createQuery(q);
		List<T> resultList = query.getResultList();
		return resultList.isEmpty() ? null : resultList.get(0);
	}
}
