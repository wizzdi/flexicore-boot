package com.wizzdi.flexicore.security.data;

import com.flexicore.model.*;
import com.flexicore.security.SecurityContextBase;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;

import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Extension
public class GenericDeleteRepository implements Plugin {
	@PersistenceContext
	private EntityManager em;
	@Autowired
	private SecuredBasicRepository securedBasicRepository;
	@Autowired
	private BaseclassRepository baseclassRepository;


	public  <T extends Basic> List<T> getObjects(Class<T> c,Set<String> ids, SecurityContextBase securityContext) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<T> q = cb.createQuery(c);
		Root<T> r = q.from(c);
		List<Predicate> predicates = new ArrayList<>();
		predicates.add(r.get(Basic_.id).in(ids));
		if(SecuredBasic.class.isAssignableFrom(c)){
			securedBasicRepository.addSecuredBasicPredicates(null,cb,q,(From<?, ? extends SecuredBasic>) r,predicates,securityContext);
		}
		if(Baseclass.class.isAssignableFrom(c)){
			baseclassRepository.addBaseclassPredicates(cb,q,(From<?,? extends Baseclass>)r,predicates,securityContext);
		}
		q.select(r).where(predicates.toArray(Predicate[]::new));
		TypedQuery<T> query = em.createQuery(q);
		return query.getResultList();

	}

	public Class<?> getType(String className){
		return em.getMetamodel().getEntities().stream().map(f->f.getJavaType()).filter(f->f.getCanonicalName().equals(className)).findFirst().orElse(null);
	}


	@Transactional
	public <T> void merge(T t) {
		securedBasicRepository.merge(t);
	}
}
