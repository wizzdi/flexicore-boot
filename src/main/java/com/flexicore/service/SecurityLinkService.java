package com.flexicore.service;

import com.flexicore.data.jsoncontainers.PaginationResponse;
import com.flexicore.interfaces.FlexiCoreService;
import com.flexicore.model.SecurityLink;
import com.flexicore.request.SecurityLinkFilter;
import com.flexicore.security.SecurityContext;

import java.util.List;

public interface SecurityLinkService extends FlexiCoreService {
    /**
     * validates #SecurityLinkFilter
     * @param securityLinkFilter used to receive security links
     * @param securityContext security context of the user to execute the action
     */
    void validate(SecurityLinkFilter securityLinkFilter, SecurityContext securityContext);

    PaginationResponse<SecurityLink> getAllSecurityLinks(SecurityLinkFilter securityLinkFilter, SecurityContext securityContext);

    List<SecurityLink> listAllSecurityLinks(SecurityLinkFilter securityLinkFilter, SecurityContext securityContext);
}
