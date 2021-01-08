package com.wizzdi.flexicore.security.service;

import com.flexicore.model.Baseclass;
import com.flexicore.model.Baselink;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.security.data.BaselinkRepository;
import com.flexicore.security.SecurityContext;
import com.wizzdi.flexicore.security.request.BaselinkCreate;
import com.wizzdi.flexicore.security.request.BaselinkFilter;
import com.wizzdi.flexicore.security.request.BaselinkUpdate;
import com.wizzdi.flexicore.security.response.PaginationResponse;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Extension
@Component
public class BaselinkService implements Plugin {

	@Autowired
	private BaseclassService baseclassService;
	@Autowired
	private BaselinkRepository baselinkRepository;


	public Baselink createBaselink(BaselinkCreate baselinkCreate, SecurityContext securityContext){
		Baselink baselink= createBaselinkNoMerge(baselinkCreate,securityContext);
		baselinkRepository.merge(baselink);
		return baselink;
	}


	public Baselink createBaselinkNoMerge(BaselinkCreate baselinkCreate, SecurityContext securityContext){
		Baselink baselink=new Baselink(baselinkCreate.getName(),securityContext);
		updateBaselinkNoMerge(baselinkCreate,baselink);
		baselinkRepository.merge(baselink);
		return baselink;
	}

	public boolean updateBaselinkNoMerge(BaselinkCreate baselinkCreate, Baselink baselink) {
		return baseclassService.updateBaseclassNoMerge(baselinkCreate,baselink);
	}

	public Baselink updateBaselink(BaselinkUpdate baselinkUpdate, SecurityContext securityContext){
		Baselink baselink=baselinkUpdate.getBaselink();
		if(updateBaselinkNoMerge(baselinkUpdate,baselink)){
			baselinkRepository.merge(baselink);
		}
		return baselink;
	}

	public void validate(BaselinkCreate baselinkCreate, SecurityContext securityContext) {
		baseclassService.validate(baselinkCreate,securityContext);
	}

	public void validate(BaselinkFilter baselinkFilter, SecurityContext securityContext) {
	}

	public <T extends Baseclass> T getByIdOrNull(String id,Class<T> c, SecurityContext securityContext) {
		return baselinkRepository.getByIdOrNull(id,c,securityContext);
	}

	public PaginationResponse<Baselink> getAllBaselinks(BaselinkFilter baselinkFilter, SecurityContext securityContext) {
		List<Baselink> list= listAllBaselinks(baselinkFilter, securityContext);
		long count=baselinkRepository.countAllBaselinks(baselinkFilter,securityContext);
		return new PaginationResponse<>(list,baselinkFilter,count);
	}

	public List<Baselink> listAllBaselinks(BaselinkFilter baselinkFilter, SecurityContext securityContext) {
		return baselinkRepository.listAllBaselinks(baselinkFilter, securityContext);
	}
}
