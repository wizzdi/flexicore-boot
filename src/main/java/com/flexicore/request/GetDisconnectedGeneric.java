package com.flexicore.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.model.Baseclass;
import com.flexicore.model.FilteringInformationHolder;

public class GetDisconnectedGeneric<Base extends Baseclass,Link extends Baseclass,BaseFilter extends FilteringInformationHolder,LinkFilter extends FilteringInformationHolder> {
    private String wantedClassName;
    @JsonIgnore
    private Class<Base> wantedClass;
    private String linkClassName;
    @JsonIgnore
    private Class<Link> linkClass;
    private LinkFilter linkFilter;
    private BaseFilter baseFilter;
    @JsonIgnore
    private GenericAddPredicates<Link,LinkFilter> linkAddPredicates;
    @JsonIgnore
    private GenericAddPredicates<Base,BaseFilter> baseAddPredicates;
    @JsonIgnore
    private GenericJoinLinkToRoot<Link,Base> genericJoinLinkToRoot;

    public String getWantedClassName() {
        return wantedClassName;
    }

    public <T extends GetDisconnectedGeneric<Base, Link, BaseFilter, LinkFilter>> T setWantedClassName(String wantedClassName) {
        this.wantedClassName = wantedClassName;
        return (T) this;
    }

    @JsonIgnore
    public Class<Base> getWantedClass() {
        return wantedClass;
    }

    public <T extends GetDisconnectedGeneric<Base, Link, BaseFilter, LinkFilter>> T setWantedClass(Class<Base> wantedClass) {
        this.wantedClass = wantedClass;
        return (T) this;
    }

    public String getLinkClassName() {
        return linkClassName;
    }

    public <T extends GetDisconnectedGeneric<Base, Link, BaseFilter, LinkFilter>> T setLinkClassName(String linkClassName) {
        this.linkClassName = linkClassName;
        return (T) this;
    }

    @JsonIgnore
    public Class<Link> getLinkClass() {
        return linkClass;
    }

    public <T extends GetDisconnectedGeneric<Base, Link, BaseFilter, LinkFilter>> T setLinkClass(Class<Link> linkClass) {
        this.linkClass = linkClass;
        return (T) this;
    }

    public LinkFilter getLinkFilter() {
        return linkFilter;
    }

    public <T extends GetDisconnectedGeneric<Base, Link, BaseFilter, LinkFilter>> T setLinkFilter(LinkFilter linkFilter) {
        this.linkFilter = linkFilter;
        return (T) this;
    }

    public BaseFilter getBaseFilter() {
        return baseFilter;
    }

    public <T extends GetDisconnectedGeneric<Base, Link, BaseFilter, LinkFilter>> T setBaseFilter(BaseFilter baseFilter) {
        this.baseFilter = baseFilter;
        return (T) this;
    }

    @JsonIgnore
    public GenericAddPredicates<Link, LinkFilter> getLinkAddPredicates() {
        return linkAddPredicates;
    }

    public <T extends GetDisconnectedGeneric<Base, Link, BaseFilter, LinkFilter>> T setLinkAddPredicates(GenericAddPredicates<Link, LinkFilter> linkAddPredicates) {
        this.linkAddPredicates = linkAddPredicates;
        return (T) this;
    }

    @JsonIgnore
    public GenericAddPredicates<Base, BaseFilter> getBaseAddPredicates() {
        return baseAddPredicates;
    }

    public <T extends GetDisconnectedGeneric<Base, Link, BaseFilter, LinkFilter>> T setBaseAddPredicates(GenericAddPredicates<Base, BaseFilter> baseAddPredicates) {
        this.baseAddPredicates = baseAddPredicates;
        return (T) this;
    }

    @JsonIgnore
    public GenericJoinLinkToRoot<Link, Base> getGenericJoinLinkToRoot() {
        return genericJoinLinkToRoot;
    }

    public <T extends GetDisconnectedGeneric<Base, Link, BaseFilter, LinkFilter>> T setGenericJoinLinkToRoot(GenericJoinLinkToRoot<Link, Base> genericJoinLinkToRoot) {
        this.genericJoinLinkToRoot = genericJoinLinkToRoot;
        return (T) this;
    }
}
