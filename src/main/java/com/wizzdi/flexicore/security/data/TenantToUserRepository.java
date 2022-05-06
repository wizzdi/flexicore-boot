package com.wizzdi.flexicore.security.data;

import com.flexicore.model.*;
import com.flexicore.security.SecurityContextBase;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.security.request.TenantToUserFilter;
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
import java.util.stream.Collectors;

@Component
@Extension
public class TenantToUserRepository implements Plugin {
	@PersistenceContext
	private EntityManager em;
	@Autowired
	private BaseclassRepository baseclassRepository;
	@Autowired
	private BaselinkRepository baselinkRepository;

	public List<TenantToUser> listAllTenantToUsers(TenantToUserFilter tenantToUserFilter, SecurityContextBase securityContext){
		CriteriaBuilder cb=em.getCriteriaBuilder();
		CriteriaQuery<TenantToUser> q=cb.createQuery(TenantToUser.class);
		Root<TenantToUser> r=q.from(TenantToUser.class);
		List<Predicate> predicates=new ArrayList<>();
		addTenantToUserPredicates(tenantToUserFilter,cb,q,r,predicates,securityContext);
		q.select(r).where(predicates.toArray(Predicate[]::new));
		TypedQuery<TenantToUser> query = em.createQuery(q);
		BaseclassRepository.addPagination(tenantToUserFilter,query);
		return query.getResultList();

	}

	public <T extends TenantToUser> void addTenantToUserPredicates(TenantToUserFilter tenantToUserFilter, CriteriaBuilder cb, CommonAbstractCriteria q, From<?,T> r, List<Predicate> predicates, SecurityContextBase securityContext) {
		baselinkRepository.addBaselinkPredicates(tenantToUserFilter,cb,q,r,predicates,securityContext);
		if(tenantToUserFilter.getSecurityTenants()!=null&&!tenantToUserFilter.getSecurityTenants().isEmpty()){
			Set<String> ids=tenantToUserFilter.getSecurityTenants().stream().map(f->f.getId()).collect(Collectors.toSet());
			Join<T, SecurityTenant> join=cb.treat(r.join(Baselink_.leftside),SecurityTenant.class);
			predicates.add(join.get(SecurityTenant_.id).in(ids));
		}

		if(tenantToUserFilter.getSecurityUsers()!=null&&!tenantToUserFilter.getSecurityUsers().isEmpty()){
			Set<String> ids=tenantToUserFilter.getSecurityUsers().stream().map(f->f.getId()).collect(Collectors.toSet());
			Join<T, SecurityUser> join=cb.treat(r.join(Baselink_.rightside),SecurityUser.class);
			predicates.add(join.get(SecurityUser_.id).in(ids));
		}
	}

	public long countAllTenantToUsers(TenantToUserFilter tenantToUserFilter, SecurityContextBase securityContext){
		CriteriaBuilder cb=em.getCriteriaBuilder();
		CriteriaQuery<Long> q=cb.createQuery(Long.class);
		Root<TenantToUser> r=q.from(TenantToUser.class);
		List<Predicate> predicates=new ArrayList<>();
		addTenantToUserPredicates(tenantToUserFilter,cb,q,r,predicates,securityContext);
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
