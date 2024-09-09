package com.wizzdi.flexicore.security.service;

import com.flexicore.model.Baseclass;
import com.flexicore.model.Basic;
import com.flexicore.model.Clazz;
import com.flexicore.security.SecurityContextBase;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.security.data.ClazzRepository;
import com.wizzdi.flexicore.security.request.ClazzCreate;
import com.wizzdi.flexicore.security.request.ClazzFilter;
import com.wizzdi.flexicore.security.request.ClazzUpdate;
import com.wizzdi.flexicore.security.response.PaginationResponse;
import jakarta.persistence.metamodel.SingularAttribute;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Extension
@Component
public class ClazzService implements Plugin {

	@Autowired
	private BasicService basicService;
	@Autowired
	private ClazzRepository ClazzRepository;


	public Clazz createClazz(ClazzCreate ClazzCreate, SecurityContextBase securityContext){
		Clazz Clazz= createClazzNoMerge(ClazzCreate,securityContext);
		ClazzRepository.merge(Clazz);
		return Clazz;
	}


	public Clazz createClazzNoMerge(ClazzCreate ClazzCreate, SecurityContextBase securityContext){
		Clazz clazz=new Clazz();
		clazz.setId(UUID.randomUUID().toString());
		updateClazzNoMerge(ClazzCreate,clazz);
		BaseclassService.createSecurityObjectNoMerge(clazz,securityContext);
		ClazzRepository.merge(clazz);
		return clazz;
	}

	public boolean updateClazzNoMerge(ClazzCreate ClazzCreate, Clazz Clazz) {
		return basicService.updateBasicNoMerge(ClazzCreate,Clazz);
	}

	public Clazz updateClazz(ClazzUpdate ClazzUpdate, SecurityContextBase securityContext){
		Clazz Clazz=ClazzUpdate.getClazz();
		if(updateClazzNoMerge(ClazzUpdate,Clazz)){
			ClazzRepository.merge(Clazz);
		}
		return Clazz;
	}




	public PaginationResponse<Clazz> getAllClazzs(ClazzFilter ClazzFilter, SecurityContextBase securityContext) {
		List<Clazz> list= listAllClazzs(ClazzFilter, securityContext);
		long count=ClazzRepository.countAllClazzs(ClazzFilter,securityContext);
		return new PaginationResponse<>(list,ClazzFilter,count);
	}

	public List<Clazz> listAllClazzs(ClazzFilter ClazzFilter, SecurityContextBase securityContext) {
		return ClazzRepository.listAllClazzs(ClazzFilter, securityContext);
	}

	public <T extends Baseclass> List<T> listByIds(Class<T> c, Set<String> ids, SecurityContextBase securityContext) {
		return ClazzRepository.listByIds(c, ids, securityContext);
	}

	public <T extends Baseclass> T getByIdOrNull(String id, Class<T> c, SecurityContextBase securityContext) {
		return ClazzRepository.getByIdOrNull(id, c, securityContext);
	}

	public <D extends Basic, E extends Baseclass, T extends D> T getByIdOrNull(String id, Class<T> c, SingularAttribute<D, E> baseclassAttribute, SecurityContextBase securityContext) {
		return ClazzRepository.getByIdOrNull(id, c, baseclassAttribute, securityContext);
	}

	public <D extends Basic, E extends Baseclass, T extends D> List<T> listByIds(Class<T> c, Set<String> ids, SingularAttribute<D, E> baseclassAttribute, SecurityContextBase securityContext) {
		return ClazzRepository.listByIds(c, ids, baseclassAttribute, securityContext);
	}

	public <D extends Basic, T extends D> List<T> findByIds(Class<T> c, Set<String> ids, SingularAttribute<D, String> idAttribute) {
		return ClazzRepository.findByIds(c, ids, idAttribute);
	}

	public <T extends Basic> List<T> findByIds(Class<T> c, Set<String> requested) {
		return ClazzRepository.findByIds(c, requested);
	}

	public <T> T findByIdOrNull(Class<T> type, String id) {
		return ClazzRepository.findByIdOrNull(type, id);
	}


	public <T> T merge(T base) {
		return ClazzRepository.merge(base);
	}


	public void massMerge(List<?> toMerge) {
		ClazzRepository.massMerge(toMerge);
	}
}
