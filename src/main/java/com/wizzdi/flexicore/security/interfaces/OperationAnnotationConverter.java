package com.wizzdi.flexicore.security.interfaces;

import com.flexicore.annotations.IOperation;

import java.lang.reflect.Method;

public interface OperationAnnotationConverter {

    IOperation getIOperation(Method t);
}
