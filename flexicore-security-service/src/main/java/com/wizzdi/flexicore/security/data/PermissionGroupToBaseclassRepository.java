package com.wizzdi.flexicore.security.data;

import com.flexicore.model.Baseclass_;
import com.flexicore.model.PermissionGroupToBaseclass;
import com.flexicore.model.Baseclass;
import com.flexicore.model.PermissionGroupToBaseclass_;
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
		q.select(r).where(predicates.toArray(Predicate[]::new)).orderBy(getSorting(permissionGroupToBaseclassFilter, r, cb));
		TypedQuery<PermissionGroupToBaseclass> query = em.createQuery(q);
		BasicRepository.addPagination(permissionGroupToBaseclassFilter,query);
		return query.getResultList();

	}

	private static Order getSorting(PermissionGroupToBaseclassFilter permissionGroupToBaseclassFilter, Root<PermissionGroupToBaseclass> r, CriteriaBuilder cb) {
		PermissionGroupToBaseclassFilter.Sorting sorting = permissionGroupToBaseclassFilter.getSorting();
		if (sorting == null) {
			sorting=new PermissionGroupToBaseclassFilter.Sorting(PermissionGroupToBaseclassFilter.SortBy.BASECLASS_NAME,true);
		}
		Path<?> sortPath = switch (sorting.sortBy()) {
			case CLAZZ_NAME -> r.join(PermissionGroupToBaseclass_.baseclass).get(Baseclass_.clazz).get(Baseclass_.name);
			case BASECLASS_ID -> r.join(PermissionGroupToBaseclass_.baseclass).get(Baseclass_.id);
			case BASECLASS_NAME -> r.join(PermissionGroupToBaseclass_.baseclass).get(Baseclass_.name);
			case BASECLASS_CREATION_DATE -> r.join(PermissionGroupToBaseclass_.baseclass).get(Baseclass_.creationDate);
		};
        return sorting.asc() ? cb.asc(sortPath) : cb.desc(sortPath);
	}

	public  <T extends PermissionGroupToBaseclass> void addPermissionGroupToBaseclassPredicates(PermissionGroupToBaseclassFilter permissionGroupToBaseclassFilter, CriteriaBuilder cb, CommonAbstractCriteria q, From<?,T> r, List<Predicate> predicates, SecurityContextBase securityContext) {
		securedBasicRepository.addSecuredBasicPredicates(permissionGroupToBaseclassFilter.getBasicPropertiesFilter(),cb,q,r,predicates,securityContext);
		if (permissionGroupToBaseclassFilter.getBaseclasses() != null && !permissionGroupToBaseclassFilter.getBaseclasses().isEmpty()) {
			predicates.add(r.get(PermissionGroupToBaseclass_.baseclass).in(permissionGroupToBaseclassFilter.getBaseclasses()));
		}
		if (permissionGroupToBaseclassFilter.getClazzes() != null && !permissionGroupToBaseclassFilter.getClazzes().isEmpty()) {
			Join<T, Baseclass> baseclassJoin = r.join(PermissionGroupToBaseclass_.baseclass);
			predicates.add(baseclassJoin.get(Baseclass_.clazz).in(permissionGroupToBaseclassFilter.getClazzes()));
		}
		if (permissionGroupToBaseclassFilter.getPermissionGroups() != null && permissionGroupToBaseclassFilter.getPermissionGroups().isEmpty()) {
			predicates.add(r.get(PermissionGroupToBaseclass_.permissionGroup).in(permissionGroupToBaseclassFilter.getPermissionGroups()));
		}
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


	public <T> T merge(T o){
		return securedBasicRepository.merge(o);
	}


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
