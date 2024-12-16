package com.wizzdi.flexicore.security.service;

import com.flexicore.model.OperationToClazz;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.security.data.OperationToClazzRepository;
import com.wizzdi.flexicore.security.request.OperationToClazzCreate;
import com.wizzdi.flexicore.security.request.OperationToClazzFilter;
import com.wizzdi.flexicore.security.response.PaginationResponse;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Extension
@Component
public class OperationToClazzService implements Plugin {


	@Autowired
	private OperationToClazzRepository operationToClazzRepository;


	public OperationToClazz addOperationToClazz(OperationToClazzCreate operationToClazzCreate){
		OperationToClazz operationToClazz=new OperationToClazz(operationToClazzCreate.getSecurityOperation(),operationToClazzCreate.getType());
		operationToClazzRepository.addOperationToClazz(operationToClazz);
		return operationToClazz;
	}



	public PaginationResponse<OperationToClazz> getAllOperationToClazz(OperationToClazzFilter operationToClazzFilter ) {
		List<OperationToClazz> list= listAllOperationToClazz(operationToClazzFilter);
		long count=operationToClazzRepository.countAllOperationToClazzs(operationToClazzFilter);
		return new PaginationResponse<>(list,operationToClazzFilter,count);
	}

	public List<OperationToClazz> listAllOperationToClazz(OperationToClazzFilter operationToClazzFilter) {
		return operationToClazzRepository.listAllOperationToClazzs(operationToClazzFilter );
	}
}
