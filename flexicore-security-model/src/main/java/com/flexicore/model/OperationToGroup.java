package com.flexicore.model;

import com.wizzdi.segmantix.api.model.IOperationGroupLink;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;

@Entity
public class OperationToGroup extends Baseclass implements IOperationGroupLink {

    @ManyToOne(targetEntity = SecurityOperation.class)
    private SecurityOperation operation;
    @ManyToOne(targetEntity = OperationGroup.class)
    private OperationGroup operationGroup;


    @ManyToOne(targetEntity = SecurityOperation.class)
    public SecurityOperation getOperation() {
        return operation;
    }

    public <T extends OperationToGroup> T setOperation(SecurityOperation securityOperation) {
        this.operation = securityOperation;
        return (T) this;
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
