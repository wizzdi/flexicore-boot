package com.wizzdi.flexicore.security.request;

public class SecurityUserFilter extends SecurityEntityFilter {
	private String searchStringLike;

	public String getSearchStringLike() {
		return searchStringLike;
	}

	public <T extends SecurityUserFilter> T setSearchStringLike(String searchStringLike) {
		this.searchStringLike = searchStringLike;
		return (T) this;
	}
}
