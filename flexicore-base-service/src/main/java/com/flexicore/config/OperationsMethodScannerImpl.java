package com.flexicore.config;

import com.flexicore.annotations.IOperation;
import com.flexicore.model.Baseclass;
import com.flexicore.request.OperationCreate;
import com.flexicore.service.impl.OperationService;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.security.interfaces.OperationsMethodScanner;
import com.wizzdi.flexicore.security.response.OperationScanContext;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotatedElementUtils;

import java.lang.reflect.Method;

@Extension
@Configuration
public class OperationsMethodScannerImpl implements OperationsMethodScanner, Plugin {

	@Autowired
	private OperationService operationService;

	@Override
	public OperationScanContext scanOperationOnMethod(Method method) {
		IOperation ioperation = AnnotatedElementUtils.findMergedAnnotation(method,IOperation.class);

		if (ioperation == null) {
			io.swagger.v3.oas.annotations.Operation apiOperation = AnnotatedElementUtils.findMergedAnnotation(method,io.swagger.v3.oas.annotations.Operation.class);
			if (apiOperation != null) {
				ioperation = operationService.getIOperationFromApiOperation(apiOperation, method);
			}
		}

		if (ioperation != null) {
			Class<? extends Baseclass>[] relatedClasses = ioperation.relatedClazzes();
			if (relatedClasses.length == 0 && method.getReturnType() != null && Baseclass.class.isAssignableFrom(method.getReturnType())) {
				relatedClasses =  (Class<? extends Baseclass>[]) new Class<?>[]{method.getReturnType()};
			}
			String id = Baseclass.generateUUIDFromString(method.toString());
			return new OperationScanContext(new OperationCreate()
					.setDefaultAccess(ioperation.access())
					.setSystemObject(true)
					.setDescription(ioperation.Description())
					.setName(ioperation.Name())
					.setIdForCreate(id),relatedClasses);
		}
		return null;
	}
}
