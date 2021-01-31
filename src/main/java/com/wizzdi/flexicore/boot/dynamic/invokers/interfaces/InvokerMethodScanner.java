package com.wizzdi.flexicore.boot.dynamic.invokers.interfaces;

import com.wizzdi.flexicore.boot.dynamic.invokers.response.InvokerMethodInfo;

import java.lang.reflect.Method;

public interface InvokerMethodScanner {

	InvokerMethodInfo scan(Class<?> c,Method method);
}
