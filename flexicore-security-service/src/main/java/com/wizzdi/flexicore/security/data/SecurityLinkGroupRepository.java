package com.wizzdi.flexicore.security.data;

import com.flexicore.model.*;
import com.flexicore.security.SecurityContextBase;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.security.request.SecurityLinkGroupFilter;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
@Extension
public class SecurityLinkGroupRepository implements Plugin {
	@PersistenceContext
	private EntityManager em;
	@Autowired
	private SecuredBasicRepository securedBasicRepository;
	@Autowired
	private SecurityLinkRepository securityLinkRepository;


	public List<SecurityLinkGroup> listAllSecurityLinkGroups(SecurityLinkGroupFilter securityLinkGroupFilter, SecurityContextBase securityContext){
		CriteriaBuilder cb=em.getCriteriaBuilder();
		CriteriaQuery<SecurityLinkGroup> q=cb.createQuery(SecurityLinkGroup.class);
		Root<SecurityLinkGroup> r=q.from(SecurityLinkGroup.class);
		List<Predicate> predicates=new ArrayList<>();
		addSecurityLinkGroupPredicates(securityLinkGroupFilter,cb,q,r,predicates,securityContext);
		q.select(r).where(predicates.toArray(Predicate[]::new));
		TypedQuery<SecurityLinkGroup> query = em.createQuery(q);
		BasicRepository.addPagination(securityLinkGroupFilter,query);
		return query.getResultList();

	}

	public <T extends SecurityLinkGroup> void addSecurityLinkGroupPredicates(SecurityLinkGroupFilter securityLinkGroupFilter, CriteriaBuilder cb, CommonAbstractCriteria q, From<?,T> r, List<Predicate> predicates, SecurityContextBase securityContext) {
		securedBasicRepository.addSecuredBasicPredicates(securityLinkGroupFilter.getBasicPropertiesFilter(),cb,q,r,predicates,securityContext);
		if(securityLinkGroupFilter.getSecurityLinkFilter()!=null){
			Join<T,SecurityLink> join=r.join(SecurityLinkGroup_.securityLinks);
			securityLinkRepository.addSecurityLinkPredicates(securityLinkGroupFilter.getSecurityLinkFilter(),cb,q,join,predicates,securityContext);
		}

	}

	public long countAllSecurityLinkGroups(SecurityLinkGroupFilter securityLinkGroupFilter, SecurityContextBase securityContext){
		CriteriaBuilder cb=em.getCriteriaBuilder();
		CriteriaQuery<Long> q=cb.createQuery(Long.class);
		Root<SecurityLinkGroup> r=q.from(SecurityLinkGroup.class);
		List<Predicate> predicates=new ArrayList<>();
		addSecurityLinkGroupPredicates(securityLinkGroupFilter,cb,q,r,predicates,securityContext);
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
