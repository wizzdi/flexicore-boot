package com.flexicore.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wizzdi.segmantix.api.model.IOperation;
import com.wizzdi.segmantix.api.model.IOperationGroupLink;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Transient;

@Entity
public class OperationToGroup extends Baseclass implements IOperationGroupLink {

    private String operationId;
    @ManyToOne(targetEntity = OperationGroup.class)
    private OperationGroup operationGroup;


    public String getOperationId() {
        return operationId;
    }

    public <T extends OperationToGroup> T setOperationId(String operationId) {
        this.operationId = operationId;
        return (T) this;
    }

    @Transient
    @JsonIgnore
    @Override
    public IOperation getOperation() {
        return SecurityOperation.ofId(operationId);
    }

    @ManyToOne(targetEntity = OperationGroup.class)
    public OperationGroup getOperationGroup() {
        return operationGroup;
    }

    public <T extends OperationToGroup> T setOperationGroup(OperationGroup operationGroup) {
        this.operationGroup = operationGroup;
        return (T) this;
    }
}
