package com.wizzdi.flexicore.security.data;

import com.flexicore.model.UserToBaseclass;
import com.flexicore.model.Baseclass;
import com.flexicore.model.UserToBaseclass_;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.security.request.UserToBaseclassFilter;
import com.flexicore.security.SecurityContextBase;
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
public class UserToBaseclassRepository implements Plugin {
	@PersistenceContext
	private EntityManager em;
	@Autowired
	private SecurityLinkRepository securityLinkRepository;


	public List<UserToBaseclass> listAllUserToBaseclasss(UserToBaseclassFilter userToBaseclassFilter, SecurityContextBase securityContext){
		CriteriaBuilder cb=em.getCriteriaBuilder();
		CriteriaQuery<UserToBaseclass> q=cb.createQuery(UserToBaseclass.class);
		Root<UserToBaseclass> r=q.from(UserToBaseclass.class);
		List<Predicate> predicates=new ArrayList<>();
		addUserToBaseclassPredicates(userToBaseclassFilter,cb,q,r,predicates,securityContext);
		q.select(r).where(predicates.toArray(Predicate[]::new));
		TypedQuery<UserToBaseclass> query = em.createQuery(q);
		BasicRepository.addPagination(userToBaseclassFilter,query);
		return query.getResultList();

	}

	public <T extends UserToBaseclass> void addUserToBaseclassPredicates(UserToBaseclassFilter userToBaseclassFilter, CriteriaBuilder cb, CommonAbstractCriteria q, From<?,T> r, List<Predicate> predicates, SecurityContextBase securityContext) {
		securityLinkRepository.addSecurityLinkPredicates(userToBaseclassFilter,cb,q,r,predicates,securityContext);
		if(userToBaseclassFilter.getUsers()!=null&&!userToBaseclassFilter.getUsers().isEmpty()){
			predicates.add(r.get(UserToBaseclass_.user).in(userToBaseclassFilter.getUsers()));
		}
	}

	public long countAllUserToBaseclasss(UserToBaseclassFilter userToBaseclassFilter, SecurityContextBase securityContext){
		CriteriaBuilder cb=em.getCriteriaBuilder();
		CriteriaQuery<Long> q=cb.createQuery(Long.class);
		Root<UserToBaseclass> r=q.from(UserToBaseclass.class);
		List<Predicate> predicates=new ArrayList<>();
		addUserToBaseclassPredicates(userToBaseclassFilter,cb,q,r,predicates,securityContext);
		q.select(cb.count(r)).where(predicates.toArray(Predicate[]::new));
		TypedQuery<Long> query = em.createQuery(q);
		return query.getSingleResult();

	}

	@Transactional
	public <T> T merge(T o){
		return securityLinkRepository.merge(o);
	}

	@Transactional
	public void massMerge(List<Object> list){
		securityLinkRepository.massMerge(list);
	}


	public <T extends Baseclass> List<T> listByIds(Class<T> c,Set<String> ids,  SecurityContextBase securityContext) {
		return securityLinkRepository.listByIds(c, ids, securityContext);
	}

	public <T extends Baseclass> T getByIdOrNull(String id, Class<T> c, SecurityContextBase securityContext) {
		return securityLinkRepository.getByIdOrNull(id, c, securityContext);
	}
}
