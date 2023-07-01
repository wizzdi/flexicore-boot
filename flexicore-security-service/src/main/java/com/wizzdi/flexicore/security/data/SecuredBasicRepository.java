package com.wizzdi.flexicore.security.data;

import com.flexicore.model.Baseclass;
import com.flexicore.model.Basic;
import com.flexicore.model.SecuredBasic;
import com.flexicore.model.SecuredBasic_;
import com.flexicore.security.SecurityContextBase;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.security.request.BasicPropertiesFilter;
import com.wizzdi.flexicore.security.request.SoftDeleteOption;
import org.pf4j.Extension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.criteria.*;
import jakarta.persistence.metamodel.SingularAttribute;
import java.util.List;
import java.util.Set;


@Extension
@Component
public class SecuredBasicRepository implements Plugin {
	private static final Logger logger = LoggerFactory.getLogger(SecuredBasicRepository.class);
	@Autowired
	private BaseclassRepository baseclassRepository;


	public <T extends SecuredBasic> void addSecuredBasicPredicates(BasicPropertiesFilter basicPropertiesFilter, CriteriaBuilder cb, CommonAbstractCriteria q, From<?, T> r, List<Predicate> predicates, SecurityContextBase securityContextBase) {
		if(basicPropertiesFilter!=null){
			BasicRepository.addBasicPropertiesFilter(basicPropertiesFilter,cb,q,r,predicates);
		}
		else{
			BasicRepository.addBasicPropertiesFilter(new BasicPropertiesFilter().setSoftDelete(SoftDeleteOption.DEFAULT),cb,q,r,predicates);
		}
		if(securityContextBase!=null){
			Join<T, Baseclass> join= r.join(SecuredBasic_.security);
			baseclassRepository.addBaseclassPredicates(cb,q,join,predicates,securityContextBase);
		}
	}

	public <T extends Baseclass> void addBaseclassPredicates(BasicPropertiesFilter basicPropertiesFilter, CriteriaBuilder cb, CommonAbstractCriteria q, From<?, T> r, List<Predicate> predicates, SecurityContextBase securityContextBase) {
		if(basicPropertiesFilter!=null){
			BasicRepository.addBasicPropertiesFilter(basicPropertiesFilter,cb,q,r,predicates);
		}
		else{
			BasicRepository.addBasicPropertiesFilter(new BasicPropertiesFilter().setSoftDelete(SoftDeleteOption.DEFAULT),cb,q,r,predicates);
		}
		if(securityContextBase!=null){
			baseclassRepository.addBaseclassPredicates(cb,q,r,predicates,securityContextBase);
		}
	}

	@Transactional
	public <T> T merge(T base, boolean updateDate, boolean propagateEvents) {
		return baseclassRepository.merge(base, updateDate, propagateEvents);
	}

	@Transactional
	public void massMerge(List<?> toMerge, boolean updatedate, boolean propagateEvents) {
		baseclassRepository.massMerge(toMerge, updatedate, propagateEvents);
	}

	public <T extends Baseclass> List<T> listByIds(Class<T> c, Set<String> ids, SecurityContextBase securityContext) {
		return baseclassRepository.listByIds(c, ids, securityContext);
	}

	public <T extends Baseclass> T getByIdOrNull(String id, Class<T> c, SecurityContextBase securityContext) {
		return baseclassRepository.getByIdOrNull(id, c, securityContext);
	}

	public <D extends Basic, E extends Baseclass, T extends D> T getByIdOrNull(String id, Class<T> c, SingularAttribute<D, E> baseclassAttribute, SecurityContextBase securityContext) {
		return baseclassRepository.getByIdOrNull(id, c, baseclassAttribute, securityContext);
	}

	public <D extends Basic, E extends Baseclass, T extends D> List<T> listByIds(Class<T> c, Set<String> ids, SingularAttribute<D, E> baseclassAttribute, SecurityContextBase securityContext) {
		return baseclassRepository.listByIds(c, ids, baseclassAttribute, securityContext);
	}

	public <D extends Basic, T extends D> List<T> findByIds(Class<T> c, Set<String> ids, SingularAttribute<D, String> idAttribute) {
		return baseclassRepository.findByIds(c, ids, idAttribute);
	}

	public <T extends Basic> List<T> findByIds(Class<T> c, Set<String> requested) {
		return baseclassRepository.findByIds(c, requested);
	}

	public <T> T findByIdOrNull(Class<T> type, String id) {
		return baseclassRepository.findByIdOrNull(type, id);
	}

	@Transactional
	public void merge(Object base) {
		baseclassRepository.merge(base);
	}

	@Transactional
	public void massMerge(List<?> toMerge) {
		baseclassRepository.massMerge(toMerge);
	}
}
