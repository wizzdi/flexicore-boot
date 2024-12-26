package com.wizzdi.flexicore.security.response;

import com.flexicore.model.OperationGroup;
import com.flexicore.model.SecurityOperation;

public record OperationToGroupContainer(String id,OperationGroup operationGroup, SecurityOperation securityOperation) {
}
