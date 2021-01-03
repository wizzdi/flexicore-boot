/*******************************************************************************
 *  Copyright (C) FlexiCore, Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Avishay Ben Natan And Asaf Ben Natan, October 2015
 ******************************************************************************/
package com.flexicore.service.impl;

import com.flexicore.data.jsoncontainers.PaginationResponse;
import com.flexicore.interfaces.FlexiCoreService;
import com.flexicore.model.Baseclass;
import com.flexicore.model.Clazz;
import com.flexicore.model.User;
import com.flexicore.request.ClazzFilter;
import com.flexicore.security.SecurityContext;
import org.pf4j.Extension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import javax.inject.Named;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Named
@Primary
@Component
@Extension
public class ClazzService implements FlexiCoreService{
	private static final Logger logger = LoggerFactory.getLogger(ClazzService.class);


	public List<Clazz> getallClazz(User user) {
		// TODO: securiy stuff with user
		return Baseclass.getAllClazz();
	}

	public ClazzService() {
		// TODO Auto-generated constructor stub
	}

	public Clazz getclazz(String clazzName) {
		return Baseclass.getClazzByName(clazzName);
	}
	
	  public static List<Field> getInheritedFields(Class<?> type) {
	        List<Field> fields = new ArrayList<>();
	        for (Class<?> c = type; c != null; c = c.getSuperclass()) {
	            fields.addAll(Arrays.asList(c.getDeclaredFields()));
	        }
	        return fields;
	    }


	public PaginationResponse<Clazz> getAllClazz(ClazzFilter filter, SecurityContext securityContext) {
		List<Clazz> allClazz = Baseclass.getAllClazz();
		Stream<Clazz> clazzStream = allClazz.parallelStream();

		if(filter.getNameLike()!=null){
			String nameLike=filter.getNameLike().replace("%","");
			clazzStream=clazzStream.filter(f->f.getName()!=null && f.getName().contains(nameLike));
		}
		if(filter.getPageSize()!=null && filter.getCurrentPage()!=null && filter.getCurrentPage()> -1 && filter.getPageSize() > 0){
			clazzStream=clazzStream.skip(filter.getPageSize()*filter.getCurrentPage()).limit(filter.getPageSize());
		}
		return new PaginationResponse<>(clazzStream.collect(Collectors.toList()),filter,allClazz.size());
	}
}
