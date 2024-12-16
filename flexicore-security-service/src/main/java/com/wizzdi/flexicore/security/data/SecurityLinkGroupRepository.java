package com.wizzdi.flexicore.security.data;

import com.flexicore.model.*;
import com.wizzdi.flexicore.security.configuration.SecurityContext;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.security.request.SecurityLinkGroupFilter;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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


	public List<SecurityLinkGroup> listAllSecurityLinkGroups(SecurityLinkGroupFilter securityLinkGroupFilter, SecurityContext securityContext){
		CriteriaBuilder cb=em.getCriteriaBuilder();
		CriteriaQuery<SecurityLinkGroup> q=cb.createQuery(SecurityLinkGroup.class);
		Root<SecurityLinkGroup> r=q.from(SecurityLinkGroup.class);
		List<Predicate> predicates=new ArrayList<>();
		Join<SecurityLinkGroup, SecurityLink> join = addSecurityLinkGroupPredicates(securityLinkGroupFilter, cb, q, r, predicates, securityContext);
		q.select(r).where(predicates.toArray(Predicate[]::new));
		if(securityLinkGroupFilter.getSecurityLinkFilter()!=null&&securityLinkGroupFilter.getSecurityLinkFilter().getSorting()!=null&&join!=null){
			Order order=securityLinkRepository.getOrder(cb,join,securityLinkGroupFilter.getSecurityLinkFilter().getSorting());
			q=q.orderBy(order);
		}
		TypedQuery<SecurityLinkGroup> query = em.createQuery(q);
		BasicRepository.addPagination(securityLinkGroupFilter,query);
		return query.getResultList();

	}

	public <T extends SecurityLinkGroup> Join<T,SecurityLink> addSecurityLinkGroupPredicates(SecurityLinkGroupFilter securityLinkGroupFilter, CriteriaBuilder cb, CommonAbstractCriteria q, From<?,T> r, List<Predicate> predicates, SecurityContext securityContext) {
		securedBasicRepository.addSecuredBasicPredicates(securityLinkGroupFilter.getBasicPropertiesFilter(),cb,q,r,predicates,securityContext);
		if(securityLinkGroupFilter.getSecurityLinkFilter()!=null){
			Join<T,SecurityLink> join=r.join(SecurityLinkGroup_.securityLinks);
			securityLinkRepository.addSecurityLinkPredicates(securityLinkGroupFilter.getSecurityLinkFilter(),cb,q,join,predicates,securityContext);
			return join;
		}
		return null;

	}

	public long countAllSecurityLinkGroups(SecurityLinkGroupFilter securityLinkGroupFilter, SecurityContext securityContext){
		CriteriaBuilder cb=em.getCriteriaBuilder();
		CriteriaQuery<Long> q=cb.createQuery(Long.class);
		Root<SecurityLinkGroup> r=q.from(SecurityLinkGroup.class);
		List<Predicate> predicates=new ArrayList<>();
		addSecurityLinkGroupPredicates(securityLinkGroupFilter,cb,q,r,predicates,securityContext);
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

	public <T extends Baseclass> List<T> listByIds(Class<T> c, Set<String> ids, SecurityContext securityContext) {
		return securedBasicRepository.listByIds(c, ids, securityContext);
	}

	public <T extends Baseclass> T getByIdOrNull(String id, Class<T> c, SecurityContext securityContext) {
		return securedBasicRepository.getByIdOrNull(id, c, securityContext);
	}
}
