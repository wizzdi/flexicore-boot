package com.wizzdi.flexicore.security.data;

import com.flexicore.model.UserToBaseClass;
import com.flexicore.model.Baseclass;
import com.flexicore.model.Baseclass_;
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
	private BaseclassRepository baseclassRepository;
	@Autowired
	private SecurityLinkRepository securityLinkRepository;

	public List<UserToBaseClass> listAllUserToBaseclasss(UserToBaseclassFilter userToBaseclassFilter, SecurityContextBase securityContext){
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

	public <T extends UserToBaseClass> void addUserToBaseclassPredicates(UserToBaseclassFilter userToBaseclassFilter, CriteriaBuilder cb, CommonAbstractCriteria q, From<?,T> r, List<Predicate> predicates, SecurityContextBase securityContext) {
		securityLinkRepository.addSecurityLinkPredicates(userToBaseclassFilter,cb,q,r,predicates,securityContext);
	}

	public long countAllUserToBaseclasss(UserToBaseclassFilter userToBaseclassFilter, SecurityContextBase securityContext){
		CriteriaBuilder cb=em.getCriteriaBuilder();
		CriteriaQuery<Long> q=cb.createQuery(Long.class);
		Root<UserToBaseClass> r=q.from(UserToBaseClass.class);
		List<Predicate> predicates=new ArrayList<>();
		addUserToBaseclassPredicates(userToBaseclassFilter,cb,q,r,predicates,securityContext);
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


	public <T extends Baseclass> List<T> listByIds(Class<T> c,Set<String> ids,  SecurityContextBase securityContext) {
		return baseclassRepository.listByIds(c, ids, securityContext);
	}

	public <T extends Baseclass> T getByIdOrNull(String id, Class<T> c, SecurityContextBase securityContext) {
		return baseclassRepository.getByIdOrNull(id, c, securityContext);
	}
}
