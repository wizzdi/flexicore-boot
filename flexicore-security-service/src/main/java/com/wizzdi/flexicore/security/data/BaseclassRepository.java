package com.wizzdi.flexicore.security.data;

import com.flexicore.annotations.IOperation;
import com.flexicore.annotations.rest.All;
import com.flexicore.model.*;
import com.flexicore.security.SecurityContextBase;
import com.flexicore.security.SecurityPermissionEntry;
import com.flexicore.security.SecurityPermissions;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.security.request.BaseclassFilter;
import com.wizzdi.flexicore.security.request.BasicPropertiesFilter;
import com.wizzdi.flexicore.security.request.SoftDeleteOption;

import org.pf4j.Extension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import jakarta.persistence.metamodel.SingularAttribute;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Component
@Extension
public class BaseclassRepository implements Plugin {

	private static final Logger logger = LoggerFactory.getLogger(BaseclassRepository.class);
	@PersistenceContext
	private EntityManager em;

	@Autowired
	@Qualifier("allOps")
	@Lazy
	private SecurityOperation allOp;
	@Autowired
	@Qualifier("securityWildcard")
	@Lazy
	private SecurityWildcard securityWildcard;

	@Autowired
	private BasicRepository basicRepository;


	public static <T> boolean addPagination(BaseclassFilter baseclassFilter, TypedQuery<T> q) {
		return BasicRepository.addPagination(baseclassFilter, q);
	}

	public <T extends Baseclass> void addBaseclassPredicates(BasicPropertiesFilter basicPropertiesFilter, CriteriaBuilder cb, CommonAbstractCriteria q, From<?, T> r, List<Predicate> predicates, SecurityContextBase securityContextBase) {
		if(basicPropertiesFilter!=null){
			BasicRepository.addBasicPropertiesFilter(basicPropertiesFilter, cb, q, r, predicates);
		} else {
			BasicRepository.addBasicPropertiesFilter(new BasicPropertiesFilter().setSoftDelete(SoftDeleteOption.DEFAULT), cb, q, r, predicates);
		}
		if (securityContextBase != null) {
			addBaseclassPredicates(cb, q, r, predicates, securityContextBase);
		}
	}

	public SecurityPermissions getSecurityPermissions(SecurityContextBase securityContextBase) {
		if(securityContextBase.getSecurityPermissions()!=null){
			return securityContextBase.getSecurityPermissions();
		}
		List<SecurityLink> securityLinks = getSecurityLinks(securityContextBase);
		Map<String, Role> allRoles = securityContextBase.getAllRoles().stream().collect(Collectors.toMap(f -> f.getId(), f -> f,(a,b)->a));
		Map<String,SecurityTenant> allTenants=securityContextBase.getTenants().stream().collect(Collectors.toMap(f->f.getId(),f->f,(a,b)->a));
		Set<String> relevantOps = securityContextBase.getOperation()!=null?Set.of(allOp.getId(), securityContextBase.getOperation().getId()):null;
		List<UserToBaseclass> user = securityLinks.stream().filter(f -> relevantOps==null||relevantOps.contains(f.getOperation().getId())).filter(f -> f instanceof UserToBaseclass).map(f->(UserToBaseclass)f).toList();
		Map<String,List<RoleToBaseclass>> role = securityLinks.stream().filter(f -> relevantOps==null||relevantOps.contains(f.getOperation().getId())).filter(f -> f instanceof RoleToBaseclass).map(f->(RoleToBaseclass)f).collect(Collectors.groupingBy(f->f.getRole().getId()));
		Map<String,List<TenantToBaseclass>> tenant = securityLinks.stream().filter(f -> relevantOps==null||relevantOps.contains(f.getOperation().getId())).filter(f -> f instanceof TenantToBaseclass).map(f->(TenantToBaseclass)f).collect(Collectors.groupingBy(f->f.getTenant().getId()));
		return new SecurityPermissions(SecurityPermissionEntry.of(securityContextBase.getUser(), user),role.entrySet().stream().map(f->SecurityPermissionEntry.of(allRoles.get(f.getKey()),f.getValue())).toList(),tenant.entrySet().stream().map(f->SecurityPermissionEntry.of(allTenants.get(f.getKey()),f.getValue())).toList());

	}

	public List<SecurityLink> getSecurityLinks(SecurityContextBase securityContextBase) {
		CriteriaBuilder cb=em.getCriteriaBuilder();
		CriteriaQuery<SecurityLink> q=cb.createQuery(SecurityLink.class);
		Root<SecurityLink> r=q.from(SecurityLink.class);
		Root<UserToBaseclass> user = cb.treat(r, UserToBaseclass.class);
		Root<RoleToBaseclass> role = cb.treat(r, RoleToBaseclass.class);
		Root<TenantToBaseclass> tenant=cb.treat(r,TenantToBaseclass.class);
		Map<String,List<Role>> roleMap = securityContextBase.getRoleMap();
		List<Role> roles= roleMap.values()
				.stream()
				.flatMap(List::stream).toList();
		q.select(r).where(cb.or(
				user.get(UserToBaseclass_.user).in(securityContextBase.getUser()),
				roles.isEmpty()?cb.or():role.get(RoleToBaseclass_.role).in(roles),
				securityContextBase.getTenants().isEmpty()?cb.or():tenant.get(TenantToBaseclass_.tenant).in(securityContextBase.getTenants())
		));
		return em.createQuery(q).getResultList();
	}

	public static <T extends Baseclass> Predicate permissionGroupPredicate(From<?, T> r, List<PermissionGroup> denied, AtomicReference<Join<T, PermissionGroupToBaseclass>> atomicReference){
		if(atomicReference.get()==null){
			atomicReference.set(r.join(Baseclass_.permissionGroupToBaseclasses));
		}
		Join<T, PermissionGroupToBaseclass> join = atomicReference.get();
		return join.get(PermissionGroupToBaseclass_.permissionGroup).in(denied);
	}
	public <T extends Baseclass> void addBaseclassPredicates(CriteriaBuilder cb, CommonAbstractCriteria q, From<?, T> r, List<Predicate> predicates, SecurityContextBase securityContext) {
		if (securityContext == null) {
			return;
		}
		Map<String,List<Role>> roles= securityContext.getRoleMap();
		List<Role> allRoles = roles.values().stream().flatMap(f->f.stream()).toList();
		if(isSuperAdmin(allRoles)){
			return;
		}

		SecurityPermissions securityPermissions = getSecurityPermissions(securityContext);
		List<Predicate> securityPreds = new ArrayList<>();
		AtomicReference<Join<T, PermissionGroupToBaseclass>> join = new AtomicReference<>(null);
		securityPreds.add(cb.equal(r.get(Baseclass_.creator), securityContext.getUser()));//creator
		//user
		SecurityPermissionEntry<SecurityUser> user = securityPermissions.userPermissions();
		if (!user.allowed().isEmpty()) {
			securityPreds.add(r.in(user.allowed()));
		}
		List<Baseclass> userDenied = user.denied();
		if (!user.allowedTypes().isEmpty()) {
			securityPreds.add(cb.and(
					user.allowAll()?cb.or():r.get(Baseclass_.clazz).in(user.allowedTypes()),
					r.get(Baseclass_.tenant).in(securityContext.getTenants()),
					userDenied.isEmpty() ? cb.and() : cb.not(r.in(userDenied)),
					user.deniedPermissionGroups().isEmpty()?cb.and(): cb.not(permissionGroupPredicate(r,user.deniedPermissionGroups(),join))
			));

		}
		if (!user.allowedPermissionGroups().isEmpty()) {
			securityPreds.add(permissionGroupPredicate(r,user.allowedPermissionGroups(),join));
		}
		//role
		for (SecurityPermissionEntry<Role> role : securityPermissions.rolePermissions()) {
			Role roleEntity = role.entity();
			SecurityTenant securityTenant = roleEntity.getSecurity().getTenant();
			if (!role.allowed().isEmpty()) {
				securityPreds.add(cb.and(
						r.in(role.allowed()),
						userDenied.isEmpty() ? cb.and() : cb.not(r.in(userDenied)),
						user.deniedPermissionGroups().isEmpty()?cb.and():cb.not(permissionGroupPredicate(r,user.deniedPermissionGroups(),join))
				));
			}
			if (!role.allowedTypes().isEmpty()) {
				securityPreds.add(cb.and(
						cb.equal(r.get(Baseclass_.tenant), securityTenant),
						role.allowAll()?cb.or():r.get(Baseclass_.clazz).in(role.allowedTypes()),
						userDenied.isEmpty() ? cb.and() : cb.not(r.in(userDenied)),
						role.denied().isEmpty() ? cb.and() : cb.not(r.in(role.denied())),
						user.deniedPermissionGroups().isEmpty()?cb.and():cb.not(permissionGroupPredicate(r,user.deniedPermissionGroups(),join)),
						role.deniedPermissionGroups().isEmpty()?cb.and():cb.not(permissionGroupPredicate(r,role.deniedPermissionGroups(),join))

				));
			}
			if (!role.allowedPermissionGroups().isEmpty()) {
				securityPreds.add(cb.and(
						permissionGroupPredicate(r,role.allowedPermissionGroups(),join),
						userDenied.isEmpty() ? cb.and() : cb.not(r.in(userDenied)),
						user.deniedPermissionGroups().isEmpty()?cb.and():cb.not(permissionGroupPredicate(r,user.deniedPermissionGroups(),join))

				));
			}

		}
		//tenant
		List<SecurityPermissionEntry<SecurityTenant>> tenants = securityPermissions.tenantPermissions();
		List<PermissionGroup> tenantAllowedPermissionGroups = tenants.stream().map(f -> f.allowedPermissionGroups()).flatMap(f -> f.stream()).collect(Collectors.toList());
		List<Baseclass> tenantAllowed = tenants.stream().map(f -> f.allowed()).flatMap(f -> f.stream()).collect(Collectors.toList());

		List<Baseclass> roleDenied = securityPermissions.rolePermissions().stream().map(f -> f.denied()).flatMap(f -> f.stream()).collect(Collectors.toList());
		List<PermissionGroup> rolePermissionGroupDenied = securityPermissions.rolePermissions().stream().map(f -> f.deniedPermissionGroups()).flatMap(f -> f.stream()).collect(Collectors.toList());

		if (!tenantAllowed.isEmpty()) {
			securityPreds.add(cb.and(
					r.in(tenantAllowed),
					userDenied.isEmpty() ? cb.and() : cb.not(r.in(userDenied)),
					roleDenied.isEmpty() ? cb.and() : cb.not(r.in(roleDenied)),
					user.deniedPermissionGroups().isEmpty()?cb.and():cb.not(permissionGroupPredicate(r,user.deniedPermissionGroups(),join)),
					rolePermissionGroupDenied.isEmpty()?cb.and():cb.not(permissionGroupPredicate(r,rolePermissionGroupDenied,join))

			));
		}
		if (!tenantAllowedPermissionGroups.isEmpty()) {
			securityPreds.add(cb.and(
					permissionGroupPredicate(r,tenantAllowedPermissionGroups,join),
					userDenied.isEmpty() ? cb.and() : cb.not(r.in(userDenied)),
					roleDenied.isEmpty() ? cb.and() : cb.not(r.in(roleDenied))

			));
		}
		for (SecurityPermissionEntry<SecurityTenant> tenant : tenants) {
			if (!tenant.allowedTypes().isEmpty()) {
				SecurityTenant tenantEntity = tenant.entity();
				securityPreds.add(cb.and(
						cb.equal(r.get(Baseclass_.tenant), tenantEntity),

						tenant.allowAll()?cb.or():r.get(Baseclass_.clazz).in(tenant.allowedTypes()),
						userDenied.isEmpty() ? cb.and() : cb.not(r.in(userDenied)),
						roleDenied.isEmpty() ? cb.and() : cb.not(r.in(roleDenied)),
						tenant.denied().isEmpty() ? cb.and() : cb.not(r.in(tenant.denied())),
						user.deniedPermissionGroups().isEmpty()?cb.and():cb.not(permissionGroupPredicate(r,user.deniedPermissionGroups(),join)),
						rolePermissionGroupDenied.isEmpty()?cb.and():cb.not(permissionGroupPredicate(r,rolePermissionGroupDenied,join)),
						tenant.deniedPermissionGroups().isEmpty()?cb.and():cb.not(permissionGroupPredicate(r,tenant.deniedPermissionGroups(),join))
				));
			}
		}


		predicates.add(cb.or(securityPreds.toArray(new Predicate[0])));

	}



	private boolean isSuperAdmin(List<Role> roles) {
		for (Role role : roles) {
			if (role.getId().equals("HzFnw-nVR0Olq6WBvwKcQg")) {
				return true;
			}

		}
		return false;

	}


	public <T extends Baseclass> List<T> listByIds(Class<T> c, Set<String> ids, SecurityContextBase securityContext) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<T> q = cb.createQuery(c);
		Root<T> r = q.from(c);
		List<Predicate> predicates = new ArrayList<>();
		predicates.add(r.get(Baseclass_.id).in(ids));
		addBaseclassPredicates(cb, q, r, predicates, securityContext);
		q.select(r).where(predicates.toArray(Predicate[]::new));
		TypedQuery<T> query = em.createQuery(q);
		return query.getResultList();
	}

	public <T extends Baseclass> T getByIdOrNull(String id, Class<T> c, SecurityContextBase securityContext) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<T> q = cb.createQuery(c);
		Root<T> r = q.from(c);
		List<Predicate> predicates = new ArrayList<>();
		predicates.add(cb.equal(r.get(Baseclass_.id), id));
		addBaseclassPredicates(cb, q, r, predicates, securityContext);
		q.select(r).where(predicates.toArray(Predicate[]::new));
		TypedQuery<T> query = em.createQuery(q);
		List<T> resultList = query.getResultList();
		return resultList.isEmpty() ? null : resultList.get(0);
	}

	public <D extends Basic, E extends Baseclass, T extends D> T getByIdOrNull(String id, Class<T> c, SingularAttribute<D, E> baseclassAttribute, SecurityContextBase securityContext) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<T> q = cb.createQuery(c);
		Root<T> r = q.from(c);
		List<Predicate> predicates = new ArrayList<>();
		predicates.add(cb.equal(r.get(Basic_.id), id));
		addBaseclassPredicates(cb, q, r.join(baseclassAttribute), predicates, securityContext);
		q.select(r).where(predicates.toArray(Predicate[]::new));
		TypedQuery<T> query = em.createQuery(q);
		List<T> resultList = query.getResultList();
		return resultList.isEmpty() ? null : resultList.get(0);
	}

	public <D extends Basic, E extends Baseclass, T extends D> List<T> listByIds(Class<T> c, Set<String> ids, SingularAttribute<D, E> baseclassAttribute, SecurityContextBase securityContext) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<T> q = cb.createQuery(c);
		Root<T> r = q.from(c);
		List<Predicate> predicates = new ArrayList<>();
		predicates.add(r.get(Basic_.id).in(ids));
		addBaseclassPredicates(cb, q, r.join(baseclassAttribute), predicates, securityContext);
		q.select(r).where(predicates.toArray(Predicate[]::new));
		TypedQuery<T> query = em.createQuery(q);
		return query.getResultList();
	}


	public <D extends Basic, T extends D> List<T> findByIds(Class<T> c, Set<String> ids, SingularAttribute<D, String> idAttribute) {
		return basicRepository.findByIds(c, ids, idAttribute);
	}

	public <T extends Basic> List<T> findByIds(Class<T> c, Set<String> requested) {
		return basicRepository.findByIds(c, requested);
	}

	public <T> T findByIdOrNull(Class<T> type, String id) {
		return basicRepository.findByIdOrNull(type, id);
	}

	@Transactional
	public <T> T merge(T base) {
		return basicRepository.merge(base);
	}

	@Transactional
	public <T> T merge(T base, boolean updateDate, boolean propagateEvents) {
		return basicRepository.merge(base, updateDate, propagateEvents);
	}

	@Transactional
	public void massMerge(List<?> toMerge, boolean updatedate, boolean propagateEvents) {
		basicRepository.massMerge(toMerge, updatedate, propagateEvents);
	}

	@Transactional
	public <T> T merge(T base, boolean updateDate) {
		return basicRepository.merge(base, updateDate);
	}

	@Transactional
	public void massMerge(List<?> toMerge) {
		basicRepository.massMerge(toMerge);
	}

	@Transactional
	public void massMerge(List<?> toMerge, boolean updatedate) {
		basicRepository.massMerge(toMerge, updatedate);
	}
}
