package com.wizzdi.flexicore.security.response;

import com.flexicore.model.SecurityLinkGroup;

import java.util.List;

public record SecurityLinkGroupContainer(SecurityLinkGroup securityLinkGroup, List<SecurityLinkContainer> links) {
}
