package com.wizzdi.flexicore.security.data;

import com.flexicore.model.*;
import com.wizzdi.segmantix.model.SecurityContext;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.security.request.OperationToClazzFilter;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Extension
public class OperationToClazzRepository implements Plugin {
	@PersistenceContext
	private EntityManager em;
	@Autowired
	private SecuredBasicRepository securedBasicRepository;


	public List<OperationToClazz> listAllOperationToClazzs(OperationToClazzFilter operationToClazzFilter, SecurityContext securityContext){
		CriteriaBuilder cb=em.getCriteriaBuilder();
		CriteriaQuery<OperationToClazz> q=cb.createQuery(OperationToClazz.class);
		Root<OperationToClazz> r=q.from(OperationToClazz.class);
		List<Predicate> predicates=new ArrayList<>();
		addOperationToClazzPredicates(operationToClazzFilter,cb,q,r,predicates,securityContext);
		q.select(r).where(predicates.toArray(Predicate[]::new));
		TypedQuery<OperationToClazz> query = em.createQuery(q);
		BasicRepository.addPagination(operationToClazzFilter,query);
		return query.getResultList();

	}

	public  <T extends OperationToClazz> void addOperationToClazzPredicates(OperationToClazzFilter operationToClazzFilter, CriteriaBuilder cb, CommonAbstractCriteria q, From<?,T> r, List<Predicate> predicates, SecurityContext securityContext) {
		BasicRepository.addBasicPropertiesFilter(operationToClazzFilter.getBasicPropertiesFilter(),cb,q,r,predicates);
		if(operationToClazzFilter.getClazzes()!=null&&!operationToClazzFilter.getClazzes().isEmpty()){
			Set<String> ids=operationToClazzFilter.getClazzes().stream().map(f->f.name()).collect(Collectors.toSet());
			predicates.add(r.get(OperationToClazz_.type).in(ids));
		}

		if(operationToClazzFilter.getSecurityOperations()!=null&&!operationToClazzFilter.getSecurityOperations().isEmpty()){
			Set<String> ids=operationToClazzFilter.getSecurityOperations().stream().map(f->f.getId()).collect(Collectors.toSet());
			Join<T, SecurityOperation> join=r.join(OperationToClazz_.operation);
			predicates.add(join.get(SecurityOperation_.id).in(ids));
		}
	}

	public long countAllOperationToClazzs(OperationToClazzFilter operationToClazzFilter, SecurityContext securityContext){
		CriteriaBuilder cb=em.getCriteriaBuilder();
		CriteriaQuery<Long> q=cb.createQuery(Long.class);
		Root<OperationToClazz> r=q.from(OperationToClazz.class);
		List<Predicate> predicates=new ArrayList<>();
		addOperationToClazzPredicates(operationToClazzFilter,cb,q,r,predicates,securityContext);
		q.select(cb.count(r)).where(predicates.toArray(Predicate[]::new));
		TypedQuery<Long> query = em.createQuery(q);
		return query.getSingleResult();

	}


	public <T> T merge(T o){
		return securedBasicRepository.merge(o);
	}


	public void massMerge(List<Object> list){
		securedBasicRepository.massMerge(list);
	}

	public <T extends Baseclass> List<T> listByIds(Class<T> c, Set<String> ids, SecurityContext securityContext) {
		return securedBasicRepository.listByIds(c, ids, securityContext);
	}

	public <T extends Baseclass> T getByIdOrNull(String id, Class<T> c, SecurityContext securityContext) {
		return securedBasicRepository.getByIdOrNull(id, c, securityContext);
	}
}
