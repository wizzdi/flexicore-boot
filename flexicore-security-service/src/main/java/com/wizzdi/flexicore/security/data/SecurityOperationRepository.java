package com.wizzdi.flexicore.security.data;

import com.flexicore.model.*;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.security.request.SecurityOperationFilter;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;
import com.wizzdi.flexicore.security.request.BasicPropertiesFilter;

@Component
@Extension
public class SecurityOperationRepository implements Plugin {

	private final Map<String,SecurityOperation> securityOperations=new ConcurrentHashMap<>();


	public List<SecurityOperation> listAllOperations(SecurityOperationFilter securityOperationFilter ) {
		return streamSecurityOperation(securityOperationFilter).sorted(Comparator.comparing(f->f.name())).toList();

	}

	private Stream<SecurityOperation> streamSecurityOperation(SecurityOperationFilter securityOperationFilter) {
		return securityOperations.values().stream().filter(f-> filterBasic(f,securityOperationFilter.getBasicPropertiesFilter()));
	}

	private boolean filterBasic(SecurityOperation securityOperation, BasicPropertiesFilter basicPropertiesFilter) {
		if(basicPropertiesFilter==null){
			return true;
		}
		boolean pass=true;
		if(basicPropertiesFilter.getNameLike()!=null){
			String like = basicPropertiesFilter.getNameLike().replace("%", "");
			if(basicPropertiesFilter.isNameLikeCaseSensitive()){
				pass=pass&&securityOperation.name().contains(like);
			}
			else{

				pass=pass&&securityOperation.name().toLowerCase().contains(like.toLowerCase());
			}
		}
		if(basicPropertiesFilter.getNameLike()!=null){
			pass=pass&&basicPropertiesFilter.getNameLike().contains(securityOperation.name());
		}
		return pass;

	}



	public long countAllOperations(SecurityOperationFilter securityOperationFilter) {
		return streamSecurityOperation(securityOperationFilter).count();

	}

	public SecurityOperation addOperation(SecurityOperation securityOperation) {
		securityOperations.put(securityOperation.id(),securityOperation);
		return securityOperation;
	}
}
