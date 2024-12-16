package com.wizzdi.security.bearer.jwt.testUser;

import com.flexicore.model.Baseclass;
import com.wizzdi.flexicore.security.configuration.SecurityContext;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.security.data.BasicRepository;
import com.wizzdi.flexicore.security.data.SecurityUserRepository;
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


@Extension
@Component
public class TestUserRepository implements Plugin {

    @PersistenceContext
    private EntityManager em;
    @Autowired
    private SecurityUserRepository securityUserRepository;

    public List<TestUser> listAllTestUsers(TestUserFilter testUserFiltering,
                                                 SecurityContext securityContext) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<TestUser> q = cb.createQuery(TestUser.class);
        Root<TestUser> r = q.from(TestUser.class);
        List<Predicate> preds = new ArrayList<>();
        addTestUsersPredicates(testUserFiltering, r, cb, q, preds, securityContext);
        q.select(r).where(preds.toArray(new Predicate[0]));
        TypedQuery<TestUser> query = em.createQuery(q);
        BasicRepository.addPagination(testUserFiltering, query);
        return query.getResultList();
    }


    public <T extends TestUser> void addTestUsersPredicates(TestUserFilter testUserFiltering, From<?, T> r, CriteriaBuilder cb, CommonAbstractCriteria q, List<Predicate> preds, SecurityContext securityContext) {
        securityUserRepository.addSecurityUserPredicates(testUserFiltering,cb,q,r,preds,securityContext);
        if(testUserFiltering.getUsernames()!=null&&!testUserFiltering.getUsernames().isEmpty()){
            preds.add(r.get("username").in(testUserFiltering.getUsernames()));
        }

    }

    public long countAllTestUsers(TestUserFilter testUserFiltering,
                                     SecurityContext securityContext) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> q = cb.createQuery(Long.class);
        Root<TestUser> r = q.from(TestUser.class);
        List<Predicate> preds = new ArrayList<>();
        addTestUsersPredicates(testUserFiltering, r, cb, q, preds, securityContext);
        q.select(cb.count(r)).where(preds.toArray(new Predicate[0]));
        TypedQuery<Long> query = em.createQuery(q);
        return query.getSingleResult();
    }


    public <T extends Baseclass> List<T> listByIds(Class<T> c, Set<String> ids, SecurityContext securityContext) {
        return securityUserRepository.listByIds(c, ids, securityContext);
    }

    public <T extends Baseclass> T getByIdOrNull(String id, Class<T> c, SecurityContext securityContext) {
        return securityUserRepository.getByIdOrNull(id, c, securityContext);
    }


    public <T> T findByIdOrNull(Class<T> type, String id) {
        return securityUserRepository.findByIdOrNull(type, id);
    }

    @Transactional
    public void merge(Object base) {
        securityUserRepository.merge(base);
    }

}
