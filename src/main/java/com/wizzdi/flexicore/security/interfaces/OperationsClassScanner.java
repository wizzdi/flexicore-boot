package com.wizzdi.flexicore.security.interfaces;

import com.wizzdi.flexicore.security.response.OperationScanContext;

import java.util.List;

public interface OperationsClassScanner {

	List<? extends OperationScanContext> scan(Class<?> c);


}
