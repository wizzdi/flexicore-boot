package com.flexicore.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wizzdi.flexicore.file.model.FileResource;

public class TenantCreate extends BaseclassCreate {
    private UserCreate tenantAdmin;
    private String iconId;
    @JsonIgnore
    private FileResource icon;

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
