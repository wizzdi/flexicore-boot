package com.wizzdi.flexicore.security.data;

import com.flexicore.model.*;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.flexicore.security.SecurityContextBase;
import com.wizzdi.flexicore.security.request.RoleFilter;
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
public class RoleRepository implements Plugin {
	@PersistenceContext
	private EntityManager em;
	@Autowired
	private BaseclassRepository baseclassRepository;

	@Autowired
	private SecurityEntityRepository securityEntityRepository;

	public List<Role> listAllRoles(RoleFilter roleFilter, SecurityContextBase securityContext){
		CriteriaBuilder cb=em.getCriteriaBuilder();
		CriteriaQuery<Role> q=cb.createQuery(Role.class);
		Root<Role> r=q.from(Role.class);
		List<Predicate> predicates=new ArrayList<>();
		addRolePredicates(roleFilter,cb,q,r,predicates,securityContext);
		q.select(r).where(predicates.toArray(Predicate[]::new)).orderBy(cb.asc(r.get(Role_.name)));
		TypedQuery<Role> query = em.createQuery(q);
		BaseclassRepository.addPagination(roleFilter,query);
		return query.getResultList();

	}

	public <T extends Role> void addRolePredicates(RoleFilter roleFilter, CriteriaBuilder cb, CommonAbstractCriteria q, From<?,T> r, List<Predicate> predicates, SecurityContextBase securityContext) {
		securityEntityRepository.addSecurityEntityPredicates(roleFilter,cb,q,r,predicates,securityContext);
		if(roleFilter.getSecurityTenants()!=null &&!roleFilter.getSecurityTenants().isEmpty()){
			Set<String> ids=roleFilter.getSecurityTenants().stream().map(f->f.getId()).collect(Collectors.toSet());
			Join<T, SecurityTenant> join=r.join(Role_.tenant);
			predicates.add(join.get(SecurityTenant_.id).in(ids));
		}
	}

	public long countAllRoles(RoleFilter roleFilter, SecurityContextBase securityContext){
		CriteriaBuilder cb=em.getCriteriaBuilder();
		CriteriaQuery<Long> q=cb.createQuery(Long.class);
		Root<Role> r=q.from(Role.class);
		List<Predicate> predicates=new ArrayList<>();
		addRolePredicates(roleFilter,cb,q,r,predicates,securityContext);
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

	public <T extends Baseclass> List<T> findByIds(Class<T> c, Set<String> requested) {
		return baseclassRepository.findByIds(c, requested);
	}

	public <T> T findByIdOrNull(Class<T> type, String id) {
		return baseclassRepository.findByIdOrNull(type, id);
	}
}
