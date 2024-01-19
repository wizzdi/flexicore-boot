package com.wizzdi.flexicore.security.response;

import com.flexicore.model.SecurityOperation;
import com.wizzdi.flexicore.security.request.OperationGroupCreate;

import java.util.List;

public record OperationGroupContext(OperationGroupCreate operationGroupCreate,List<SecurityOperation> operations) {
}
