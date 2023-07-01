package com.flexicore.config;

import com.flexicore.model.*;
import com.flexicore.request.OperationCreate;
import com.flexicore.security.SecurityContext;
import com.flexicore.security.SecurityContextBase;
import com.flexicore.service.impl.OperationService;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.security.interfaces.OperationBuilder;
import com.wizzdi.flexicore.security.request.OperationToClazzCreate;
import com.wizzdi.flexicore.security.response.OperationScanContext;
import com.wizzdi.flexicore.security.service.OperationToClazzService;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Extension
@Configuration
public class OperationBuilderImpl implements OperationBuilder, Plugin {

	@Autowired
	private OperationService operationService;
	@Autowired
	private OperationToClazzService operationToClazzService;

	@Override
	public SecurityOperation upsertOperationNoMerge(OperationScanContext e, Map<String, SecurityOperation> securityOperationMap, Map<String, Map<String, OperationToClazz>> relatedClazzes, List<Object> toMerge, Map<String, Clazz> clazzes, SecurityContextBase<?, ?, ?, ?> securityContextBase) {
		return createOperation(e,securityOperationMap,relatedClazzes,toMerge,clazzes,(SecurityContext) securityContextBase);
	}


	private SecurityOperation createOperation(OperationScanContext operationScanContext, Map<String, SecurityOperation> existing, Map<String, Map<String, OperationToClazz>> relatedClazzes, List<Object> toMerge, Map<String, Clazz> clazzes, SecurityContext securityContextBase) {
		OperationCreate securityOperationCreate =(OperationCreate) operationScanContext.getSecurityOperationCreate();
		Operation securityOperation= (Operation) existing.get(securityOperationCreate.getIdForCreate());
		if(securityOperation==null){
			securityOperation=operationService.createOperationNoMerge(securityOperationCreate,securityContextBase);
			securityOperation.setId(securityOperationCreate.getIdForCreate());
			existing.put(securityOperation.getId(),securityOperation);
			operationService.merge(securityOperation);
		}
		else{
			if(operationService.updateOperationNoMerge(securityOperationCreate,securityOperation)){
				operationService.merge(securityOperation);
			}
		}
		Class<?>[] relatedClasses = operationScanContext.getRelatedClasses();
		if(relatedClasses !=null){
			for (Class<?> relatedClass : relatedClasses) {
				String clazzId = Baseclass.generateUUIDFromString(relatedClass.getCanonicalName());
				Clazz clazz=clazzes.get(clazzId);
				Map<String,OperationToClazz> operationClazzes=relatedClazzes.computeIfAbsent(securityOperation.getId(),f->new HashMap<>());
				OperationToClazz existingOperationToClazz=operationClazzes.get(clazzId);
				if(existingOperationToClazz==null){
					OperationToClazzCreate operationToClazzCreate=new OperationToClazzCreate()
							.setClazz(clazz)
							.setSecurityOperation(securityOperation);
					existingOperationToClazz = operationToClazzService.createOperationToClazzNoMerge(operationToClazzCreate, securityContextBase);
					operationToClazzService.merge(existingOperationToClazz);
					operationClazzes.put(clazzId, existingOperationToClazz);
				}
			}
		}
		return securityOperation;
	}
}
