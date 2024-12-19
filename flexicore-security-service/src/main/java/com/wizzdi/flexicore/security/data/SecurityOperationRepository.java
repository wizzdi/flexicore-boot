package com.wizzdi.flexicore.security.data;

import com.flexicore.model.*;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.security.request.SecurityOperationFilter;
import com.wizzdi.flexicore.security.response.Operations;
import org.pf4j.Extension;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import com.wizzdi.flexicore.security.request.BasicPropertiesFilter;

@Component
@Extension
public class SecurityOperationRepository implements Plugin {

	private final Map<String,SecurityOperation> securityOperations=new ConcurrentHashMap<>();

	public SecurityOperationRepository( Operations operations) {
		this.securityOperations.putAll(operations.getOperations().stream().collect(Collectors.toMap(f->f.id(),f->f)));
	}



	public List<SecurityOperation> listAllOperations(SecurityOperationFilter securityOperationFilter ) {
		return streamSecurityOperation(securityOperationFilter).sorted(Comparator.comparing(f->f.name())).collect(Collectors.toList());

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
		if(basicPropertiesFilter.getOnlyIds()!=null&&!basicPropertiesFilter.getOnlyIds().isEmpty()){
			pass=pass&&basicPropertiesFilter.getOnlyIds().contains(securityOperation.id());
		}

		if(basicPropertiesFilter.getNames()!=null&&!basicPropertiesFilter.getNames().isEmpty()){
			pass=pass&&basicPropertiesFilter.getNames().contains(securityOperation.name());
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
