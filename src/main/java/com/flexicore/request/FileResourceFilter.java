package com.flexicore.request;

import com.flexicore.model.FilteringInformationHolder;

import java.util.Set;

public class FileResourceFilter extends FilteringInformationHolder {

    private Set<String> md5s;

    public Set<String> getMd5s() {
        return md5s;
    }

    public <T extends FileResourceFilter> T setMd5s(Set<String> md5s) {
        this.md5s = md5s;
        return (T) this;
    }
}
