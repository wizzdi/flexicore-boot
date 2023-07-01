package com.flexicore.data.jsoncontainers;

import com.flexicore.model.FilteringInformationHolder;

import java.util.List;

public class PaginationResponse<T> extends com.wizzdi.flexicore.security.response.PaginationResponse<T> {

    public PaginationResponse(com.wizzdi.flexicore.security.response.PaginationResponse<T> other) {
        this.setList(other.getList());
        this.setEndPage(other.getEndPage());
        this.setStartPage(other.getStartPage());
        this.setTotalPages(other.getTotalPages());
        this.setTotalRecords(other.getTotalRecords());
    }

    public PaginationResponse() {
    }

    public PaginationResponse(List<T> list, FilteringInformationHolder baseclassFilter, long totalRecords) {
        super(list, baseclassFilter.getPageSize(), totalRecords);
    }
}
