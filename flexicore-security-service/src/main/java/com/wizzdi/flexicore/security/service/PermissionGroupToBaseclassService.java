package com.wizzdi.flexicore.security.service;

import com.flexicore.model.Baseclass;
import com.flexicore.model.PermissionGroup;
import com.flexicore.model.PermissionGroupToBaseclass;
import com.wizzdi.segmantix.model.SecurityContext;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.security.data.PermissionGroupToBaseclassRepository;
import com.wizzdi.flexicore.security.request.PermissionGroupToBaseclassCreate;
import com.wizzdi.flexicore.security.request.PermissionGroupToBaseclassFilter;
import com.wizzdi.flexicore.security.request.PermissionGroupToBaseclassMassCreate;
import com.wizzdi.flexicore.security.request.PermissionGroupToBaseclassUpdate;
import com.wizzdi.flexicore.security.response.PaginationResponse;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Extension
@Component
public class PermissionGroupToBaseclassService implements Plugin {

	@Autowired
	private BasicService basicService;
	@Autowired
	private PermissionGroupToBaseclassRepository permissionGroupToBaseclassRepository;


	public PermissionGroupToBaseclass createPermissionGroupToBaseclass(PermissionGroupToBaseclassCreate permissionGroupToBaseclassCreate, SecurityContext securityContext){
		PermissionGroupToBaseclass permissionGroupToBaseclass= createPermissionGroupToBaseclassNoMerge(permissionGroupToBaseclassCreate,securityContext);
		permissionGroupToBaseclassRepository.merge(permissionGroupToBaseclass);
		return permissionGroupToBaseclass;
	}
	public void merge(Object o){
		permissionGroupToBaseclassRepository.merge(o);
	}
	public void massMerge(List<Object> list){
		permissionGroupToBaseclassRepository.massMerge(list);
	}

	public <T extends Baseclass> List<T> listByIds(Class<T> c, Set<String> ids, SecurityContext securityContext) {
		return permissionGroupToBaseclassRepository.listByIds(c, ids, securityContext);
	}

	public PermissionGroupToBaseclass createPermissionGroupToBaseclassNoMerge(PermissionGroupToBaseclassCreate permissionGroupToBaseclassCreate, SecurityContext securityContext){
		PermissionGroupToBaseclass permissionGroupToBaseclass=new PermissionGroupToBaseclass();
		permissionGroupToBaseclass.setId(UUID.randomUUID().toString());
		updatePermissionGroupToBaseclassNoMerge(permissionGroupToBaseclassCreate,permissionGroupToBaseclass);
		BaseclassService.createSecurityObjectNoMerge(permissionGroupToBaseclass,securityContext);
		return permissionGroupToBaseclass;
	}

	public boolean updatePermissionGroupToBaseclassNoMerge(PermissionGroupToBaseclassCreate permissionGroupToBaseclassCreate, PermissionGroupToBaseclass permissionGroupToBaseclass) {
		boolean update = basicService.updateBasicNoMerge(permissionGroupToBaseclassCreate, permissionGroupToBaseclass);
		if(permissionGroupToBaseclassCreate.getSecuredId()!=null&&!permissionGroupToBaseclassCreate.getSecuredId().equals(permissionGroupToBaseclass.getSecuredId())){
			permissionGroupToBaseclass.setSecuredId(permissionGroupToBaseclassCreate.getSecuredId());
			update=true;
		}

		if(permissionGroupToBaseclassCreate.getSecuredType()!=null&&!permissionGroupToBaseclassCreate.getSecuredType().name().equals(permissionGroupToBaseclass.getSecuredType())){
			permissionGroupToBaseclass.setSecuredType(permissionGroupToBaseclassCreate.getSecuredType().name());
			update=true;
		}
		if(permissionGroupToBaseclassCreate.getPermissionGroup()!=null&&(permissionGroupToBaseclass.getPermissionGroup()==null||!permissionGroupToBaseclassCreate.getPermissionGroup().getId().equals(permissionGroupToBaseclass.getPermissionGroup().getId()))){
			permissionGroupToBaseclass.setPermissionGroup(permissionGroupToBaseclassCreate.getPermissionGroup());
			update=true;
		}
		return update;
	}

	public PermissionGroupToBaseclass updatePermissionGroupToBaseclass(PermissionGroupToBaseclassUpdate permissionGroupToBaseclassUpdate, SecurityContext securityContext){
		PermissionGroupToBaseclass permissionGroupToBaseclass=permissionGroupToBaseclassUpdate.getPermissionGroupToBaseclass();
		if(updatePermissionGroupToBaseclassNoMerge(permissionGroupToBaseclassUpdate,permissionGroupToBaseclass)){
			permissionGroupToBaseclassRepository.merge(permissionGroupToBaseclass);
		}
		return permissionGroupToBaseclass;
	}

	@Deprecated
	public void validate(PermissionGroupToBaseclassCreate permissionGroupToBaseclassCreate, SecurityContext securityContext) {
		basicService.validate(permissionGroupToBaseclassCreate,securityContext);
	}



	public <T extends Baseclass> T getByIdOrNull(String id, Class<T> c, SecurityContext securityContext) {
		return permissionGroupToBaseclassRepository.getByIdOrNull(id,c,securityContext);
	}

	public PaginationResponse<PermissionGroupToBaseclass> getAllPermissionGroupToBaseclass(PermissionGroupToBaseclassFilter permissionGroupToBaseclassFilter, SecurityContext securityContext) {
		List<PermissionGroupToBaseclass> list= listAllPermissionGroupToBaseclass(permissionGroupToBaseclassFilter, securityContext);
		long count=permissionGroupToBaseclassRepository.countAllPermissionGroupToBaseclasss(permissionGroupToBaseclassFilter,securityContext);
		return new PaginationResponse<>(list,permissionGroupToBaseclassFilter,count);
	}

	public List<PermissionGroupToBaseclass> listAllPermissionGroupToBaseclass(PermissionGroupToBaseclassFilter permissionGroupToBaseclassFilter, SecurityContext securityContext) {
		return permissionGroupToBaseclassRepository.listAllPermissionGroupToBaseclasss(permissionGroupToBaseclassFilter, securityContext);
	}

	public Map<String, Map<String, PermissionGroupToBaseclass>> massCreatePermissionLinks(PermissionGroupToBaseclassMassCreate permissionGroupToBaseclassMassCreate, SecurityContext securityContext) {
		List<PermissionGroup> permissionGroups = permissionGroupToBaseclassMassCreate.getPermissionGroups();
		Set<String> baseclasses = permissionGroupToBaseclassMassCreate.getSecuredIds();
		Map<String,Map<String, PermissionGroupToBaseclass>> permissionLinks = baseclasses.isEmpty()||permissionGroups.isEmpty()?new HashMap<>():listAllPermissionGroupToBaseclass(new PermissionGroupToBaseclassFilter().setPermissionGroups(new ArrayList<>(permissionGroups)).setSecuredIds(baseclasses), securityContext).stream().collect(Collectors.groupingBy(f->f.getPermissionGroup().getId(),Collectors.toMap(f -> f.getSecuredId(), f -> f, (a, b) -> a)));
		for (PermissionGroup permissionGroup : permissionGroups) {
			Map<String, PermissionGroupToBaseclass> permissionGroupLinks = permissionLinks.computeIfAbsent(permissionGroup.getId(), f -> new HashMap<>());
			for (String baseclass : baseclasses) {
				PermissionGroupToBaseclass existing=permissionGroupLinks.get(baseclass);
				if(existing==null){
					PermissionGroupToBaseclass permissionGroupToBaseclass = createPermissionGroupToBaseclass(new PermissionGroupToBaseclassCreate().setSecuredId(baseclass).setPermissionGroup(permissionGroup), securityContext);
					permissionGroupLinks.put(permissionGroupToBaseclass.getSecuredId(),permissionGroupToBaseclass);
				}
			}
		}

		return permissionLinks;
	}
}
