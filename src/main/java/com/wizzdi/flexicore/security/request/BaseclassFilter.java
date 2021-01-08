package com.wizzdi.flexicore.security.request;

public class BaseclassFilter {
	private Integer pageSize;
	private Integer currentPage;

	public Integer getPageSize() {
		return pageSize;
	}

	public <T extends BaseclassFilter> T setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
		return (T) this;
	}

	public Integer getCurrentPage() {
		return currentPage;
	}

	public <T extends BaseclassFilter> T setCurrentPage(Integer currentPage) {
		this.currentPage = currentPage;
		return (T) this;
	}
}
