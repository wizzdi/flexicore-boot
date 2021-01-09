package com.wizzdi.flexicore.security.service;

import com.flexicore.model.Baseclass;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.flexicore.security.SecurityContextBase;
import com.wizzdi.flexicore.security.request.BaseclassCreate;
import org.pf4j.Extension;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;

@Component
@Extension
public class BaseclassService implements Plugin {



	public boolean updateBaseclassNoMerge(BaseclassCreate baseclassCreate, Baseclass baseclass) {
		boolean update = false;

		if (baseclassCreate.getUpdateDate() != null && (!baseclassCreate.getUpdateDate().equals(baseclass.getUpdateDate()))) {
			baseclass.setUpdateDate(baseclassCreate.getUpdateDate());
			update = true;
		}

		if (baseclassCreate.getName() != null && (!baseclassCreate.getName().equals(baseclass.getName()))) {
			baseclass.setName(baseclassCreate.getName());
			update = true;
		}

		if (baseclassCreate.getDescription() != null && (!baseclassCreate.getDescription().equals(baseclass.getDescription()))) {
			baseclass.setDescription(baseclassCreate.getDescription());
			update = true;
		}

		return update;
	}

	public void validate(BaseclassCreate baseclassCreate, SecurityContextBase securityContext) {

	}


}
