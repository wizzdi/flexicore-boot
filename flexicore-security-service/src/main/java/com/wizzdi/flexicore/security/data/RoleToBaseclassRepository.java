package com.wizzdi.flexicore.security.data;

import com.flexicore.model.Role;
import com.flexicore.model.RoleToBaseclass;
import com.flexicore.model.Baseclass;
import com.flexicore.model.RoleToBaseclass_;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.flexicore.security.SecurityContextBase;
import com.wizzdi.flexicore.security.request.RoleToBaseclassFilter;
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
public class RoleToBaseclassRepository implements Plugin {
	@PersistenceContext
	private EntityManager em;

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
		BasicRepository.addPagination(roleToBaseclassFilter,query);
		return query.getResultList();

	}

	public <T extends RoleToBaseclass> void addRoleToBaseclassPredicates(RoleToBaseclassFilter roleToBaseclassFilter, CriteriaBuilder cb, CommonAbstractCriteria q, From<?,T> r, List<Predicate> predicates, SecurityContextBase securityContext) {
		securityLinkRepository.addSecurityLinkPredicates(roleToBaseclassFilter,cb,q,r,predicates,securityContext);
		if(roleToBaseclassFilter.getRoles()!=null&&!roleToBaseclassFilter.getRoles().isEmpty()){
			predicates.add(r.get(RoleToBaseclass_.role).in(roleToBaseclassFilter.getRoles()));
		}
	}

	public long countAllRoleToBaseclasss(RoleToBaseclassFilter roleToBaseclassFilter, SecurityContextBase securityContext){
		CriteriaBuilder cb=em.getCriteriaBuilder();
		CriteriaQuery<Long> q=cb.createQuery(Long.class);
		Root<RoleToBaseclass> r=q.from(RoleToBaseclass.class);
		List<Predicate> predicates=new ArrayList<>();
		addRoleToBaseclassPredicates(roleToBaseclassFilter,cb,q,r,predicates,securityContext);
		q.select(cb.count(r)).where(predicates.toArray(Predicate[]::new));
		TypedQuery<Long> query = em.createQuery(q);
		return query.getSingleResult();

	}


	public <T> T merge(T o){
		return securityLinkRepository.merge(o);
	}


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
