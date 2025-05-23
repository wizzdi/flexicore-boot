package com.wizzdi.flexicore.security.data;

import com.flexicore.model.*;

import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.security.request.BaseclassFilter;
import com.wizzdi.flexicore.security.request.BasicPropertiesFilter;
import com.wizzdi.flexicore.security.request.SoftDeleteOption;
import com.wizzdi.flexicore.security.configuration.SecurityContext;
import com.wizzdi.segmantix.service.SecurityRepository;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Table;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import jakarta.persistence.metamodel.EntityType;
import jakarta.persistence.metamodel.Metamodel;
import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.transaction.Transactional;
import org.pf4j.Extension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
@Extension
public class BaseclassRepository implements Plugin {

	private static final Logger logger = LoggerFactory.getLogger(BaseclassRepository.class);
	@PersistenceContext
	private EntityManager em;
	@Autowired
	private SecurityRepository securityRepository;
	@Value("${flexicore.baseclass.createIndexes:true}")
	private boolean createIndexes;

	@Value("${flexicore.baseclass.recreateIndexes:false}")
	private boolean recreateIndexes;



	@Autowired
	private BasicRepository basicRepository;


	public <T extends Baseclass> List<T> listAllBaseclass(Class<T> c,BaseclassFilter baseclassFilter, SecurityContext securityContext){
		CriteriaBuilder cb=em.getCriteriaBuilder();
		CriteriaQuery<T> q=cb.createQuery(c);
		Root<T> r=q.from(c);
		List<Predicate> predicates=new ArrayList<>();
		addBaseclassPredicates(baseclassFilter,cb,q,r,predicates,securityContext);
		q.select(r).where(predicates.toArray(Predicate[]::new)).orderBy(cb.asc(r.get(Baseclass_.name)));
		TypedQuery<T> query = em.createQuery(q);
		BasicRepository.addPagination(baseclassFilter,query);
		return query.getResultList();

	}

	public <T extends Baseclass> void addBaseclassPredicates(BaseclassFilter baseclassFilter, CriteriaBuilder cb, CommonAbstractCriteria q, From<?,T> r, List<Predicate> predicates, SecurityContext securityContext) {
		addBaseclassPredicates(baseclassFilter.getBasicPropertiesFilter(),cb,q,r,predicates,securityContext);
	}

	public <T extends Baseclass> long countAllBaseclass(Class<T> c,BaseclassFilter baseclassFilter,SecurityContext securityContext){
		CriteriaBuilder cb=em.getCriteriaBuilder();
		CriteriaQuery<Long> q=cb.createQuery(Long.class);
		Root<T> r=q.from(c);
		List<Predicate> predicates=new ArrayList<>();
		addBaseclassPredicates(baseclassFilter,cb,q,r,predicates,securityContext);
		q.select(cb.count(r)).where(predicates.toArray(Predicate[]::new));
		TypedQuery<Long> query = em.createQuery(q);
		return query.getSingleResult();

	}




	public static <T> boolean addPagination(BaseclassFilter baseclassFilter, TypedQuery<T> q) {
		return BasicRepository.addPagination(baseclassFilter, q);
	}

	public <T extends Baseclass> void addBaseclassPredicates(BasicPropertiesFilter basicPropertiesFilter, CriteriaBuilder cb, CommonAbstractCriteria q, From<?, T> r, List<Predicate> predicates, SecurityContext securityContext) {
		if (basicPropertiesFilter != null) {
			BasicRepository.addBasicPropertiesFilter(basicPropertiesFilter, cb, q, r, predicates);
		} else {
			BasicRepository.addBasicPropertiesFilter(new BasicPropertiesFilter().setSoftDelete(SoftDeleteOption.DEFAULT), cb, q, r, predicates);
		}
		if (securityContext != null) {
			addBaseclassPredicates(cb, q, r, predicates, securityContext);
		}
	}

	public <T extends Baseclass> void addBaseclassPredicates(CriteriaBuilder cb, CommonAbstractCriteria q, From<?, T> r, List<Predicate> predicates, SecurityContext securityContext) {
		securityRepository.addSecurityPredicates(em,cb,q,r,predicates,securityContext);
	}

	public boolean requiresSecurityPredicates(SecurityContext securityContext) {
		return securityRepository.requiresSecurityPredicates(securityContext);
	}


	public <T extends Baseclass> List<T> listByIds(Class<T> c, Set<String> ids, SecurityContext securityContext) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<T> q = cb.createQuery(c);
		Root<T> r = q.from(c);
		List<Predicate> predicates = new ArrayList<>();
		predicates.add(r.get(Baseclass_.id).in(ids));
		addBaseclassPredicates(cb, q, r, predicates, securityContext);
		q.select(r).where(predicates.toArray(Predicate[]::new));
		TypedQuery<T> query = em.createQuery(q);
		return query.getResultList();
	}

	public <T extends Baseclass> T getByIdOrNull(String id, Class<T> c, SecurityContext securityContext) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<T> q = cb.createQuery(c);
		Root<T> r = q.from(c);
		List<Predicate> predicates = new ArrayList<>();
		predicates.add(cb.equal(r.get(Baseclass_.id), id));
		addBaseclassPredicates(cb, q, r, predicates, securityContext);
		q.select(r).where(predicates.toArray(Predicate[]::new));
		TypedQuery<T> query = em.createQuery(q);
		List<T> resultList = query.getResultList();
		return resultList.isEmpty() ? null : resultList.getFirst();
	}

	public <D extends Basic, E extends Baseclass, T extends D> T getByIdOrNull(String id, Class<T> c, SingularAttribute<D, E> baseclassAttribute, SecurityContext securityContext) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<T> q = cb.createQuery(c);
		Root<T> r = q.from(c);
		List<Predicate> predicates = new ArrayList<>();
		predicates.add(cb.equal(r.get(Basic_.id), id));
		if(requiresSecurityPredicates(securityContext)){
			Join<T, E> join = r.join(baseclassAttribute);
			addBaseclassPredicates(cb, q, join, predicates, securityContext);
		}

		q.select(r).where(predicates.toArray(Predicate[]::new));
		TypedQuery<T> query = em.createQuery(q);
		List<T> resultList = query.getResultList();
		return resultList.isEmpty() ? null : resultList.getFirst();
	}



	public <D extends Basic, E extends Baseclass, T extends D> List<T> listByIds(Class<T> c, Set<String> ids, SingularAttribute<D, E> baseclassAttribute, SecurityContext securityContext) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<T> q = cb.createQuery(c);
		Root<T> r = q.from(c);
		List<Predicate> predicates = new ArrayList<>();
		predicates.add(r.get(Basic_.id).in(ids));
		if(requiresSecurityPredicates(securityContext)){
			Join<T, E> join = r.join(baseclassAttribute);
			addBaseclassPredicates(cb, q, join, predicates, securityContext);
		}

		q.select(r).where(predicates.toArray(Predicate[]::new));
		TypedQuery<T> query = em.createQuery(q);
		return query.getResultList();
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

	public <T> T merge(T base) {
		return basicRepository.merge(base);
	}

	public <T> T merge(T base, boolean updateDate, boolean propagateEvents) {
		return basicRepository.merge(base, updateDate, propagateEvents);
	}


	public void massMerge(List<?> toMerge, boolean updatedate, boolean propagateEvents) {
		basicRepository.massMerge(toMerge, updatedate, propagateEvents);
	}


	public <T> T merge(T base, boolean updateDate) {
		return basicRepository.merge(base, updateDate);
	}


	public void massMerge(List<?> toMerge) {
		basicRepository.massMerge(toMerge);
	}


	public void massMerge(List<?> toMerge, boolean updatedate) {
		basicRepository.massMerge(toMerge, updatedate);
	}

	public Set<EntityType<?>> getEntities() {
		return em.getMetamodel().getEntities();
	}


	public <T> List<T> listAll(Class<T> c,BaseclassFilter baseclassFilter, SecurityContext securityContext){
		CriteriaBuilder cb=em.getCriteriaBuilder();
		CriteriaQuery<T> q=cb.createQuery(c);
		Root<T> r=q.from(c);
		List<Predicate> predicates=new ArrayList<>();
		securityRepository.addSecurityPredicates(em,cb,q,r,predicates,securityContext);
		q.select(r).where(predicates.toArray(Predicate[]::new));
		TypedQuery<T> query = em.createQuery(q);
		BasicRepository.addPagination(baseclassFilter,query);
		return query.getResultList();

	}



	public <T> long countAll(Class<T> c,BaseclassFilter baseclassFilter,SecurityContext securityContext){
		CriteriaBuilder cb=em.getCriteriaBuilder();
		CriteriaQuery<Long> q=cb.createQuery(Long.class);
		Root<T> r=q.from(c);
		List<Predicate> predicates=new ArrayList<>();
		securityRepository.addSecurityPredicates(em,cb,q,r,predicates,securityContext);
		q.select(cb.count(r)).where(predicates.toArray(Predicate[]::new));
		TypedQuery<Long> query = em.createQuery(q);
		return query.getSingleResult();

	}


	@Transactional
	public void createIndexes() throws Exception {
		if(!createIndexes){
			logger.info("will not create baseclass indexes");
		}
		Set<String> tablesForIndexCreation = em.getMetamodel().getEntities().stream().filter(f -> Baseclass.class.isAssignableFrom(f.getJavaType())).map(f -> getTableName(f)).filter(f -> f != null).collect(Collectors.toSet());
		for (String table : tablesForIndexCreation) {
			{
				String sql = "create index if not exists %s on %s (softdelete,tenant_id,creator_id,securityId,dtype)";
				String indexName = table + "_security_idx";
				if (recreateIndexes) {
					dropIndex(em, indexName);
				}
				sql = sql.formatted(indexName, table);
				logger.debug("creating index: {}", sql);
				em.createNativeQuery(sql).executeUpdate();
			}


			{
				String sql = "create index if not exists %s on %s (securityId)";
				String indexName = table + "_securityId_idx";

				if (recreateIndexes) {
					dropIndex(em, indexName);
				}
				sql = sql.formatted(indexName, table);
				logger.debug("creating index: {}", sql);
				em.createNativeQuery(sql).executeUpdate();
			}
		}
		{

			String sql = "create index if not exists permissiongrouptobaseclass_link_idx on permissiongrouptobaseclass (securedId,permissiongroup_id)";
			logger.debug("creating index: {}", sql);
			em.createNativeQuery(sql).executeUpdate();
		}

	}

	private void dropIndex(EntityManager em, String indexName) {
		String sql = "drop index if exists %s";
		sql = sql.formatted(indexName);
		em.createNativeQuery(sql).executeUpdate();
	}

	public static String getTableName(EntityType<?> entityType) {
		Class<?> clazz = entityType.getJavaType();

		// Traverse the class hierarchy to find the @Table annotation
		Class<?> lastConcreate = null;
		for (Class<?> current = clazz; current != null; current = current.getSuperclass()) {
			if (!current.isAnnotationPresent(MappedSuperclass.class)&&current.isAnnotationPresent(Entity.class)) {
				lastConcreate = current;
			} else {
				break;
			}

		}
		if (lastConcreate == null) {
			return null;
		}
		Table tableAnnotation = lastConcreate.getAnnotation(Table.class);
		if (tableAnnotation != null && !tableAnnotation.name().isBlank()) {
			return tableAnnotation.name();
		}
		return lastConcreate.getSimpleName();
	}
}
