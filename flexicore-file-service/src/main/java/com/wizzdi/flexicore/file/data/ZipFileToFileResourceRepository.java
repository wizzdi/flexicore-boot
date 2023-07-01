package com.wizzdi.flexicore.file.data;

import com.flexicore.model.Baseclass;
import com.flexicore.model.Basic;
import com.flexicore.security.SecurityContextBase;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.file.model.*;
import com.wizzdi.flexicore.file.request.ZipFileToFileResourceFilter;
import com.wizzdi.flexicore.security.data.BaseclassRepository;
import com.wizzdi.flexicore.security.data.BasicRepository;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import jakarta.persistence.metamodel.SingularAttribute;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Extension
public class ZipFileToFileResourceRepository implements Plugin {
	@PersistenceContext
	private EntityManager em;
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
		if(zipFileToFileResourceFilter.getZipFiles()!=null&&!zipFileToFileResourceFilter.getZipFiles().isEmpty()){
			Set<String> ids=zipFileToFileResourceFilter.getZipFiles().stream().map(f->f.getId()).collect(Collectors.toSet());
			Join<T, ZipFile> join=r.join(ZipFileToFileResource_.zipFile);
			predicates.add(join.get(ZipFile_.id).in(ids));
		}

		if(zipFileToFileResourceFilter.getFileResources()!=null&&!zipFileToFileResourceFilter.getFileResources().isEmpty()){
			Set<String> ids=zipFileToFileResourceFilter.getFileResources().stream().map(f->f.getId()).collect(Collectors.toSet());
			Join<T, FileResource> join=r.join(ZipFileToFileResource_.zippedFile);
			predicates.add(join.get(FileResource_.id).in(ids));
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

	public <D extends Basic, T extends D> List<T> findByIds(Class<T> c, Set<String> ids, SingularAttribute<D, String> idAttribute) {
		return basicRepository.findByIds(c, ids, idAttribute);
	}

	public <T extends Basic> List<T> findByIds(Class<T> c, Set<String> requested) {
		return basicRepository.findByIds(c, requested);
	}

	public <T> T findByIdOrNull(Class<T> type, String id) {
		return basicRepository.findByIdOrNull(type, id);
	}

	@Transactional
	public void merge(Object base) {
		basicRepository.merge(base);
	}

	@Transactional
	public void merge(Object base, boolean updateDate) {
		basicRepository.merge(base, updateDate);
	}

	@Transactional
	public void massMerge(List<?> toMerge) {
		basicRepository.massMerge(toMerge);
	}

	@Transactional
	public void massMerge(List<?> toMerge, boolean updatedate) {
		basicRepository.massMerge(toMerge, updatedate);
	}
}
