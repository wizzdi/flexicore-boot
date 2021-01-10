package com.wizzdi.flexicore.security.service;

import com.flexicore.model.Baseclass;
import com.flexicore.model.Clazz;
import com.flexicore.security.SecurityContextBase;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.security.data.ClazzRepository;
import com.wizzdi.flexicore.security.request.ClazzCreate;
import com.wizzdi.flexicore.security.request.ClazzFilter;
import com.wizzdi.flexicore.security.request.ClazzUpdate;
import com.wizzdi.flexicore.security.response.PaginationResponse;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Extension
@Component
public class ClazzService implements Plugin {

	@Autowired
	private BaseclassService baseclassService;
	@Autowired
	private ClazzRepository ClazzRepository;


	public Clazz createClazz(ClazzCreate ClazzCreate, SecurityContextBase securityContext){
		Clazz Clazz= createClazzNoMerge(ClazzCreate,securityContext);
		ClazzRepository.merge(Clazz);
		return Clazz;
	}

	public void merge(Object o){
		ClazzRepository.merge(o);
	}
	public void massMerge(List<Object> list){
		ClazzRepository.massMerge(list);
	}

	public <T extends Baseclass> List<T> listByIds(Class<T> c,Set<String> ids,  SecurityContextBase securityContext) {
		return ClazzRepository.listByIds(c, ids, securityContext);
	}

	public Clazz createClazzNoMerge(ClazzCreate ClazzCreate, SecurityContextBase securityContext){
		Clazz Clazz=new Clazz(ClazzCreate.getName(),securityContext);
		updateClazzNoMerge(ClazzCreate,Clazz);
		ClazzRepository.merge(Clazz);
		return Clazz;
	}

	public boolean updateClazzNoMerge(ClazzCreate ClazzCreate, Clazz Clazz) {
		return baseclassService.updateBaseclassNoMerge(ClazzCreate,Clazz);
	}

	public Clazz updateClazz(ClazzUpdate ClazzUpdate, SecurityContextBase securityContext){
		Clazz Clazz=ClazzUpdate.getClazz();
		if(updateClazzNoMerge(ClazzUpdate,Clazz)){
			ClazzRepository.merge(Clazz);
		}
		return Clazz;
	}

	public void validate(ClazzCreate ClazzCreate, SecurityContextBase securityContext) {
		baseclassService.validate(ClazzCreate,securityContext);
	}

	public void validate(ClazzFilter ClazzFilter, SecurityContextBase securityContext) {
	}

	public <T extends Baseclass> T getByIdOrNull(String id,Class<T> c, SecurityContextBase securityContext) {
		return ClazzRepository.getByIdOrNull(id,c,securityContext);
	}

	public PaginationResponse<Clazz> getAllClazzs(ClazzFilter ClazzFilter, SecurityContextBase securityContext) {
		List<Clazz> list= listAllClazzs(ClazzFilter, securityContext);
		long count=ClazzRepository.countAllClazzs(ClazzFilter,securityContext);
		return new PaginationResponse<>(list,ClazzFilter,count);
	}

	public List<Clazz> listAllClazzs(ClazzFilter ClazzFilter, SecurityContextBase securityContext) {
		return ClazzRepository.listAllClazzs(ClazzFilter, securityContext);
	}

	public <T extends Baseclass> List<T> findByIds(Class<T> c, Set<String> requested) {
		return ClazzRepository.findByIds(c, requested);
	}
}
