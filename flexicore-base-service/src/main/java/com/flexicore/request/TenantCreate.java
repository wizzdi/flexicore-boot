package com.flexicore.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wizzdi.flexicore.file.model.FileResource;
import com.wizzdi.flexicore.security.request.SecurityTenantCreate;

public class TenantCreate extends SecurityTenantCreate {
    private UserCreate tenantAdmin;
    private String iconId;
    @JsonIgnore
    private FileResource icon;

    public TenantCreate(SecurityTenantCreate other) {
        super(other);
    }

    public TenantCreate() {
    }

    public UserCreate getTenantAdmin() {
        return tenantAdmin;
    }

    public TenantCreate setTenantAdmin(UserCreate tenantAdmin) {
        this.tenantAdmin = tenantAdmin;
        return this;
    }


    public String getIconId() {
        return iconId;
    }

    public <T extends TenantCreate> T setIconId(String iconId) {
        this.iconId = iconId;
        return (T) this;
    }

    @JsonIgnore
    public FileResource getIcon() {
        return icon;
    }

    public <T extends TenantCreate> T setIcon(FileResource icon) {
        this.icon = icon;
        return (T) this;
    }
}
