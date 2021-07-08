package com.wizzdi.flexicore.security.response;

import com.wizzdi.flexicore.security.request.BaseclassFilter;
import com.wizzdi.flexicore.security.request.PaginationFilter;

import java.util.List;

public class PaginationResponse<T> {

	private List<T> list;
	private Long totalRecords;
	private Long totalPages;
	private Long startPage;
	private Long endPage;

	public PaginationResponse(List<T> list, Integer pageSize, long totalRecords) {
		this.list = list;
		this.startPage = 0L;
		this.totalRecords = totalRecords;
		this.totalPages = pageSize != null ? (long)Math.ceil(totalRecords / (double)pageSize) : 1;
		this.endPage = totalPages-1;
	}

	public PaginationResponse(List<T> list, PaginationFilter baseclassFilter, long totalRecords) {
		this(list, baseclassFilter != null ? baseclassFilter.getPageSize() : null, totalRecords);
	}


	public PaginationResponse() {
	}

	public List<T> getList() {
		return list;
	}

	public PaginationResponse<T> setList(List<T> list) {
		this.list = list;
		return this;
	}

	public Long getTotalRecords() {
		return totalRecords;
	}

	public PaginationResponse<T> setTotalRecords(Long totalRecords) {
		this.totalRecords = totalRecords;
		return this;
	}

	public Long getTotalPages() {
		return totalPages;
	}

	public PaginationResponse<T> setTotalPages(Long totalPages) {
		this.totalPages = totalPages;
		return this;
	}

	public Long getStartPage() {
		return startPage;
	}

	public PaginationResponse<T> setStartPage(Long startPage) {
		this.startPage = startPage;
		return this;
	}

	public Long getEndPage() {
		return endPage;
	}

	public PaginationResponse<T> setEndPage(Long endPage) {
		this.endPage = endPage;
		return this;
	}
}
