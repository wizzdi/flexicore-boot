package com.wizzdi.flexicore.security.service;

import com.flexicore.model.*;
import com.wizzdi.flexicore.security.configuration.SecurityContext;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.security.data.SecurityLinkGroupRepository;
import com.wizzdi.flexicore.security.events.BasicUpdated;
import com.wizzdi.flexicore.security.request.*;
import com.wizzdi.flexicore.security.response.PaginationResponse;
import com.wizzdi.flexicore.security.response.SecurityLinkGroupContainer;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
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
	@Autowired
	private RoleService roleService;
	@Autowired
	private SecurityTenantService securityTenantService;



	public void merge(Object o){
		securityLinkGroupRepository.merge(o);
	}
	public void massMerge(List<Object> list){
		securityLinkGroupRepository.massMerge(list);
	}

	public <T extends Baseclass> List<T> listByIds(Class<T> c, Set<String> ids, SecurityContext securityContext) {
		return securityLinkGroupRepository.listByIds(c, ids, securityContext);
	}



	public boolean updateSecurityLinkGroupNoMerge(SecurityLinkGroupCreate securityLinkGroupCreate, SecurityLinkGroup securityLinkGroup) {
        return basicService.updateBasicNoMerge(securityLinkGroupCreate, securityLinkGroup);
	}

	public SecurityLinkGroup updateSecurityLinkGroup(SecurityLinkGroupUpdate securityLinkGroupUpdate, SecurityContext securityContext){
		SecurityLinkGroup securityLinkGroup=securityLinkGroupUpdate.getSecurityLinkGroup();
		if(updateSecurityLinkGroupNoMerge(securityLinkGroupUpdate,securityLinkGroup)){
			securityLinkGroupRepository.merge(securityLinkGroup);
		}
		return securityLinkGroup;
	}


	public <T extends Baseclass> T getByIdOrNull(String id, Class<T> c, SecurityContext securityContext) {
		return securityLinkGroupRepository.getByIdOrNull(id,c,securityContext);
	}

	public PaginationResponse<SecurityLinkGroup> getAllSecurityLinkGroups(SecurityLinkGroupFilter securityLinkGroupFilter, SecurityContext securityContext) {
		List<SecurityLinkGroup> list= listAllSecurityLinkGroups(securityLinkGroupFilter, securityContext);
		long count=securityLinkGroupRepository.countAllSecurityLinkGroups(securityLinkGroupFilter,securityContext);
		return new PaginationResponse<>(list,securityLinkGroupFilter,count);
	}

	public List<SecurityLinkGroup> listAllSecurityLinkGroups(SecurityLinkGroupFilter securityLinkGroupFilter, SecurityContext securityContext) {
		return securityLinkGroupRepository.listAllSecurityLinkGroups(securityLinkGroupFilter, securityContext);
	}

	public SecurityLinkGroup createSecurityLinkGroup(SecurityLinkGroupCreate securityLinkGroupCreate, SecurityContext securityContext){
		SecurityLinkGroup securityLinkGroup= createSecurityLinkGroupNoMerge(securityLinkGroupCreate,securityContext);
		securityLinkGroupRepository.merge(securityLinkGroup);
		return securityLinkGroup;
	}

	public SecurityLinkGroup createSecurityLinkGroupNoMerge(SecurityLinkGroupCreate securityLinkGroupCreate, SecurityContext securityContext){
		SecurityLinkGroup securityLinkGroup=new SecurityLinkGroup();
		securityLinkGroup.setId(UUID.randomUUID().toString());
		updateSecurityLinkGroupNoMerge(securityLinkGroupCreate,securityLinkGroup);
		BaseclassService.createSecurityObjectNoMerge(securityLinkGroup,securityContext);
		return securityLinkGroup;
	}

	public PaginationResponse<SecurityLinkGroupContainer> getAllSecurityLinkGroupContainers(SecurityLinkGroupFilter securityLinkGroupFilter, SecurityContext securityContext) {
		SecurityLinkFilter securityLinkFilter = securityLinkGroupFilter.getSecurityLinkFilter();
		List<SecurityLinkOrder> sorting = null;
		if (securityLinkFilter != null) {
			sorting=securityLinkFilter.getSorting();
			List<SecurityUser> relevantUsers = securityLinkFilter.getRelevantUsers();
			if (relevantUsers != null && !relevantUsers.isEmpty()) {
				if (securityLinkFilter.getRelevantRoles() == null || securityLinkFilter.getRelevantRoles().isEmpty()) {
					List<Role> roles = roleService.listAllRoles(new RoleFilter().setUsers(relevantUsers), null);
					securityLinkFilter.setRelevantRoles(roles);
				}
				if (securityLinkFilter.getRelevantTenants() == null || securityLinkFilter.getRelevantTenants().isEmpty()) {
					List<SecurityTenant> securityTenants = securityTenantService.listAllTenants(new SecurityTenantFilter().setUsers(relevantUsers), null);
					securityLinkFilter.setRelevantTenants(securityTenants);
				}


			}
			if (sorting == null || sorting.isEmpty()) {
				securityLinkFilter.setSorting(Arrays.stream(SecurityLinkOrder.values()).toList());
			}
		}

		PaginationResponse<SecurityLinkGroup> paginationResponse = getAllSecurityLinkGroups(securityLinkGroupFilter, securityContext);
		List<SecurityLinkGroup> linkGroups = paginationResponse.getList();
		List<SecurityLink> links = linkGroups.isEmpty() ? new ArrayList<>() : securityLinkService.listAllSecurityLinks(new SecurityLinkFilter().setSorting(sorting).setSecurityLinkGroups(linkGroups), securityContext);
		Map<String, List<SecurityLink>> linksGrouped = links.stream().collect(Collectors.groupingBy(f -> f.getSecurityLinkGroup().getId(), LinkedHashMap::new, Collectors.toList()));
		List<SecurityLinkGroupContainer> containers = linkGroups.stream().map(f -> new SecurityLinkGroupContainer(f, linksGrouped.getOrDefault(f.getId(), new ArrayList<>()))).toList();
		return new PaginationResponse<>(containers,securityLinkGroupFilter,paginationResponse.getTotalRecords());
	}

	@EventListener
	public void onSecurityLinkGroupUpdate(BasicUpdated<SecurityLinkGroup> securityLinkGroupBasicUpdated) {
		SecurityLinkGroup securityLinkGroup = securityLinkGroupBasicUpdated.getBaseclass();
		if (securityLinkGroup.isSoftDelete()) {
			List<SecurityLink> securityLinks = securityLinkService.listAllSecurityLinks(new SecurityLinkFilter().setSecurityLinkGroups(Collections.singletonList(securityLinkGroup)), null);
			List<Object> toMerge = new ArrayList<>();
			for (SecurityLink securityLink : securityLinks) {
				securityLinkService.updateSecurityLinkNoMerge(new SecurityLinkCreate().setSoftDelete(true), securityLink);
			}
			securityLinkService.massMerge(toMerge);
		}
	}
}
