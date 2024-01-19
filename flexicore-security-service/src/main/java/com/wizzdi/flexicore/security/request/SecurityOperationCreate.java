package com.wizzdi.flexicore.security.request;

import com.flexicore.annotations.IOperation;

public class SecurityOperationCreate extends BasicCreate {

    private IOperation.Access defaultAccess;

    private String category;

    public IOperation.Access getDefaultAccess() {
        return defaultAccess;
    }

    public <T extends SecurityOperationCreate> T setDefaultAccess(IOperation.Access defaultAccess) {
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
