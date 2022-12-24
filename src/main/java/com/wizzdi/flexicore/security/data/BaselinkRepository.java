package com.wizzdi.flexicore.security.data;

import com.flexicore.model.Baseclass;
import com.flexicore.model.Baseclass_;
import com.flexicore.model.Baselink;
import com.flexicore.model.Baselink_;
import com.flexicore.security.SecurityContextBase;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.security.request.BaselinkFilter;
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
import java.util.stream.Collectors;

@Component
@Extension
public class BaselinkRepository implements Plugin {
	@PersistenceContext
	private EntityManager em;
	@Autowired
	private BaseclassRepository baseclassRepository;

	public List<Baselink> listAllBaselinks(BaselinkFilter baselinkFilter, SecurityContextBase securityContext) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Baselink> q = cb.createQuery(Baselink.class);
		Root<Baselink> r = q.from(Baselink.class);
		List<Predicate> predicates = new ArrayList<>();
		addBaselinkPredicates(baselinkFilter, cb, q, r, predicates, securityContext);
		q.select(r).where(predicates.toArray(Predicate[]::new));
		TypedQuery<Baselink> query = em.createQuery(q);
		BaseclassRepository.addPagination(baselinkFilter, query);
		return query.getResultList();

	}

	public <T extends Baselink> void addBaselinkPredicates(BaselinkFilter baselinkFilter, CriteriaBuilder cb, CommonAbstractCriteria q, From<?,T> r, List<Predicate> predicates, SecurityContextBase securityContext) {
		baseclassRepository.addBaseclassPredicates(baselinkFilter.getBasicPropertiesFilter(),cb, q, r, predicates, securityContext);
		if(baselinkFilter.getLeftside()!=null&&!baselinkFilter.getLeftside().isEmpty()){
			Set<String> ids=baselinkFilter.getLeftside().stream().map(f->f.getId()).collect(Collectors.toSet());
			Join<T,Baseclass> join=r.join(Baselink_.leftside);
			predicates.add(join.get(Baseclass_.id).in(ids));
		}
		if(baselinkFilter.getRightside()!=null&&!baselinkFilter.getRightside().isEmpty()){
			Set<String> ids=baselinkFilter.getRightside().stream().map(f->f.getId()).collect(Collectors.toSet());
			Join<T,Baseclass> join=r.join(Baselink_.rightside);
			predicates.add(join.get(Baseclass_.id).in(ids));
		}
		if(baselinkFilter.getValues()!=null&&!baselinkFilter.getValues().isEmpty()){
			Set<String> ids=baselinkFilter.getValues().stream().map(f->f.getId()).collect(Collectors.toSet());
			Join<T,Baseclass> join=r.join(Baselink_.value);
			predicates.add(join.get(Baseclass_.id).in(ids));
		}
		if(baselinkFilter.getSimpleValues()!=null&&!baselinkFilter.getSimpleValues().isEmpty()){
			predicates.add(r.get(Baselink_.simplevalue).in(baselinkFilter.getSimpleValues()));
		}
	}

	public long countAllBaselinks(BaselinkFilter baselinkFilter, SecurityContextBase securityContext) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Long> q = cb.createQuery(Long.class);
		Root<Baselink> r = q.from(Baselink.class);
		List<Predicate> predicates = new ArrayList<>();
		addBaselinkPredicates(baselinkFilter, cb, q, r, predicates, securityContext);
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

	public <T extends Baseclass> List<T> listByIds(Class<T> c, Set<String> ids, SecurityContextBase securityContext) {
		return baseclassRepository.listByIds(c, ids, securityContext);
	}

	public <T extends Baseclass> T getByIdOrNull(String id, Class<T> c, SecurityContextBase securityContext) {
		return baseclassRepository.getByIdOrNull(id, c, securityContext);
	}
}
