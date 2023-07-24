package com.wizzdi.flexicore.security.data;

import com.flexicore.model.Baseclass;
import com.flexicore.model.PermissionGroup;
import com.flexicore.model.PermissionGroup_;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.flexicore.security.SecurityContextBase;
import com.wizzdi.flexicore.security.request.PermissionGroupFilter;
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
public class PermissionGroupRepository implements Plugin {
	@PersistenceContext
	private EntityManager em;
	@Autowired
	private SecuredBasicRepository securedBasicRepository;




	public List<PermissionGroup> listAllPermissionGroups(PermissionGroupFilter permissionGroupFilter, SecurityContextBase securityContext){
		CriteriaBuilder cb=em.getCriteriaBuilder();
		CriteriaQuery<PermissionGroup> q=cb.createQuery(PermissionGroup.class);
		Root<PermissionGroup> r=q.from(PermissionGroup.class);
		List<Predicate> predicates=new ArrayList<>();
		addPermissionGroupPredicates(permissionGroupFilter,cb,q,r,predicates,securityContext);
		q.select(r).where(predicates.toArray(Predicate[]::new));
		TypedQuery<PermissionGroup> query = em.createQuery(q);
		BasicRepository.addPagination(permissionGroupFilter,query);
		return query.getResultList();

	}

	public <T extends PermissionGroup> void addPermissionGroupPredicates(PermissionGroupFilter permissionGroupFilter, CriteriaBuilder cb, CommonAbstractCriteria q, From<?,T> r, List<Predicate> predicates, SecurityContextBase securityContext) {
		securedBasicRepository.addSecuredBasicPredicates(permissionGroupFilter.getBasicPropertiesFilter(),cb,q,r,predicates,securityContext);
		if(permissionGroupFilter.getExternalIds()!=null&&!permissionGroupFilter.getExternalIds().isEmpty()){
			predicates.add(r.get(PermissionGroup_.externalId).in(permissionGroupFilter.getExternalIds()));
		}
	}

	public long countAllPermissionGroups(PermissionGroupFilter permissionGroupFilter, SecurityContextBase securityContext){
		CriteriaBuilder cb=em.getCriteriaBuilder();
		CriteriaQuery<Long> q=cb.createQuery(Long.class);
		Root<PermissionGroup> r=q.from(PermissionGroup.class);
		List<Predicate> predicates=new ArrayList<>();
		addPermissionGroupPredicates(permissionGroupFilter,cb,q,r,predicates,securityContext);
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
