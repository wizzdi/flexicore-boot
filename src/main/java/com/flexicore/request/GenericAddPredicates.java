package com.flexicore.request;

import com.flexicore.model.Baseclass;
import com.flexicore.model.FilteringInformationHolder;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

public interface GenericAddPredicates<Link extends Baseclass, LinkFilter extends FilteringInformationHolder> {

    void addLinkPredicates(LinkFilter filter, List<Predicate> preds, Root<Link> r, CriteriaBuilder cb) ;

}
