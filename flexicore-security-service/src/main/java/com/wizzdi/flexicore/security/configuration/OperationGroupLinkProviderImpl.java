package com.wizzdi.flexicore.security.configuration;

import com.flexicore.model.OperationToGroup;
import com.flexicore.model.OperationToGroup_;
import com.flexicore.model.SecurityOperation;
import com.wizzdi.segmantix.api.model.IOperation;
import com.wizzdi.segmantix.api.model.IOperationGroupLink;
import com.wizzdi.segmantix.api.service.OperationGroupLinkProvider;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class OperationGroupLinkProviderImpl implements OperationGroupLinkProvider {

    @PersistenceContext
    private  EntityManager em;


    @Override
    public List<IOperationGroupLink> listAllOperationGroupLinks(List<IOperation> operations) {
        List<SecurityOperation> ops = operations.stream().filter(f -> f instanceof SecurityOperation).map(f -> (SecurityOperation) f).toList();
        if(ops.isEmpty()){
            return Collections.emptyList();
        }
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<OperationToGroup> q = cb.createQuery(OperationToGroup.class);
        Root<OperationToGroup> r = q.from(OperationToGroup.class);
        q.select(r).where(r.get(OperationToGroup_.operation).in(ops),cb.isFalse(r.get(OperationToGroup_.softDelete)));
        return new ArrayList<>(em.createQuery(q).getResultList());
    }
}
