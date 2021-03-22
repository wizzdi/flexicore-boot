package com.wizzdi.flexicore.file.data;

import com.flexicore.model.Baseclass;
import com.flexicore.model.Basic;
import com.flexicore.security.SecurityContextBase;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.file.model.ZipFile;
import com.wizzdi.flexicore.file.request.ZipFileFilter;
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
public class ZipFileRepository implements Plugin {
	@PersistenceContext
	private EntityManager em;
	@Autowired
	private FileResourceRepository fileResourceRepository;


	public List<ZipFile> listAllZipFiles(ZipFileFilter ZipFileFilter, SecurityContextBase securityContextBase) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<ZipFile> q = cb.createQuery(ZipFile.class);
		Root<ZipFile> r = q.from(ZipFile.class);
		List<Predicate> predicates = new ArrayList<>();
		addZipFilePredicates(ZipFileFilter, cb, q, r, predicates, securityContextBase);
		q.select(r).where(predicates.toArray(Predicate[]::new));
		TypedQuery<ZipFile> query = em.createQuery(q);
		BasicRepository.addPagination(ZipFileFilter, query);
		return query.getResultList();

	}

	public <T extends ZipFile> void addZipFilePredicates(ZipFileFilter zipFileFilter, CriteriaBuilder cb, CommonAbstractCriteria q, From<?, T> r, List<Predicate> predicates, SecurityContextBase securityContextBase) {
		fileResourceRepository.addFileResourcePredicates(zipFileFilter,cb,q,r,predicates,securityContextBase);

	}

	public long countAllZipFiles(ZipFileFilter ZipFileFilter, SecurityContextBase securityContextBase) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Long> q = cb.createQuery(Long.class);
		Root<ZipFile> r = q.from(ZipFile.class);
		List<Predicate> predicates = new ArrayList<>();
		addZipFilePredicates(ZipFileFilter, cb, q, r, predicates, securityContextBase);
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
		return fileResourceRepository.listByIds(c, ids, securityContextBase);
	}

	public <T extends Baseclass> T getByIdOrNull(String id, Class<T> c, SecurityContextBase securityContextBase) {
		return fileResourceRepository.getByIdOrNull(id, c, securityContextBase);
	}

	public <T extends Baseclass> List<T> findByIds(Class<T> c, Set<String> requested) {
		return fileResourceRepository.findByIds(c, requested);
	}


	public <D extends Basic, E extends Baseclass, T extends D> T getByIdOrNull(String id, Class<T> c, SingularAttribute<D, E> baseclassAttribute, SecurityContextBase securityContextBase) {
		return fileResourceRepository.getByIdOrNull(id, c, baseclassAttribute, securityContextBase);
	}

	public <D extends Basic, E extends Baseclass, T extends D> List<T> listByIds(Class<T> c, Set<String> ids, SingularAttribute<D, E> baseclassAttribute, SecurityContextBase securityContextBase) {
		return fileResourceRepository.listByIds(c, ids, baseclassAttribute, securityContextBase);
	}

	public <D extends Basic, T extends D> List<T> findByIds(Class<T> c, Set<String> ids, SingularAttribute<D, String> idAttribute) {
		return fileResourceRepository.findByIds(c, ids, idAttribute);
	}

	public <T> T findByIdOrNull(Class<T> type, String id) {
		return fileResourceRepository.findByIdOrNull(type, id);
	}
}
