package com.wizzdi.flexicore.security.service;

import com.flexicore.model.Baseclass;
import com.flexicore.model.Basic;
import com.flexicore.model.SecurityUser;
import com.wizzdi.flexicore.security.configuration.SecurityContext;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.security.data.SecurityUserRepository;
import com.wizzdi.flexicore.security.request.SecurityUserCreate;
import com.wizzdi.flexicore.security.request.SecurityUserFilter;
import com.wizzdi.flexicore.security.request.SecurityUserUpdate;
import com.wizzdi.flexicore.security.response.PaginationResponse;
import jakarta.persistence.metamodel.SingularAttribute;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Extension
@Component
public class SecurityUserService implements Plugin {

	@Autowired
	private SecurityEntityService securityEntityService;
	@Autowired
	private SecurityUserRepository securityUserRepository;


	public SecurityUser createSecurityUser(SecurityUserCreate securityUserCreate, SecurityContext securityContext){
		SecurityUser securityUser= createSecurityUserNoMerge(securityUserCreate,securityContext);
		securityUserRepository.merge(securityUser);
		return securityUser;
	}


	public SecurityUser createSecurityUserNoMerge(SecurityUserCreate securityUserCreate, SecurityContext securityContext){
		SecurityUser securityUser=new SecurityUser();
		securityUser.setId(UUID.randomUUID().toString());
		updateSecurityUserNoMerge(securityUserCreate,securityUser);
		BaseclassService.createSecurityObjectNoMerge(securityUser,securityContext);
		return securityUser;
	}

	public boolean updateSecurityUserNoMerge(SecurityUserCreate securityUserCreate, SecurityUser securityUser) {
		return securityEntityService.updateNoMerge(securityUserCreate,securityUser);
	}

	public SecurityUser updateSecurityUser(SecurityUserUpdate securityUserUpdate, SecurityContext securityContext){
		SecurityUser securityUser=securityUserUpdate.getSecurityUser();
		if(updateSecurityUserNoMerge(securityUserUpdate,securityUser)){
			securityUserRepository.merge(securityUser);
		}
		return securityUser;
	}



	public PaginationResponse<SecurityUser> getAllSecurityUsers(SecurityUserFilter securityUserFilter, SecurityContext securityContext) {
		List<SecurityUser> list= listAllSecurityUsers(securityUserFilter, securityContext);
		long count=securityUserRepository.countAllSecurityUsers(securityUserFilter,securityContext);
		return new PaginationResponse<>(list,securityUserFilter,count);
	}

	public List<SecurityUser> listAllSecurityUsers(SecurityUserFilter securityUserFilter, SecurityContext securityContext) {
		return securityUserRepository.listAllSecurityUsers(securityUserFilter, securityContext);
	}

	public <T extends Baseclass> List<T> listByIds(Class<T> c, Set<String> ids, SecurityContext securityContext) {
		return securityUserRepository.listByIds(c, ids, securityContext);
	}

	public <T extends Baseclass> T getByIdOrNull(String id, Class<T> c, SecurityContext securityContext) {
		return securityUserRepository.getByIdOrNull(id, c, securityContext);
	}

	public <D extends Basic, E extends Baseclass, T extends D> T getByIdOrNull(String id, Class<T> c, SingularAttribute<D, E> baseclassAttribute, SecurityContext securityContext) {
		return securityUserRepository.getByIdOrNull(id, c, baseclassAttribute, securityContext);
	}

	public <D extends Basic, E extends Baseclass, T extends D> List<T> listByIds(Class<T> c, Set<String> ids, SingularAttribute<D, E> baseclassAttribute, SecurityContext securityContext) {
		return securityUserRepository.listByIds(c, ids, baseclassAttribute, securityContext);
	}

	public <D extends Basic, T extends D> List<T> findByIds(Class<T> c, Set<String> ids, SingularAttribute<D, String> idAttribute) {
		return securityUserRepository.findByIds(c, ids, idAttribute);
	}

	public <T extends Basic> List<T> findByIds(Class<T> c, Set<String> requested) {
		return securityUserRepository.findByIds(c, requested);
	}

	public <T> T findByIdOrNull(Class<T> type, String id) {
		return securityUserRepository.findByIdOrNull(type, id);
	}


	public <T> T merge(T base) {
		return securityUserRepository.merge(base);
	}


	public <T> T merge(T base, boolean updateDate, boolean propagateEvents) {
		return securityUserRepository.merge(base, updateDate, propagateEvents);
	}


	public void massMerge(List<?> toMerge, boolean updatedate, boolean propagateEvents) {
		securityUserRepository.massMerge(toMerge, updatedate, propagateEvents);
	}


	public <T> T merge(T base, boolean updateDate) {
		return securityUserRepository.merge(base, updateDate);
	}


	public void massMerge(List<?> toMerge) {
		securityUserRepository.massMerge(toMerge);
	}


	public void massMerge(List<?> toMerge, boolean updatedate) {
		securityUserRepository.massMerge(toMerge, updatedate);
	}
}
