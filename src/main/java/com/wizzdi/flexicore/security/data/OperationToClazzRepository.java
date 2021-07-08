package com.wizzdi.flexicore.security.data;

import com.flexicore.model.*;
import com.flexicore.security.SecurityContextBase;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.security.request.OperationToClazzFilter;
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
import java.util.stream.Collectors;

@Component
@Extension
public class OperationToClazzRepository implements Plugin {
	@PersistenceContext
	private EntityManager em;
	@Autowired
	private BaseclassRepository baseclassRepository;
	@Autowired
	private BaselinkRepository baselinkRepository;

	public List<OperationToClazz> listAllOperationToClazzs(OperationToClazzFilter operationToClazzFilter, SecurityContextBase securityContext){
		CriteriaBuilder cb=em.getCriteriaBuilder();
		CriteriaQuery<OperationToClazz> q=cb.createQuery(OperationToClazz.class);
		Root<OperationToClazz> r=q.from(OperationToClazz.class);
		List<Predicate> predicates=new ArrayList<>();
		addOperationToClazzPredicates(operationToClazzFilter,cb,q,r,predicates,securityContext);
		q.select(r).where(predicates.toArray(Predicate[]::new));
		TypedQuery<OperationToClazz> query = em.createQuery(q);
		BaseclassRepository.addPagination(operationToClazzFilter,query);
		return query.getResultList();

	}

	public  <T extends OperationToClazz> void addOperationToClazzPredicates(OperationToClazzFilter operationToClazzFilter, CriteriaBuilder cb, CommonAbstractCriteria q, From<?,T> r, List<Predicate> predicates, SecurityContextBase securityContext) {
		baselinkRepository.addBaselinkPredicates(operationToClazzFilter,cb,q,r,predicates,securityContext);
		if(operationToClazzFilter.getClazzes()!=null&&!operationToClazzFilter.getClazzes().isEmpty()){
			Set<String> ids=operationToClazzFilter.getClazzes().stream().map(f->f.getId()).collect(Collectors.toSet());
			Join<T, Clazz> join=cb.treat(r.join(Baselink_.rightside),Clazz.class);
			predicates.add(join.get(Clazz_.id).in(ids));
		}

		if(operationToClazzFilter.getSecurityOperations()!=null&&!operationToClazzFilter.getSecurityOperations().isEmpty()){
			Set<String> ids=operationToClazzFilter.getSecurityOperations().stream().map(f->f.getId()).collect(Collectors.toSet());
			Join<T, SecurityOperation> join=cb.treat(r.join(Baselink_.leftside),SecurityOperation.class);
			predicates.add(join.get(SecurityOperation_.id).in(ids));
		}
	}

	public long countAllOperationToClazzs(OperationToClazzFilter operationToClazzFilter, SecurityContextBase securityContext){
		CriteriaBuilder cb=em.getCriteriaBuilder();
		CriteriaQuery<Long> q=cb.createQuery(Long.class);
		Root<OperationToClazz> r=q.from(OperationToClazz.class);
		List<Predicate> predicates=new ArrayList<>();
		addOperationToClazzPredicates(operationToClazzFilter,cb,q,r,predicates,securityContext);
		q.select(cb.count(r)).where(predicates.toArray(Predicate[]::new));
		TypedQuery<Long> query = em.createQuery(q);
		return query.getSingleResult();

	}

	@Transactional
	public <T> T merge(T o){
		return em.merge(o);
	}

	@Transactional
	public void massMerge(List<Object> list){
		for (Object o : list) {
			em.merge(o);
		}
	}

	public <T extends Baseclass> List<T> listByIds(Class<T> c,Set<String> ids,  SecurityContextBase securityContext) {
		return baseclassRepository.listByIds(c, ids, securityContext);
	}

	public <T extends Baseclass> T getByIdOrNull(String id, Class<T> c, SecurityContextBase securityContext) {
		return baseclassRepository.getByIdOrNull(id, c, securityContext);
	}
}
