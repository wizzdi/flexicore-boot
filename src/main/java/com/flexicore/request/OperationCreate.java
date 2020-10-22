package com.flexicore.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.annotations.IOperation;
import com.flexicore.interfaces.dynamic.FieldInfo;
import com.flexicore.interfaces.dynamic.IdRefFieldInfo;
import com.flexicore.model.dynamic.DynamicInvoker;


public class OperationCreate extends BaseclassCreate {

    @FieldInfo
    private Boolean auditable;
    @FieldInfo
    private IOperation.Access defaultaccess;
    @IdRefFieldInfo(refType = DynamicInvoker.class,list = false)
    private String dynamicInvokerId;
    @JsonIgnore
    private DynamicInvoker dynamicInvoker;

    public Boolean getAuditable() {
        return auditable;
    }

    public <T extends OperationCreate> T setAuditable(Boolean auditable) {
        this.auditable = auditable;
        return (T) this;
    }

    public IOperation.Access getDefaultaccess() {
        return defaultaccess;
    }

    public <T extends OperationCreate> T setDefaultaccess(IOperation.Access defaultaccess) {
        this.defaultaccess = defaultaccess;
        return (T) this;
    }

    public String getDynamicInvokerId() {
        return dynamicInvokerId;
    }

    public <T extends OperationCreate> T setDynamicInvokerId(String dynamicInvokerId) {
        this.dynamicInvokerId = dynamicInvokerId;
        return (T) this;
    }

    @JsonIgnore
    public DynamicInvoker getDynamicInvoker() {
        return dynamicInvoker;
    }

    public <T extends OperationCreate> T setDynamicInvoker(DynamicInvoker dynamicInvoker) {
        this.dynamicInvoker = dynamicInvoker;
        return (T) this;
    }
}
