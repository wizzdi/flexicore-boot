/*******************************************************************************
 *  Copyright (C) FlexiCore, Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Avishay Ben Natan And Asaf Ben Natan, October 2015
 ******************************************************************************/
/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.wizzdi.flexicore.common.user.data;

import com.flexicore.model.Baseclass;
import com.flexicore.model.SecurityTenant;
import com.flexicore.model.SecurityTenant_;
import com.flexicore.model.TenantToUser;
import com.flexicore.model.TenantToUser_;
import com.flexicore.model.User;
import com.flexicore.model.User_;
import com.wizzdi.flexicore.common.user.request.CommonUserFilter;
import com.flexicore.security.SecurityContextBase;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.security.data.BasicRepository;
import com.wizzdi.flexicore.security.data.SecurityUserRepository;
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
public class CommonUserRepository implements Plugin {

    @PersistenceContext
    private EntityManager em;
    @Autowired
    private SecurityUserRepository securityUserRepository;


    public List<User> getAllUsers(CommonUserFilter commonUserFilter, SecurityContextBase securityContextBase) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<User> q = cb.createQuery(User.class);
        Root<User> r = q.from(User.class);
        List<Predicate> preds = new ArrayList<>();
        addUserFiltering(commonUserFilter,q, cb, r, preds,securityContextBase);
        q.select(r).where(preds.toArray(new Predicate[0]));
        TypedQuery<User> query = em.createQuery(q);
        BasicRepository.addPagination(commonUserFilter, query);
        return query.getResultList();
    }

    public long countAllUsers(CommonUserFilter commonUserFilter, SecurityContextBase securityContextBase) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> q = cb.createQuery(Long.class);
        Root<User> r = q.from(User.class);
        List<Predicate> preds = new ArrayList<>();
        addUserFiltering(commonUserFilter,q, cb, r, preds,securityContextBase);
        q.select(cb.count(r)).where(preds.toArray(new Predicate[0]));
        TypedQuery<Long> query = em.createQuery(q);
        return query.getSingleResult();
    }

    public <T extends User> void addUserFiltering(CommonUserFilter commonUserFilter,CommonAbstractCriteria q, CriteriaBuilder cb, Root<T> r, List<Predicate> preds,SecurityContextBase securityContextBase) {
        securityUserRepository.addSecurityUserPredicates(commonUserFilter,cb,q,r,preds,securityContextBase);
        if (commonUserFilter.getEmails() != null && !commonUserFilter.getEmails().isEmpty()) {
            preds.add(r.get(User_.email).in( commonUserFilter.getEmails()));
        }
        if (commonUserFilter.getPhoneNumbers() != null && !commonUserFilter.getPhoneNumbers().isEmpty()) {
            preds.add(r.get(User_.phoneNumber).in(commonUserFilter.getPhoneNumbers()));
        }

        if (commonUserFilter.getEmailVerificationToken() != null) {
            preds.add(cb.equal(r.get(User_.emailVerificationToken), commonUserFilter.getEmailVerificationToken()));
        }

        if (commonUserFilter.getForgotPasswordToken() != null) {
            preds.add(cb.equal(r.get(User_.forgotPasswordToken), commonUserFilter.getForgotPasswordToken()));
        }


        if (commonUserFilter.getLastNameLike() != null) {
            preds.add(cb.like(r.get(User_.surName), commonUserFilter.getLastNameLike()));
        }


        if (commonUserFilter.getUserIds() != null && !commonUserFilter.getUserIds().isEmpty()) {
            preds.add(r.get(User_.id).in(commonUserFilter.getUserIds()));
        }
        if (commonUserFilter.getUserSecurityTenants() != null && !commonUserFilter.getUserSecurityTenants().isEmpty()) {
            Set<String> ids = commonUserFilter.getUserSecurityTenants().parallelStream().map(Baseclass::getId).collect(Collectors.toSet());
            Join<T, TenantToUser> join = r.join(User_.tenantToUsers);
            Join<TenantToUser, SecurityTenant> securityTenantJoin = join.join(TenantToUser_.tenant);
            preds.add(securityTenantJoin.get(SecurityTenant_.id).in(ids));
        }


    }


    @Transactional
    public <T> T merge(T o) {
        return securityUserRepository.merge(o);
    }

    @Transactional
    public void massMerge(List<Object> list) {
        securityUserRepository.massMerge(list);
    }

    public <T extends Baseclass> List<T> listByIds(Class<T> c, Set<String> ids, SecurityContextBase securityContext) {
        return securityUserRepository.listByIds(c, ids, securityContext);
    }

    public <T extends Baseclass> T getByIdOrNull(String id, Class<T> c, SecurityContextBase securityContext) {
        return securityUserRepository.getByIdOrNull(id, c, securityContext);
    }

    public <T extends Baseclass> List<T> findByIds(Class<T> c, Set<String> requested) {
        return securityUserRepository.findByIds(c, requested);
    }

    public <T> T findByIdOrNull(Class<T> type, String id) {
        return securityUserRepository.findByIdOrNull(type, id);
    }
}
