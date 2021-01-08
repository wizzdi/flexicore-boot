package com.wizzdi.flexicore.security.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.model.SecurityLink;

public class SecurityLinkUpdate extends SecurityLinkCreate{

	private String id;
	@JsonIgnore
	private SecurityLink securityLink;

	public String getId() {
		return id;
	}

	public <T extends SecurityLinkUpdate> T setId(String id) {
		this.id = id;
		return (T) this;
	}

	@JsonIgnore
	public SecurityLink getSecurityLink() {
		return securityLink;
	}

	public <T extends SecurityLinkUpdate> T setSecurityLink(SecurityLink securityLink) {
		this.securityLink = securityLink;
		return (T) this;
	}
}
