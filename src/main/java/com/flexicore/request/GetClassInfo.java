package com.flexicore.request;

import com.flexicore.model.FilteringInformationHolder;

public class GetClassInfo extends FilteringInformationHolder {

    private String className;


    public String getClassName() {
        return className;
    }

    public GetClassInfo setClassName(String className) {
        this.className = className;
        return this;
    }





}
