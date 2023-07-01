package com.flexicore.request;

import com.flexicore.model.Baseclass;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;

public interface GenericJoinLinkToRoot<Link extends Baseclass,Base extends Baseclass> {
    Join<Link, Base> joinLinkToRoot(Root<Link> r, CriteriaBuilder cb);
}
