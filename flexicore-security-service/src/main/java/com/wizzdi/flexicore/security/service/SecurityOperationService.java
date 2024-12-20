package com.wizzdi.flexicore.security.service;

import com.flexicore.annotations.rest.All;
import com.flexicore.model.Clazz;
import com.flexicore.model.SecurityOperation;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.security.data.SecurityOperationRepository;
import com.wizzdi.flexicore.security.request.BasicPropertiesFilter;
import com.wizzdi.flexicore.security.request.SecurityOperationCreate;
import com.wizzdi.flexicore.security.request.SecurityOperationFilter;
import com.wizzdi.flexicore.security.response.PaginationResponse;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Extension
@Component
public class SecurityOperationService implements Plugin {


	@Autowired
	private SecurityOperationRepository operationRepository;







	public SecurityOperation addOperation(SecurityOperationCreate securityOperationCreate){
		SecurityOperation securityOperation= getSecurityOperation(securityOperationCreate);
		return operationRepository.addOperation(securityOperation);
	}

	public static SecurityOperation getSecurityOperation(SecurityOperationCreate securityOperationCreate) {
		return new SecurityOperation(securityOperationCreate.getIdForCreate(), securityOperationCreate.getName(), securityOperationCreate.getDescription(), securityOperationCreate.getDefaultAccess(), securityOperationCreate.getCategory());
	}


	public PaginationResponse<SecurityOperation> getAllOperations(SecurityOperationFilter operationFilter) {
		List<SecurityOperation> list= listAllOperations(operationFilter);
		long count=operationRepository.countAllOperations(operationFilter);
		return new PaginationResponse<>(list,operationFilter,count);
	}

	public List<SecurityOperation> listAllOperations(SecurityOperationFilter operationFilter) {
		return operationRepository.listAllOperations(operationFilter );
	}

	public SecurityOperation getByIdOrNull(String operationId) {
		return operationRepository.listAllOperations(new SecurityOperationFilter().setBasicPropertiesFilter(new BasicPropertiesFilter().setOnlyIds(Set.of(operationId)))).stream().findFirst().orElse(null);
	}
	public SecurityOperation getAllOperations(){
		return getByIdOrNull(getStandardAccessId(All.class));
	}
	public SecurityOperation getOperation(Method method){
		return getByIdOrNull(generateUUIDFromStringCompt(method.toString()));
	}
	public List<SecurityOperation> findByIds(Set<String> ids){
		return operationRepository.listAllOperations(new SecurityOperationFilter().setBasicPropertiesFilter(new BasicPropertiesFilter().setOnlyIds(ids)));
	}
	public static String getStandardAccessId(Class<?> c){
	 return  generateUUIDFromStringCompt(c.getCanonicalName());
	}
	public static String generateUUIDFromStringCompt(String input) {

		return UUID.nameUUIDFromBytes(input.getBytes()).toString()
				.replaceAll("-", "")
				.substring(0, 22);

	}


}
