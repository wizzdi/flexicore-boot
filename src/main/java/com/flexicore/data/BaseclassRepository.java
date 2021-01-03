package com.flexicore.data;

import com.flexicore.interfaces.PluginRepository;
import org.pf4j.Extension;
import org.springframework.stereotype.Component;
import com.flexicore.model.*;
import com.flexicore.request.BaseclassCountRequest;
import com.flexicore.request.MassDeleteRequest;
import com.flexicore.response.BaseclassCount;
import com.flexicore.security.SecurityContext;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Set;

public interface BaseclassRepository extends PluginRepository {

    <T extends Baseclass> T findById(String id);

    <T> T findById(Class<T> type, String id);

    <T> T findByIdOrNull(Class<T> type, String id);

    <T extends Baseclass> T getById(String id, Class<T> c, List<String> batchString, boolean includeSoftDelete,
                                    SecurityContext securityContext);

    <T extends Baseclass> T getById(String id, Class<T> c, List<String> batchString,
                                    SecurityContext securityContext);

    <T extends Baseclass> T getByIdOrNull(String id, Class<T> c, List<String> batchString,
                                          SecurityContext securityContext);

    <T extends Baseclass> List<T> listByNames(Class<T> c, Set<String> names, SecurityContext securityContext);

    <T extends Baseclass> List<T> listByIds(Class<T> c, Set<String> ids, SecurityContext securityContext);

    <T extends Baseclass> List<T> getByName(String name, Class<T> c, List<String> batchString,
                                            SecurityContext securityContext);

    <T extends Baseclass> T getFirstByName(String name, Class<T> c, List<String> batchString,
                                           SecurityContext securityContext);

    <T extends Baseclass> List<T> getByNameLike(String name, Class<T> c, List<String> batchString,
                                                SecurityContext securityContext);

    boolean Persist(Baseclass baseclass);

    List<BaseclassCount> getBaseclassCount(BaseclassCountRequest baseclassCountRequest, SecurityContext securityContext);


    List<String> getOrderByParameters(Class<?> clazz);

    @SuppressWarnings("unchecked")
    <T extends Baseclass> List<Field> getOrderFields(Class<T> clazz);

    List<Field> getAllComplexFields(Class<?> c);

    @Transactional
    void merge(Object base);
    @Transactional
    void merge(Object base,boolean updateUpdateDate);

    void remove(Object o);

    <T extends Baseclass> void removeById(String id, Class<T> type);

    <T extends Baseclass> boolean remove(T base, Class<T> type);

    <T extends Baseclass> int remove(List<Predicate> preds, QueryInformationHolder<T> queryInformationHolder, CriteriaBuilder cb, CriteriaDelete<T> delete, Root<T> r);

    <T extends Baseclass> int removeById(String id, QueryInformationHolder<T> queryInformationHolder);

    void flush();

    <T extends Baseclass> void prepareQuery(QueryInformationHolder<T> queryInformationHolder,
                                            List<Predicate> existingPredicates, CriteriaBuilder cb, CommonAbstractCriteria q, From<?, T> r);

    <T extends Baseclass> void prepareQuery(QueryInformationHolder<T> queryInformationHolder,
                                            List<Predicate> existingPredicates, CriteriaBuilder cb, CommonAbstractCriteria q, From<?, T> r, boolean count);

    <T extends Baseclass> void prepareQuery(QueryInformationHolder<T> queryInformationHolder,
                                            List<Predicate> existingPredicates, List<Order> orders, CriteriaBuilder cb, CommonAbstractCriteria q, From<?, T> r, boolean count);

    <T extends Baseclass> void excludeDeleted(List<Predicate> existingPredicates, CriteriaBuilder cb, From<?, T> r);

    <T extends Baseclass> List<T> getAllFiltered(QueryInformationHolder<T> queryInformationHolder);

    <T extends Baseclass> long countAllFiltered(QueryInformationHolder<T> queryInformationHolder);

    <T extends Baseclass> long countAllFiltered(QueryInformationHolder<T> queryInformationHolder,
                                                List<Predicate> existingPredicates, CriteriaBuilder cb, CriteriaQuery<Long> q, Root<T> r);

    <T extends Baseclass> List<T> getAllFiltered(QueryInformationHolder<T> queryInformationHolder,
                                                 List<Predicate> existingPredicates, CriteriaBuilder cb, CriteriaQuery<T> q, From<?, T> r);

    <T extends Baseclass> List<T> getAllFiltered(QueryInformationHolder<T> queryInformationHolder,
                                                 List<Predicate> existingPredicates, List<Order> orders, CriteriaBuilder cb, CriteriaQuery<T> q, From<?, T> r);

    <T extends Baseclass, E> List<E> getAllFiltered(QueryInformationHolder<T> queryInformationHolder,
                                                    List<Predicate> existingPredicates, CriteriaBuilder cb, CriteriaQuery<E> q, From<?, T> r, Class<E> selectionClass, Selection<? extends E> select);

    <T extends Baseclass, E> List<E> getAllFiltered(QueryInformationHolder<T> queryInformationHolder,
                                                    List<Predicate> existingPredicates, List<Order> orders, CriteriaBuilder cb, CriteriaQuery<E> q, From<?, T> r, Class<E> selectionClass, Selection<? extends E> select);

    <T extends Baseclass> T getFiltered(QueryInformationHolder<T> queryInformationHolder,
                                        List<Predicate> existingPredicates);

    <T extends Baseclass> T getFiltered(QueryInformationHolder<T> queryInformationHolder,
                                        List<Predicate> existingPredicates, CriteriaBuilder cb, CriteriaQuery<T> q, Root<T> r);

    <T extends Baseclass, E> E getFiltered(QueryInformationHolder<T> queryInformationHolder,
                                           List<Predicate> existingPredicates, CriteriaBuilder cb, CriteriaQuery<E> q, Root<T> r, Class<E> selectionClass, Selection<? extends E> select);

    <T> void finalizeQuery(From<?, T> r, CriteriaQuery<T> q, List<Predicate> preds, CriteriaBuilder cb);

    <T> void finalizeQuery(From<?, T> r, CriteriaDelete<T> q, List<Predicate> preds, CriteriaBuilder cb);

    <T, E> void finalizeQuery(From<?, T> r, CriteriaQuery<E> q, List<Predicate> preds, CriteriaBuilder cb, Selection<? extends E> select);

    <T, E> void finalizeQuery(From<?, T> r, CriteriaDelete<E> q, List<Predicate> preds, CriteriaBuilder cb, Selection<? extends E> select);

    <T> void finalizeCountQuery(From<?, T> r, CriteriaQuery<Long> q, List<Predicate> preds, CriteriaBuilder cb);

    <T> T getSingleResult(TypedQuery<T> query);

    <T> T getSingleResultOrNull(TypedQuery<T> query);

    <T> List<T> getResultList(TypedQuery<T> query);

    void setPageQuery(int pagesize, int currentPage, TypedQuery<?> query);

    <T> void addSorted(CriteriaQuery<?> q, From<?, T> r, CriteriaBuilder cb, List<SortParameter> sort, Class<T> c);

    <T> void addSorted(CriteriaQuery<?> q, From<?, T> r, CriteriaBuilder cb, List<SortParameter> sort, List<Order> orders, Class<T> c);

    <T> List<T> getAll(Class<T> c);

    <T> List<T> getAll(Class<T> c, Integer pageSize, Integer currentPage);

    <T> long countAll(Class<T> c);

    Pair<List<Baseclass>, List<Baseclass>> getDenied(User user, Operation op);

    <T extends Baseclass> void addTenantsPredicate(List<Predicate> existingPredicates, From<?, T> r,
                                                   CriteriaBuilder cb, Set<String> tenantsIds);

    <T extends Baseclass> void addClazzPredicate(List<Predicate> existingPredicates, From<?, T> r,
                                                 CriteriaBuilder cb, Set<String> clazzIds);


    void addTenantToBaseClass(Baseclass b, Tenant tenant, SecurityContext securityContext);

    void refresh(Object o);

    EntityManager getEm();

    void setEm(EntityManager em);

    void refrehEntityManager();

    @Transactional
    void massMerge(List<?> toMerge);

    @Transactional
    void massMerge(List<?> toMerge,boolean updateUpdateDate);

    @Transactional
    <T extends Baseclass> List<T> findByIds(Class<T> c, Set<String> requested);

    void massDelete(MassDeleteRequest massDeleteRequest);
}
