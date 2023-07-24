package com.wizzdi.flexicore.security.data;

import com.flexicore.model.Basic;
import com.flexicore.model.SecurityOperation;
import com.flexicore.model.Baseclass;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.security.request.SecurityOperationFilter;
import com.flexicore.security.SecurityContextBase;
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
public class SecurityOperationRepository implements Plugin {
	@PersistenceContext
	private EntityManager em;
	@Autowired
	private SecuredBasicRepository securedBasicRepository;


	public List<SecurityOperation> listAllOperations(SecurityOperationFilter operationFilter, SecurityContextBase securityContext){
		CriteriaBuilder cb=em.getCriteriaBuilder();
		CriteriaQuery<SecurityOperation> q=cb.createQuery(SecurityOperation.class);
		Root<SecurityOperation> r=q.from(SecurityOperation.class);
		List<Predicate> predicates=new ArrayList<>();
		addOperationPredicates(operationFilter,cb,q,r,predicates,securityContext);
		q.select(r).where(predicates.toArray(Predicate[]::new));
		TypedQuery<SecurityOperation> query = em.createQuery(q);
		BasicRepository.addPagination(operationFilter,query);
		return query.getResultList();

	}

	public <T extends SecurityOperation> void addOperationPredicates(SecurityOperationFilter operationFilter, CriteriaBuilder cb, CommonAbstractCriteria q, From<?,T> r, List<Predicate> predicates, SecurityContextBase securityContext) {
		securedBasicRepository.addSecuredBasicPredicates(operationFilter.getBasicPropertiesFilter(),cb,q,r,predicates,securityContext);
	}

	public long countAllOperations(SecurityOperationFilter operationFilter, SecurityContextBase securityContext){
		CriteriaBuilder cb=em.getCriteriaBuilder();
		CriteriaQuery<Long> q=cb.createQuery(Long.class);
		Root<SecurityOperation> r=q.from(SecurityOperation.class);
		List<Predicate> predicates=new ArrayList<>();
		addOperationPredicates(operationFilter,cb,q,r,predicates,securityContext);
		q.select(cb.count(r)).where(predicates.toArray(Predicate[]::new));
		TypedQuery<Long> query = em.createQuery(q);
		return query.getSingleResult();

	}


	@Transactional
	public <T> T merge(T base, boolean updateDate, boolean propagateEvents) {
		return securedBasicRepository.merge(base, updateDate, propagateEvents);
	}

	@Transactional
	public void massMerge(List<?> toMerge, boolean updatedate, boolean propagateEvents) {
		securedBasicRepository.massMerge(toMerge, updatedate, propagateEvents);
	}

	public <T extends Baseclass> List<T> listByIds(Class<T> c, Set<String> ids, SecurityContextBase securityContext) {
		return securedBasicRepository.listByIds(c, ids, securityContext);
	}

	public <T extends Baseclass> T getByIdOrNull(String id, Class<T> c, SecurityContextBase securityContext) {
		return securedBasicRepository.getByIdOrNull(id, c, securityContext);
	}

	public <D extends Basic, E extends Baseclass, T extends D> T getByIdOrNull(String id, Class<T> c, SingularAttribute<D, E> baseclassAttribute, SecurityContextBase securityContext) {
		return securedBasicRepository.getByIdOrNull(id, c, baseclassAttribute, securityContext);
	}

	public <D extends Basic, E extends Baseclass, T extends D> List<T> listByIds(Class<T> c, Set<String> ids, SingularAttribute<D, E> baseclassAttribute, SecurityContextBase securityContext) {
		return securedBasicRepository.listByIds(c, ids, baseclassAttribute, securityContext);
	}

	public <D extends Basic, T extends D> List<T> findByIds(Class<T> c, Set<String> ids, SingularAttribute<D, String> idAttribute) {
		return securedBasicRepository.findByIds(c, ids, idAttribute);
	}

	public <T extends Basic> List<T> findByIds(Class<T> c, Set<String> requested) {
		return securedBasicRepository.findByIds(c, requested);
	}

	public <T> T findByIdOrNull(Class<T> type, String id) {
		return securedBasicRepository.findByIdOrNull(type, id);
	}

	@Transactional
	public <T> T merge(T base) {
		return securedBasicRepository.merge(base);
	}

	@Transactional
	public void massMerge(List<?> toMerge) {
		securedBasicRepository.massMerge(toMerge);
	}
}
