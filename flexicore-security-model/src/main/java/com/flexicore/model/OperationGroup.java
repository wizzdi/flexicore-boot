package com.flexicore.model;

import jakarta.persistence.Entity;

@Entity
public class OperationGroup extends SecuredBasic{

    private String externalId;

    public String getExternalId() {
        return externalId;
    }

    public <T extends OperationGroup> T setExternalId(String externalId) {
        this.externalId = externalId;
        return (T) this;
    }
}
