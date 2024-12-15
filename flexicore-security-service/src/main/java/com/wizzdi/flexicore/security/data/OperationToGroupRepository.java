package com.wizzdi.flexicore.security.data;

import com.flexicore.model.Baseclass;
import com.flexicore.model.Basic;
import com.flexicore.model.OperationToGroup;
import com.flexicore.model.OperationToGroup_;
import com.wizzdi.segmantix.model.SecurityContext;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.security.request.OperationToGroupFilter;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import jakarta.persistence.metamodel.SingularAttribute;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
@Extension
public class OperationToGroupRepository implements Plugin {
	@PersistenceContext
	private EntityManager em;
	@Autowired
	private SecuredBasicRepository securedBasicRepository;


	public List<OperationToGroup> listAllOperationToGroups(OperationToGroupFilter operationFilter, SecurityContext securityContext){
		CriteriaBuilder cb=em.getCriteriaBuilder();
		CriteriaQuery<OperationToGroup> q=cb.createQuery(OperationToGroup.class);
		Root<OperationToGroup> r=q.from(OperationToGroup.class);
		List<Predicate> predicates=new ArrayList<>();
		addOperationToGroupPredicates(operationFilter,cb,q,r,predicates,securityContext);
		q.select(r).where(predicates.toArray(Predicate[]::new));
		TypedQuery<OperationToGroup> query = em.createQuery(q);
		BasicRepository.addPagination(operationFilter,query);
		return query.getResultList();

	}

	public <T extends OperationToGroup> void addOperationToGroupPredicates(OperationToGroupFilter operationFilter, CriteriaBuilder cb, CommonAbstractCriteria q, From<?,T> r, List<Predicate> predicates, SecurityContext securityContext) {
		securedBasicRepository.addSecuredBasicPredicates(operationFilter.getBasicPropertiesFilter(),cb,q,r,predicates,securityContext);
		if(operationFilter.getOperationGroups()!=null&&!operationFilter.getOperationGroups().isEmpty()){
			predicates.add(r.get(OperationToGroup_.operationGroup).in(operationFilter.getOperationGroups()));
		}
		if(operationFilter.getOperations()!=null&&!operationFilter.getOperations().isEmpty()){
			predicates.add(r.get(OperationToGroup_.operation).in(operationFilter.getOperations()));
		}
	}

	public long countAllOperationToGroups(OperationToGroupFilter operationFilter, SecurityContext securityContext){
		CriteriaBuilder cb=em.getCriteriaBuilder();
		CriteriaQuery<Long> q=cb.createQuery(Long.class);
		Root<OperationToGroup> r=q.from(OperationToGroup.class);
		List<Predicate> predicates=new ArrayList<>();
		addOperationToGroupPredicates(operationFilter,cb,q,r,predicates,securityContext);
		q.select(cb.count(r)).where(predicates.toArray(Predicate[]::new));
		TypedQuery<Long> query = em.createQuery(q);
		return query.getSingleResult();

	}


	public <T> T merge(T base, boolean updateDate, boolean propagateEvents) {
		return securedBasicRepository.merge(base, updateDate, propagateEvents);
	}

	public void massMerge(List<?> toMerge, boolean updatedate, boolean propagateEvents) {
		securedBasicRepository.massMerge(toMerge, updatedate, propagateEvents);
	}

	public <T extends Baseclass> List<T> listByIds(Class<T> c, Set<String> ids, SecurityContext securityContext) {
		return securedBasicRepository.listByIds(c, ids, securityContext);
	}

	public <T extends Baseclass> T getByIdOrNull(String id, Class<T> c, SecurityContext securityContext) {
		return securedBasicRepository.getByIdOrNull(id, c, securityContext);
	}

	public <D extends Basic, E extends Baseclass, T extends D> T getByIdOrNull(String id, Class<T> c, SingularAttribute<D, E> baseclassAttribute, SecurityContext securityContext) {
		return securedBasicRepository.getByIdOrNull(id, c, baseclassAttribute, securityContext);
	}

	public <D extends Basic, E extends Baseclass, T extends D> List<T> listByIds(Class<T> c, Set<String> ids, SingularAttribute<D, E> baseclassAttribute, SecurityContext securityContext) {
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

	public <T> T merge(T base) {
		return securedBasicRepository.merge(base);
	}

	public void massMerge(List<?> toMerge) {
		securedBasicRepository.massMerge(toMerge);
	}
}
