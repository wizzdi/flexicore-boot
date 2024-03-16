package com.wizzdi.flexicore.security.data;

import com.flexicore.annotations.IOperation;
import com.flexicore.model.*;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import org.pf4j.Extension;
import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@Extension
@Repository
public class SecurityRepository implements Plugin {

	@PersistenceContext
	private EntityManager em;

	/**
	 * @param securityOperation securityOperation
	 * @param securityUser securityUser
	 * @param access access
	 * @return if the securityUser is allowed/denied (based on given access object) to the given securityOperation
	 */
	public boolean checkRole(SecurityOperation securityOperation, SecurityUser securityUser, IOperation.Access access) {

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<SecurityUser> q = cb.createQuery(SecurityUser.class);
		Root<SecurityUser> users = q.from(SecurityUser.class);
		Join<SecurityUser, RoleToUser> roleToUser = users.join(SecurityUser_.roles, JoinType.LEFT);
		Join<RoleToUser, Role> roles =roleToUser.join(RoleToUser_.role, JoinType.LEFT);
		Join<Role, RoleToBaseclass> roleToBaseClass = roles.join(Role_.roleToBaseclass);
		Predicate rolesPredicate = cb.and(
				cb.isFalse(roleToUser.get(RoleToUser_.softDelete)),
				cb.isFalse(roleToBaseClass.get(RoleToBaseclass_.softDelete)),
				cb.equal(users.get(SecurityUser_.id), securityUser.getId()),
				cb.equal(roleToBaseClass.get(RoleToBaseclass_.baseclass), securityOperation.getSecurity()),
				cb.equal(roleToBaseClass.get(RoleToBaseclass_.access), access)
		);
		List<Predicate> preds = new ArrayList<>();
		preds.add(rolesPredicate);
		q.select(users).where(preds.toArray(Predicate[]::new));
		TypedQuery<SecurityUser> query = em.createQuery(q);
		List<SecurityUser> usersList = query.getResultList();
		return !usersList.isEmpty();

	}

	public boolean checkUser(SecurityOperation securityOperation, SecurityUser securityUser, IOperation.Access access) {

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<SecurityUser> q = cb.createQuery(SecurityUser.class);
		Root<SecurityUser> users = q.from(SecurityUser.class);
		// check if this securityUser has direct connection with the securityOperation and the
		// value is Deny.
		Join<SecurityUser, UserToBaseclass> direct = users.join(SecurityUser_.userToBaseclasses, JoinType.LEFT);
		Predicate directPredicate = cb.and(
				cb.isFalse(direct.get(UserToBaseclass_.softDelete)),
				cb.equal(users.get(SecurityUser_.id), securityUser.getId()),
				cb.equal(direct.get(UserToBaseclass_.baseclass), securityOperation.getSecurity()),
				cb.equal(direct.get(UserToBaseclass_.access), access));

		List<Predicate> preds = new ArrayList<>();
		preds.add(directPredicate);
		q.select(users).where(preds.toArray(Predicate[]::new));
		TypedQuery<SecurityUser> query = em.createQuery(q);
		List<SecurityUser> usersList = query.getResultList();
		return !usersList.isEmpty();
	}
}
