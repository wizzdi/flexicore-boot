package com.wizzdi.flexicore.security.service;

import com.flexicore.model.Baseclass;
import com.flexicore.model.Baselink;
import com.flexicore.model.SecurityTenant;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.security.data.BaselinkRepository;
import com.flexicore.security.SecurityContextBase;
import com.wizzdi.flexicore.security.request.BaselinkCreate;
import com.wizzdi.flexicore.security.request.BaselinkFilter;
import com.wizzdi.flexicore.security.request.BaselinkUpdate;
import com.wizzdi.flexicore.security.response.PaginationResponse;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

@Extension
@Component
public class BaselinkService implements Plugin {

	@Autowired
	private BaseclassService baseclassService;
	@Autowired
	private BaselinkRepository baselinkRepository;


	public Baselink createBaselink(BaselinkCreate baselinkCreate, SecurityContextBase securityContext){
		Baselink baselink= createBaselinkNoMerge(baselinkCreate,securityContext);
		baselinkRepository.merge(baselink);
		return baselink;
	}
	public void merge(Object o){
		baselinkRepository.merge(o);
	}
	public void massMerge(List<Object> list){
		baselinkRepository.massMerge(list);
	}

	public <T extends Baseclass> List<T> listByIds(Class<T> c,Set<String> ids,  SecurityContextBase securityContext) {
		return baselinkRepository.listByIds(c, ids, securityContext);
	}


	public Baselink createBaselinkNoMerge(BaselinkCreate baselinkCreate, SecurityContextBase securityContext){
		Baselink baselink=new Baselink(baselinkCreate.getName(),securityContext);
		updateBaselinkNoMerge(baselinkCreate,baselink);
		baselinkRepository.merge(baselink);
		return baselink;
	}

	public boolean updateBaselinkNoMerge(BaselinkCreate baselinkCreate, Baselink baselink) {
		return baseclassService.updateBaseclassNoMerge(baselinkCreate,baselink);
	}

	public Baselink updateBaselink(BaselinkUpdate baselinkUpdate, SecurityContextBase securityContext){
		Baselink baselink=baselinkUpdate.getBaselink();
		if(updateBaselinkNoMerge(baselinkUpdate,baselink)){
			baselinkRepository.merge(baselink);
		}
		return baselink;
	}

	@Deprecated
	public void validate(BaselinkCreate baselinkCreate, SecurityContextBase securityContext) {
		baseclassService.validate(baselinkCreate,securityContext);
	}

	@Deprecated
	public void validate(BaselinkFilter baselinkFilter, SecurityContextBase securityContext) {
		Set<String> leftsideIds=baselinkFilter.getLeftsideIds();
		Map<String, Baseclass> leftsideMap=leftsideIds.isEmpty()?new HashMap<>():baselinkRepository.listByIds(Baseclass.class,leftsideIds,securityContext).stream().collect(Collectors.toMap(f->f.getId(), f->f));
		leftsideIds.removeAll(leftsideMap.keySet());
		if(!leftsideIds.isEmpty()){
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"no Baseclass(leftside) with ids "+leftsideIds);
		}
		baselinkFilter.setLeftside(new ArrayList<>(leftsideMap.values()));

		Set<String> rightsideIds=baselinkFilter.getRightsideIds();
		Map<String, Baseclass> rightsideMap=rightsideIds.isEmpty()?new HashMap<>():baselinkRepository.listByIds(Baseclass.class,rightsideIds,securityContext).stream().collect(Collectors.toMap(f->f.getId(), f->f));
		rightsideIds.removeAll(rightsideMap.keySet());
		if(!rightsideIds.isEmpty()){
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"no Baseclass(rightside) with ids "+rightsideIds);
		}
		baselinkFilter.setRightside(new ArrayList<>(rightsideMap.values()));

		Set<String> valuesIds=baselinkFilter.getValuesIds();
		Map<String, Baseclass> valuesMap=valuesIds.isEmpty()?new HashMap<>():baselinkRepository.listByIds(Baseclass.class,valuesIds,securityContext).stream().collect(Collectors.toMap(f->f.getId(), f->f));
		valuesIds.removeAll(valuesMap.keySet());
		if(!valuesIds.isEmpty()){
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"no Baseclass(value) with ids "+valuesIds);
		}
		baselinkFilter.setValues(new ArrayList<>(valuesMap.values()));
	}

	public <T extends Baseclass> T getByIdOrNull(String id,Class<T> c, SecurityContextBase securityContext) {
		return baselinkRepository.getByIdOrNull(id,c,securityContext);
	}

	public PaginationResponse<Baselink> getAllBaselinks(BaselinkFilter baselinkFilter, SecurityContextBase securityContext) {
		List<Baselink> list= listAllBaselinks(baselinkFilter, securityContext);
		long count=baselinkRepository.countAllBaselinks(baselinkFilter,securityContext);
		return new PaginationResponse<>(list,baselinkFilter,count);
	}

	public List<Baselink> listAllBaselinks(BaselinkFilter baselinkFilter, SecurityContextBase securityContext) {
		return baselinkRepository.listAllBaselinks(baselinkFilter, securityContext);
	}
}
