package com.wizzdi.flexicore.security.service;

import com.flexicore.model.Basic;
import com.flexicore.security.SecurityContextBase;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.security.request.BasicCreate;
import org.pf4j.Extension;
import org.springframework.stereotype.Component;

@Component
@Extension
public class BasicService implements Plugin {



	public boolean updateBasicNoMerge(BasicCreate basicCreate, Basic basic) {
		boolean update = false;

		if (basicCreate.getUpdateDate() != null && (!basicCreate.getUpdateDate().equals(basic.getUpdateDate()))) {
			basic.setUpdateDate(basicCreate.getUpdateDate());
			update = true;
		}

		if (basicCreate.getName() != null && (!basicCreate.getName().equals(basic.getName()))) {
			basic.setName(basicCreate.getName());
			update = true;
		}

		if (basicCreate.getDescription() != null && (!basicCreate.getDescription().equals(basic.getDescription()))) {
			basic.setDescription(basicCreate.getDescription());
			update = true;
		}
		if (basicCreate.getSoftDelete() != null && (!basicCreate.getSoftDelete().equals(basic.isSoftDelete()))) {
			basic.setSoftDelete(basicCreate.getSoftDelete());
			update = true;
		}

		return update;
	}

	public void validate(BasicCreate basicCreate, SecurityContextBase securityContext) {

	}


}
