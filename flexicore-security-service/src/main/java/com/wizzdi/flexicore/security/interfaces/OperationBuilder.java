package com.wizzdi.flexicore.security.interfaces;

import com.flexicore.model.Clazz;
import com.flexicore.model.OperationToClazz;
import com.flexicore.model.SecurityOperation;
import com.wizzdi.flexicore.security.response.OperationScanContext;

import java.util.List;
import java.util.Map;

public interface OperationBuilder {

	SecurityOperation upsertOperationNoMerge(OperationScanContext e, Map<String, SecurityOperation> securityOperationMap, Map<String, Map<String, OperationToClazz>> relatedClazzes, List<Object> toMerge, Map<String, Clazz> clazzes);

}
