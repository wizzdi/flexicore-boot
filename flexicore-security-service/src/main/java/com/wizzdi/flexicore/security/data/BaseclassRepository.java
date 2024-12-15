package com.wizzdi.flexicore.security.data;

import com.flexicore.model.*;

import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.security.request.BaseclassFilter;
import com.wizzdi.flexicore.security.request.BasicPropertiesFilter;
import com.wizzdi.flexicore.security.request.SoftDeleteOption;
import com.wizzdi.segmantix.model.SecurityContext;
import com.wizzdi.segmantix.service.SecurityRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import jakarta.persistence.metamodel.SingularAttribute;
import org.pf4j.Extension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
@Extension
public class BaseclassRepository implements Plugin {

	private static final Logger logger = LoggerFactory.getLogger(BaseclassRepository.class);
	@PersistenceContext
	private EntityManager em;
	@Autowired
	private SecurityRepository securityRepository;



	@Autowired
	private BasicRepository basicRepository;


	public List<Baseclass> listAllBaseclass(BaseclassFilter baseclassFilter, SecurityContext securityContext){
		CriteriaBuilder cb=em.getCriteriaBuilder();
		CriteriaQuery<Baseclass> q=cb.createQuery(Baseclass.class);
		Root<Baseclass> r=q.from(Baseclass.class);
		List<Predicate> predicates=new ArrayList<>();
		addBaseclassPredicates(baseclassFilter,cb,q,r,predicates,securityContext);
		q.select(r).where(predicates.toArray(Predicate[]::new)).orderBy(cb.asc(r.get(Baseclass_.name)));
		TypedQuery<Baseclass> query = em.createQuery(q);
		BasicRepository.addPagination(baseclassFilter,query);
		return query.getResultList();

	}

	public <T extends Baseclass> void addBaseclassPredicates(BaseclassFilter baseclassFilter, CriteriaBuilder cb, CommonAbstractCriteria q, From<?,T> r, List<Predicate> predicates, SecurityContext securityContext) {
		addBaseclassPredicates(baseclassFilter.getBasicPropertiesFilter(),cb,q,r,predicates,securityContext);
		if(baseclassFilter.getClazzes()!=null&&!baseclassFilter.getClazzes().isEmpty()){
			Set<String> ids=baseclassFilter.getClazzes().stream().map(f->f.name()).collect(Collectors.toSet());
			predicates.add(r.get(Baseclass_.dtype).in(baseclassFilter.getClazzes()));
		}
	}

	public long countAllBaseclass(BaseclassFilter baseclassFilter,SecurityContext securityContext){
		CriteriaBuilder cb=em.getCriteriaBuilder();
		CriteriaQuery<Long> q=cb.createQuery(Long.class);
		Root<Baseclass> r=q.from(Baseclass.class);
		List<Predicate> predicates=new ArrayList<>();
		addBaseclassPredicates(baseclassFilter,cb,q,r,predicates,securityContext);
		q.select(cb.count(r)).where(predicates.toArray(Predicate[]::new));
		TypedQuery<Long> query = em.createQuery(q);
		return query.getSingleResult();

	}




	public static <T> boolean addPagination(BaseclassFilter baseclassFilter, TypedQuery<T> q) {
		return BasicRepository.addPagination(baseclassFilter, q);
	}

	public <T extends Baseclass> void addBaseclassPredicates(BasicPropertiesFilter basicPropertiesFilter, CriteriaBuilder cb, CommonAbstractCriteria q, From<?, T> r, List<Predicate> predicates, SecurityContext securityContext) {
		if (basicPropertiesFilter != null) {
			BasicRepository.addBasicPropertiesFilter(basicPropertiesFilter, cb, q, r, predicates);
		} else {
			BasicRepository.addBasicPropertiesFilter(new BasicPropertiesFilter().setSoftDelete(SoftDeleteOption.DEFAULT), cb, q, r, predicates);
		}
		if (securityContext != null) {
			addBaseclassPredicates(cb, q, r, predicates, securityContext);
		}
	}

	public <T extends Baseclass> void addBaseclassPredicates(CriteriaBuilder cb, CommonAbstractCriteria q, From<?, T> r, List<Predicate> predicates, SecurityContext securityContext) {
		securityRepository.addBaseclassPredicates(cb,q,r,predicates,securityContext);
	}

	public boolean requiresSecurityPredicates(SecurityContext securityContext) {
		return securityRepository.requiresSecurityPredicates(securityContext);
	}


	public <T extends Baseclass> List<T> listByIds(Class<T> c, Set<String> ids, SecurityContext securityContext) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<T> q = cb.createQuery(c);
		Root<T> r = q.from(c);
		List<Predicate> predicates = new ArrayList<>();
		predicates.add(r.get(Baseclass_.id).in(ids));
		addBaseclassPredicates(cb, q, r, predicates, securityContext);
		q.select(r).where(predicates.toArray(Predicate[]::new));
		TypedQuery<T> query = em.createQuery(q);
		return query.getResultList();
	}

	public <T extends Baseclass> T getByIdOrNull(String id, Class<T> c, SecurityContext securityContext) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<T> q = cb.createQuery(c);
		Root<T> r = q.from(c);
		List<Predicate> predicates = new ArrayList<>();
		predicates.add(cb.equal(r.get(Baseclass_.id), id));
		addBaseclassPredicates(cb, q, r, predicates, securityContext);
		q.select(r).where(predicates.toArray(Predicate[]::new));
		TypedQuery<T> query = em.createQuery(q);
		List<T> resultList = query.getResultList();
		return resultList.isEmpty() ? null : resultList.getFirst();
	}

	public <D extends Basic, E extends Baseclass, T extends D> T getByIdOrNull(String id, Class<T> c, SingularAttribute<D, E> baseclassAttribute, SecurityContext securityContext) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<T> q = cb.createQuery(c);
		Root<T> r = q.from(c);
		List<Predicate> predicates = new ArrayList<>();
		predicates.add(cb.equal(r.get(Basic_.id), id));
		if(requiresSecurityPredicates(securityContext)){
			Join<T, E> join = r.join(baseclassAttribute);
			addBaseclassPredicates(cb, q, join, predicates, securityContext);
		}

		q.select(r).where(predicates.toArray(Predicate[]::new));
		TypedQuery<T> query = em.createQuery(q);
		List<T> resultList = query.getResultList();
		return resultList.isEmpty() ? null : resultList.getFirst();
	}



	public <D extends Basic, E extends Baseclass, T extends D> List<T> listByIds(Class<T> c, Set<String> ids, SingularAttribute<D, E> baseclassAttribute, SecurityContext securityContext) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<T> q = cb.createQuery(c);
		Root<T> r = q.from(c);
		List<Predicate> predicates = new ArrayList<>();
		predicates.add(r.get(Basic_.id).in(ids));
		if(requiresSecurityPredicates(securityContext)){
			Join<T, E> join = r.join(baseclassAttribute);
			addBaseclassPredicates(cb, q, join, predicates, securityContext);
		}

		q.select(r).where(predicates.toArray(Predicate[]::new));
		TypedQuery<T> query = em.createQuery(q);
		return query.getResultList();
	}


	public <D extends Basic, T extends D> List<T> findByIds(Class<T> c, Set<String> ids, SingularAttribute<D, String> idAttribute) {
		return basicRepository.findByIds(c, ids, idAttribute);
	}

	public <T extends Basic> List<T> findByIds(Class<T> c, Set<String> requested) {
		return basicRepository.findByIds(c, requested);
	}

	public <T> T findByIdOrNull(Class<T> type, String id) {
		return basicRepository.findByIdOrNull(type, id);
	}

	public <T> T merge(T base) {
		return basicRepository.merge(base);
	}

	public <T> T merge(T base, boolean updateDate, boolean propagateEvents) {
		return basicRepository.merge(base, updateDate, propagateEvents);
	}


	public void massMerge(List<?> toMerge, boolean updatedate, boolean propagateEvents) {
		basicRepository.massMerge(toMerge, updatedate, propagateEvents);
	}


	public <T> T merge(T base, boolean updateDate) {
		return basicRepository.merge(base, updateDate);
	}


	public void massMerge(List<?> toMerge) {
		basicRepository.massMerge(toMerge);
	}


	public void massMerge(List<?> toMerge, boolean updatedate) {
		basicRepository.massMerge(toMerge, updatedate);
	}
}
