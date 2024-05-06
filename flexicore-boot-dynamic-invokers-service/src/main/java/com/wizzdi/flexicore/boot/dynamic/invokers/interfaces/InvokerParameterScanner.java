package com.wizzdi.flexicore.boot.dynamic.invokers.interfaces;

import com.wizzdi.flexicore.boot.dynamic.invokers.response.ParameterInfo;

import java.lang.reflect.Field;
import java.util.List;

public interface InvokerParameterScanner {

	ParameterInfo scan(Class<?> type, List<Field> brotherFields, Field handledField);
}
