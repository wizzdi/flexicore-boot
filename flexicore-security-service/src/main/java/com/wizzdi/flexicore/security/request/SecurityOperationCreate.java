package com.wizzdi.flexicore.security.request;

import com.flexicore.annotations.IOperation;

public class SecurityOperationCreate extends BaseclassCreate {

    private IOperation.Access defaultaccess;

    public IOperation.Access getDefaultaccess() {
        return defaultaccess;
    }

    public <T extends SecurityOperationCreate> T setDefaultaccess(IOperation.Access defaultaccess) {
        this.defaultaccess = defaultaccess;
        return (T) this;
    }
}
