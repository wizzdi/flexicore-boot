package com.wizzdi.flexicore.security.service;

import com.flexicore.model.Baseclass;
import com.flexicore.model.TenantToBaseclass;
import com.wizzdi.segmantix.model.SecurityContext;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.security.data.TenantToBaseclassRepository;
import com.wizzdi.flexicore.security.request.TenantToBaseclassCreate;
import com.wizzdi.flexicore.security.request.TenantToBaseclassFilter;
import com.wizzdi.flexicore.security.request.TenantToBaseclassUpdate;
import com.wizzdi.flexicore.security.response.PaginationResponse;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Extension
@Component
public class TenantToBaseclassService implements Plugin {

	@Autowired
	private SecurityLinkService securityLinkService;
	@Autowired
	private TenantToBaseclassRepository tenantToBaseclassRepository;


	public TenantToBaseclass createTenantToBaseclass(TenantToBaseclassCreate tenantToBaseclassCreate, SecurityContext securityContext){
		TenantToBaseclass tenantToBaseclass= createTenantToBaseclassNoMerge(tenantToBaseclassCreate,securityContext);
		tenantToBaseclassRepository.merge(tenantToBaseclass);
		return tenantToBaseclass;
	}

	public void merge(Object o){
		tenantToBaseclassRepository.merge(o);
	}
	public void massMerge(List<Object> list){
		tenantToBaseclassRepository.massMerge(list);
	}
	public <T extends Baseclass> List<T> listByIds(Class<T> c, Set<String> ids, SecurityContext securityContext) {
		return tenantToBaseclassRepository.listByIds(c, ids, securityContext);
	}

	public TenantToBaseclass createTenantToBaseclassNoMerge(TenantToBaseclassCreate tenantToBaseclassCreate, SecurityContext securityContext){
		TenantToBaseclass tenantToBaseclass=new TenantToBaseclass();
		tenantToBaseclass.setId(UUID.randomUUID().toString());
		updateTenantToBaseclassNoMerge(tenantToBaseclassCreate,tenantToBaseclass);
		BaseclassService.createSecurityObjectNoMerge(tenantToBaseclass,securityContext);
		return tenantToBaseclass;
	}

	public boolean updateTenantToBaseclassNoMerge(TenantToBaseclassCreate tenantToBaseclassCreate, TenantToBaseclass tenantToBaseclass) {
		boolean update = securityLinkService.updateSecurityLinkNoMerge(tenantToBaseclassCreate, tenantToBaseclass);
		if(tenantToBaseclassCreate.getTenant()!=null&&(tenantToBaseclass.getTenant()==null||!tenantToBaseclassCreate.getTenant().getId().equals(tenantToBaseclass.getTenant().getId()))){
			tenantToBaseclass.setTenant(tenantToBaseclassCreate.getTenant());
			update=true;
		}
		return update;
	}

	public TenantToBaseclass updateTenantToBaseclass(TenantToBaseclassUpdate tenantToBaseclassUpdate, SecurityContext securityContext){
		TenantToBaseclass tenantToBaseclass=tenantToBaseclassUpdate.getTenantToBaseclass();
		if(updateTenantToBaseclassNoMerge(tenantToBaseclassUpdate,tenantToBaseclass)){
			tenantToBaseclassRepository.merge(tenantToBaseclass);
		}
		return tenantToBaseclass;
	}


	public <T extends Baseclass> T getByIdOrNull(String id, Class<T> c, SecurityContext securityContext) {
		return tenantToBaseclassRepository.getByIdOrNull(id,c,securityContext);
	}

	public PaginationResponse<TenantToBaseclass> getAllTenantToBaseclasss(TenantToBaseclassFilter tenantToBaseclassFilter, SecurityContext securityContext) {
		List<TenantToBaseclass> list= listAllTenantToBaseclasss(tenantToBaseclassFilter, securityContext);
		long count= tenantToBaseclassRepository.countAllTenantToBaseclasss(tenantToBaseclassFilter,securityContext);
		return new PaginationResponse<>(list, tenantToBaseclassFilter,count);
	}

	public List<TenantToBaseclass> listAllTenantToBaseclasss(TenantToBaseclassFilter tenantToBaseclassFilter, SecurityContext securityContext) {
		return tenantToBaseclassRepository.listAllTenantToBaseclasss(tenantToBaseclassFilter, securityContext);
	}
}
