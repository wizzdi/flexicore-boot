package com.flexicore.interfaces;

import com.flexicore.annotations.Baseclassroot;
import com.flexicore.data.impl.BaseclassRepository;
import com.flexicore.model.*;
import com.flexicore.request.MassDeleteRequest;
import com.flexicore.security.SecurityContext;
import org.apache.commons.lang3.tuple.Pair;
import org.pf4j.ExtensionPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Set;

public abstract class AbstractRepositoryPlugin implements PluginRepository, ExtensionPoint {

    @Autowired
    @Baseclassroot
    private BaseclassRepository baseclassRepository;
    protected EntityManager em;

    @PostConstruct
    private void onConstruct() {
        this.em = baseclassRepository.getEm();
    }


    public <T extends Baseclass> T findById(String id) {
        return baseclassRepository.findById(id);
    }

    public <T> T findById(Class<T> type, String id) {
        return baseclassRepository.findById(type, id);
    }

    public <T> T findByIdOrNull(Class<T> type, String id) {
        return baseclassRepository.findByIdOrNull(type, id);
    }

    public <T extends Baseclass> T getById(String id, Class<T> c, List<String> batchString, boolean includeSoftDelete, SecurityContext securityContext) {
        return baseclassRepository.getById(id, c, batchString, includeSoftDelete, securityContext);
    }

    public <T extends Baseclass> T getById(String id, Class<T> c, List<String> batchString, SecurityContext securityContext) {
        return baseclassRepository.getById(id, c, batchString, securityContext);
    }

    public <T extends Baseclass> T getByIdOrNull(String id, Class<T> c, List<String> batchString, SecurityContext securityContext) {
        return baseclassRepository.getByIdOrNull(id, c, batchString, securityContext);
    }

    public <T extends Baseclass> List<T> listByNames(Class<T> c, Set<String> names, SecurityContext securityContext) {
        return baseclassRepository.listByNames(c, names, securityContext);
    }

    public <T extends Baseclass> List<T> listByIds(Class<T> c, Set<String> ids, SecurityContext securityContext) {
        return baseclassRepository.listByIds(c, ids, securityContext);
    }

    public <T extends Baseclass> List<T> getByName(String name, Class<T> c, List<String> batchString, SecurityContext securityContext) {
        return baseclassRepository.getByName(name, c, batchString, securityContext);
    }

    public <T extends Baseclass> T getFirstByName(String name, Class<T> c, List<String> batchString, SecurityContext securityContext) {
        return baseclassRepository.getFirstByName(name, c, batchString, securityContext);
    }

    public <T extends Baseclass> List<T> getByNameLike(String name, Class<T> c, List<String> batchString, SecurityContext securityContext) {
        return baseclassRepository.getByNameLike(name, c, batchString, securityContext);
    }

    public boolean Persist(Baseclass baseclass) {
        return baseclassRepository.Persist(baseclass);
    }

    public List<String> getOrderByParameters(Class<?> clazz) {
        return baseclassRepository.getOrderByParameters(clazz);
    }

    public <T extends Baseclass> List<Field> getOrderFields(Class<T> clazz) {
        return baseclassRepository.getOrderFields(clazz);
    }

    public List<Field> getAllComplexFields(Class<?> c) {
        return baseclassRepository.getAllComplexFields(c);
    }

    @Transactional
    public void merge(Object base) {
        baseclassRepository.merge(base);
    }

    public void remove(Object o) {
        baseclassRepository.remove(o);
    }

    public <T extends Baseclass> void removeById(String id, Class<T> type) {
        baseclassRepository.removeById(id, type);
    }

    public <T extends Baseclass> boolean remove(T base, Class<T> type) {
        return baseclassRepository.remove(base, type);
    }

    public <T extends Baseclass> int remove(List<Predicate> preds, QueryInformationHolder<T> queryInformationHolder, CriteriaBuilder cb, CriteriaDelete<T> delete, Root<T> r) {
        return baseclassRepository.remove(preds, queryInformationHolder, cb, delete, r);
    }

    public <T extends Baseclass> int removeById(String id, QueryInformationHolder<T> queryInformationHolder) {
        return baseclassRepository.removeById(id, queryInformationHolder);
    }

    public void flush() {
        baseclassRepository.flush();
    }

    public <T extends Baseclass> void prepareQuery(QueryInformationHolder<T> queryInformationHolder, List<Predicate> existingPredicates, CriteriaBuilder cb, CommonAbstractCriteria q, From<?, T> r) {
        baseclassRepository.prepareQuery(queryInformationHolder, existingPredicates, cb, q, r);
    }

    public <T extends Baseclass> void prepareQuery(QueryInformationHolder<T> queryInformationHolder, List<Predicate> existingPredicates, CriteriaBuilder cb, CommonAbstractCriteria q, From<?, T> r, boolean count) {
        baseclassRepository.prepareQuery(queryInformationHolder, existingPredicates, cb, q, r, count);
    }

    public <T extends Baseclass> void prepareQuery(QueryInformationHolder<T> queryInformationHolder, List<Predicate> existingPredicates, List<Order> orders, CriteriaBuilder cb, CommonAbstractCriteria q, From<?, T> r, boolean count) {
        baseclassRepository.prepareQuery(queryInformationHolder, existingPredicates, orders, cb, q, r, count);
    }

    public <T extends Baseclass> void excludeDeleted(List<Predicate> existingPredicates, CriteriaBuilder cb, From<?, T> r) {
        baseclassRepository.excludeDeleted(existingPredicates, cb, r);
    }

    public <T extends Baseclass> List<T> getAllFiltered(QueryInformationHolder<T> queryInformationHolder) {
        return baseclassRepository.getAllFiltered(queryInformationHolder);
    }

    public <T extends Baseclass> long countAllFiltered(QueryInformationHolder<T> queryInformationHolder) {
        return baseclassRepository.countAllFiltered(queryInformationHolder);
    }

    public <T extends Baseclass> long countAllFiltered(QueryInformationHolder<T> queryInformationHolder, List<Predicate> existingPredicates, CriteriaBuilder cb, CriteriaQuery<Long> q, Root<T> r) {
        return baseclassRepository.countAllFiltered(queryInformationHolder, existingPredicates, cb, q, r);
    }

    public <T extends Baseclass> List<T> getAllFiltered(QueryInformationHolder<T> queryInformationHolder, List<Predicate> existingPredicates, CriteriaBuilder cb, CriteriaQuery<T> q, From<?, T> r) {
        return baseclassRepository.getAllFiltered(queryInformationHolder, existingPredicates, cb, q, r);
    }

    public <T extends Baseclass> List<T> getAllFiltered(QueryInformationHolder<T> queryInformationHolder, List<Predicate> existingPredicates, List<Order> orders, CriteriaBuilder cb, CriteriaQuery<T> q, From<?, T> r) {
        return baseclassRepository.getAllFiltered(queryInformationHolder, existingPredicates, orders, cb, q, r);
    }

    public <T extends Baseclass, E> List<E> getAllFiltered(QueryInformationHolder<T> queryInformationHolder, List<Predicate> existingPredicates, CriteriaBuilder cb, CriteriaQuery<E> q, From<?, T> r, Class<E> selectionClass, Selection<? extends E> select) {
        return baseclassRepository.getAllFiltered(queryInformationHolder, existingPredicates, cb, q, r, selectionClass, select);
    }

    public <T extends Baseclass, E> List<E> getAllFiltered(QueryInformationHolder<T> queryInformationHolder, List<Predicate> existingPredicates, List<Order> orders, CriteriaBuilder cb, CriteriaQuery<E> q, From<?, T> r, Class<E> selectionClass, Selection<? extends E> select) {
        return baseclassRepository.getAllFiltered(queryInformationHolder, existingPredicates, orders, cb, q, r, selectionClass, select);
    }

    public <T extends Baseclass> T getFiltered(QueryInformationHolder<T> queryInformationHolder, List<Predicate> existingPredicates) {
        return baseclassRepository.getFiltered(queryInformationHolder, existingPredicates);
    }

    public <T extends Baseclass> T getFiltered(QueryInformationHolder<T> queryInformationHolder, List<Predicate> existingPredicates, CriteriaBuilder cb, CriteriaQuery<T> q, Root<T> r) {
        return baseclassRepository.getFiltered(queryInformationHolder, existingPredicates, cb, q, r);
    }

    public <T extends Baseclass, E> E getFiltered(QueryInformationHolder<T> queryInformationHolder, List<Predicate> existingPredicates, CriteriaBuilder cb, CriteriaQuery<E> q, Root<T> r, Class<E> selectionClass, Selection<? extends E> select) {
        return baseclassRepository.getFiltered(queryInformationHolder, existingPredicates, cb, q, r, selectionClass, select);
    }

    public <T> void finalizeQuery(From<?, T> r, CriteriaQuery<T> q, List<Predicate> preds, CriteriaBuilder cb) {
        baseclassRepository.finalizeQuery(r, q, preds, cb);
    }

    public <T> void finalizeQuery(From<?, T> r, CriteriaDelete<T> q, List<Predicate> preds, CriteriaBuilder cb) {
        baseclassRepository.finalizeQuery(r, q, preds, cb);
    }

    public <T, E> void finalizeQuery(From<?, T> r, CriteriaQuery<E> q, List<Predicate> preds, CriteriaBuilder cb, Selection<? extends E> select) {
        baseclassRepository.finalizeQuery(r, q, preds, cb, select);
    }

    public <T, E> void finalizeQuery(From<?, T> r, CriteriaDelete<E> q, List<Predicate> preds, CriteriaBuilder cb, Selection<? extends E> select) {
        baseclassRepository.finalizeQuery(r, q, preds, cb, select);
    }

    public <T> void finalizeCountQuery(From<?, T> r, CriteriaQuery<Long> q, List<Predicate> preds, CriteriaBuilder cb) {
        baseclassRepository.finalizeCountQuery(r, q, preds, cb);
    }

    public <T> T getSingleResult(TypedQuery<T> query) {
        return baseclassRepository.getSingleResult(query);
    }

    public <T> T getSingleResultOrNull(TypedQuery<T> query) {
        return baseclassRepository.getSingleResultOrNull(query);
    }

    public <T> List<T> getResultList(TypedQuery<T> query) {
        return baseclassRepository.getResultList(query);
    }

    public void setPageQuery(int pagesize, int currentPage, TypedQuery<?> query) {
        baseclassRepository.setPageQuery(pagesize, currentPage, query);
    }

    public <T> void addSorted(CriteriaQuery<?> q, From<?, T> r, CriteriaBuilder cb, List<SortParameter> sort, Class<T> c) {
        baseclassRepository.addSorted(q, r, cb, sort, c);
    }

    public <T> void addSorted(CriteriaQuery<?> q, From<?, T> r, CriteriaBuilder cb, List<SortParameter> sort, List<Order> orders, Class<T> c) {
        baseclassRepository.addSorted(q, r, cb, sort, orders, c);
    }

    public <T> List<T> getAll(Class<T> c) {
        return baseclassRepository.getAll(c);
    }

    public <T> List<T> getAll(Class<T> c, Integer pageSize, Integer currentPage) {
        return baseclassRepository.getAll(c, pageSize, currentPage);
    }

    public <T> long countAll(Class<T> c) {
        return baseclassRepository.countAll(c);
    }

    public Pair<List<Baseclass>, List<Baseclass>> getDenied(User user, Operation op) {
        return baseclassRepository.getDenied(user, op);
    }

    public <T extends Baseclass> void addTenantsPredicate(List<Predicate> existingPredicates, From<?, T> r, CriteriaBuilder cb, Set<String> tenantsIds) {
        baseclassRepository.addTenantsPredicate(existingPredicates, r, cb, tenantsIds);
    }

    public <T extends Baseclass> void addClazzPredicate(List<Predicate> existingPredicates, From<?, T> r, CriteriaBuilder cb, Set<String> clazzIds) {
        baseclassRepository.addClazzPredicate(existingPredicates, r, cb, clazzIds);
    }

    public void addTenantToBaseClass(Baseclass b, Tenant tenant, SecurityContext securityContext) {
        baseclassRepository.addTenantToBaseClass(b, tenant, securityContext);
    }

    public void refresh(Object o) {
        baseclassRepository.refresh(o);
    }

    public EntityManager getEm() {
        return baseclassRepository.getEm();
    }

    public void setEm(EntityManager em) {
        baseclassRepository.setEm(em);
    }


    public void refrehEntityManager() {
        baseclassRepository.refrehEntityManager();
    }

    @Transactional
    public void massMerge(List<?> toMerge) {
        baseclassRepository.massMerge(toMerge);
    }

    @Transactional
    public <T extends Baseclass> List<T> findByIds(Class<T> c, Set<String> requested) {
        return baseclassRepository.findByIds(c, requested);
    }

    @Transactional
    public void merge(Object base, boolean updateUpdateDate) {
        baseclassRepository.merge(base, updateUpdateDate);
    }

    @Transactional
    public void massMerge(List<?> toMerge, boolean updateUpdateDate) {
        baseclassRepository.massMerge(toMerge, updateUpdateDate);
    }

    public void massDelete(MassDeleteRequest massDeleteRequest) {
        baseclassRepository.massDelete(massDeleteRequest);
    }
}
