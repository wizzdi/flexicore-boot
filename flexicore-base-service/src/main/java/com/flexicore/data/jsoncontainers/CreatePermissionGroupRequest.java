package com.flexicore.data.jsoncontainers;

public class CreatePermissionGroupRequest {

    private String name;
    private String description;
    private String externalId;


    public String getName() {
        return name;
    }

    public CreatePermissionGroupRequest setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public CreatePermissionGroupRequest setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getExternalId() {
        return externalId;
    }

    public <T extends CreatePermissionGroupRequest> T setExternalId(String externalId) {
        this.externalId = externalId;
        return (T) this;
    }
}
