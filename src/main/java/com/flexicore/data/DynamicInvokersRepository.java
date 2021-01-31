/*******************************************************************************
 *  Copyright (C) FlexiCore, Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Avishay Ben Natan And Asaf Ben Natan, October 2015
 ******************************************************************************/
package com.flexicore.data;

import org.pf4j.Extension;
import org.springframework.stereotype.Component;
import com.flexicore.data.impl.BaseclassRepository;
import com.flexicore.model.Operation;
import com.flexicore.model.Operation_;
import com.flexicore.model.QueryInformationHolder;
import com.flexicore.model.dynamic.*;
import com.flexicore.request.InvokersFilter;
import com.flexicore.request.InvokersOperationFilter;
import com.flexicore.security.SecurityContext;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Component
@Extension
public class DynamicInvokersRepository extends BaseclassRepository {


    public List<Operation> getInvokersOperations(InvokersOperationFilter invokersOperationFilter, SecurityContext securityContext) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Operation> q = cb.createQuery(Operation.class);
        Root<Operation> r = q.from(Operation.class);
        List<Predicate> preds = new ArrayList<>();
        addInvokersOperationFilter(invokersOperationFilter, r, preds);
        QueryInformationHolder<Operation> queryInformationHolder = new QueryInformationHolder<>(invokersOperationFilter, Operation.class, securityContext);
        return getAllFiltered(queryInformationHolder, preds, cb, q, r);
    }

    public long countInvokersOperations(InvokersOperationFilter invokersOperationFilter, SecurityContext securityContext) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> q = cb.createQuery(Long.class);
        Root<Operation> r = q.from(Operation.class);
        List<Predicate> preds = new ArrayList<>();
        addInvokersOperationFilter(invokersOperationFilter, r, preds);
        QueryInformationHolder<Operation> queryInformationHolder = new QueryInformationHolder<>(invokersOperationFilter, Operation.class, securityContext);
        return countAllFiltered(queryInformationHolder, preds, cb, q, r);
    }

    private void addInvokersOperationFilter(InvokersOperationFilter invokersOperationFilter, Root<Operation> r, List<Predicate> preds) {
        if (invokersOperationFilter.getInvokers() != null && !invokersOperationFilter.getInvokers().isEmpty()) {
            Set<String> ids = invokersOperationFilter.getInvokers().parallelStream().map(f -> f.getId()).collect(Collectors.toSet());
            Join<Operation, DynamicInvoker> invokerJoin = r.join(Operation_.dynamicInvoker);
            preds.add(invokerJoin.get(DynamicInvoker_.id).in(ids));
        }
    }

    public List<DynamicInvoker> getAllInvokers(InvokersFilter invokersFilter, SecurityContext securityContext) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<DynamicInvoker> q = cb.createQuery(DynamicInvoker.class);
        Root<DynamicInvoker> r = q.from(DynamicInvoker.class);
        List<Predicate> preds = new ArrayList<>();
        addDynamicInvokersPredicate(invokersFilter, r, preds);
        QueryInformationHolder<DynamicInvoker> queryInformationHolder = new QueryInformationHolder<>(invokersFilter, DynamicInvoker.class, securityContext);
        return getAllFiltered(queryInformationHolder, preds, cb, q, r);

    }

    public Long countAllInvokers(InvokersFilter invokersFilter, SecurityContext securityContext) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> q = cb.createQuery(Long.class);
        Root<DynamicInvoker> r = q.from(DynamicInvoker.class);
        List<Predicate> preds = new ArrayList<>();
        addDynamicInvokersPredicate(invokersFilter, r, preds);
        QueryInformationHolder<DynamicInvoker> queryInformationHolder = new QueryInformationHolder<>(invokersFilter, DynamicInvoker.class, securityContext);
        return countAllFiltered(queryInformationHolder, preds, cb, q, r);

    }

    private void addDynamicInvokersPredicate(InvokersFilter invokersFilter, Root<DynamicInvoker> r, List<Predicate> preds) {
        if (!invokersFilter.getInvokerIds().isEmpty()) {
            preds.add(r.get(DynamicInvoker_.id).in(invokersFilter.getInvokerIds()));
        }

        if (!invokersFilter.getInvokerTypes().isEmpty()) {
            preds.add(r.get(DynamicInvoker_.canonicalName).in(invokersFilter.getInvokerTypes()));
        }
    }
}
