package com.wizzdi.flexicore.security.service;

import com.flexicore.model.SecurityEntity;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.security.data.SecurityEntityRepository;
import com.flexicore.security.SecurityContextBase;
import com.wizzdi.flexicore.security.request.SecurityEntityCreate;
import com.wizzdi.flexicore.security.request.SecurityEntityFilter;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Extension
public class SecurityEntityService implements Plugin {


	@Autowired
	private BaseclassService baseclassService;


	public boolean updateNoMerge(SecurityEntityCreate securityEntityCreate, SecurityEntity securityEntity){
		return baseclassService.updateBaseclassNoMerge(securityEntityCreate,securityEntity);
	}

	public void validate(SecurityEntityCreate securityEntityCreate, SecurityContextBase securityContext) {
		baseclassService.validate(securityEntityCreate,securityContext);
	}

	public void validate(SecurityEntityFilter securityEntityFilter, SecurityContextBase securityContext) {
	}
}
