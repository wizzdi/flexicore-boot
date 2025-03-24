package com.wizzdi.flexicore.security.service;

import com.flexicore.model.*;
import com.wizzdi.flexicore.security.configuration.SecurityContext;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.security.data.TenantToUserRepository;
import com.wizzdi.flexicore.security.request.TenantToUserCreate;
import com.wizzdi.flexicore.security.request.TenantToUserFilter;
import com.wizzdi.flexicore.security.request.TenantToUserMassCreate;
import com.wizzdi.flexicore.security.request.TenantToUserUpdate;
import com.wizzdi.flexicore.security.response.PaginationResponse;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Extension
@Component
public class TenantToUserService implements Plugin {

	@Autowired
	private BasicService basicService;
	@Autowired
	private TenantToUserRepository tenantToUserRepository;


	public TenantToUser createTenantToUser(TenantToUserCreate tenantToUserCreate, SecurityContext securityContext){
		TenantToUser tenantToUser= createTenantToUserNoMerge(tenantToUserCreate,securityContext);
		tenantToUserRepository.merge(tenantToUser);
		return tenantToUser;
	}

	public <T> T merge(T o){
		return tenantToUserRepository.merge(o);
	}
	public void massMerge(List<Object> list){
		tenantToUserRepository.massMerge(list);
	}
	public <T extends Baseclass> List<T> listByIds(Class<T> c, Set<String> ids, SecurityContext securityContext) {
		return tenantToUserRepository.listByIds(c, ids, securityContext);
	}

	public TenantToUser createTenantToUserNoMerge(TenantToUserCreate tenantToUserCreate, SecurityContext securityContext){
		TenantToUser tenantToUser=new TenantToUser();
		tenantToUser.setId(UUID.randomUUID().toString());
		updateTenantToUserNoMerge(tenantToUserCreate,tenantToUser);
		BaseclassService.createSecurityObjectNoMerge(tenantToUser,false,securityContext);
		return tenantToUser;
	}

	public boolean updateTenantToUserNoMerge(TenantToUserCreate tenantToUserCreate, TenantToUser tenantToUser) {
		boolean update = basicService.updateBasicNoMerge(tenantToUserCreate, tenantToUser);
		if(tenantToUserCreate.getUser()!=null&&(tenantToUser.getUser()==null||!tenantToUserCreate.getUser().getId().equals(tenantToUser.getUser().getId()))){
			tenantToUser.setUser(tenantToUserCreate.getUser());
			update=true;
		}
		if(tenantToUserCreate.getTenant()!=null&&(tenantToUser.getTenant()==null||!tenantToUserCreate.getTenant().getId().equals(tenantToUser.getTenant().getId()))){
			tenantToUser.setTenant(tenantToUserCreate.getTenant());
			update=true;
		}

		if(tenantToUserCreate.getDefaultTenant()!=null&&!tenantToUserCreate.getDefaultTenant().equals(tenantToUser.isDefaultTenant())){
			tenantToUser.setDefaultTenant(tenantToUserCreate.getDefaultTenant());
			update=true;
		}
		return update;
	}

	public TenantToUser updateTenantToUser(TenantToUserUpdate tenantToUserUpdate, SecurityContext securityContext){
		TenantToUser tenantToUser=tenantToUserUpdate.getTenantToUser();
		if(updateTenantToUserNoMerge(tenantToUserUpdate,tenantToUser)){
			tenantToUserRepository.merge(tenantToUser);
		}
		return tenantToUser;
	}

	@Deprecated
	public void validate(TenantToUserCreate tenantToUserCreate, SecurityContext securityContext) {
		basicService.validate(tenantToUserCreate,securityContext);
	}



	public <T extends Baseclass> T getByIdOrNull(String id, Class<T> c, SecurityContext securityContext) {
		return tenantToUserRepository.getByIdOrNull(id,c,securityContext);
	}

	public PaginationResponse<TenantToUser> getAllTenantToUsers(TenantToUserFilter tenantToUserFilter, SecurityContext securityContext) {
		List<TenantToUser> list= listAllTenantToUsers(tenantToUserFilter, securityContext);
		long count=tenantToUserRepository.countAllTenantToUsers(tenantToUserFilter,securityContext);
		return new PaginationResponse<>(list,tenantToUserFilter,count);
	}

	public List<TenantToUser> listAllTenantToUsers(TenantToUserFilter tenantToUserFilter, SecurityContext securityContext) {
		return tenantToUserRepository.listAllTenantToUsers(tenantToUserFilter, securityContext);
	}

	public <T extends Baseclass> List<T> findByIds(Class<T> c, Set<String> requested) {
		return tenantToUserRepository.findByIds(c, requested);
	}


	public <T> T findByIdOrNull(Class<T> type, String id) {
		return tenantToUserRepository.findByIdOrNull(type, id);
	}

	public List<TenantToUser> massCreateTenantToUser(TenantToUserMassCreate tenantToUserMassCreate, SecurityContext securityContext) {
		List<SecurityTenant> tenants=new ArrayList<>(tenantToUserMassCreate.getTenantToUserCreates().stream().map(f->f.getTenant()).collect(Collectors.toMap(f->f.getId(), f->f,(a, b)->a)).values());
		List<SecurityUser> users=new ArrayList<>(tenantToUserMassCreate.getTenantToUserCreates().stream().map(f->f.getUser()).collect(Collectors.toMap(f->f.getId(), f->f,(a, b)->a)).values());
		Map<String, Map<String, TenantToUser>> links = listAllTenantToUsers(new TenantToUserFilter().setTenants(tenants).setUsers(users), null).stream().collect(Collectors.groupingBy(f -> f.getUser().getId(), Collectors.toMap(f -> f.getTenant().getId(), f -> f, (a, b) -> a)));
		List<Object> toMerge=new ArrayList<>();
		for (TenantToUserCreate create : tenantToUserMassCreate.getTenantToUserCreates()) {
			Map<String, TenantToUser> map=links.computeIfAbsent(create.getUser().getId(), f->new HashMap<>());
			map.computeIfAbsent(create.getTenant().getId(),f->{
				TenantToUser tenantToUser = createTenantToUser(create, securityContext);
				toMerge.add(tenantToUser);
				return tenantToUser;
			});
		}


		tenantToUserRepository.massMerge(toMerge);
		return links.values().stream().map(f->f.values()).flatMap(Collection::stream).toList();
	}
}
