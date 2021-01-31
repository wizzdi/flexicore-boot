package com.wizzdi.flexicore.security.data;

import com.flexicore.model.RoleToBaseclass;
import com.flexicore.model.Baseclass;
import com.flexicore.model.Baseclass_;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.flexicore.security.SecurityContextBase;
import com.wizzdi.flexicore.security.request.RoleToBaseclassFilter;
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
public class RoleToBaseclassRepository implements Plugin {
	@PersistenceContext
	private EntityManager em;
	@Autowired
	private BaseclassRepository baseclassRepository;
	@Autowired
	private SecurityLinkRepository securityLinkRepository;

	public List<RoleToBaseclass> listAllRoleToBaseclasss(RoleToBaseclassFilter roleToBaseclassFilter, SecurityContextBase securityContext){
		CriteriaBuilder cb=em.getCriteriaBuilder();
		CriteriaQuery<RoleToBaseclass> q=cb.createQuery(RoleToBaseclass.class);
		Root<RoleToBaseclass> r=q.from(RoleToBaseclass.class);
		List<Predicate> predicates=new ArrayList<>();
		addRoleToBaseclassPredicates(roleToBaseclassFilter,cb,q,r,predicates,securityContext);
		q.select(r).where(predicates.toArray(Predicate[]::new));
		TypedQuery<RoleToBaseclass> query = em.createQuery(q);
		BaseclassRepository.addPagination(roleToBaseclassFilter,query);
		return query.getResultList();

	}

	public <T extends RoleToBaseclass> void addRoleToBaseclassPredicates(RoleToBaseclassFilter roleToBaseclassFilter, CriteriaBuilder cb, CommonAbstractCriteria q, From<?,T> r, List<Predicate> predicates, SecurityContextBase securityContext) {
		securityLinkRepository.addSecurityLinkPredicates(roleToBaseclassFilter,cb,q,r,predicates,securityContext);
	}

	public long countAllRoleToBaseclasss(RoleToBaseclassFilter roleToBaseclassFilter, SecurityContextBase securityContext){
		CriteriaBuilder cb=em.getCriteriaBuilder();
		CriteriaQuery<Long> q=cb.createQuery(Long.class);
		Root<RoleToBaseclass> r=q.from(RoleToBaseclass.class);
		List<Predicate> predicates=new ArrayList<>();
		addRoleToBaseclassPredicates(roleToBaseclassFilter,cb,q,r,predicates,securityContext);
		q.select(cb.count(r)).where(predicates.toArray(Predicate[]::new));
		TypedQuery<Long> query = em.createQuery(q);
		BaseclassRepository.addPagination(roleToBaseclassFilter,query);
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

	public <T extends Baseclass> List<T> listByIds(Class<T> c,Set<String> ids,  SecurityContextBase securityContext) {
		return baseclassRepository.listByIds(c, ids, securityContext);
	}

	public <T extends Baseclass> T getByIdOrNull(String id, Class<T> c, SecurityContextBase securityContext) {
		return baseclassRepository.getByIdOrNull(id, c, securityContext);
	}
}
