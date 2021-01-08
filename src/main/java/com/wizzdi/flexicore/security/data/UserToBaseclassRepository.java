package com.wizzdi.flexicore.security.data;

import com.flexicore.model.UserToBaseClass;
import com.flexicore.model.Baseclass;
import com.flexicore.model.Baseclass_;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.security.request.UserToBaseclassFilter;
import com.flexicore.security.SecurityContext;
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

@Component
@Extension
public class UserToBaseclassRepository implements Plugin {
	@PersistenceContext
	private EntityManager em;
	@Autowired
	private BaseclassRepository baseclassRepository;
	@Autowired
	private SecurityLinkRepository securityLinkRepository;

	public List<UserToBaseClass> listAllUserToBaseclasss(UserToBaseclassFilter userToBaseclassFilter, SecurityContext securityContext){
		CriteriaBuilder cb=em.getCriteriaBuilder();
		CriteriaQuery<UserToBaseClass> q=cb.createQuery(UserToBaseClass.class);
		Root<UserToBaseClass> r=q.from(UserToBaseClass.class);
		List<Predicate> predicates=new ArrayList<>();
		addUserToBaseclassPredicates(userToBaseclassFilter,cb,q,r,predicates,securityContext);
		q.select(r).where(predicates.toArray(Predicate[]::new));
		TypedQuery<UserToBaseClass> query = em.createQuery(q);
		BaseclassRepository.addPagination(userToBaseclassFilter,query);
		return query.getResultList();

	}

	public <T extends UserToBaseClass> void addUserToBaseclassPredicates(UserToBaseclassFilter userToBaseclassFilter, CriteriaBuilder cb, CommonAbstractCriteria q, Path<T> r, List<Predicate> predicates, SecurityContext securityContext) {
		securityLinkRepository.addSecurityLinkPredicates(userToBaseclassFilter,cb,q,r,predicates,securityContext);
	}

	public long countAllUserToBaseclasss(UserToBaseclassFilter userToBaseclassFilter, SecurityContext securityContext){
		CriteriaBuilder cb=em.getCriteriaBuilder();
		CriteriaQuery<Long> q=cb.createQuery(Long.class);
		Root<UserToBaseClass> r=q.from(UserToBaseClass.class);
		List<Predicate> predicates=new ArrayList<>();
		addUserToBaseclassPredicates(userToBaseclassFilter,cb,q,r,predicates,securityContext);
		q.select(cb.count(r)).where(predicates.toArray(Predicate[]::new));
		TypedQuery<Long> query = em.createQuery(q);
		BaseclassRepository.addPagination(userToBaseclassFilter,query);
		return query.getFirstResult();

	}

	@Transactional
	public void merge(Object o){
		em.merge(o);
	}

	@Transactional
	public void massMerge(List<Object> list){
		for (Object o : list) {
			em.merge(o);
		}
	}

	public <T extends Baseclass> T getByIdOrNull(String id,Class<T> c, SecurityContext securityContext) {
		CriteriaBuilder cb=em.getCriteriaBuilder();
		CriteriaQuery<T> q=cb.createQuery(c);
		Root<T> r=q.from(c);
		List<Predicate> predicates=new ArrayList<>();
		predicates.add(cb.equal(r.get(Baseclass_.id),id));
		baseclassRepository.addBaseclassPredicates(cb,q,r,predicates,securityContext);
		q.select(r).where(predicates.toArray(Predicate[]::new));
		TypedQuery<T> query = em.createQuery(q);
		List<T> resultList = query.getResultList();
		return resultList.isEmpty()?null:resultList.get(0);
	}
}
