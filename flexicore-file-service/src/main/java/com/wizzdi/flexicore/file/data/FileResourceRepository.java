package com.wizzdi.flexicore.file.data;

import com.flexicore.model.Baseclass;
import com.flexicore.model.Basic;
import com.wizzdi.flexicore.security.configuration.SecurityContext;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.file.request.FileResourceFilter;
import com.wizzdi.flexicore.security.data.BasicRepository;
import com.wizzdi.flexicore.file.model.FileResource;
import com.wizzdi.flexicore.file.model.FileResource_;
import com.wizzdi.flexicore.security.data.SecuredBasicRepository;
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
public class FileResourceRepository implements Plugin {
	@PersistenceContext
	private EntityManager em;

	@Autowired
	private SecuredBasicRepository securedBasicRepository;


	public List<FileResource> listAllFileResources(FileResourceFilter FileResourceFilter, SecurityContext securityContext) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<FileResource> q = cb.createQuery(FileResource.class);
		Root<FileResource> r = q.from(FileResource.class);
		List<Predicate> predicates = new ArrayList<>();
		addFileResourcePredicates(FileResourceFilter, cb, q, r, predicates, securityContext);
		q.select(r).where(predicates.toArray(Predicate[]::new));
		TypedQuery<FileResource> query = em.createQuery(q);
		BasicRepository.addPagination(FileResourceFilter, query);
		return query.getResultList();

	}

	public <T extends FileResource> void addFileResourcePredicates(FileResourceFilter fileResourceFilter, CriteriaBuilder cb, CommonAbstractCriteria q, From<?, T> r, List<Predicate> predicates, SecurityContext securityContext) {
		
		securedBasicRepository.addSecuredBasicPredicates(fileResourceFilter.getBasicPropertiesFilter(),cb,q,r,predicates, securityContext);

		if(fileResourceFilter.getMd5s()!=null&&!fileResourceFilter.getMd5s().isEmpty()){
			predicates.add(r.get(FileResource_.md5).in(fileResourceFilter.getMd5s()));
		}



	}

	public long countAllFileResources(FileResourceFilter FileResourceFilter, SecurityContext securityContext) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Long> q = cb.createQuery(Long.class);
		Root<FileResource> r = q.from(FileResource.class);
		List<Predicate> predicates = new ArrayList<>();
		addFileResourcePredicates(FileResourceFilter, cb, q, r, predicates, securityContext);
		q.select(cb.count(r)).where(predicates.toArray(Predicate[]::new));
		TypedQuery<Long> query = em.createQuery(q);
		return query.getSingleResult();

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
