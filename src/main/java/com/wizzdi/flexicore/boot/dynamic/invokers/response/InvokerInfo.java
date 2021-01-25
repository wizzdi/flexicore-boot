package com.wizzdi.flexicore.boot.dynamic.invokers.response;

import com.wizzdi.flexicore.boot.dynamic.invokers.annotations.Invoker;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class InvokerInfo {


    private Class<? extends Invoker> name;
    private String description;
    private String displayName;
    private Class<?> handlingType;
    private List<InvokerMethodInfo> methods= new ArrayList<>();

    public InvokerInfo() {
    }

    public InvokerInfo(InvokerInfo other, Set<String> allowedOps) {
        this.name = other.name;
        this.description = other.description;
        this.displayName = other.displayName;
        this.handlingType = other.handlingType;
        this.methods = other.methods.parallelStream().filter(f->allowedOps.contains(f.getId())).collect(Collectors.toList());

    }

    public InvokerInfo(Invoker Invoker) {
        com.wizzdi.flexicore.boot.dynamic.invokers.annotations.InvokerInfo InvokerInfo=Invoker.getClass().getDeclaredAnnotation(com.wizzdi.flexicore.boot.dynamic.invokers.annotations.InvokerInfo.class);
        name=Invoker.getClass();
        displayName=InvokerInfo!=null&&!InvokerInfo.displayName().isEmpty()?InvokerInfo.displayName():Invoker.getClass().getName();
        description=InvokerInfo!=null&&!InvokerInfo.description().isEmpty()?InvokerInfo.description():"No Description";
        handlingType=Invoker.getHandlingClass();
        Method[] methods=Invoker.getClass().getDeclaredMethods();
        for (Method method : methods) {
            com.wizzdi.flexicore.boot.dynamic.invokers.annotations.InvokerMethodInfo InvokerMethodInfo=method.getDeclaredAnnotation(com.wizzdi.flexicore.boot.dynamic.invokers.annotations.InvokerMethodInfo.class);
            if(InvokerMethodInfo!=null){
                    this.methods.add(new InvokerMethodInfo(method,InvokerMethodInfo));



            }
        }


    }


    public Class<? extends Invoker> getName() {
        return name;
    }

    public InvokerInfo setName(Class<?extends Invoker> name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public InvokerInfo setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getDisplayName() {
        return displayName;
    }

    public InvokerInfo setDisplayName(String displayName) {
        this.displayName = displayName;
        return this;
    }

    public List<InvokerMethodInfo> getMethods() {
        return methods;
    }

    public InvokerInfo setMethods(List<InvokerMethodInfo> methods) {
        this.methods = methods;
        return this;
    }

    public Class<?> getHandlingType() {
        return handlingType;
    }

    public InvokerInfo setHandlingType(Class<?> handlingType) {
        this.handlingType = handlingType;
        return this;
    }

    @Override
    public String toString() {
        return "InvokerInfo{" +
                "name=" + name +
                ", description='" + description + '\'' +
                ", displayName='" + displayName + '\'' +
                ", handlingType=" + handlingType +
                ", methods=" + methods +
                '}';
    }
}
