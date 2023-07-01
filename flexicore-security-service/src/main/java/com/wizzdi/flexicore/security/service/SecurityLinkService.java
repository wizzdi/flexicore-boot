package com.wizzdi.flexicore.security.service;

import com.flexicore.model.Baseclass;
import com.flexicore.model.SecurityLink;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.security.data.SecurityLinkRepository;
import com.flexicore.security.SecurityContextBase;
import com.wizzdi.flexicore.security.request.SecurityLinkCreate;
import com.wizzdi.flexicore.security.request.SecurityLinkFilter;
import com.wizzdi.flexicore.security.request.SecurityLinkUpdate;
import com.wizzdi.flexicore.security.response.PaginationResponse;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Set;

@Extension
@Component
public class SecurityLinkService implements Plugin {

	@Autowired
	private BaselinkService baselinkService;
	@Autowired
	private SecurityLinkRepository securityLinkRepository;


	public SecurityLink createSecurityLink(SecurityLinkCreate securityLinkCreate, SecurityContextBase securityContext){
		SecurityLink securityLink= createSecurityLinkNoMerge(securityLinkCreate,securityContext);
		securityLinkRepository.merge(securityLink);
		return securityLink;
	}

	public void merge(Object o){
		securityLinkRepository.merge(o);
	}
	public void massMerge(List<Object> list){
		securityLinkRepository.massMerge(list);
	}

	public <T extends Baseclass> List<T> listByIds(Class<T> c,Set<String> ids,  SecurityContextBase securityContext) {
		return securityLinkRepository.listByIds(c, ids, securityContext);
	}

	public SecurityLink createSecurityLinkNoMerge(SecurityLinkCreate securityLinkCreate, SecurityContextBase securityContext){
		SecurityLink securityLink=new SecurityLink(securityLinkCreate.getName(),securityContext);
		updateSecurityLinkNoMerge(securityLinkCreate,securityLink);
		securityLinkRepository.merge(securityLink);
		return securityLink;
	}

	public boolean updateSecurityLinkNoMerge(SecurityLinkCreate securityLinkCreate, SecurityLink securityLink) {
		boolean updated = baselinkService.updateBaselinkNoMerge(securityLinkCreate, securityLink);
		if(securityLinkCreate.getSimpleValue()!=null&&!securityLinkCreate.getSimpleValue().equals(securityLink.getSimplevalue())){
			securityLink.setSimplevalue(securityLinkCreate.getSimpleValue());
			updated=true;
		}
		if(securityLinkCreate.getValue()!=null&&(securityLink.getValue()==null||!securityLinkCreate.getValue().getId().equals(securityLink.getValue().getId()))){
			securityLink.setValue(securityLinkCreate.getValue());
			updated=true;
		}
		return updated;
	}

	public SecurityLink updateSecurityLink(SecurityLinkUpdate securityLinkUpdate, SecurityContextBase securityContext){
		SecurityLink securityLink=securityLinkUpdate.getSecurityLink();
		if(updateSecurityLinkNoMerge(securityLinkUpdate,securityLink)){
			securityLinkRepository.merge(securityLink);
		}
		return securityLink;
	}

	@Deprecated
	public void validate(SecurityLinkCreate securityLinkCreate, SecurityContextBase securityContext) {
		baselinkService.validate(securityLinkCreate,securityContext);
		String valueId= securityLinkCreate.getValueId();;
		Baseclass value=valueId!=null?getByIdOrNull(valueId,Baseclass.class,securityContext):null;
		if(valueId!=null&&value==null){
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"no value with id "+valueId);
		}
		securityLinkCreate.setValue(value);
	}

	@Deprecated
	public void validate(SecurityLinkFilter securityLinkFilter, SecurityContextBase securityContext) {
		baselinkService.validate(securityLinkFilter,securityContext);
	}

	public <T extends Baseclass> T getByIdOrNull(String id,Class<T> c, SecurityContextBase securityContext) {
		return securityLinkRepository.getByIdOrNull(id,c,securityContext);
	}

	public PaginationResponse<SecurityLink> getAllSecurityLinks(SecurityLinkFilter securityLinkFilter, SecurityContextBase securityContext) {
		List<SecurityLink> list= listAllSecurityLinks(securityLinkFilter, securityContext);
		long count=securityLinkRepository.countAllSecurityLinks(securityLinkFilter,securityContext);
		return new PaginationResponse<>(list,securityLinkFilter,count);
	}

	public List<SecurityLink> listAllSecurityLinks(SecurityLinkFilter securityLinkFilter, SecurityContextBase securityContext) {
		return securityLinkRepository.listAllSecurityLinks(securityLinkFilter, securityContext);
	}
}
