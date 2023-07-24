package com.wizzdi.flexicore.security.request;

public class PermissionGroupCreate extends BasicCreate {


    private String externalId;

    public String getExternalId() {
        return externalId;
    }

    public <T extends PermissionGroupCreate> T setExternalId(String externalId) {
        this.externalId = externalId;
        return (T) this;
    }

}
