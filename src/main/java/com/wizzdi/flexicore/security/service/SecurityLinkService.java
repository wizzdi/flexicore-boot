package com.wizzdi.flexicore.security.service;

import com.flexicore.model.Baseclass;
import com.flexicore.model.SecurityLink;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.security.data.SecurityLinkRepository;
import com.flexicore.security.SecurityContext;
import com.wizzdi.flexicore.security.request.SecurityLinkCreate;
import com.wizzdi.flexicore.security.request.SecurityLinkFilter;
import com.wizzdi.flexicore.security.request.SecurityLinkUpdate;
import com.wizzdi.flexicore.security.response.PaginationResponse;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Extension
@Component
public class SecurityLinkService implements Plugin {

	@Autowired
	private BaselinkService baselinkService;
	@Autowired
	private SecurityLinkRepository securityLinkRepository;


	public SecurityLink createSecurityLink(SecurityLinkCreate securityLinkCreate, SecurityContext securityContext){
		SecurityLink securityLink= createSecurityLinkNoMerge(securityLinkCreate,securityContext);
		securityLinkRepository.merge(securityLink);
		return securityLink;
	}


	public SecurityLink createSecurityLinkNoMerge(SecurityLinkCreate securityLinkCreate, SecurityContext securityContext){
		SecurityLink securityLink=new SecurityLink(securityLinkCreate.getName(),securityContext);
		updateSecurityLinkNoMerge(securityLinkCreate,securityLink);
		securityLinkRepository.merge(securityLink);
		return securityLink;
	}

	public boolean updateSecurityLinkNoMerge(SecurityLinkCreate securityLinkCreate, SecurityLink securityLink) {
		return baselinkService.updateBaselinkNoMerge(securityLinkCreate,securityLink);
	}

	public SecurityLink updateSecurityLink(SecurityLinkUpdate securityLinkUpdate, SecurityContext securityContext){
		SecurityLink securityLink=securityLinkUpdate.getSecurityLink();
		if(updateSecurityLinkNoMerge(securityLinkUpdate,securityLink)){
			securityLinkRepository.merge(securityLink);
		}
		return securityLink;
	}

	public void validate(SecurityLinkCreate securityLinkCreate, SecurityContext securityContext) {
		baselinkService.validate(securityLinkCreate,securityContext);
	}

	public void validate(SecurityLinkFilter securityLinkFilter, SecurityContext securityContext) {
		baselinkService.validate(securityLinkFilter,securityContext);
	}

	public <T extends Baseclass> T getByIdOrNull(String id,Class<T> c, SecurityContext securityContext) {
		return securityLinkRepository.getByIdOrNull(id,c,securityContext);
	}

	public PaginationResponse<SecurityLink> getAllSecurityLinks(SecurityLinkFilter securityLinkFilter, SecurityContext securityContext) {
		List<SecurityLink> list= listAllSecurityLinks(securityLinkFilter, securityContext);
		long count=securityLinkRepository.countAllSecurityLinks(securityLinkFilter,securityContext);
		return new PaginationResponse<>(list,securityLinkFilter,count);
	}

	public List<SecurityLink> listAllSecurityLinks(SecurityLinkFilter securityLinkFilter, SecurityContext securityContext) {
		return securityLinkRepository.listAllSecurityLinks(securityLinkFilter, securityContext);
	}
}
