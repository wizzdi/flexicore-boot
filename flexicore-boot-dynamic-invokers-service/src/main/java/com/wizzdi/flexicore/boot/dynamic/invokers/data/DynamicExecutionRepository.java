package com.wizzdi.flexicore.boot.dynamic.invokers.data;

import com.flexicore.model.Baseclass;
import com.flexicore.model.Basic;
import com.flexicore.model.Basic_;
import com.wizzdi.flexicore.security.configuration.SecurityContext;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.boot.dynamic.invokers.model.DynamicExecution;
import com.wizzdi.flexicore.boot.dynamic.invokers.model.DynamicExecution_;
import com.wizzdi.flexicore.boot.dynamic.invokers.model.ServiceCanonicalName;
import com.wizzdi.flexicore.boot.dynamic.invokers.model.ServiceCanonicalName_;
import com.wizzdi.flexicore.boot.dynamic.invokers.request.DynamicExecutionFilter;
import com.wizzdi.flexicore.boot.dynamic.invokers.request.DynamicInvokerFilter;
import com.wizzdi.flexicore.boot.dynamic.invokers.request.DynamicInvokerMethodFilter;
import com.wizzdi.flexicore.security.data.BasicRepository;
import com.wizzdi.flexicore.security.data.SecuredBasicRepository;
import com.wizzdi.flexicore.security.request.BasicPropertiesFilter;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import jakarta.persistence.metamodel.SingularAttribute;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
@Extension
public class DynamicExecutionRepository implements Plugin {
	@PersistenceContext
	private EntityManager em;
	@Autowired
	private SecuredBasicRepository securedBasicRepository;

	public List<DynamicExecution> listAllDynamicExecutions(DynamicExecutionFilter dynamicExecutionFilter, SecurityContext securityContext) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<DynamicExecution> q = cb.createQuery(DynamicExecution.class);
		Root<DynamicExecution> r = q.from(DynamicExecution.class);
		List<Predicate> predicates = new ArrayList<>();
		addDynamicExecutionPredicates(dynamicExecutionFilter, cb, q, r, predicates, securityContext);
		q.select(r).where(predicates.toArray(Predicate[]::new)).orderBy(cb.asc(r.get(Basic_.name)));
		TypedQuery<DynamicExecution> query = em.createQuery(q);
		BasicRepository.addPagination(dynamicExecutionFilter, query);
		return query.getResultList();

	}


	public <T extends DynamicExecution> void addDynamicExecutionPredicates(DynamicExecutionFilter dynamicExecutionFilter, CriteriaBuilder cb, CommonAbstractCriteria q, From<?,T> r, List<Predicate> predicates, SecurityContext securityContext) {
		securedBasicRepository.addSecuredBasicPredicates(dynamicExecutionFilter.getBasicPropertiesFilter(),cb,q,r,predicates,securityContext);
		DynamicInvokerMethodFilter dynamicInvokerMethodFilter = dynamicExecutionFilter.getDynamicInvokerMethodFilter();
		if(dynamicInvokerMethodFilter!=null){
			addDynamicInvokerMethodPredicates(cb, r, predicates, dynamicInvokerMethodFilter);
		}
	}

	private <T extends DynamicExecution> void addDynamicInvokerMethodPredicates(CriteriaBuilder cb, From<?, T> r, List<Predicate> predicates, DynamicInvokerMethodFilter dynamicInvokerMethodFilter) {
		BasicPropertiesFilter basicPropertiesFilter = dynamicInvokerMethodFilter.getBasicPropertiesFilter();
		if(basicPropertiesFilter !=null){
			String nameLike = basicPropertiesFilter.getNameLike();
			if(nameLike !=null){
				if(basicPropertiesFilter.isNameLikeCaseSensitive()){
					predicates.add(cb.like(r.get(DynamicExecution_.methodName), nameLike));
				}
				else{
					predicates.add(cb.like(cb.lower(r.get(DynamicExecution_.methodName)), nameLike.toLowerCase()));

				}
			}
			if(basicPropertiesFilter.getNames()!=null&&!basicPropertiesFilter.getNames().isEmpty()){
				predicates.add(r.get(DynamicExecution_.methodName).in(basicPropertiesFilter.getNames()));

			}
		}
		boolean byCategories = dynamicInvokerMethodFilter.getCategories() != null && !dynamicInvokerMethodFilter.getCategories().isEmpty();
		if(byCategories || dynamicInvokerMethodFilter.isEmptyCategories()){
			Predicate pred=dynamicInvokerMethodFilter.isEmptyCategories()?r.get(DynamicExecution_.category).isNull():r.get(DynamicExecution_.category).isNotNull();
			if(byCategories){
				pred=cb.or(pred, r.get(DynamicExecution_.category).in(dynamicInvokerMethodFilter.getCategories()));

			}
			predicates.add(pred);
		}
		DynamicInvokerFilter dynamicInvokerFilter = dynamicInvokerMethodFilter.getDynamicInvokerFilter();
		if(dynamicInvokerFilter !=null){
			addDynamicInvokersPredicates(cb, r, predicates, dynamicInvokerFilter);
		}

	}

	private <T extends DynamicExecution> void addDynamicInvokersPredicates(CriteriaBuilder cb, From<?, T> r, List<Predicate> predicates, DynamicInvokerFilter dynamicInvokerFilter) {
		Join<T,ServiceCanonicalName> join=null;

		BasicPropertiesFilter basicPropertiesFilter = dynamicInvokerFilter.getBasicPropertiesFilter();
		if(basicPropertiesFilter !=null){
			String nameLike = basicPropertiesFilter.getNameLike();
			if(nameLike !=null){
				join=join==null?r.join(DynamicExecution_.serviceCanonicalNames):join;
				if(basicPropertiesFilter.isNameLikeCaseSensitive()){
					predicates.add(cb.like(join.get(ServiceCanonicalName_.serviceCanonicalName), nameLike));
				}
				else{
					predicates.add(cb.like(cb.lower(join.get(ServiceCanonicalName_.serviceCanonicalName)), nameLike.toLowerCase()));
				}
			}
			if(basicPropertiesFilter.getNames()!=null&&!basicPropertiesFilter.getNames().isEmpty()){
				join=join==null?r.join(DynamicExecution_.serviceCanonicalNames):join;
				predicates.add(join.get(ServiceCanonicalName_.serviceCanonicalName).in(basicPropertiesFilter.getNames()));

			}

		}
		if(dynamicInvokerFilter.getInvokerTypes()!=null&&!dynamicInvokerFilter.getInvokerTypes().isEmpty()){
			join=join==null?r.join(DynamicExecution_.serviceCanonicalNames):join;
			predicates.add(join.get(ServiceCanonicalName_.serviceCanonicalName).in(dynamicInvokerFilter.getInvokerTypes()));
		}
		if(dynamicInvokerFilter.getHandlingTypeLike()!=null){
			join=join==null?r.join(DynamicExecution_.serviceCanonicalNames):join;
			if(dynamicInvokerFilter.isHandlingTypeLikeCaseSensitive()){
				predicates.add(cb.like(join.get(ServiceCanonicalName_.serviceCanonicalName),(dynamicInvokerFilter.getHandlingTypeLike())));
			}
			else{
				predicates.add(cb.like(cb.lower(join.get(ServiceCanonicalName_.serviceCanonicalName)),(dynamicInvokerFilter.getHandlingTypeLike().toLowerCase())));

			}

		}
	}

	public long countAllDynamicExecutions(DynamicExecutionFilter dynamicExecutionFilter, SecurityContext securityContext) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Long> q = cb.createQuery(Long.class);
		Root<DynamicExecution> r = q.from(DynamicExecution.class);
		List<Predicate> predicates = new ArrayList<>();
		addDynamicExecutionPredicates(dynamicExecutionFilter, cb, q, r, predicates, securityContext);
		q.select(cb.count(r)).where(predicates.toArray(Predicate[]::new));
		TypedQuery<Long> query = em.createQuery(q);
		return query.getSingleResult();

	}

	public List<ServiceCanonicalName> getAllServiceCanonicalNames(DynamicExecution dynamicExecution) {

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<ServiceCanonicalName> q = cb.createQuery(ServiceCanonicalName.class);
		Root<ServiceCanonicalName> r = q.from(ServiceCanonicalName.class);
		q.select(r).where(cb.equal(r.get(ServiceCanonicalName_.dynamicExecution),dynamicExecution));
		return em.createQuery(q).getResultList();
	}

	public <T extends Baseclass> List<T> listByIds(Class<T> c, Set<String> ids, SecurityContext securityContext) {
		return securedBasicRepository.listByIds(c, ids, securityContext);
	}

	public <T extends Baseclass> T getByIdOrNull(String id, Class<T> c, SecurityContext securityContext) {
		return securedBasicRepository.getByIdOrNull(id, c, securityContext);
	}

	public <D extends Basic, E extends Baseclass, T extends D> T getByIdOrNull(String id, Class<T> c, SingularAttribute<D, E> baseclassAttribute, SecurityContext securityContext) {
		return securedBasicRepository.getByIdOrNull(id, c, baseclassAttribute, securityContext);
	}

	public <D extends Basic, E extends Baseclass, T extends D> List<T> listByIds(Class<T> c, Set<String> ids, SingularAttribute<D, E> baseclassAttribute, SecurityContext securityContext) {
		return securedBasicRepository.listByIds(c, ids, baseclassAttribute, securityContext);
	}

	public <D extends Basic, T extends D> List<T> findByIds(Class<T> c, Set<String> ids, SingularAttribute<D, String> idAttribute) {
		return securedBasicRepository.findByIds(c, ids, idAttribute);
	}

	public <T extends Basic> List<T> findByIds(Class<T> c, Set<String> requested) {
		return securedBasicRepository.findByIds(c, requested);
	}

	public <T> T findByIdOrNull(Class<T> type, String id) {
		return securedBasicRepository.findByIdOrNull(type, id);
	}

	public void merge(Object base) {
		securedBasicRepository.merge(base);
	}

	public void massMerge(List<?> toMerge) {
		securedBasicRepository.massMerge(toMerge);
	}
}
