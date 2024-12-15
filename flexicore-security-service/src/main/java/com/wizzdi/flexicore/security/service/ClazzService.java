package com.wizzdi.flexicore.security.service;

import com.flexicore.annotations.rest.All;
import com.flexicore.model.Clazz;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.security.data.ClazzRepository;
import com.wizzdi.flexicore.security.request.ClazzFilter;
import com.wizzdi.flexicore.security.response.PaginationResponse;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Extension
@Component
public class ClazzService implements Plugin {

	@Autowired
	private ClazzRepository clazzRepository;

	public static String getClazzId(Class<?> c) {
		String string = c.getCanonicalName();
		return getIdFromString(string);
	}

	public static String getIdFromString(String string) {
		return UUID.nameUUIDFromBytes(string.getBytes()).toString();
	}


	public PaginationResponse<Clazz> getAllClazzs(ClazzFilter clazzFilter) {
		List<Clazz> list= listAllClazzs(clazzFilter );
		long count= clazzRepository.countAllClazzs(clazzFilter);
		return new PaginationResponse<>(list,clazzFilter,count);
	}

	public List<Clazz> listAllClazzs(ClazzFilter clazzFilter) {
		return clazzRepository.listAllClazzs(clazzFilter );
	}


}
