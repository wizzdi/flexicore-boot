package com.wizzdi.flexicore.security.service;

import com.flexicore.model.UserToBaseclass;
import com.flexicore.model.Baseclass;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.security.data.UserToBaseclassRepository;
import com.wizzdi.flexicore.security.request.UserToBaseclassCreate;
import com.wizzdi.flexicore.security.request.UserToBaseclassFilter;
import com.wizzdi.flexicore.security.request.UserToBaseclassUpdate;
import com.flexicore.security.SecurityContextBase;
import com.wizzdi.flexicore.security.response.PaginationResponse;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Extension
@Component
public class UserToBaseclassService implements Plugin {

	@Autowired
	private SecurityLinkService securityLinkService;
	@Autowired
	private UserToBaseclassRepository userToBaseclassRepository;


	public UserToBaseclass createUserToBaseclass(UserToBaseclassCreate userToBaseclassCreate, SecurityContextBase securityContext){
		UserToBaseclass userToBaseclass= createUserToBaseclassNoMerge(userToBaseclassCreate,securityContext);
		userToBaseclassRepository.merge(userToBaseclass);
		return userToBaseclass;
	}


	public UserToBaseclass createUserToBaseclassNoMerge(UserToBaseclassCreate userToBaseclassCreate, SecurityContextBase securityContext){
		UserToBaseclass userToBaseclass=new UserToBaseclass();
		userToBaseclass.setId(UUID.randomUUID().toString());
		updateUserToBaseclassNoMerge(userToBaseclassCreate,userToBaseclass);
		BaseclassService.createSecurityObjectNoMerge(userToBaseclass,securityContext);
		userToBaseclassRepository.merge(userToBaseclass);
		return userToBaseclass;
	}

	public boolean updateUserToBaseclassNoMerge(UserToBaseclassCreate userToBaseclassCreate, UserToBaseclass userToBaseclass) {
		boolean update = securityLinkService.updateSecurityLinkNoMerge(userToBaseclassCreate, userToBaseclass);
		if(userToBaseclassCreate.getUser()!=null&&(userToBaseclass.getUser()==null||!userToBaseclassCreate.getUser().getId().equals(userToBaseclass.getUser().getId()))){
			userToBaseclass.setUser(userToBaseclassCreate.getUser());
			update=true;
		}
		return update;
	}

	public UserToBaseclass updateUserToBaseclass(UserToBaseclassUpdate userToBaseclassUpdate, SecurityContextBase securityContext){
		UserToBaseclass userToBaseclass=userToBaseclassUpdate.getUserToBaseclass();
		if(updateUserToBaseclassNoMerge(userToBaseclassUpdate,userToBaseclass)){
			userToBaseclassRepository.merge(userToBaseclass);
		}
		return userToBaseclass;
	}
	public void merge(Object o){
		userToBaseclassRepository.merge(o);
	}
	public void massMerge(List<Object> list){
		userToBaseclassRepository.massMerge(list);
	}



	public <T extends Baseclass> T getByIdOrNull(String id,Class<T> c, SecurityContextBase securityContext) {
		return userToBaseclassRepository.getByIdOrNull(id,c,securityContext);
	}

	public <T extends Baseclass> List<T> listByIds(Class<T> c,Set<String> ids,  SecurityContextBase securityContext) {
		return userToBaseclassRepository.listByIds(c, ids, securityContext);
	}

	public PaginationResponse<UserToBaseclass> getAllUserToBaseclass(UserToBaseclassFilter userToBaseclassFilter, SecurityContextBase securityContext) {
		List<UserToBaseclass> list= listAllUserToBaseclasss(userToBaseclassFilter, securityContext);
		long count=userToBaseclassRepository.countAllUserToBaseclasss(userToBaseclassFilter,securityContext);
		return new PaginationResponse<>(list,userToBaseclassFilter,count);
	}

	public List<UserToBaseclass> listAllUserToBaseclasss(UserToBaseclassFilter userToBaseclassFilter, SecurityContextBase securityContext) {
		return userToBaseclassRepository.listAllUserToBaseclasss(userToBaseclassFilter, securityContext);
	}
}
