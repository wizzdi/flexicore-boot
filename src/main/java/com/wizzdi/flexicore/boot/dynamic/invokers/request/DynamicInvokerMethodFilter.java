package com.wizzdi.flexicore.boot.dynamic.invokers.request;

import com.wizzdi.flexicore.security.request.BaseclassFilter;

import java.util.Set;

public class DynamicInvokerMethodFilter extends BaseclassFilter {

    private String nameLike;
    private Set<String> categories;
    private DynamicInvokerFilter dynamicInvokerFilter;


    public String getNameLike() {
        return nameLike;
    }

    public <T extends DynamicInvokerMethodFilter> T setNameLike(String nameLike) {
        this.nameLike = nameLike;
        return (T) this;
    }

    public Set<String> getCategories() {
        return categories;
    }

    public <T extends DynamicInvokerMethodFilter> T setCategories(Set<String> categories) {
        this.categories = categories;
        return (T) this;
    }

    public DynamicInvokerFilter getDynamicInvokerFilter() {
        return dynamicInvokerFilter;
    }

    public <T extends DynamicInvokerMethodFilter> T setDynamicInvokerFilter(DynamicInvokerFilter dynamicInvokerFilter) {
        this.dynamicInvokerFilter = dynamicInvokerFilter;
        return (T) this;
    }
}
