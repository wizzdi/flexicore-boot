package com.flexicore.response;

import com.flexicore.interfaces.dynamic.Invoker;
import com.flexicore.model.dynamic.DynamicInvoker;

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

    public InvokerInfo(InvokerInfo other, Set<String> allowedOps) {
        this.name = other.name;
        this.description = other.description;
        this.displayName = other.displayName;
        this.handlingType = other.handlingType;
        this.methods = other.methods.parallelStream().filter(f->allowedOps.contains(f.getId())).collect(Collectors.toList());

    }

    public InvokerInfo(Invoker Invoker) {
        com.flexicore.interfaces.dynamic.InvokerInfo InvokerInfo=Invoker.getClass().getDeclaredAnnotation(com.flexicore.interfaces.dynamic.InvokerInfo.class);
        name=Invoker.getClass();
        displayName=InvokerInfo!=null&&!InvokerInfo.displayName().isEmpty()?InvokerInfo.displayName():Invoker.getClass().getName();
        description=InvokerInfo!=null&&!InvokerInfo.description().isEmpty()?InvokerInfo.description():"No Description";
        handlingType=Invoker.getHandlingClass();
        Method[] methods=Invoker.getClass().getDeclaredMethods();
        for (Method method : methods) {
            com.flexicore.interfaces.dynamic.InvokerMethodInfo InvokerMethodInfo=method.getDeclaredAnnotation(com.flexicore.interfaces.dynamic.InvokerMethodInfo.class);
            if(InvokerMethodInfo!=null){
                    this.methods.add(new InvokerMethodInfo(method,InvokerMethodInfo));



            }
        }


    }

    public InvokerInfo(DynamicInvoker dynamicInvoker) {
        this.displayName= dynamicInvoker.getName();
        this.description= dynamicInvoker.getDescription();

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
}
