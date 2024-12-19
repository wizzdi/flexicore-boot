package com.wizzdi.flexicore.security.service;

import com.flexicore.model.Baseclass;
import com.wizzdi.flexicore.security.configuration.SecurityContext;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.segmantix.service.SecurityRepository;
import com.wizzdi.flexicore.security.request.BaseclassCreate;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Extension
public class BaseclassService implements Plugin {

	@Autowired
	private BasicService basicService;
	@Autowired
	private SecurityRepository securityRepository;


	public boolean updateBaseclassNoMerge(BaseclassCreate baseclassCreate, Baseclass baseclass) {
		boolean update = basicService.updateBasicNoMerge(baseclassCreate,baseclass);


		return update;
	}



	public static <T extends Baseclass> Baseclass createSecurityObjectNoMerge(T subject, SecurityContext securityContext) {
		subject.setSecurityId(subject.getId());
		if(securityContext==null){
			return subject;
		}
		subject.setCreator(securityContext.getUser());
		subject.setTenant(securityContext.getTenantToCreateIn());
		//TODO:clazz?
		return subject;
	}


}
