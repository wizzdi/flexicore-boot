package com.wizzdi.flexicore.security.data;

import com.flexicore.model.*;
import com.flexicore.security.SecurityContextBase;
import com.flexicore.security.SecurityPermissionEntry;
import com.flexicore.security.SecurityPermissions;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.security.events.BasicCreated;
import com.wizzdi.flexicore.security.events.BasicUpdated;
import com.wizzdi.flexicore.security.request.BaseclassFilter;
import com.wizzdi.flexicore.security.request.BasicPropertiesFilter;
import com.wizzdi.flexicore.security.request.OperationToGroupFilter;
import com.wizzdi.flexicore.security.request.SoftDeleteOption;
import com.wizzdi.flexicore.security.service.OperationToGroupService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import jakarta.persistence.metamodel.SingularAttribute;
import org.pf4j.Extension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.Cache;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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
	@Lazy
	private OperationToGroupService operationToGroupService;

	@Autowired
	private BasicRepository basicRepository;
	@Autowired
	@Qualifier("dataAccessControlCache")
	private Cache dataAccessControlCache;

	@Autowired
	@Qualifier("operationToOperationGroupCache")
	private Cache operationToOperationGroupCache;

	public List<Baseclass> listAllBaseclass(BaseclassFilter baseclassFilter,SecurityContextBase securityContextBase){
		CriteriaBuilder cb=em.getCriteriaBuilder();
		CriteriaQuery<Baseclass> q=cb.createQuery(Baseclass.class);
		Root<Baseclass> r=q.from(Baseclass.class);
		List<Predicate> predicates=new ArrayList<>();
		addBaseclassPredicates(baseclassFilter,cb,q,r,predicates,securityContextBase);
		q.select(r).where(predicates.toArray(Predicate[]::new)).orderBy(cb.asc(r.get(Baseclass_.name)));
		TypedQuery<Baseclass> query = em.createQuery(q);
		BasicRepository.addPagination(baseclassFilter,query);
		return query.getResultList();

	}

	private <T extends Baseclass> void addBaseclassPredicates(BaseclassFilter baseclassFilter, CriteriaBuilder cb, CommonAbstractCriteria q, From<?,T> r, List<Predicate> predicates, SecurityContextBase securityContextBase) {
		addBaseclassPredicates(baseclassFilter.getBasicPropertiesFilter(),cb,q,r,predicates,securityContextBase);
		if(baseclassFilter.getClazzes()!=null&&!baseclassFilter.getClazzes().isEmpty()){
			predicates.add(r.get(Baseclass_.clazz).in(baseclassFilter.getClazzes()));
		}
	}

	public long countAllBaseclass(BaseclassFilter baseclassFilter,SecurityContextBase securityContextBase){
		CriteriaBuilder cb=em.getCriteriaBuilder();
		CriteriaQuery<Long> q=cb.createQuery(Long.class);
		Root<Baseclass> r=q.from(Baseclass.class);
		List<Predicate> predicates=new ArrayList<>();
		addBaseclassPredicates(baseclassFilter,cb,q,r,predicates,securityContextBase);
		q.select(cb.count(r)).where(predicates.toArray(Predicate[]::new));
		TypedQuery<Long> query = em.createQuery(q);
		return query.getSingleResult();

	}


	@EventListener
	public void invalidateUserCache(BasicCreated<UserToBaseclass> securityLink) {
		SecurityLink link = securityLink.getBaseclass();
		invalidateCache(link);
	}

	@EventListener
	public void invalidateRoleCache(BasicCreated<RoleToBaseclass> securityLink) {
		SecurityLink link = securityLink.getBaseclass();
		invalidateCache(link);
	}

	@EventListener
	public void invalidateTenantCache(BasicCreated<TenantToBaseclass> securityLink) {
		SecurityLink link = securityLink.getBaseclass();
		invalidateCache(link);
	}

	@EventListener
	public void invalidateUserCache(BasicUpdated<UserToBaseclass> securityLink) {
		SecurityLink link = securityLink.getBaseclass();
		invalidateCache(link);
	}

	@EventListener
	public void invalidateRoleCache(BasicUpdated<RoleToBaseclass> securityLink) {
		SecurityLink link = securityLink.getBaseclass();
		invalidateCache(link);
	}

	@EventListener
	public void invalidateTenantCache(BasicUpdated<TenantToBaseclass> securityLink) {
		SecurityLink link = securityLink.getBaseclass();
		invalidateCache(link);
	}

	@EventListener
	public void invalidateOperationGroupCache(BasicUpdated<OperationToGroup> operationToGroup){
		invalidateCache(operationToGroup.getBaseclass());
	}

	@EventListener
	public void invalidateOperationGroupCache(BasicCreated<OperationToGroup> operationToGroup){
		invalidateCache(operationToGroup.getBaseclass());
	}

	private void invalidateCache(OperationToGroup operationToGroup) {
		if(operationToGroup.getOperation()==null){
			return;
		}
		operationToOperationGroupCache.evict(operationToGroup.getOperation().getId());
	}


	private void invalidateCache(SecurityLink link) {
		if (link.getSecurityEntity() != null) {
			dataAccessControlCache.evict(link.getSecurityEntity().getId());
			logger.debug("evicted security entity " + link.getSecurityEntity().getId());
		}
	}

	public static <T> boolean addPagination(BaseclassFilter baseclassFilter, TypedQuery<T> q) {
		return BasicRepository.addPagination(baseclassFilter, q);
	}

	public <T extends Baseclass> void addBaseclassPredicates(BasicPropertiesFilter basicPropertiesFilter, CriteriaBuilder cb, CommonAbstractCriteria q, From<?, T> r, List<Predicate> predicates, SecurityContextBase securityContextBase) {
		if (basicPropertiesFilter != null) {
			BasicRepository.addBasicPropertiesFilter(basicPropertiesFilter, cb, q, r, predicates);
		} else {
			BasicRepository.addBasicPropertiesFilter(new BasicPropertiesFilter().setSoftDelete(SoftDeleteOption.DEFAULT), cb, q, r, predicates);
		}
		if (securityContextBase != null) {
			addBaseclassPredicates(cb, q, r, predicates, securityContextBase);
		}
	}

	record SecurityLinkHolder(List<UserToBaseclass> users, List<RoleToBaseclass> roles,
							  List<TenantToBaseclass> tenants) {
	}


	public SecurityPermissions getSecurityPermissions(SecurityContextBase securityContextBase) {
		SecurityLinkHolder securityLinkHolder = getSecurityLinkHolder(securityContextBase);
		List<UserToBaseclass> userLinks = securityLinkHolder.users();
		List<RoleToBaseclass> roleLinks = securityLinkHolder.roles();
		List<TenantToBaseclass> tenantLinks = securityLinkHolder.tenants();
		Map<String, Role> allRoles = securityContextBase.getAllRoles().stream().collect(Collectors.toMap(f -> f.getId(), f -> f, (a, b) -> a));
		Map<String, SecurityTenant> allTenants = securityContextBase.getTenants().stream().collect(Collectors.toMap(f -> f.getId(), f -> f, (a, b) -> a));
		Set<String> relevantOps = securityContextBase.getOperation() != null ? Set.of(allOp.getId(), securityContextBase.getOperation().getId()) : null;
		Set<String> relevantOpGroups=securityContextBase.getOperation()!=null?getRelevantOpGroups(securityContextBase.getOperation()):null;
		List<UserToBaseclass> user = userLinks.stream().filter(f -> filterSecurityLinkForOperation(f, relevantOps, relevantOpGroups)).toList();
		Map<String, List<RoleToBaseclass>> role = roleLinks.stream().filter(f ->filterSecurityLinkForOperation(f,relevantOps,relevantOpGroups)).collect(Collectors.groupingBy(f -> f.getRole().getId()));
		Map<String, List<TenantToBaseclass>> tenant = tenantLinks.stream().filter(f -> filterSecurityLinkForOperation(f,relevantOps,relevantOpGroups)).collect(Collectors.groupingBy(f -> f.getTenant().getId()));
		return new SecurityPermissions(SecurityPermissionEntry.of(securityContextBase.getUser(), user), role.entrySet().stream().map(f -> SecurityPermissionEntry.of(allRoles.get(f.getKey()), f.getValue())).toList(), tenant.entrySet().stream().map(f -> SecurityPermissionEntry.of(allTenants.get(f.getKey()), f.getValue())).toList());

	}

	private static boolean filterSecurityLinkForOperation(SecurityLink f, Set<String> relevantOps, Set<String> relevantOpGroups) {
		return relevantOps == null || relevantOpGroups == null || (f.getOperation() != null && relevantOps.contains(f.getOperation().getId())) || (f.getOperationGroup() != null && relevantOpGroups.contains(f.getOperationGroup().getId()));
	}

	private Set<String> getRelevantOpGroups(SecurityOperation op) {
		return operationToOperationGroupCache.get(op.getId(), () -> operationToGroupService.listAllOperationToGroups(new OperationToGroupFilter().setOperations(Collections.singletonList(op)),null).stream().map(f->f.getOperationGroup().getId()).collect(Collectors.toSet()));
	}

	private SecurityLinkHolder getSecurityLinkHolder(SecurityContextBase securityContextBase) {
		List<UserToBaseclass> userPermissions = dataAccessControlCache.get(securityContextBase.getUser().getId(), List.class);
		List<List<RoleToBaseclass>> rolePermissions = securityContextBase.getAllRoles().stream().map(f -> (List<RoleToBaseclass>) dataAccessControlCache.get(f.getId(), List.class)).toList();
		List<List<TenantToBaseclass>> tenantPermissions = securityContextBase.getTenants().stream().map(f -> (List<TenantToBaseclass>) dataAccessControlCache.get(f.getId(), List.class)).toList();
		if (userPermissions != null && rolePermissions.stream().allMatch(f -> f != null) && tenantPermissions.stream().allMatch(f -> f != null)) {
			List<RoleToBaseclass> roleLinks = rolePermissions.stream().flatMap(f -> f.stream()).toList();
			List<TenantToBaseclass> tenantLinks = tenantPermissions.stream().flatMap(f -> f.stream()).toList();
			logger.debug("cache hit users: {} , roles: {} , tenants: {}", userPermissions.size(), roleLinks.size(), tenantLinks.size());
			return new SecurityLinkHolder(userPermissions, roleLinks, tenantLinks);

		}
		List<SecurityLink> securityLinks = getSecurityLinks(securityContextBase);
		List<UserToBaseclass> userLinks = securityLinks.stream().filter(f -> f instanceof UserToBaseclass).map(f -> (UserToBaseclass) f).toList();
		List<RoleToBaseclass> roleLinks = securityLinks.stream().filter(f -> f instanceof RoleToBaseclass).map(f -> (RoleToBaseclass) f).toList();
		List<TenantToBaseclass> tenantLinks = securityLinks.stream().filter(f -> f instanceof TenantToBaseclass).map(f -> (TenantToBaseclass) f).toList();
		dataAccessControlCache.put(securityContextBase.getUser().getId(), userLinks);
		for (Role role : securityContextBase.getAllRoles()) {
			dataAccessControlCache.put(role.getId(), roleLinks.stream().filter(f -> f.getRole().getId().equals(role.getId())).toList());
		}
		for (SecurityTenant tenant : securityContextBase.getTenants()) {
			dataAccessControlCache.put(tenant.getId(), tenantLinks.stream().filter(f -> f.getTenant().getId().equals(tenant.getId())).toList());
		}
		return new SecurityLinkHolder(userLinks, roleLinks, tenantLinks);

	}

	public List<SecurityLink> getSecurityLinks(SecurityContextBase securityContextBase) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<SecurityLink> q = cb.createQuery(SecurityLink.class);
		Root<SecurityLink> r = q.from(SecurityLink.class);
		Root<UserToBaseclass> user = cb.treat(r, UserToBaseclass.class);
		Root<RoleToBaseclass> role = cb.treat(r, RoleToBaseclass.class);
		Root<TenantToBaseclass> tenant = cb.treat(r, TenantToBaseclass.class);
		Map<String, List<Role>> roleMap = securityContextBase.getRoleMap();
		List<Role> roles = roleMap.values()
				.stream()
				.flatMap(List::stream).toList();
		q.select(r).where(
				cb.and(
						cb.isFalse(r.get(SecurityLink_.softDelete)),
						cb.or(
								user.get(UserToBaseclass_.user).in(securityContextBase.getUser()),
								roles.isEmpty()?cb.or():role.get(RoleToBaseclass_.role).in(roles),
								securityContextBase.getTenants().isEmpty()?cb.or():tenant.get(TenantToBaseclass_.tenant).in(securityContextBase.getTenants())
						)));
		return em.createQuery(q).getResultList();
	}

	public static <T extends Baseclass> Predicate permissionGroupPredicate(From<?, T> r, List<PermissionGroup> denied, AtomicReference<Join<T, PermissionGroupToBaseclass>> atomicReference){
		if(atomicReference.get()==null){
			atomicReference.set(r.join(Baseclass_.permissionGroupToBaseclasses,JoinType.LEFT));
		}
		Join<T, PermissionGroupToBaseclass> join = atomicReference.get();
		return join.get(PermissionGroupToBaseclass_.permissionGroup).in(denied);
	}
	public <T extends Baseclass> void addBaseclassPredicates(CriteriaBuilder cb, CommonAbstractCriteria q, From<?, T> r, List<Predicate> predicates, SecurityContextBase securityContext) {
		if (!requiresSecurityPredicates(securityContext)) {
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
		List<SecurityTenant> allowAllTenantsWithoutDeny=new ArrayList<>(); // in the case of allow all tenants ,and no denies we can improve the query or avoid a bunch of ors and use an IN clause
		if (specificUserTypePermissionRequired(user,userDenied)) {
			securityPreds.add(cb.and(
					r.get(Baseclass_.tenant).in(securityContext.getTenants()),
					user.allowAll() ? cb.and() : r.get(Baseclass_.clazz).in(user.allowedTypes()),
					userDenied.isEmpty() ? cb.and() : cb.not(r.in(userDenied)),
					user.deniedPermissionGroups().isEmpty()?cb.and(): cb.not(permissionGroupPredicate(r,user.deniedPermissionGroups(),join))
			));

		}
		else{
			if(user.allowAll()){
				allowAllTenantsWithoutDeny.addAll(securityContext.getTenants());
			}
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
			if (specificRoleTypesPermissionRequired(role,userDenied,user)) {
				securityPreds.add(cb.and(
						cb.equal(r.get(Baseclass_.tenant), securityTenant),
						role.allowAll() ? cb.and() : r.get(Baseclass_.clazz).in(role.allowedTypes()),
						userDenied.isEmpty() ? cb.and() : cb.not(r.in(userDenied)),
						role.denied().isEmpty() ? cb.and() : cb.not(r.in(role.denied())),
						user.deniedPermissionGroups().isEmpty()?cb.and():cb.not(permissionGroupPredicate(r,user.deniedPermissionGroups(),join)),
						role.deniedPermissionGroups().isEmpty()?cb.and():cb.not(permissionGroupPredicate(r,role.deniedPermissionGroups(),join))

				));
			}
			else{
				if(role.allowAll()){
					allowAllTenantsWithoutDeny.add(securityTenant);
				}
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
			if (specificTenantTypesPermissionRequired(tenant,userDenied,roleDenied,user,rolePermissionGroupDenied)) {
				SecurityTenant tenantEntity = tenant.entity();
				securityPreds.add(cb.and(
						cb.equal(r.get(Baseclass_.tenant), tenantEntity),

						tenant.allowAll() ? cb.and() : r.get(Baseclass_.clazz).in(tenant.allowedTypes()),
						userDenied.isEmpty() ? cb.and() : cb.not(r.in(userDenied)),
						roleDenied.isEmpty() ? cb.and() : cb.not(r.in(roleDenied)),
						tenant.denied().isEmpty() ? cb.and() : cb.not(r.in(tenant.denied())),
						user.deniedPermissionGroups().isEmpty()?cb.and():cb.not(permissionGroupPredicate(r,user.deniedPermissionGroups(),join)),
						rolePermissionGroupDenied.isEmpty()?cb.and():cb.not(permissionGroupPredicate(r,rolePermissionGroupDenied,join)),
						tenant.deniedPermissionGroups().isEmpty()?cb.and():cb.not(permissionGroupPredicate(r,tenant.deniedPermissionGroups(),join))
				));
			}
			else{
				if(tenant.allowAll()){
					allowAllTenantsWithoutDeny.add(tenant.entity());
				}
			}
		}
		if(!allowAllTenantsWithoutDeny.isEmpty()){
			securityPreds.add(cb.or(r.get(Baseclass_.tenant).in(allowAllTenantsWithoutDeny)));
		}


		predicates.add(cb.and(r.get(Baseclass_.tenant).in(securityContext.getTenants()),cb.or(securityPreds.toArray(new Predicate[0]))));

	}

	private static boolean specificTenantTypesPermissionRequired(SecurityPermissionEntry<SecurityTenant> tenant, List<Baseclass> userDenied, List<Baseclass> roleDenied, SecurityPermissionEntry<SecurityUser> user, List<PermissionGroup> rolePermissionGroupDenied) {
		return !tenant.allowedTypes().isEmpty() && (!tenant.allowAll() || !userDenied.isEmpty() || !roleDenied.isEmpty() || !user.deniedPermissionGroups().isEmpty() || !rolePermissionGroupDenied.isEmpty());
	}

	private static boolean specificUserTypePermissionRequired(SecurityPermissionEntry<SecurityUser> user, List<Baseclass> userDenied) {
		return !user.allowedTypes().isEmpty()&& (!user.allowAll() || !userDenied.isEmpty() || !user.deniedPermissionGroups().isEmpty());
	}

	private static boolean specificRoleTypesPermissionRequired(SecurityPermissionEntry<Role> role, List<Baseclass> userDenied, SecurityPermissionEntry<SecurityUser> user) {
		return !role.allowedTypes().isEmpty() && (!role.allowAll() || !userDenied.isEmpty() || !user.deniedPermissionGroups().isEmpty());
	}

	public boolean requiresSecurityPredicates(SecurityContextBase securityContext) {
		if (securityContext == null) {
			return false;
		}
		Map<String, List<Role>> roles = securityContext.getRoleMap();
		List<Role> allRoles = roles.values().stream().flatMap(f -> f.stream()).toList();
		return !isSuperAdmin(allRoles);
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
		return resultList.isEmpty() ? null : resultList.getFirst();
	}

	public <D extends Basic, E extends Baseclass, T extends D> T getByIdOrNull(String id, Class<T> c, SingularAttribute<D, E> baseclassAttribute, SecurityContextBase securityContext) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<T> q = cb.createQuery(c);
		Root<T> r = q.from(c);
		List<Predicate> predicates = new ArrayList<>();
		predicates.add(cb.equal(r.get(Basic_.id), id));
		if(requiresSecurityPredicates(securityContext)){
			Join<T, E> join = r.join(baseclassAttribute);
			addBaseclassPredicates(cb, q, join, predicates, securityContext);
		}

		q.select(r).where(predicates.toArray(Predicate[]::new));
		TypedQuery<T> query = em.createQuery(q);
		List<T> resultList = query.getResultList();
		return resultList.isEmpty() ? null : resultList.getFirst();
	}

	public <D extends Basic, E extends Baseclass, T extends D> List<T> listByIds(Class<T> c, Set<String> ids, SingularAttribute<D, E> baseclassAttribute, SecurityContextBase securityContext) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<T> q = cb.createQuery(c);
		Root<T> r = q.from(c);
		List<Predicate> predicates = new ArrayList<>();
		predicates.add(r.get(Basic_.id).in(ids));
		if(requiresSecurityPredicates(securityContext)){
			Join<T, E> join = r.join(baseclassAttribute);
			addBaseclassPredicates(cb, q, join, predicates, securityContext);
		}

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
