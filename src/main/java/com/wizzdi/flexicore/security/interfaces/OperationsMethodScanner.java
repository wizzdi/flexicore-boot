package com.wizzdi.flexicore.security.interfaces;

import com.wizzdi.flexicore.security.response.OperationScanContext;

import java.lang.reflect.Method;

public interface OperationsMethodScanner {

	OperationScanContext scanOperationOnMethod(Method method);


}
