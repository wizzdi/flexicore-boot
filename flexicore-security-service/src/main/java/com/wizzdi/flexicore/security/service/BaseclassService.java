package com.wizzdi.flexicore.security.service;

import com.flexicore.model.Baseclass;
import com.flexicore.model.SecuredBasic;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.flexicore.security.SecurityContextBase;
import com.wizzdi.flexicore.security.data.BaseclassRepository;
import com.wizzdi.flexicore.security.request.BaseclassCreate;
import com.wizzdi.flexicore.security.request.BaseclassFilter;
import com.wizzdi.flexicore.security.response.PaginationResponse;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Extension
public class BaseclassService implements Plugin {

	@Autowired
	private BasicService basicService;
	@Autowired
	private BaseclassRepository baseclassRepository;


	public boolean updateBaseclassNoMerge(BaseclassCreate baseclassCreate, Baseclass baseclass) {
		boolean update = basicService.updateBasicNoMerge(baseclassCreate,baseclass);

		if (baseclassCreate.getSystemObject() != null && (!baseclassCreate.getSystemObject().equals(baseclass.isSystemObject()))) {
			baseclass.setSystemObject(baseclassCreate.getSystemObject());
			update = true;
		}

		return update;
	}

	public PaginationResponse<Baseclass> getAllBaseclass(BaseclassFilter baseclasssFilter , SecurityContextBase securityContext) {
		List<Baseclass> baseclasses = listAllBaseclass(baseclasssFilter, securityContext);
		long count = baseclassRepository.countAllBaseclass(baseclasssFilter,securityContext);
		return new PaginationResponse<>(baseclasses, baseclasssFilter, count);
	}

	public List<Baseclass> listAllBaseclass(BaseclassFilter baseclasssFilter , SecurityContextBase securityContext) {
		return baseclassRepository.listAllBaseclass(baseclasssFilter,securityContext);
	}

	@Deprecated
	public void validate(BaseclassCreate baseclassCreate, SecurityContextBase securityContext) {
		basicService.validate(baseclassCreate,securityContext);
	}


	public static <T extends SecuredBasic> Baseclass createSecurityObjectNoMerge(T subject, SecurityContextBase securityContextBase) {
		Baseclass security=new Baseclass(subject.getName(),subject.getClass(),securityContextBase);
		subject.setSecurity(security);
		return security;
	}


}
