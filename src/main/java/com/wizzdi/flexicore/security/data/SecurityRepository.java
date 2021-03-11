package com.wizzdi.flexicore.security.data;

import com.flexicore.annotations.IOperation;
import com.flexicore.model.*;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import org.pf4j.Extension;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
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
		Join<RoleToUser, Role> roles = cb.treat(roleToUser.join(RoleToUser_.leftside, JoinType.LEFT),Role.class);
		Join<Role, RoleToBaseclass> roleToBaseClass = roles.join(Role_.roleToBaseclass);
		Predicate rolesPredicate = cb.and(
				cb.isFalse(roleToBaseClass.get(RoleToBaseclass_.softDelete)),
				cb.equal(users.get(SecurityUser_.id), securityUser.getId()),
				cb.equal(roleToBaseClass.get(RoleToBaseclass_.rightside), securityOperation),
				cb.equal(roleToBaseClass.get(RoleToBaseclass_.simplevalue), access.name())
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
		Join<SecurityUser, UserToBaseClass> direct = users.join(SecurityUser_.userToBaseClasses, JoinType.LEFT);
		Predicate directPredicate = cb.and(
				cb.isFalse(direct.get(RoleToBaseclass_.softDelete)),
				cb.equal(users.get(SecurityUser_.id), securityUser.getId()),
				cb.equal(direct.get(UserToBaseClass_.rightside), securityOperation),
				cb.equal(direct.get(UserToBaseClass_.simplevalue), access.name()));

		List<Predicate> preds = new ArrayList<>();
		preds.add(directPredicate);
		q.select(users).where(preds.toArray(Predicate[]::new));
		TypedQuery<SecurityUser> query = em.createQuery(q);
		List<SecurityUser> usersList = query.getResultList();
		return !usersList.isEmpty();
	}
}
