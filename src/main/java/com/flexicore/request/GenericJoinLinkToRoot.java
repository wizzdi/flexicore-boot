package com.flexicore.request;

import com.flexicore.model.Baseclass;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;

public interface GenericJoinLinkToRoot<Link extends Baseclass,Base extends Baseclass> {
    Join<Link, Base> joinLinkToRoot(Root<Link> r, CriteriaBuilder cb);
}
