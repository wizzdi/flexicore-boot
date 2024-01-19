package com.wizzdi.flexicore.security.service;

import com.wizzdi.flexicore.security.response.OperationGroupContext;
import com.wizzdi.flexicore.security.response.Operations;

public interface OperationGroupProvider {

    OperationGroupContext getOperationGroupContext(Operations operations);
}
