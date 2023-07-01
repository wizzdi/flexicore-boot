package com.wizzdi.flexicore.security.interfaces;

import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.security.response.OperationScanContext;

import java.util.List;

public interface StandardOperationScanner extends Plugin {

	List<OperationScanContext> getStandardOperations();
}
