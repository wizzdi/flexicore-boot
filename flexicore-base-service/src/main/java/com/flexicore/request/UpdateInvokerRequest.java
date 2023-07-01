package com.flexicore.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.model.dynamic.DynamicInvoker;

public class UpdateInvokerRequest extends CreateInvokerRequest {

    @JsonIgnore
    private DynamicInvoker invoker;
    private String dynamicInvokerId;

    @JsonIgnore
    public DynamicInvoker getInvoker() {
        return invoker;
    }

    public UpdateInvokerRequest setInvoker(DynamicInvoker invoker) {
        this.invoker = invoker;
        return this;
    }

    @Override
    public UpdateInvokerRequest setDisplayName(String displayName) {
        return (UpdateInvokerRequest) super.setDisplayName(displayName);
    }

    @Override
    public UpdateInvokerRequest setDescription(String description) {
        return (UpdateInvokerRequest) super.setDescription(description);
    }

    @Override
    public UpdateInvokerRequest setCanonicalName(String canonicalName) {
        return (UpdateInvokerRequest) super.setCanonicalName(canonicalName);
    }

    public String getDynamicInvokerId() {
        return dynamicInvokerId;
    }

    public UpdateInvokerRequest setDynamicInvokerId(String dynamicInvokerId) {
        this.dynamicInvokerId = dynamicInvokerId;
        return this;
    }
}
