package com.flexicore.data.jsoncontainers;

import com.flexicore.model.FilteringInformationHolder;

import java.util.List;

public class PaginationResponse<T> {

    private List<T> list;
    private long totalRecords;
    private long totalPages;
    private long startPage;
    private long endPage;

        public PaginationResponse(List<T> list, FilteringInformationHolder filteringInformationHolder, long totalRecords) {
            this.list = list;
            this.startPage=0;
            this.totalRecords=totalRecords;
            this.totalPages=filteringInformationHolder!=null&&filteringInformationHolder.getPageSize()!=null?1+totalRecords/filteringInformationHolder.getPageSize():totalRecords;
            this.endPage=totalPages;
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

    public long getTotalRecords() {
        return totalRecords;
    }

    public PaginationResponse<T> setTotalRecords(long totalRecords) {
        this.totalRecords = totalRecords;
        return this;
    }

    public long getTotalPages() {
        return totalPages;
    }

    public PaginationResponse<T> setTotalPages(long totalPages) {
        this.totalPages = totalPages;
        return this;
    }

    public long getStartPage() {
        return startPage;
    }

    public PaginationResponse<T> setStartPage(long startPage) {
        this.startPage = startPage;
        return this;
    }

    public long getEndPage() {
        return endPage;
    }

    public PaginationResponse<T> setEndPage(long endPage) {
        this.endPage = endPage;
        return this;
    }
}
