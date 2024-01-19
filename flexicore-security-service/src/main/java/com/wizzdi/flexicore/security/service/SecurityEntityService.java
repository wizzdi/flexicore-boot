package com.wizzdi.flexicore.security.service;

import com.flexicore.model.SecurityEntity;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.flexicore.security.SecurityContextBase;
import com.wizzdi.flexicore.security.request.SecurityEntityCreate;
import com.wizzdi.flexicore.security.request.SecurityEntityFilter;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Extension
public class SecurityEntityService implements Plugin {


	@Autowired
	private BasicService basicService;


	public boolean updateNoMerge(SecurityEntityCreate securityEntityCreate, SecurityEntity securityEntity){
		return basicService.updateBasicNoMerge(securityEntityCreate,securityEntity);
	}

}
