package com.wizzdi.flexicore.boot.dynamic.invokers.interfaces;

import com.wizzdi.flexicore.boot.dynamic.invokers.response.ParameterInfo;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;

public interface InvokerParameterScanner {

	ParameterInfo scan(Parameter parameter, List<Field> allFields, Field field);
}
