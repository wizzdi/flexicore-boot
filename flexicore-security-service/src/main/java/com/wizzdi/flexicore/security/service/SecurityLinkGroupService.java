package com.wizzdi.flexicore.security.service;

import com.flexicore.model.Baseclass;
import com.flexicore.model.SecurityLink;
import com.flexicore.model.SecurityLinkGroup;
import com.flexicore.security.SecurityContextBase;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.security.data.SecurityLinkGroupRepository;
import com.wizzdi.flexicore.security.request.SecurityLinkFilter;
import com.wizzdi.flexicore.security.request.SecurityLinkGroupCreate;
import com.wizzdi.flexicore.security.request.SecurityLinkGroupFilter;
import com.wizzdi.flexicore.security.request.SecurityLinkGroupUpdate;
import com.wizzdi.flexicore.security.response.PaginationResponse;
import com.wizzdi.flexicore.security.response.SecurityLinkGroupContainer;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Extension
@Component
public class SecurityLinkGroupService implements Plugin {

	@Autowired
	private BasicService basicService;
	@Autowired
	private SecurityLinkGroupRepository securityLinkGroupRepository;
	@Autowired
	private SecurityLinkService securityLinkService;



	public void merge(Object o){
		securityLinkGroupRepository.merge(o);
	}
	public void massMerge(List<Object> list){
		securityLinkGroupRepository.massMerge(list);
	}

	public <T extends Baseclass> List<T> listByIds(Class<T> c,Set<String> ids,  SecurityContextBase securityContext) {
		return securityLinkGroupRepository.listByIds(c, ids, securityContext);
	}



	public boolean updateSecurityLinkGroupNoMerge(SecurityLinkGroupCreate securityLinkGroupCreate, SecurityLinkGroup securityLinkGroup) {
        return basicService.updateBasicNoMerge(securityLinkGroupCreate, securityLinkGroup);
	}

	public SecurityLinkGroup updateSecurityLinkGroup(SecurityLinkGroupUpdate securityLinkGroupUpdate, SecurityContextBase securityContext){
		SecurityLinkGroup securityLinkGroup=securityLinkGroupUpdate.getSecurityLinkGroup();
		if(updateSecurityLinkGroupNoMerge(securityLinkGroupUpdate,securityLinkGroup)){
			securityLinkGroupRepository.merge(securityLinkGroup);
		}
		return securityLinkGroup;
	}


	public <T extends Baseclass> T getByIdOrNull(String id,Class<T> c, SecurityContextBase securityContext) {
		return securityLinkGroupRepository.getByIdOrNull(id,c,securityContext);
	}

	public PaginationResponse<SecurityLinkGroup> getAllSecurityLinkGroups(SecurityLinkGroupFilter securityLinkGroupFilter, SecurityContextBase securityContext) {
		List<SecurityLinkGroup> list= listAllSecurityLinkGroups(securityLinkGroupFilter, securityContext);
		long count=securityLinkGroupRepository.countAllSecurityLinkGroups(securityLinkGroupFilter,securityContext);
		return new PaginationResponse<>(list,securityLinkGroupFilter,count);
	}

	public List<SecurityLinkGroup> listAllSecurityLinkGroups(SecurityLinkGroupFilter securityLinkGroupFilter, SecurityContextBase securityContext) {
		return securityLinkGroupRepository.listAllSecurityLinkGroups(securityLinkGroupFilter, securityContext);
	}

	public SecurityLinkGroup createSecurityLinkGroup(SecurityLinkGroupCreate securityLinkGroupCreate, SecurityContextBase securityContext){
		SecurityLinkGroup securityLinkGroup= createSecurityLinkGroupNoMerge(securityLinkGroupCreate,securityContext);
		securityLinkGroupRepository.merge(securityLinkGroup);
		return securityLinkGroup;
	}

	public SecurityLinkGroup createSecurityLinkGroupNoMerge(SecurityLinkGroupCreate securityLinkGroupCreate, SecurityContextBase securityContext){
		SecurityLinkGroup securityLinkGroup=new SecurityLinkGroup();
		securityLinkGroup.setId(UUID.randomUUID().toString());
		updateSecurityLinkGroupNoMerge(securityLinkGroupCreate,securityLinkGroup);
		BaseclassService.createSecurityObjectNoMerge(securityLinkGroup,securityContext);
		securityLinkGroupRepository.merge(securityLinkGroup);
		return securityLinkGroup;
	}

	public PaginationResponse<SecurityLinkGroupContainer> getAllSecurityLinkGroupContainers(SecurityLinkGroupFilter securityLinkGroupFilter, SecurityContextBase securityContext) {
		PaginationResponse<SecurityLinkGroup> paginationResponse = getAllSecurityLinkGroups(securityLinkGroupFilter, securityContext);
		List<SecurityLinkGroup> list= paginationResponse.getList();
		Map<String,List<SecurityLink>> links=list.isEmpty()?new HashMap<>():securityLinkService.listAllSecurityLinks(new SecurityLinkFilter().setSecurityLinkGroups(list),securityContext).stream().collect(Collectors.groupingBy(f->f.getSecurityLinkGroup().getId()));
		List<SecurityLinkGroupContainer> containers=list.stream().map(f->new SecurityLinkGroupContainer(f,links.getOrDefault(f.getId(),new ArrayList<>()))).toList();
		return new PaginationResponse<>(containers,securityLinkGroupFilter,paginationResponse.getTotalRecords());
	}
}
