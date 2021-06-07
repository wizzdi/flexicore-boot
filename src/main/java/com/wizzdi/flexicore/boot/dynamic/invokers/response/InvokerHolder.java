package com.wizzdi.flexicore.boot.dynamic.invokers.response;

import com.wizzdi.flexicore.boot.dynamic.invokers.annotations.Invoker;

import java.util.List;

public class InvokerHolder {

    private final InvokerInfo invokerInfo;

    public InvokerHolder() {
        this.invokerInfo=new InvokerInfo();
    }

    public InvokerHolder(InvokerInfo invokerInfo) {
        this.invokerInfo = invokerInfo;
    }

    public void addInvokerMethodInfo(InvokerMethodInfo invokerMethodInfo) {
        invokerInfo.addInvokerMethodInfo(invokerMethodInfo);
    }

    public Class<?> getName() {
        return invokerInfo.getName();
    }

    public InvokerInfo setName(Class<? extends Invoker> name) {
        return invokerInfo.setName(name);
    }

    public String getDescription() {
        return invokerInfo.getDescription();
    }

    public InvokerInfo setDescription(String description) {
        return invokerInfo.setDescription(description);
    }

    public String getDisplayName() {
        return invokerInfo.getDisplayName();
    }

    public InvokerInfo setDisplayName(String displayName) {
        return invokerInfo.setDisplayName(displayName);
    }

    public Class<?> getHandlingType() {
        return invokerInfo.getHandlingType();
    }

    public InvokerInfo setHandlingType(Class<?> handlingType) {
        return invokerInfo.setHandlingType(handlingType);
    }


    @Override
    public String toString() {
        return invokerInfo.toString();
    }
}
