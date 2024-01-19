package com.wizzdi.flexicore.security.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.annotations.IOperation;
import com.flexicore.model.*;
import com.wizzdi.flexicore.security.validation.IdValid;
import jakarta.validation.Valid;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class SecurityLinkGroupFilter extends PaginationFilter {

    private BasicPropertiesFilter basicPropertiesFilter;

    @Valid
    private SecurityLinkFilter securityLinkFilter;

    public BasicPropertiesFilter getBasicPropertiesFilter() {
        return basicPropertiesFilter;
    }

    public <T extends SecurityLinkGroupFilter> T setBasicPropertiesFilter(BasicPropertiesFilter basicPropertiesFilter) {
        this.basicPropertiesFilter = basicPropertiesFilter;
        return (T) this;
    }

    public SecurityLinkFilter getSecurityLinkFilter() {
        return securityLinkFilter;
    }

    public <T extends SecurityLinkGroupFilter> T setSecurityLinkFilter(SecurityLinkFilter securityLinkFilter) {
        this.securityLinkFilter = securityLinkFilter;
        return (T) this;
    }
}
