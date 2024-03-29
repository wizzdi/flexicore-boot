package com.wizzdi.flexicore.security.data;

import com.flexicore.model.*;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.flexicore.security.SecurityContextBase;
import com.wizzdi.flexicore.security.request.SecurityLinkFilter;
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
public class SecurityLinkRepository implements Plugin {
	@PersistenceContext
	private EntityManager em;
	@Autowired
	private SecuredBasicRepository securedBasicRepository;


	public List<SecurityLink> listAllSecurityLinks(SecurityLinkFilter securityLinkFilter, SecurityContextBase securityContext){
		CriteriaBuilder cb=em.getCriteriaBuilder();
		CriteriaQuery<SecurityLink> q=cb.createQuery(SecurityLink.class);
		Root<SecurityLink> r=q.from(SecurityLink.class);
		List<Predicate> predicates=new ArrayList<>();
		addSecurityLinkPredicates(securityLinkFilter,cb,q,r,predicates,securityContext);
		q.select(r).where(predicates.toArray(Predicate[]::new));
		TypedQuery<SecurityLink> query = em.createQuery(q);
		BasicRepository.addPagination(securityLinkFilter,query);
		return query.getResultList();

	}

	public <T extends SecurityLink> void addSecurityLinkPredicates(SecurityLinkFilter securityLinkFilter, CriteriaBuilder cb, CommonAbstractCriteria q, From<?,T> r, List<Predicate> predicates, SecurityContextBase securityContext) {
		securedBasicRepository.addSecuredBasicPredicates(securityLinkFilter.getBasicPropertiesFilter(),cb,q,r,predicates,securityContext);
		if(securityLinkFilter.getSecurityLinkGroups()!=null&&!securityLinkFilter.getSecurityLinkGroups().isEmpty()){
			predicates.add(r.get(SecurityLink_.securityLinkGroup).in(securityLinkFilter.getSecurityLinkGroups()));
		}
		if(securityLinkFilter.getBaseclasses()!=null&&!securityLinkFilter.getBaseclasses().isEmpty()){
			predicates.add(r.get(SecurityLink_.baseclass).in(securityLinkFilter.getBaseclasses()));
		}
		if(securityLinkFilter.getAccesses()!=null&&!securityLinkFilter.getAccesses().isEmpty()){
			predicates.add(r.get(SecurityLink_.access).in(securityLinkFilter.getAccesses()));
		}
		if(securityLinkFilter.getOperations()!=null&&!securityLinkFilter.getOperations().isEmpty()){
			predicates.add(r.get(SecurityLink_.operation).in(securityLinkFilter.getOperations()));
		}
		if(securityLinkFilter.getRelevantUsers()!=null&&!securityLinkFilter.getRelevantUsers().isEmpty()){
			Path<SecurityLink> p = (Path<SecurityLink>) r;
			Predicate pred=cb.and(
					cb.equal(r.type(), UserToBaseclass.class),
					cb.treat(p, UserToBaseclass.class).get(UserToBaseclass_.user).in(securityLinkFilter.getRelevantUsers()));
			if(securityLinkFilter.getRelevantRoles()!=null&&!securityLinkFilter.getRelevantRoles().isEmpty()){
				pred=cb.or(pred,cb.and(
						cb.equal(r.type(), RoleToBaseclass.class),
						cb.treat(p, RoleToBaseclass.class).get(RoleToBaseclass_.role).in(securityLinkFilter.getRelevantRoles())));
			}
			if(securityLinkFilter.getRelevantTenants()!=null&&!securityLinkFilter.getRelevantTenants().isEmpty()){
				pred=cb.or(pred,cb.and(
						cb.equal(r.type(), TenantToBaseclass.class),
						cb.treat(p, TenantToBaseclass.class).get(TenantToBaseclass_.tenant).in(securityLinkFilter.getRelevantTenants())));
			}
			predicates.add(pred);
		}
	}

	public long countAllSecurityLinks(SecurityLinkFilter securityLinkFilter, SecurityContextBase securityContext){
		CriteriaBuilder cb=em.getCriteriaBuilder();
		CriteriaQuery<Long> q=cb.createQuery(Long.class);
		Root<SecurityLink> r=q.from(SecurityLink.class);
		List<Predicate> predicates=new ArrayList<>();
		addSecurityLinkPredicates(securityLinkFilter,cb,q,r,predicates,securityContext);
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
