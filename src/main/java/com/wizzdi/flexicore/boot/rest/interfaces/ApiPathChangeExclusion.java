package com.wizzdi.flexicore.boot.rest.interfaces;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ApiPathChangeExclusion {

    private  List<Class<?>> exclusion=new ArrayList<>();
    private  Set<String> excludedPackages=new HashSet<>();

    public <T extends ApiPathChangeExclusion> T setExclusion(List<Class<?>> exclusion) {
        this.exclusion = exclusion;
        return (T) this;
    }

    public Set<String> getExcludedPackages() {
        return excludedPackages;
    }

    public <T extends ApiPathChangeExclusion> T setExcludedPackages(Set<String> excludedPackages) {
        this.excludedPackages = excludedPackages;
        return (T) this;
    }

    public List<Class<?>> getExclusion() {
        return exclusion;
    }
}
