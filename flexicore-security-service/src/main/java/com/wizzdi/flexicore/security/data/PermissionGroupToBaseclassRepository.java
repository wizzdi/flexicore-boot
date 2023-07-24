package com.wizzdi.flexicore.security.data;

import com.flexicore.model.PermissionGroupToBaseclass;
import com.flexicore.model.Baseclass;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.flexicore.security.SecurityContextBase;
import com.wizzdi.flexicore.security.request.PermissionGroupToBaseclassFilter;
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
public class PermissionGroupToBaseclassRepository implements Plugin {
	@PersistenceContext
	private EntityManager em;
	@Autowired
	private SecuredBasicRepository securedBasicRepository;


	public List<PermissionGroupToBaseclass> listAllPermissionGroupToBaseclasss(PermissionGroupToBaseclassFilter permissionGroupToBaseclassFilter, SecurityContextBase securityContext){
		CriteriaBuilder cb=em.getCriteriaBuilder();
		CriteriaQuery<PermissionGroupToBaseclass> q=cb.createQuery(PermissionGroupToBaseclass.class);
		Root<PermissionGroupToBaseclass> r=q.from(PermissionGroupToBaseclass.class);
		List<Predicate> predicates=new ArrayList<>();
		addPermissionGroupToBaseclassPredicates(permissionGroupToBaseclassFilter,cb,q,r,predicates,securityContext);
		q.select(r).where(predicates.toArray(Predicate[]::new));
		TypedQuery<PermissionGroupToBaseclass> query = em.createQuery(q);
		BasicRepository.addPagination(permissionGroupToBaseclassFilter,query);
		return query.getResultList();

	}

	public  <T extends PermissionGroupToBaseclass> void addPermissionGroupToBaseclassPredicates(PermissionGroupToBaseclassFilter permissionGroupToBaseclassFilter, CriteriaBuilder cb, CommonAbstractCriteria q, From<?,T> r, List<Predicate> predicates, SecurityContextBase securityContext) {
		securedBasicRepository.addSecuredBasicPredicates(permissionGroupToBaseclassFilter.getBasicPropertiesFilter(),cb,q,r,predicates,securityContext);
	}

	public long countAllPermissionGroupToBaseclasss(PermissionGroupToBaseclassFilter permissionGroupToBaseclassFilter, SecurityContextBase securityContext){
		CriteriaBuilder cb=em.getCriteriaBuilder();
		CriteriaQuery<Long> q=cb.createQuery(Long.class);
		Root<PermissionGroupToBaseclass> r=q.from(PermissionGroupToBaseclass.class);
		List<Predicate> predicates=new ArrayList<>();
		addPermissionGroupToBaseclassPredicates(permissionGroupToBaseclassFilter,cb,q,r,predicates,securityContext);
		q.select(cb.count(r)).where(predicates.toArray(Predicate[]::new));
		TypedQuery<Long> query = em.createQuery(q);
		return query.getSingleResult();

	}

	@Transactional
	public <T> T merge(T o){
		return securedBasicRepository.merge(o);
	}

	@Transactional
	public void massMerge(List<Object> list){
		securedBasicRepository.massMerge(list);
	}

	public <T extends Baseclass> List<T> listByIds(Class<T> c,Set<String> ids,  SecurityContextBase securityContext) {
		return securedBasicRepository.listByIds(c, ids, securityContext);
	}

	public <T extends Baseclass> T getByIdOrNull(String id, Class<T> c, SecurityContextBase securityContext) {
		return securedBasicRepository.getByIdOrNull(id, c, securityContext);
	}

}
