package com.wizzdi.flexicore.security.data;

import com.flexicore.model.*;
import com.wizzdi.flexicore.security.configuration.SecurityContext;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.security.request.SecurityLinkFilter;
import com.wizzdi.flexicore.security.request.SecurityLinkOrder;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
public class SecurityLinkRepository implements Plugin {
	@PersistenceContext
	private EntityManager em;
	@Autowired
	private SecuredBasicRepository securedBasicRepository;


	public List<SecurityLink> listAllSecurityLinks(SecurityLinkFilter securityLinkFilter, SecurityContext securityContext){
		CriteriaBuilder cb=em.getCriteriaBuilder();
		CriteriaQuery<SecurityLink> q=cb.createQuery(SecurityLink.class);
		Root<SecurityLink> r=q.from(SecurityLink.class);
		List<Predicate> predicates=new ArrayList<>();
		addSecurityLinkPredicates(securityLinkFilter,cb,q,r,predicates,securityContext);
		q.select(r).where(predicates.toArray(Predicate[]::new));
		if(securityLinkFilter.getSorting()!=null){
			Order order=getOrder(cb,r, securityLinkFilter.getSorting());
			q=q.orderBy(order);
		}
		TypedQuery<SecurityLink> query = em.createQuery(q);
		BasicRepository.addPagination(securityLinkFilter,query);
		return query.getResultList();

	}

	public Order getOrder(CriteriaBuilder cb, From<?,SecurityLink> r, List<SecurityLinkOrder> sorting) {
		if(sorting.isEmpty()){
			return null;
		}
		return cb.asc(r.get(SecurityLink_.dtype));
	}

	public <T extends SecurityLink> void addSecurityLinkPredicates(SecurityLinkFilter securityLinkFilter, CriteriaBuilder cb, CommonAbstractCriteria q, From<?,T> r, List<Predicate> predicates, SecurityContext securityContext) {
		securedBasicRepository.addSecuredBasicPredicates(securityLinkFilter.getBasicPropertiesFilter(),cb,q,r,predicates,securityContext);
		if(securityLinkFilter.getSecurityLinkGroups()!=null&&!securityLinkFilter.getSecurityLinkGroups().isEmpty()){
			predicates.add(r.get(SecurityLink_.securityLinkGroup).in(securityLinkFilter.getSecurityLinkGroups()));
		}
		if(securityLinkFilter.getSecuredIds()!=null&&!securityLinkFilter.getSecuredIds().isEmpty()){
			predicates.add(r.get(SecurityLink_.securedId).in(securityLinkFilter.getSecuredIds()));
		}
		if(securityLinkFilter.getClazzes()!=null&&!securityLinkFilter.getClazzes().isEmpty()){
			Set<String> types = securityLinkFilter.getClazzes().stream().map(f -> f.name()).collect(Collectors.toSet());
			predicates.add(r.get(SecurityLink_.securedType).in(types));
		}

		if(securityLinkFilter.getPermissionGroups()!=null&&!securityLinkFilter.getPermissionGroups().isEmpty()){
			predicates.add(r.get(SecurityLink_.permissionGroup).in(securityLinkFilter.getPermissionGroups()));
		}
		if(securityLinkFilter.getOperationGroups()!=null&&!securityLinkFilter.getOperationGroups().isEmpty()){
			predicates.add(r.get(SecurityLink_.operationGroup).in(securityLinkFilter.getOperationGroups()));
		}
		if(securityLinkFilter.getAccesses()!=null&&!securityLinkFilter.getAccesses().isEmpty()){
			predicates.add(r.get(SecurityLink_.access).in(securityLinkFilter.getAccesses()));
		}
		if(securityLinkFilter.getOperations()!=null&&!securityLinkFilter.getOperations().isEmpty()){
			Set<String> operationIds=securityLinkFilter.getOperations().stream().map(f->f.getId()).collect(Collectors.toSet());
			predicates.add(r.get(SecurityLink_.operationId).in(operationIds));
		}
		List<SecurityUser> relevantUsers = securityLinkFilter.getRelevantUsers();
		if(relevantUsers !=null&&!relevantUsers.isEmpty()){
			Set<String> relevantUsersIds=relevantUsers.stream().map(f->f.getId()).collect(Collectors.toSet());
			Path<SecurityLink> p = (Path<SecurityLink>) r;
			Predicate pred=cb.and(
					cb.equal(r.type(), UserToBaseclass.class),
					cb.treat(p, UserToBaseclass.class).get(UserToBaseclass_.user).get(SecurityUser_.id).in(relevantUsersIds));
			List<Role> relevantRoles = securityLinkFilter.getRelevantRoles();
			if(relevantRoles !=null&&!relevantRoles.isEmpty()){
				Set<String> relevantRoleIds=relevantRoles.stream().map(f->f.getId()).collect(Collectors.toSet());
				pred=cb.or(pred,cb.and(
						cb.equal(r.type(), RoleToBaseclass.class),
						cb.treat(p, RoleToBaseclass.class).get(RoleToBaseclass_.role).get(Role_.id).in(relevantRoleIds)));
			}
			List<SecurityTenant> relevantTenants = securityLinkFilter.getRelevantTenants();
			if(relevantTenants !=null&&!relevantTenants.isEmpty()){
				Set<String> relevantTenantIds=relevantTenants.stream().map(f->f.getId()).collect(Collectors.toSet());
				pred=cb.or(pred,cb.and(
						cb.equal(r.type(), TenantToBaseclass.class),
						cb.treat(p, TenantToBaseclass.class).get(TenantToBaseclass_.tenant).get(SecurityTenant_.id).in(relevantTenantIds)));
			}
			predicates.add(pred);
		}
	}

	public long countAllSecurityLinks(SecurityLinkFilter securityLinkFilter, SecurityContext securityContext){
		CriteriaBuilder cb=em.getCriteriaBuilder();
		CriteriaQuery<Long> q=cb.createQuery(Long.class);
		Root<SecurityLink> r=q.from(SecurityLink.class);
		List<Predicate> predicates=new ArrayList<>();
		addSecurityLinkPredicates(securityLinkFilter,cb,q,r,predicates,securityContext);
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
