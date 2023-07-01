package com.wizzdi.dynamic.properties.converter.postgresql;

import com.wizzdi.dynamic.properties.converter.DynamicPropertiesUtils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Component
public class AuthorRepository {

    @Autowired
    private EntityManager em;


    @Transactional
    public Author merge(Author author){
       return em.merge(author);
    }

    public List<Author> getAuthors(Map<String, DynamicFilterItem> filter) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Author> q = cb.createQuery(Author.class);
        Root<Author> r = q.from(Author.class);
        List<Predicate> preds = FilterDynamicPropertiesUtils.filterDynamic(filter,cb,r,"dynamicProperties");

        q.select(r).where(preds.toArray(Predicate[]::new));
        TypedQuery<Author> query = em.createQuery(q);
        return query.getResultList();
    }
}
