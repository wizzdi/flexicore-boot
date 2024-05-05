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

import java.util.ArrayList;
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

    public List<Author> getAuthors(AuthorFilter filter) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Author> q = cb.createQuery(Author.class);
        Root<Author> r = q.from(Author.class);
        List<Predicate> preds = getPredicates(filter, cb, r);

        q.select(r).where(preds.toArray(Predicate[]::new)).distinct(true);
        TypedQuery<Author> query = em.createQuery(q);
        return query.getResultList();
    }

    private static List<Predicate> getPredicates(AuthorFilter filter, CriteriaBuilder cb, Root<Author> r) {
        List<Predicate> preds=new ArrayList<>();
        if(filter.getDynamicPropertiesFilter()!=null){
            preds.addAll(FilterDynamicPropertiesUtils.filterDynamic(filter.getDynamicPropertiesFilter(), cb, r, "dynamicProperties"));
        }
        if(filter.getStaticPropertiesFilter()!=null){
            preds.addAll(FilterStaticPropertiesUtils.filterStatic(filter.getStaticPropertiesFilter(),cb,r));
        }
        return preds;
    }

    @Transactional
    public Book mergeBook(Book book) {
        return em.merge(book);

    }

    public List<Book> getAuthorsBooks(Author author) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Book> q = cb.createQuery(Book.class);
        Root<Book> r = q.from(Book.class);
        List<Predicate> preds = new ArrayList<>();
        preds.add(cb.equal(r.get("author"),author));
        q.select(r).where(preds.toArray(Predicate[]::new));
        TypedQuery<Book> query = em.createQuery(q);
        return query.getResultList();
    }

    @Transactional
    public void deleteAll() {
        em.createQuery("delete from Book").executeUpdate();
        em.createQuery("delete from Author").executeUpdate();

    }
}
