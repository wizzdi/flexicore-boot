package com.wizzdi.flexicore.security.service;

import com.flexicore.model.Baseclass;
import com.wizzdi.flexicore.security.configuration.SecurityContext;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.security.data.BaseclassRepository;
import com.wizzdi.flexicore.security.request.BaseclassFilter;
import com.wizzdi.flexicore.security.response.PaginationResponse;
import com.wizzdi.flexicore.security.request.BaseclassCreate;
import org.pf4j.Extension;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Component
@Extension
public class BaseclassService implements Plugin, InitializingBean {

	@Autowired
	private BasicService basicService;
	@Autowired
	private BaseclassRepository baseclassRepository;




	public boolean updateBaseclassNoMerge(BaseclassCreate baseclassCreate, Baseclass baseclass) {
		boolean update = basicService.updateBasicNoMerge(baseclassCreate,baseclass);


		return update;
	}

	public PaginationResponse<?> getAllBaseclass(BaseclassFilter baseclasssFilter, SecurityContext securityContext) {
		List<?> baseclasses = listAllBaseclass(baseclasssFilter, securityContext);
		long count = countAllBaseclass(baseclasssFilter, securityContext);
		return new PaginationResponse<>(baseclasses, baseclasssFilter, count);
	}

	private long countAllBaseclass(BaseclassFilter baseclasssFilter, SecurityContext securityContext) {
		if(baseclasssFilter.getClazzes()==null||baseclasssFilter.getClazzes().size()!=1){
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"listing baseclass generically only supports exactly one clazz to be provided");
		}
		String typeSimpleName = baseclasssFilter.getClazzes().getFirst().name();
		Class<?> type = baseclassRepository.getEntities().stream().map(f -> f.getJavaType()).filter(f -> f.getSimpleName().equals(typeSimpleName)).findFirst().orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "no entity of type %s".formatted(typeSimpleName)));
		if(Baseclass.class.isAssignableFrom(type)){
			return baseclassRepository.countAllBaseclass((Class<? extends Baseclass>) type, baseclasssFilter, securityContext);
		}
		return baseclassRepository.countAll(type,baseclasssFilter,securityContext);

	}

	public List<?> listAllBaseclass(BaseclassFilter baseclasssFilter, SecurityContext securityContext) {
		if(baseclasssFilter.getClazzes()==null||baseclasssFilter.getClazzes().size()!=1){
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"listing baseclass generically only supports exactly one clazz to be provided");
		}
		String typeSimpleName = baseclasssFilter.getClazzes().getFirst().name();
		Class<?> type = baseclassRepository.getEntities().stream().map(f -> f.getJavaType()).filter(f -> f.getSimpleName().equals(typeSimpleName)).findFirst().orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "no entity of type %s".formatted(typeSimpleName)));
		if(Baseclass.class.isAssignableFrom(type)){
			return baseclassRepository.listAllBaseclass((Class<? extends Baseclass>) type, baseclasssFilter, securityContext);
		}
		return baseclassRepository.listAll(type,baseclasssFilter,securityContext);

	}

	public static <T extends Baseclass> Baseclass createSecurityObjectNoMerge(T subject, SecurityContext securityContext) {

		return createSecurityObjectNoMerge(subject,true,securityContext);
	}
	public static <T extends Baseclass> Baseclass createSecurityObjectNoMerge(T subject,boolean updateTenant, SecurityContext securityContext) {
		subject.setSecurityId(subject.getId());
		if(securityContext==null){
			return subject;
		}
		subject.setCreator(securityContext.getUser());
		if(updateTenant||subject.getTenant()==null){
			subject.setTenant(securityContext.getTenantToCreateIn());
		}
		//TODO:clazz?
		return subject;
	}



	@Override
	public void afterPropertiesSet() throws Exception {
		baseclassRepository.createIndexes();
	}
}
