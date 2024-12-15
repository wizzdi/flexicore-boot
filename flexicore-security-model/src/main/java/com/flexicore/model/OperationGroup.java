package com.flexicore.model;

import com.wizzdi.segmantix.api.model.IOperationGroup;
import jakarta.persistence.Entity;

@Entity
public class OperationGroup extends Baseclass implements IOperationGroup {

    private String externalId;

    public String getExternalId() {
        return externalId;
    }

    public <T extends OperationGroup> T setExternalId(String externalId) {
        this.externalId = externalId;
        return (T) this;
    }
}
