package com.wizzdi.flexicore.security.service;

import com.flexicore.model.Baseclass;
import com.flexicore.model.Basic;
import com.flexicore.model.SecuredBasic;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.flexicore.security.SecurityContextBase;
import com.wizzdi.flexicore.security.request.BaseclassCreate;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;

@Component
@Extension
public class BaseclassService implements Plugin {

	@Autowired
	private BasicService basicService;


	public boolean updateBaseclassNoMerge(BaseclassCreate baseclassCreate, Baseclass baseclass) {
		boolean update = basicService.updateBasicNoMerge(baseclassCreate,baseclass);

		if (baseclassCreate.getSystemObject() != null && (!baseclassCreate.getSystemObject().equals(baseclass.isSystemObject()))) {
			baseclass.setSystemObject(baseclassCreate.getSystemObject());
			update = true;
		}

		return update;
	}

	public void validate(BaseclassCreate baseclassCreate, SecurityContextBase securityContext) {
		basicService.validate(baseclassCreate,securityContext);
	}


	public static <T extends SecuredBasic> Baseclass createSecurityObjectNoMerge(T subject, SecurityContextBase securityContextBase) {
		Baseclass security=new Baseclass(subject.getName(),subject.getClass(),securityContextBase);
		subject.setSecurity(security);
		return security;
	}


}
