package com.wizzdi.flexicore.security.request;

import jakarta.validation.Valid;


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
