package com.wizzdi.flexicore.file.data;

import com.flexicore.model.Baseclass;
import com.flexicore.model.Basic;
import com.flexicore.security.SecurityContextBase;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.file.model.ZipFileToFileResource;
import com.wizzdi.flexicore.file.request.ZipFileToFileResourceFilter;
import com.wizzdi.flexicore.security.data.BaseclassRepository;
import com.wizzdi.flexicore.security.data.BasicRepository;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import javax.persistence.metamodel.SingularAttribute;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
@Extension
public class ZipFileToFileResourceRepository implements Plugin {
	@PersistenceContext
	private EntityManager em;
	@Autowired
	private BaseclassRepository baseclassRepository;
	@Autowired
	private BasicRepository basicRepository;


	public List<ZipFileToFileResource> listAllZipFileToFileResources(ZipFileToFileResourceFilter ZipFileToFileResourceFilter, SecurityContextBase securityContextBase) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<ZipFileToFileResource> q = cb.createQuery(ZipFileToFileResource.class);
		Root<ZipFileToFileResource> r = q.from(ZipFileToFileResource.class);
		List<Predicate> predicates = new ArrayList<>();
		addZipFileToFileResourcePredicates(ZipFileToFileResourceFilter, cb, q, r, predicates, securityContextBase);
		q.select(r).where(predicates.toArray(Predicate[]::new));
		TypedQuery<ZipFileToFileResource> query = em.createQuery(q);
		BasicRepository.addPagination(ZipFileToFileResourceFilter, query);
		return query.getResultList();

	}

	public <T extends ZipFileToFileResource> void addZipFileToFileResourcePredicates(ZipFileToFileResourceFilter zipFileToFileResourceFilter, CriteriaBuilder cb, CommonAbstractCriteria q, From<?, T> r, List<Predicate> predicates, SecurityContextBase securityContextBase) {
		
		if(zipFileToFileResourceFilter.getBasicPropertiesFilter()!=null){
			BasicRepository.addBasicPropertiesFilter(zipFileToFileResourceFilter.getBasicPropertiesFilter(),cb,q,r,predicates);
		}

	}

	public long countAllZipFileToFileResources(ZipFileToFileResourceFilter ZipFileToFileResourceFilter, SecurityContextBase securityContextBase) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Long> q = cb.createQuery(Long.class);
		Root<ZipFileToFileResource> r = q.from(ZipFileToFileResource.class);
		List<Predicate> predicates = new ArrayList<>();
		addZipFileToFileResourcePredicates(ZipFileToFileResourceFilter, cb, q, r, predicates, securityContextBase);
		q.select(cb.count(r)).where(predicates.toArray(Predicate[]::new));
		TypedQuery<Long> query = em.createQuery(q);
		return query.getSingleResult();

	}

	@Transactional
	public void merge(Object o) {
		em.merge(o);
	}

	@Transactional
	public void massMerge(List<Object> list) {
		for (Object o : list) {
			em.merge(o);
		}
	}

	public <T extends Baseclass> List<T> listByIds(Class<T> c, Set<String> ids, SecurityContextBase securityContextBase) {
		return baseclassRepository.listByIds(c, ids, securityContextBase);
	}

	public <T extends Baseclass> T getByIdOrNull(String id, Class<T> c, SecurityContextBase securityContextBase) {
		return baseclassRepository.getByIdOrNull(id, c, securityContextBase);
	}

	public <T extends Baseclass> List<T> findByIds(Class<T> c, Set<String> requested) {
		return baseclassRepository.findByIds(c, requested);
	}


	public <D extends Basic, E extends Baseclass, T extends D> T getByIdOrNull(String id, Class<T> c, SingularAttribute<D, E> baseclassAttribute, SecurityContextBase securityContextBase) {
		return baseclassRepository.getByIdOrNull(id, c, baseclassAttribute, securityContextBase);
	}

	public <D extends Basic, E extends Baseclass, T extends D> List<T> listByIds(Class<T> c, Set<String> ids, SingularAttribute<D, E> baseclassAttribute, SecurityContextBase securityContextBase) {
		return baseclassRepository.listByIds(c, ids, baseclassAttribute, securityContextBase);
	}

	public <D extends Basic, T extends D> List<T> findByIds(Class<T> c, Set<String> ids, SingularAttribute<D, String> idAttribute) {
		return baseclassRepository.findByIds(c, ids, idAttribute);
	}

	public <T> T findByIdOrNull(Class<T> type, String id) {
		return baseclassRepository.findByIdOrNull(type, id);
	}
}
