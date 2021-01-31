package com.flexicore.config;

import com.flexicore.annotations.IOperation;
import com.flexicore.service.impl.OperationService;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.boot.dynamic.invokers.interfaces.InvokerMethodScanner;
import com.wizzdi.flexicore.boot.dynamic.invokers.response.InvokerMethodInfo;
import io.swagger.v3.oas.annotations.Operation;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.reflect.Method;

import static com.wizzdi.flexicore.boot.dynamic.invokers.utils.InvokerUtils.getIOOperation;
import static com.wizzdi.flexicore.boot.dynamic.invokers.utils.InvokerUtils.getInvokerMethodInfo;

@Extension
@Configuration
public class CustomInvokerMethodScanner implements InvokerMethodScanner, Plugin {

	@Autowired
	private OperationService operationService;

	@Override
	public InvokerMethodInfo scan(Class<?> invokerClass, Method method) {
		com.wizzdi.flexicore.boot.dynamic.invokers.annotations.InvokerMethodInfo InvokerMethodInfo = AnnotatedElementUtils.findMergedAnnotation(method, com.wizzdi.flexicore.boot.dynamic.invokers.annotations.InvokerMethodInfo.class);
		if (InvokerMethodInfo != null) {
			return new InvokerMethodInfo(method, InvokerMethodInfo);
		}
		else{
			RequestMapping requestMapping = AnnotatedElementUtils.findMergedAnnotation(method, RequestMapping.class);
			IOperation iOperation=AnnotatedElementUtils.findMergedAnnotation(method,IOperation.class);
			Operation operation=AnnotatedElementUtils.findMergedAnnotation(method,Operation.class);
			if(requestMapping!=null||iOperation!=null||operation!=null){
				if(iOperation==null){
					iOperation=operation!=null?operationService.getIOperationFromApiOperation(operation,method):getIOOperation(method);
				}
				com.wizzdi.flexicore.boot.dynamic.invokers.annotations.InvokerMethodInfo invokerMethodInfo=getInvokerMethodInfo(invokerClass, method,iOperation);
				return new InvokerMethodInfo(method, invokerMethodInfo);
			}

		}
		return null;
	}
}
