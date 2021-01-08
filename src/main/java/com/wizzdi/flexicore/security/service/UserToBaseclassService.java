package com.wizzdi.flexicore.security.service;

import com.flexicore.model.UserToBaseClass;
import com.flexicore.model.Baseclass;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.security.data.UserToBaseclassRepository;
import com.wizzdi.flexicore.security.request.UserToBaseclassCreate;
import com.wizzdi.flexicore.security.request.UserToBaseclassFilter;
import com.wizzdi.flexicore.security.request.UserToBaseclassUpdate;
import com.flexicore.security.SecurityContext;
import com.wizzdi.flexicore.security.response.PaginationResponse;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Extension
@Component
public class UserToBaseclassService implements Plugin {

	@Autowired
	private SecurityLinkService securityLinkService;
	@Autowired
	private UserToBaseclassRepository userToBaseclassRepository;


	public UserToBaseClass createUserToBaseclass(UserToBaseclassCreate userToBaseclassCreate, SecurityContext securityContext){
		UserToBaseClass userToBaseclass= createUserToBaseclassNoMerge(userToBaseclassCreate,securityContext);
		userToBaseclassRepository.merge(userToBaseclass);
		return userToBaseclass;
	}


	public UserToBaseClass createUserToBaseclassNoMerge(UserToBaseclassCreate userToBaseclassCreate, SecurityContext securityContext){
		UserToBaseClass userToBaseclass=new UserToBaseClass(userToBaseclassCreate.getName(),securityContext);
		updateUserToBaseclassNoMerge(userToBaseclassCreate,userToBaseclass);
		userToBaseclassRepository.merge(userToBaseclass);
		return userToBaseclass;
	}

	public boolean updateUserToBaseclassNoMerge(UserToBaseclassCreate userToBaseclassCreate, UserToBaseClass userToBaseclass) {
		return securityLinkService.updateSecurityLinkNoMerge(userToBaseclassCreate,userToBaseclass);
	}

	public UserToBaseClass updateUserToBaseclass(UserToBaseclassUpdate userToBaseclassUpdate, SecurityContext securityContext){
		UserToBaseClass userToBaseclass=userToBaseclassUpdate.getUserToBaseclass();
		if(updateUserToBaseclassNoMerge(userToBaseclassUpdate,userToBaseclass)){
			userToBaseclassRepository.merge(userToBaseclass);
		}
		return userToBaseclass;
	}

	public void validate(UserToBaseclassCreate userToBaseclassCreate, SecurityContext securityContext) {
		securityLinkService.validate(userToBaseclassCreate,securityContext);
	}

	public void validate(UserToBaseclassFilter userToBaseclassFilter, SecurityContext securityContext) {
		securityLinkService.validate(userToBaseclassFilter,securityContext);
	}

	public <T extends Baseclass> T getByIdOrNull(String id,Class<T> c, SecurityContext securityContext) {
		return userToBaseclassRepository.getByIdOrNull(id,c,securityContext);
	}

	public PaginationResponse<UserToBaseClass> getAllUserToBaseclass(UserToBaseclassFilter userToBaseclassFilter, SecurityContext securityContext) {
		List<UserToBaseClass> list= listAllUserToBaseclasss(userToBaseclassFilter, securityContext);
		long count=userToBaseclassRepository.countAllUserToBaseclasss(userToBaseclassFilter,securityContext);
		return new PaginationResponse<>(list,userToBaseclassFilter,count);
	}

	public List<UserToBaseClass> listAllUserToBaseclasss(UserToBaseclassFilter userToBaseclassFilter, SecurityContext securityContext) {
		return userToBaseclassRepository.listAllUserToBaseclasss(userToBaseclassFilter, securityContext);
	}
}
