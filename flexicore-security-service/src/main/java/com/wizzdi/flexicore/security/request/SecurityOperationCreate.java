package com.wizzdi.flexicore.security.request;

import com.wizzdi.segmantix.model.Access;

public class SecurityOperationCreate extends BasicCreate {

    private Access defaultAccess;

    private String category;

    public Access getDefaultAccess() {
        return defaultAccess;
    }

    public <T extends SecurityOperationCreate> T setDefaultAccess(Access defaultAccess) {
        this.defaultAccess = defaultAccess;
        return (T) this;
    }

    public String getCategory() {
        return category;
    }

    public <T extends SecurityOperationCreate> T setCategory(String category) {
        this.category = category;
        return (T) this;
    }
}
