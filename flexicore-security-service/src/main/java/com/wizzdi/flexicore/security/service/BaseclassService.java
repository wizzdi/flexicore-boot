package com.wizzdi.flexicore.security.service;

import com.flexicore.model.Baseclass;
import com.flexicore.model.SecurityTenant;
import com.flexicore.model.SecurityUser;
import com.wizzdi.segmantix.model.SecurityContext;
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
		subject.setCreator((SecurityUser) securityContext.user());
		subject.setTenant((SecurityTenant) securityContext.getTenantToCreateIn());
		//TODO:clazz?
		return subject;
	}


}
