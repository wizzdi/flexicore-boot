package com.wizzdi.flexicore.security.data;

import com.flexicore.model.*;
import com.wizzdi.flexicore.security.configuration.SecurityContext;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.security.request.BasicPropertiesFilter;
import com.wizzdi.flexicore.security.request.OperationToClazzFilter;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@Extension
public class OperationToClazzRepository implements Plugin {

	private Map<String,OperationToClazz> operationToClazzMap=new ConcurrentHashMap<>();

	public List<OperationToClazz> listAllOperationToClazzs(OperationToClazzFilter operationToClazzFilter ) {
		return streamOperationToClazz(operationToClazzFilter).sorted(Comparator.comparing((OperationToClazz f)->f.operation().name()).thenComparing((OperationToClazz f)->f.clazz().name())).toList();

	}

	private Stream<OperationToClazz> streamOperationToClazz(OperationToClazzFilter operationToClazzFilter) {
		return operationToClazzMap.values().stream().filter(f->filter(f,operationToClazzFilter));
	}

	private boolean filter(OperationToClazz operationToClazz, OperationToClazzFilter operationToClazzFilter) {

		boolean pass=true;
		List<Clazz> clazzes = operationToClazzFilter.getClazzes();
		if(clazzes !=null&&!clazzes.isEmpty()){
			pass=pass&&clazzes.contains(operationToClazz.clazz());
		}
		List<SecurityOperation> securityOperations = operationToClazzFilter.getSecurityOperations();
		if(securityOperations!=null&&!securityOperations.isEmpty()){
			pass=pass&&securityOperations.contains(operationToClazz.operation());
		}

		return pass;
	}




	public long countAllOperationToClazzs(OperationToClazzFilter operationToClazzFilter) {
		return streamOperationToClazz(operationToClazzFilter).count();

	}

	public void addOperationToClazz(OperationToClazz operationToClazz) {

		operationToClazzMap.put(operationToClazz.clazz().name()+"_"+operationToClazz.operation().getId(),operationToClazz);
	}
}
