package com.wizzdi.flexicore.boot.base.init;

import org.springframework.beans.factory.config.DependencyDescriptor;
import org.springframework.core.MethodParameter;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

public class FlexicoreDependencyDescriptor extends DependencyDescriptor {
    private final Set<String> visitedContextIds=new HashSet<>();
    public FlexicoreDependencyDescriptor(MethodParameter methodParameter, boolean required) {
        super(methodParameter, required);
    }

    public FlexicoreDependencyDescriptor(MethodParameter methodParameter, boolean required, boolean eager) {
        super(methodParameter, required, eager);
    }

    public FlexicoreDependencyDescriptor(Field field, boolean required) {
        super(field, required);
    }

    public FlexicoreDependencyDescriptor(Field field, boolean required, boolean eager) {
        super(field, required, eager);
    }

    public FlexicoreDependencyDescriptor(DependencyDescriptor original) {
        super(original);
    }
    public boolean addVisitedContextId(String id){
        return visitedContextIds.add(id);
    }
}
