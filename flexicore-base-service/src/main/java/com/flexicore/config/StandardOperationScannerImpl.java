package com.flexicore.config;

import com.flexicore.annotations.IOperation;
import com.flexicore.annotations.rest.*;
import com.flexicore.model.Baseclass;
import com.flexicore.request.OperationCreate;
import com.wizzdi.flexicore.security.interfaces.StandardOperationScanner;
import com.wizzdi.flexicore.security.response.OperationScanContext;
import org.pf4j.Extension;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
@Extension
public class StandardOperationScannerImpl implements StandardOperationScanner {


	private OperationScanContext standardAccess(Class<?> standardAccess) {
		IOperation ioperation=standardAccess.getDeclaredAnnotation(IOperation.class);
		return new OperationScanContext(new OperationCreate()
				.setDefaultAccess(ioperation.access())
				.setSystemObject(true)
				.setDescription(ioperation.Description())
				.setName(ioperation.Name())
				.setIdForCreate(Baseclass.generateUUIDFromString(standardAccess.getCanonicalName()))
				,null);
	}

	@Override
	public List<OperationScanContext> getStandardOperations() {
		return Arrays.asList(Delete.class, Read.class, Update.class, Write.class, All.class).stream().map(this::standardAccess).collect(Collectors.toList());
	}
}
