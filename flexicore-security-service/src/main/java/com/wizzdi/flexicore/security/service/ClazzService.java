package com.wizzdi.flexicore.security.service;

import com.flexicore.model.Clazz;
import com.flexicore.model.SecurityWildcard;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.security.data.ClazzRepository;
import com.wizzdi.flexicore.security.request.BasicPropertiesFilter;
import com.wizzdi.flexicore.security.request.ClazzFilter;
import com.wizzdi.flexicore.security.response.PaginationResponse;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Extension
@Component
public class ClazzService implements Plugin {

	@Autowired
	private ClazzRepository clazzRepository;



	public PaginationResponse<Clazz> getAllClazzs(ClazzFilter clazzFilter) {
		List<Clazz> list= listAllClazzs(clazzFilter );
		long count= clazzRepository.countAllClazzs(clazzFilter);
		return new PaginationResponse<>(list,clazzFilter,count);
	}

	public List<Clazz> listAllClazzs(ClazzFilter clazzFilter) {
		return clazzRepository.listAllClazzs(clazzFilter );
	}


	public Optional<Clazz> getClazz(Class<?> aClass) {
		return clazzRepository.listAllClazzs(new ClazzFilter().setBasicPropertiesFilter(new BasicPropertiesFilter().setNames(Set.of(aClass.getSimpleName())))).stream().findFirst();
	}
	public Clazz getWildcardClazz(){
		return getClazz(SecurityWildcard.class).orElse(null);
	}
}
