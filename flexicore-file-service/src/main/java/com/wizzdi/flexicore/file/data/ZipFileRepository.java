package com.wizzdi.flexicore.file.data;

import com.flexicore.model.Baseclass;
import com.flexicore.model.Basic;
import com.wizzdi.flexicore.security.configuration.SecurityContext;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.file.model.ZipFile;
import com.wizzdi.flexicore.file.model.ZipFile_;
import com.wizzdi.flexicore.file.request.ZipFileFilter;
import com.wizzdi.flexicore.security.data.BasicRepository;
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
public class ZipFileRepository implements Plugin {
	@PersistenceContext
	private EntityManager em;
	@Autowired
	private FileResourceRepository fileResourceRepository;


	public List<ZipFile> listAllZipFiles(ZipFileFilter ZipFileFilter, SecurityContext securityContext) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<ZipFile> q = cb.createQuery(ZipFile.class);
		Root<ZipFile> r = q.from(ZipFile.class);
		List<Predicate> predicates = new ArrayList<>();
		addZipFilePredicates(ZipFileFilter, cb, q, r, predicates, securityContext);
		q.select(r).where(predicates.toArray(Predicate[]::new));
		TypedQuery<ZipFile> query = em.createQuery(q);
		BasicRepository.addPagination(ZipFileFilter, query);
		return query.getResultList();

	}

	public <T extends ZipFile> void addZipFilePredicates(ZipFileFilter zipFileFilter, CriteriaBuilder cb, CommonAbstractCriteria q, From<?, T> r, List<Predicate> predicates, SecurityContext securityContext) {
		fileResourceRepository.addFileResourcePredicates(zipFileFilter,cb,q,r,predicates, securityContext);
		if(zipFileFilter.getUniqueFilesMd5()!=null){
			predicates.add(cb.equal(r.get(ZipFile_.uniqueFilesMd5),zipFileFilter.getUniqueFilesMd5()));
		}

	}

	public long countAllZipFiles(ZipFileFilter ZipFileFilter, SecurityContext securityContext) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Long> q = cb.createQuery(Long.class);
		Root<ZipFile> r = q.from(ZipFile.class);
		List<Predicate> predicates = new ArrayList<>();
		addZipFilePredicates(ZipFileFilter, cb, q, r, predicates, securityContext);
		q.select(cb.count(r)).where(predicates.toArray(Predicate[]::new));
		TypedQuery<Long> query = em.createQuery(q);
		return query.getSingleResult();

	}


	public <T extends Baseclass> List<T> listByIds(Class<T> c, Set<String> ids, SecurityContext securityContext) {
		return fileResourceRepository.listByIds(c, ids, securityContext);
	}

	public <T extends Baseclass> T getByIdOrNull(String id, Class<T> c, SecurityContext securityContext) {
		return fileResourceRepository.getByIdOrNull(id, c, securityContext);
	}

	public <D extends Basic, E extends Baseclass, T extends D> T getByIdOrNull(String id, Class<T> c, SingularAttribute<D, E> baseclassAttribute, SecurityContext securityContext) {
		return fileResourceRepository.getByIdOrNull(id, c, baseclassAttribute, securityContext);
	}

	public <D extends Basic, E extends Baseclass, T extends D> List<T> listByIds(Class<T> c, Set<String> ids, SingularAttribute<D, E> baseclassAttribute, SecurityContext securityContext) {
		return fileResourceRepository.listByIds(c, ids, baseclassAttribute, securityContext);
	}

	public <D extends Basic, T extends D> List<T> findByIds(Class<T> c, Set<String> ids, SingularAttribute<D, String> idAttribute) {
		return fileResourceRepository.findByIds(c, ids, idAttribute);
	}

	public <T extends Basic> List<T> findByIds(Class<T> c, Set<String> requested) {
		return fileResourceRepository.findByIds(c, requested);
	}

	public <T> T findByIdOrNull(Class<T> type, String id) {
		return fileResourceRepository.findByIdOrNull(type, id);
	}

	public void merge(Object base) {
		fileResourceRepository.merge(base);
	}

	public void massMerge(List<?> toMerge) {
		fileResourceRepository.massMerge(toMerge);
	}
}
