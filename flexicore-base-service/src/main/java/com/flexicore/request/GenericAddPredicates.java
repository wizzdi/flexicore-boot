package com.flexicore.request;

import com.flexicore.model.Baseclass;
import com.flexicore.model.FilteringInformationHolder;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.List;

public interface GenericAddPredicates<Link extends Baseclass, LinkFilter extends FilteringInformationHolder> {

    void addLinkPredicates(LinkFilter filter, List<Predicate> preds, Root<Link> r, CriteriaBuilder cb) ;

}
