package com.wizzdi.flexicore.security.service;

import com.flexicore.model.Baseclass;
import com.flexicore.model.SecurityLink;
import com.flexicore.model.SecurityOperation;
import com.wizzdi.flexicore.security.configuration.SecurityContext;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.security.data.SecurityLinkRepository;
import com.wizzdi.flexicore.security.request.*;
import com.wizzdi.flexicore.security.response.PaginationResponse;
import jakarta.validation.Valid;
import org.pf4j.Extension;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Extension
@Component
public class SecurityLinkService implements Plugin, InitializingBean {

	@Autowired
	private BasicService basicService;
	@Autowired
	private SecurityLinkRepository securityLinkRepository;
	@Autowired
	private SecurityTenantService securityTenantService;
	@Autowired
	private RoleService roleService;



	public void merge(Object o){
		securityLinkRepository.merge(o);
	}
	public void massMerge(List<Object> list){
		securityLinkRepository.massMerge(list);
	}

	public <T extends Baseclass> List<T> listByIds(Class<T> c, Set<String> ids, SecurityContext securityContext) {
		return securityLinkRepository.listByIds(c, ids, securityContext);
	}



	public boolean updateSecurityLinkNoMerge(SecurityLinkCreate securityLinkCreate, SecurityLink securityLink) {
		boolean updated = basicService.updateBasicNoMerge(securityLinkCreate, securityLink);
		if(securityLinkCreate.getAccess()!=null&&!securityLinkCreate.getAccess().equals(securityLink.getAccess())){
			securityLink.setAccess(securityLinkCreate.getAccess());
			updated=true;
		}
		if(securityLinkCreate.getOperation()!=null&&!securityLinkCreate.getOperation().getId().equals(securityLink.getOperationId())){
			securityLink.setOperationId(securityLinkCreate.getOperation().getId());
			updated=true;
		}
		if(securityLinkCreate.getOperationGroup()!=null&&(securityLink.getOperationGroup()==null||!securityLinkCreate.getOperationGroup().getId().equals(securityLink.getOperationGroup().getId()))){
			securityLink.setOperationGroup(securityLinkCreate.getOperationGroup());
			updated=true;
		}
		if (securityLinkCreate.getSecuredId() != null && !securityLinkCreate.getSecuredId().equals(securityLink.getSecuredId())) {
			securityLink.setSecuredId(securityLinkCreate.getSecuredId());
			updated = true;
		}
		if(securityLinkCreate.getPermissionGroup()!=null&&(securityLink.getPermissionGroup()==null || !securityLinkCreate.getPermissionGroup().getId().equals(securityLink.getPermissionGroup().getId()))){
			securityLink.setPermissionGroup(securityLinkCreate.getPermissionGroup());
			updated=true;
		}

		if(securityLinkCreate.getClazz()!=null&&!securityLinkCreate.getClazz().name().equals(securityLink.getSecuredType())){
			securityLink.setSecuredType(securityLinkCreate.getClazz().name());
			updated=true;
		}
		if(securityLinkCreate.getSecured() instanceof Baseclass baseclass){
			if(securityLink.getSecuredName()==null||!baseclass.getName().equals(securityLink.getSecuredName())){
				securityLink.setSecuredName(baseclass.getName());
			}
		}
		if(securityLinkCreate.getSecurityLinkGroup()!=null&&(securityLink.getSecurityLinkGroup()==null || !securityLinkCreate.getSecurityLinkGroup().getId().equals(securityLink.getSecurityLinkGroup().getId()))){
			securityLink.setSecurityLinkGroup(securityLinkCreate.getSecurityLinkGroup());
			updated=true;
		}
		String searchString=createSearchString(securityLink);
		if(!searchString.equals(securityLink.getSearchString())){
			securityLink.setSearchString(searchString);
			updated=true;
		}
		return updated;
	}

	private String createSearchString(SecurityLink securityLink) {
		List<String> l=List.of(
				getStringOrPlaceHolder(Optional.ofNullable(securityLink.getSecurityEntity()).map(f->f.getName())),
				getStringOrPlaceHolder(securityLink.getSecuredName()),
				getStringOrPlaceHolder(securityLink.getSecuredType()),
				getStringOrPlaceHolder(Optional.ofNullable(securityLink.getOperation()).filter(f->f instanceof SecurityOperation).map(f->(SecurityOperation)f).map(f->f.getName())),
				getStringOrPlaceHolder(Optional.ofNullable(securityLink.getOperationGroup()).map(f->f.getName())),
				getStringOrPlaceHolder(Optional.ofNullable(securityLink.getPermissionGroup()).map(f->f.getName()))
		);
		return l.stream().map(String::toLowerCase).collect(Collectors.joining("|"));
	}
	public String getStringOrPlaceHolder(String s){
		return s!=null?s:"#";
	}
	public String getStringOrPlaceHolder(Optional<String> s){
		return s.orElse("#");
	}

	public SecurityLink updateSecurityLink(SecurityLinkUpdate securityLinkUpdate, SecurityContext securityContext) {
		SecurityLink securityLink=securityLinkUpdate.getSecurityLink();
		if(updateSecurityLinkNoMerge(securityLinkUpdate,securityLink)){
			securityLinkRepository.merge(securityLink);
		}
		return securityLink;
	}


	public <T extends Baseclass> T getByIdOrNull(String id, Class<T> c, SecurityContext securityContext) {
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

	public void setRelevant(SecurityLinkFilter securityLinkFilter, SecurityContext securityContext) {
		if(securityLinkFilter.getRelevantUsers()!=null&&!securityLinkFilter.getRelevantUsers().isEmpty()){
			securityLinkFilter.setRelevantRoles(roleService.listAllRoles(new RoleFilter().setUsers(securityLinkFilter.getRelevantUsers()),securityContext));
			securityLinkFilter.setRelevantTenants(securityTenantService.listAllTenants(new SecurityTenantFilter().setUsers(securityLinkFilter.getRelevantUsers()),securityContext));
		}
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		securityLinkRepository.createSearchIndex();
	}

	public void refreshSearchString(SecurityLinkFilter securityLinkFilter, SecurityContext securityContext) {
		List<SecurityLink> list=listAllSecurityLinks(securityLinkFilter,securityContext);
		for (List<SecurityLink> securityLinks : partition(list, 100)) {
			List<Object> toMerge=new ArrayList<>();
			for (SecurityLink securityLink : securityLinks) {
				if(updateSecurityLinkNoMerge(new SecurityLinkCreate(),securityLink)){
					toMerge.add(securityLink);
				}

			}
			securityLinkRepository.massMerge(toMerge);

		}

	}
	public static <T> List<List<T>> partition(List<T> list, int size) {
		if (size <= 0) {
			throw new IllegalArgumentException("Size must be greater than 0");
		}
		List<List<T>> partitions = new ArrayList<>();
		for (int i = 0; i < list.size(); i += size) {
			partitions.add(list.subList(i, Math.min(i + size, list.size())));
		}
		return partitions;
	}
}
