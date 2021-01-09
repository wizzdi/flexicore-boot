/*******************************************************************************
 *  Copyright (C) FlexiCore, Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Avishay Ben Natan And Asaf Ben Natan, October 2015
 ******************************************************************************/
package com.flexicore.data.impl;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.annotations.Baseclassroot;
import com.flexicore.annotations.FullTextSearch;
import com.flexicore.annotations.FullTextSearchOptions;
import com.flexicore.annotations.IOperation;
import com.flexicore.annotations.rest.All;
import com.flexicore.data.jsoncontainers.SortingOrder;
import com.flexicore.events.BaseclassCreated;
import com.flexicore.events.BaseclassUpdated;
import com.flexicore.model.*;
import com.flexicore.request.BaseclassCountRequest;
import com.flexicore.request.MassDeleteRequest;
import com.flexicore.response.BaseclassCount;
import com.flexicore.security.SecurityContext;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.eclipse.persistence.internal.jpa.querydef.CriteriaBuilderImpl;
import org.pf4j.Extension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import javax.persistence.criteria.*;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.PluralAttribute;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Baseclassroot
@Component("BaseclassRepositoryBase")
@Extension
public class BaseclassRepository implements com.flexicore.data.BaseclassRepository {

    /**
     *
     */
    private static final long serialVersionUID = -6837211315450751680L;
    @PersistenceContext
    protected EntityManager em;
    private static final Logger logger = LoggerFactory.getLogger(BaseclassRepository.class);
    private static Operation allOp;

    @Autowired
    private com.wizzdi.flexicore.security.data.BaseclassRepository baseclassRepository;

    @Autowired
    private ApplicationEventPublisher eventPublisher;


    @Override
    @SuppressWarnings("unchecked")

    public <T extends Baseclass> T findById(String id) {
        return (T) em.find(Baseclass.class, id);
    }

    @Override
    public <T> T findById(Class<T> type, String id) {
        return em.find(type, id);
    }

    @Override
    public <T> T findByIdOrNull(Class<T> type, String id) {
        try {
            return em.find(type, id);


        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public <T extends Baseclass> T getById(String id, Class<T> c, List<String> batchString, boolean includeSoftDelete,
                                           SecurityContext securityContext) {

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<T> q = cb.createQuery(c);
        Root<T> r = q.from(c);
        Predicate p = cb.equal(r.get(Baseclass_.id), id);
        List<Predicate> preds = new ArrayList<>();
        preds.add(p);
        QueryInformationHolder<T> info = new QueryInformationHolder<>(new FilteringInformationHolder().setFetchSoftDelete(includeSoftDelete), c, securityContext);
        info.setBatchFetchString(batchString);

        return getFiltered(info, preds, cb, q, r);

    }

    @Override
    public <T extends Baseclass> T getById(String id, Class<T> c, List<String> batchString,
                                           SecurityContext securityContext) {

        return this.getById(id, c, batchString, false, securityContext);

    }

    @Override
    public <T extends Baseclass> T getByIdOrNull(String id, Class<T> c, List<String> batchString,
                                                 SecurityContext securityContext) {
        try {
            return getById(id, c, batchString, securityContext);

        } catch (NoResultException e) {
            return null;
        }

    }

    @Override
    public <T extends Baseclass> List<T> listByNames(Class<T> c, Set<String> names, SecurityContext securityContext) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<T> q = cb.createQuery(c);
        Root<T> r = q.from(c);
        List<Predicate> preds = new ArrayList<>();

        if (!names.isEmpty()) {
            Predicate predicate = r.get(Baseclass_.name).in(names);
            preds.add(predicate);
        }

        QueryInformationHolder<T> queryInformationHolder = new QueryInformationHolder<>(c, securityContext);
        return getAllFiltered(queryInformationHolder, preds, cb, q, r);
    }

    @Override
    public <T extends Baseclass> List<T> listByIds(Class<T> c, Set<String> ids, SecurityContext securityContext) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<T> q = cb.createQuery(c);
        Root<T> r = q.from(c);
        List<Predicate> preds = new ArrayList<>();

        if (!ids.isEmpty()) {
            Predicate predicate = r.get(Baseclass_.id).in(ids);
            preds.add(predicate);
        }

        QueryInformationHolder<T> queryInformationHolder = new QueryInformationHolder<>(c, securityContext);
        return getAllFiltered(queryInformationHolder, preds, cb, q, r);
    }

    @Override
    public <T extends Baseclass> List<T> getByName(String name, Class<T> c, List<String> batchString,
                                                   SecurityContext securityContext) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<T> q = cb.createQuery(c);
        Root<T> r = q.from(c);
        Predicate p = cb.equal(r.get(Baseclass_.name), name);
        List<Predicate> preds = new ArrayList<>();
        preds.add(p);
        QueryInformationHolder<T> info = new QueryInformationHolder<>(c, securityContext);
        info.setBatchFetchString(batchString);
        return getAllFiltered(info, preds, cb, q, r);

    }

    @Override
    public <T extends Baseclass> T getFirstByName(String name, Class<T> c, List<String> batchString,
                                                  SecurityContext securityContext) {

        List<T> l = getByName(name, c, batchString, securityContext);
        return l.isEmpty() ? null : l.get(0);

    }


    @Override
    public <T extends Baseclass> List<T> getByNameLike(String name, Class<T> c, List<String> batchString,
                                                       SecurityContext securityContext) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<T> q = cb.createQuery(c);
        Root<T> r = q.from(c);
        Predicate p = cb.like(r.get(Baseclass_.name), name);
        List<Predicate> preds = new ArrayList<>();
        preds.add(p);
        QueryInformationHolder<T> info = new QueryInformationHolder<>(c, securityContext);
        info.setBatchFetchString(batchString);
        return getAllFiltered(info, preds, cb, q, r);

    }

    public void addtocache(Clazz clazz) {
        logger.debug("have added: " + clazz.getName() + "   class type: " + clazz.getClass().getCanonicalName());
        Baseclass.addClazz(clazz);

    }

    @Override
    @Transactional
    public boolean Persist(Baseclass baseclass) {

        try {
            em.persist(baseclass);
            return true;
        } catch (Exception e) {
            logger.error( "Error while persisting entity: "
                    + baseclass.toString(), e);
        }
        return false;
    }

    @Override
    public List<String> getOrderByParameters(Class<?> clazz) {
        Field[] fields = clazz.getFields();
        List<String> SortedByFields = new ArrayList<>();
        for (Field field : fields) {

            SortedByFields.add(field.getName());

        }

        return SortedByFields;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Baseclass> List<Field> getOrderFields(Class<T> clazz) {
        List<Field> fields = new ArrayList<>();
        Field[] f = clazz.getDeclaredFields();
        for (Field field : f) {
            if (!field.isAnnotationPresent(JsonIgnore.class)) {
                fields.add(field);
            }
        }

        if (clazz.getSuperclass() != null && Baseclass.class.isAssignableFrom(clazz.getSuperclass())) {
            fields.addAll(getOrderFields((Class<T>) clazz.getSuperclass()));
        }
        return fields;
    }

    private boolean isQurifyable(Class<?> clazz, String s) {
        Field f;
        try {
            f = getFieldOninheritenceTree(clazz, s);

            if (f != null && !f.isAnnotationPresent(JsonIgnore.class)) {
                return true;
            }

        } catch (NoSuchFieldException | SecurityException e) {
            logger.warn( "cant find field", e);

        }
        return false;

    }

    private Field getFieldOninheritenceTree(Class<?> cls, String fieldName) throws NoSuchFieldException {
        for (Class acls = cls; acls != null; acls = acls.getSuperclass()) {
            try {
                // if not found exception thrown
                // else return field
                return acls.getDeclaredField(fieldName);
            } catch (NoSuchFieldException ex) {
                // ignore
            }
        }
        throw new NoSuchFieldException(fieldName);
    }


    @Override
    public List<Field> getAllComplexFields(Class<?> c) {
        List<Field> fieldsToRet = new ArrayList<>();
        for (Class acls = c; acls != null; acls = acls.getSuperclass()) {

            Field[] fields = c.getDeclaredFields();
            for (Field field : fields) {
                if (field.getClass().isAnnotationPresent(Entity.class)) {
                    fieldsToRet.add(field);
                }
            }

        }
        return fieldsToRet;
    }

    @Override
    @Transactional
    public void merge(Object base,boolean updateDate) {
        Baseclass base1=null;
        boolean created=false;
        if(base instanceof Baseclass){
            OffsetDateTime now = OffsetDateTime.now();
            base1 = (Baseclass) base;
            created=base1.getUpdateDate()==null;
            if(updateDate){
                base1.setUpdateDate(now);
            }
            if(logger.isDebugEnabled()){
                logger.debug("merging "+ base1.getId()+ " updateDate flag is "+updateDate +" update date "+base1.getUpdateDate());
            }
            updateSearchKey(base1);


        }

        em.merge(base);
        if(base1!=null){
            if(created){
                eventPublisher.publishEvent(new BaseclassCreated<>(base1));
            }
            else{
                eventPublisher.publishEvent(new BaseclassUpdated<>(base1));

            }
        }

    }

    @Override
    public void remove(Object o) {
        em.remove(o);
    }

    @Override
    public <T extends Baseclass> void removeById(String id, Class<T> type) {
        T t = em.find(type, id);
        em.remove(t);
    }

    @Override
    public <T extends Baseclass> boolean remove(T base, Class<T> type) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaDelete<T> delete = cb.createCriteriaDelete(type);
        Root<T> r = delete.from(type);
        delete.where(cb.equal(r.get(Baseclass_.id), base.getId()));
        Query query = em.createQuery(delete);
        int i = query.executeUpdate();

        return i > 0;

    }

    @Override
    public <T extends Baseclass> int remove(List<Predicate> preds, QueryInformationHolder<T> queryInformationHolder, CriteriaBuilder cb, CriteriaDelete<T> delete, Root<T> r) {
        if (cb == null) {
            cb = em.getCriteriaBuilder();
        }

        Class<T> type = queryInformationHolder.getType();
        if (delete == null) {
            delete = cb.createCriteriaDelete(type);
        }
        if (r == null) {
            r = delete.from(type);
        }

        prepareQuery(queryInformationHolder, preds, cb, delete, r);
        finalizeQuery(r, delete, preds, cb);
        Query query = em.createQuery(delete);
        return query.executeUpdate();

    }

    @Override
    public <T extends Baseclass> int removeById(String id, QueryInformationHolder<T> queryInformationHolder) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        Class<T> type = queryInformationHolder.getType();
        CriteriaDelete<T> delete = cb.createCriteriaDelete(type);
        Root<T> r = delete.from(type);
        Predicate p = cb.equal(r.get(Baseclass_.id), id);
        List<Predicate> preds = new ArrayList<>();
        preds.add(p);
        return remove(preds, queryInformationHolder, cb, delete, r);
    }


    @Override
    @Transactional
    public void flush() {
        em.flush();
    }


    @Override
    public <T extends Baseclass> void prepareQuery(QueryInformationHolder<T> queryInformationHolder,
                                                   List<Predicate> existingPredicates, CriteriaBuilder cb, CommonAbstractCriteria q, From<?, T> r) {
        prepareQuery(queryInformationHolder, existingPredicates, cb, q, r, false);
    }

    @Override
    public <T extends Baseclass> void prepareQuery(QueryInformationHolder<T> queryInformationHolder,
                                                   List<Predicate> existingPredicates, CriteriaBuilder cb, CommonAbstractCriteria q, From<?, T> r, boolean count) {
        prepareQuery(queryInformationHolder, existingPredicates, null, cb, q, r, count);
    }

    @Override
    public <T extends Baseclass> void prepareQuery(QueryInformationHolder<T> queryInformationHolder,
                                                   List<Predicate> existingPredicates, List<Order> orders, CriteriaBuilder cb, CommonAbstractCriteria q, From<?, T> r, boolean count) {

        Class<T> c = queryInformationHolder.getType();
        if (queryInformationHolder.getFilteringInformationHolder() == null) {
            queryInformationHolder.setFilteringInformationHolder(new FilteringInformationHolder());
        }
        List<SortParameter> sort = queryInformationHolder.getFilteringInformationHolder().getSort();
        String likeName = queryInformationHolder.getFilteringInformationHolder().getNameLike();
        String permissionContextLike = queryInformationHolder.getFilteringInformationHolder().getPermissionContextLike();

        String fullTextLike = queryInformationHolder.getFilteringInformationHolder().getFullTextLike();

        OffsetDateTime fromDate = queryInformationHolder.getFilteringInformationHolder().getFromDate();
        OffsetDateTime toDate = queryInformationHolder.getFilteringInformationHolder().getToDate();

        SecurityContext securityContext = queryInformationHolder.getSecurityContext();
        List<Tenant> tenant = securityContext != null ? securityContext.getTenants() : null;

        User user = securityContext != null ? securityContext.getUser() : null;
        boolean impersonated = securityContext != null && securityContext.isImpersonated();

        Operation operation = securityContext != null ? securityContext.getOperation() : null;
        boolean fetchSoftDelete = queryInformationHolder.getFilteringInformationHolder().isFetchSoftDelete();
        Integer pagesize = queryInformationHolder.getFilteringInformationHolder().getPageSize();
        Integer currentPage = queryInformationHolder.getFilteringInformationHolder().getCurrentPage();

        if (sort == null || sort.isEmpty()) {
            sort = new ArrayList<>();
            if (orders == null || orders.isEmpty()) {
                sort.add(new SortParameter("name", SortingOrder.ASCENDING));

            }
        }
        List<TenantIdFiltering> tenantIds = queryInformationHolder.getFilteringInformationHolder().getTenantIds();
        if (tenantIds != null && !tenantIds.isEmpty()) {
            Set<String> tenantsIds = tenantIds.parallelStream().map(f -> f.getId()).collect(Collectors.toSet());
            addTenantsPredicate(existingPredicates, r, cb, tenantsIds);
        }
        List<ClazzIdFiltering> clazzIds = queryInformationHolder.getFilteringInformationHolder().getClazzIds();
        if (clazzIds != null && !clazzIds.isEmpty()) {
            Set<String> ids = clazzIds.parallelStream().map(f -> f.getId()).collect(Collectors.toSet());
            addClazzPredicate(existingPredicates, r, cb, ids);
        }

        if (!count && q instanceof CriteriaQuery<?>) {
            addSorted((CriteriaQuery<?>) q, r, cb, sort, orders, c);
        }
        if (fromDate != null) {
            existingPredicates.add(cb.greaterThanOrEqualTo(r.get(Baseclass_.creationDate), fromDate));
        }

        if (toDate != null) {
            existingPredicates.add(cb.lessThanOrEqualTo(r.get(Baseclass_.creationDate), toDate));

        }
        if (fullTextLike != null && !fullTextLike.isEmpty()) {
            existingPredicates.add(cb.like(cb.lower(r.get(Baseclass_.searchKey)), fullTextLike.toLowerCase()));
        }

        List<BaseclassNotIdFiltering> excludingIds = queryInformationHolder.getFilteringInformationHolder().getExcludingIds();
        if (excludingIds != null && !excludingIds.isEmpty()) {
            Set<String> ids=excludingIds.stream().map(f->f.getId()).collect(Collectors.toSet());
            existingPredicates.add(cb.not(r.get(Baseclass_.id).in(ids)));
        }

        List<BaseclassOnlyIdFiltering> onlyIds = queryInformationHolder.getFilteringInformationHolder().getOnlyIds();
        if (onlyIds != null && !onlyIds.isEmpty()) {
            Set<String> ids=onlyIds.stream().map(f->f.getId()).collect(Collectors.toSet());
            existingPredicates.add(r.get(Baseclass_.id).in(ids));
        }
        if (likeName != null && !likeName.isEmpty()) {
            Predicate like;
            if (queryInformationHolder.getFilteringInformationHolder().isLikeCaseSensitive()) {
                like = cb.like(r.get(Baseclass_.name), likeName);
            } else {
                like = cb.like(cb.lower(r.get(Baseclass_.name)), likeName.toLowerCase());

            }

            existingPredicates.add(like);
        }

        if (!fetchSoftDelete) {
            excludeDeleted(existingPredicates, cb, r);

        }
        if(queryInformationHolder.getFilteringInformationHolder().supportingDynamic()&&queryInformationHolder.getFilteringInformationHolder().getGenericPredicates()!=null && !queryInformationHolder.getFilteringInformationHolder().getGenericPredicates().isEmpty()&&cb instanceof CriteriaBuilderImpl){
            CriteriaBuilderImpl cbi= (CriteriaBuilderImpl) cb;
            for (Map.Entry<String, Object> predicate : queryInformationHolder.getFilteringInformationHolder().getGenericPredicates().entrySet()) {
                String key=predicate.getKey();
                Object val=predicate.getValue();
                List<Expression<String>> params= new ArrayList<>(Arrays.asList(r.get("jsonNode")));
                params.addAll(Stream.of(key.split("\\.")).map(f->cb.literal(f)).collect(Collectors.toList()));
                Expression<?>[] paramsArr=new Expression<?>[params.size()];
                params.toArray(paramsArr);
                Expression<String> jsonb_extract_path_text = cb.function("jsonb_extract_path_text", String.class, paramsArr);
                org.eclipse.persistence.expressions.Expression extract = cbi.toExpression(jsonb_extract_path_text).cast("boolean");

                Expression castedExpression = cbi.fromExpression(extract);
                Predicate pred = cb.equal(castedExpression,val);
                existingPredicates.add(pred);
            }

        }
        if (securityContext != null) {

            baseclassRepository.addBaseclassPredicates(cb,q,r,existingPredicates,securityContext);

        }

    }

    @Override
    public <T extends Baseclass> void excludeDeleted(List<Predicate> existingPredicates, CriteriaBuilder cb, From<?, T> r) {
        existingPredicates.add(cb.or(r.get(Baseclass_.softDelete).isNull(), cb.isFalse(r.get(Baseclass_.softDelete))));
    }


    /**
     * get a list of instances filtered (for now) by access control, categories,
     * keywords, paged and sorted. The method handles n+1 through EclipseLink
     * Batchfetch See <a href="https://www.eclipse.org/eclipselink/documentation/2.5/jpa/extensions/a_batchfetch.htm">eclipselink docs</a>
     * This method should be called only if there are no predicates added by the
     * caller
     *
     * @param queryInformationHolder object containing all filters , and jpa criteria objects used
     * @return list of filtered baseclasses
     */

    @Override
    public <T extends Baseclass> List<T> getAllFiltered(QueryInformationHolder<T> queryInformationHolder) {

        return getAllFiltered(queryInformationHolder, new ArrayList<>(), null, null, null);
    }

    @Override
    public <T extends Baseclass> long countAllFiltered(QueryInformationHolder<T> queryInformationHolder) {
        return countAllFiltered(queryInformationHolder, new ArrayList<>(), null, null, null);
    }

    @Override
    public <T extends Baseclass> long countAllFiltered(QueryInformationHolder<T> queryInformationHolder,
                                                       List<Predicate> existingPredicates, CriteriaBuilder cb, CriteriaQuery<Long> q, Root<T> r) {
        Class<T> c = queryInformationHolder.getType();
        if (cb == null) {
            cb = em.getCriteriaBuilder();
        }
        if (q == null) {
            q = cb.createQuery(Long.class);
        }
        if (r == null) {
            r = q.from(c);
        }

        if (existingPredicates == null) {
            existingPredicates = new ArrayList<>();
        }
        prepareQuery(queryInformationHolder, existingPredicates, cb, q, r, true);
        finalizeCountQuery(r, q, existingPredicates, cb);
        TypedQuery<Long> query = em.createQuery(q);

        return getSingleResult(query);
    }

    /**
     * get a list of instances filtered (for now) by access control, categories,
     * keywords, paged and sorted. The method handles n+1 through EclipseLink
     * Batchfetch See <a href="https://www.eclipse.org/eclipselink/documentation/2.5/jpa/extensions/a_batchfetch.htm">eclipselink docs</a>
     * This method should be called if there are predicates added by the caller
     * , all predicate are 'AND'
     *
     * @param queryInformationHolder object containing all filters , and jpa criteria objects used
     * @param existingPredicates epredicates
     * @param cb criteria builder
     * @param q query
     * @param r root
     * @return filtered baseclass
     */
    @Override
    public <T extends Baseclass> List<T> getAllFiltered(QueryInformationHolder<T> queryInformationHolder,
                                                        List<Predicate> existingPredicates, CriteriaBuilder cb, CriteriaQuery<T> q, From<?, T> r) {
        return getAllFiltered(queryInformationHolder, existingPredicates, cb, q, r, queryInformationHolder.getType(), r);
    }

    @Override
    public <T extends Baseclass> List<T> getAllFiltered(QueryInformationHolder<T> queryInformationHolder,
                                                        List<Predicate> existingPredicates, List<Order> orders, CriteriaBuilder cb, CriteriaQuery<T> q, From<?, T> r) {
        return getAllFiltered(queryInformationHolder, existingPredicates, orders, cb, q, r, queryInformationHolder.getType(), r);
    }


    /**
     * get a list of instances filtered (for now) by access control, categories,
     * keywords, paged and sorted. The method handles n+1 through EclipseLink
     * Batchfetch See <a href="https://www.eclipse.org/eclipselink/documentation/2.5/jpa/extensions/a_batchfetch.htm">eclipselink docs</a>
     * This method should be called if there are predicates added by the caller
     * , all predicate are 'AND'
     *
     * @param queryInformationHolder object containing all filters , and jpa criteria objects used
     * @param existingPredicates predicates
     * @param cb criteria builder
     * @param q query
     * @param r root
     * @return list of filtered baseclasses
     */
    @Override
    public <T extends Baseclass, E> List<E> getAllFiltered(QueryInformationHolder<T> queryInformationHolder,
                                                           List<Predicate> existingPredicates, CriteriaBuilder cb, CriteriaQuery<E> q, From<?, T> r, Class<E> selectionClass, Selection<? extends E> select) {
        return getAllFiltered(queryInformationHolder, existingPredicates, null, cb, q, r, selectionClass, select);
    }

    @Override
    public <T extends Baseclass, E> List<E> getAllFiltered(QueryInformationHolder<T> queryInformationHolder,
                                                           List<Predicate> existingPredicates, List<Order> orders, CriteriaBuilder cb, CriteriaQuery<E> q, From<?, T> r, Class<E> selectionClass, Selection<? extends E> select) {
        Class<T> c = queryInformationHolder.getType();
        if (cb == null) {
            cb = em.getCriteriaBuilder();
        }
        if (q == null) {
            q = cb.createQuery(selectionClass);
        }
        if (r == null) {
            r = q.from(c);

        }
        if (select == null) {
            select = (Selection<? extends E>) r;
        }
        if (existingPredicates == null) {
            existingPredicates = new ArrayList<>();
        }
        if (orders == null) {
            orders = new ArrayList<>();
        }
        prepareQuery(queryInformationHolder, existingPredicates, cb, q, r);
        Integer pagesize = queryInformationHolder.getFilteringInformationHolder().getPageSize();
        Integer currentPage = queryInformationHolder.getFilteringInformationHolder().getCurrentPage();
        finalizeQuery(r, q, existingPredicates, cb, select);
       // q.getGroupList().add(r.get(Baseclass_.id));

        TypedQuery<E> query = em.createQuery(q);

        setBatchFetch(query, queryInformationHolder.getBatchFetchString());
        if (pagesize!=null&&currentPage!=null&&pagesize>0&&currentPage>-1) {
            setPageQuery(pagesize, currentPage, query);
        }
        return getResultList(query);

    }


    @Override
    public <T extends Baseclass> T getFiltered(QueryInformationHolder<T> queryInformationHolder,
                                               List<Predicate> existingPredicates) {
        return getFiltered(queryInformationHolder, existingPredicates, null, null, null);
    }

    @Override
    public <T extends Baseclass> T getFiltered(QueryInformationHolder<T> queryInformationHolder,
                                               List<Predicate> existingPredicates, CriteriaBuilder cb, CriteriaQuery<T> q, Root<T> r) {
        Class<T> c = queryInformationHolder.getType();
        if (cb == null) {
            cb = em.getCriteriaBuilder();
        }
        if (q == null) {
            q = cb.createQuery(c);
        }
        if (r == null) {
            r = q.from(c);
        }

        if (existingPredicates == null) {
            existingPredicates = new ArrayList<>();
        }
        prepareQuery(queryInformationHolder, existingPredicates, cb, q, r);
        Integer pagesize = queryInformationHolder.getFilteringInformationHolder().getPageSize();
        Integer currentPage = queryInformationHolder.getFilteringInformationHolder().getCurrentPage();
        finalizeQuery(r, q, existingPredicates, cb);
        TypedQuery<T> query = em.createQuery(q);
        setBatchFetch(query, queryInformationHolder.getBatchFetchString());

        if (pagesize!=null&&currentPage!=null&&pagesize>0&&currentPage>-1) {
            setPageQuery(pagesize, currentPage, query);
        }
        return getSingleResult(query);
    }

    @Override
    public <T extends Baseclass, E> E getFiltered(QueryInformationHolder<T> queryInformationHolder,
                                                  List<Predicate> existingPredicates, CriteriaBuilder cb, CriteriaQuery<E> q, Root<T> r, Class<E> selectionClass, Selection<? extends E> select) {
        Class<T> c = queryInformationHolder.getType();
        if (cb == null) {
            cb = em.getCriteriaBuilder();
        }
        if (q == null) {
            q = cb.createQuery(selectionClass);
        }
        if (r == null) {
            r = q.from(c);
        }

        if (existingPredicates == null) {
            existingPredicates = new ArrayList<>();
        }
        prepareQuery(queryInformationHolder, existingPredicates, cb, q, r);
        Integer pagesize = queryInformationHolder.getFilteringInformationHolder().getPageSize();
        Integer currentPage = queryInformationHolder.getFilteringInformationHolder().getCurrentPage();
        finalizeQuery(r, q, existingPredicates, cb, select);
        TypedQuery<E> query = em.createQuery(q);
        setBatchFetch(query, queryInformationHolder.getBatchFetchString());

        if (pagesize!=null&&currentPage!=null&&pagesize>0&&currentPage>-1) {
            setPageQuery(pagesize, currentPage, query);
        }
        return getSingleResult(query);
    }

    /**
     * adds hints for batch fetch (this is Eclipselink specific!, solves n+1
     * syndrome)
     *
     * @param query query
     * @param list list
     */
    private <T extends Baseclass> void setBatchFetch(TypedQuery<?> query, List<String> list) {
        if (list != null) {
            for (String string : list) {
                query.setHint("eclipselink.batch", string);
            }

            query.setHint("eclipselink.batch.type", "IN");
        }

    }

    /**
     * adds all predicates to the query
     *
     * @param r root
     * @param q query
     * @param preds predicates
     * @param cb criteria builder
     */
    @Override
    public <T> void finalizeQuery(From<?, T> r, CriteriaQuery<T> q, List<Predicate> preds, CriteriaBuilder cb) {
        finalizeQuery(r, q, preds, cb, r);

    }

    /**
     * adds all predicates to the query
     *
     * @param r root
     * @param q query
     * @param preds predicates
     * @param cb criteria builder
     */
    @Override
    public <T> void finalizeQuery(From<?, T> r, CriteriaDelete<T> q, List<Predicate> preds, CriteriaBuilder cb) {
        finalizeQuery(r, q, preds, cb, r);

    }

    /**
     * adds all predicates to the query
     *
     * @param r root
     * @param q query
     * @param preds predicates
     * @param cb criteria builder
     */
    @Override
    public <T, E> void finalizeQuery(From<?, T> r, CriteriaQuery<E> q, List<Predicate> preds, CriteriaBuilder cb, Selection<? extends E> select) {
        if (preds != null && !preds.isEmpty()) {
            Predicate[] predsArray = new Predicate[preds.size()];
            predsArray = preds.toArray(predsArray);
            q.select(select).where(cb.and(predsArray)).distinct(true);


        } else {
            q.select(select);
        }

    }

    /**
     * adds all predicates to the query
     *
     * @param r root
     * @param q query
     * @param preds predicates
     * @param cb criteria builder
     */
    @Override
    public <T, E> void finalizeQuery(From<?, T> r, CriteriaDelete<E> q, List<Predicate> preds, CriteriaBuilder cb, Selection<? extends E> select) {
        Predicate[] predsArray = new Predicate[preds.size()];
        predsArray = preds.toArray(predsArray);
        q.where(cb.and(predsArray));


    }

    @Override
    public <T> void finalizeCountQuery(From<?, T> r, CriteriaQuery<Long> q, List<Predicate> preds, CriteriaBuilder cb) {
        finalizeQuery(r, q, preds, cb, cb.countDistinct(r));

    }

    @Override
    public <T> T getSingleResult(TypedQuery<T> query) {
        return query.getSingleResult();
    }

    @Override
    public <T> T getSingleResultOrNull(TypedQuery<T> query) {
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }

    }

    @Override
    public <T> List<T> getResultList(TypedQuery<T> query) {
        return query.getResultList();
    }

    @Override
    public void setPageQuery(int pagesize, int currentPage, TypedQuery<?> query) {
        query.setFirstResult(currentPage * pagesize);
        query.setMaxResults(pagesize);
    }

    @Override
    public <T> void addSorted(CriteriaQuery<?> q, From<?, T> r, CriteriaBuilder cb, List<SortParameter> sort, Class<T> c) {
        addSorted(q, r, cb, sort, null, c);
    }


    @Override
    public <T> void addSorted(CriteriaQuery<?> q, From<?, T> r, CriteriaBuilder cb, List<SortParameter> sort, List<Order> orders, Class<T> c) {
        List<Order> orderby = new ArrayList<>();
        if (orders != null) {
            for (Order order : orders) {
                q.orderBy(order);
            }
        }

        if (sort != null && !sort.isEmpty()) {
            Order o;
            for (SortParameter sortparameter : sort) {
                if (isQurifyable(c, sortparameter.getName())) {
                    if (sortparameter.getSortingOrder().equals(SortingOrder.ASCENDING)) {
                        o = cb.asc(r.get(sortparameter.getName()));
                    } else {
                        o = cb.desc(r.get(sortparameter.getName()));

                    }

                    if (o != null) {
                        orderby.add(o);
                    }
                } else {
                    logger.warn( sortparameter.getName() + " is not qurifyable");
                }

            }
            if (!orderby.isEmpty()) {
                q.orderBy(orderby);
            }

        }

    }


    private boolean isSuperAdmin(List<Role> roles) {
        for (Role role : roles) {
            if (role.getId().equals("HzFnw-nVR0Olq6WBvwKcQg")) {
                return true;
            }

        }
        return false;

    }



    private <X, E, Z extends X> PluralAttribute<Z, List<E>, E> convertListToPlural(ListAttribute<X, E> attr, Class<Z> other) {
        return (PluralAttribute<Z, List<E>, E>) attr;
    }

    @Override
    public <T> List<T> getAll(Class<T> c) {
        return getAll(c, null, null);
    }


    @Override
    public <T> List<T> getAll(Class<T> c, Integer pageSize, Integer currentPage) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<T> q = cb.createQuery(c);
        Root<T> r = q.from(c);
        q.select(r);
        TypedQuery<T> query = em.createQuery(q);
        if (pageSize != null && pageSize > 0 && currentPage != null && currentPage > -1) {
            setPageQuery(pageSize, currentPage, query);
        }
        return query.getResultList();

    }

    @Override
    public <T> long countAll(Class<T> c) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> q = cb.createQuery(Long.class);
        Root<T> r = q.from(c);
        q.select(cb.count(r));
        TypedQuery<Long> query = em.createQuery(q);

        return query.getSingleResult();

    }


    /**
     * @param user user
     * @param op operation
     * @return a list of denied baseclasses  for user using Operation
     */
    @Override
    public Pair<List<Baseclass>, List<Baseclass>> getDenied(User user, Operation op) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<UserToBaseClass> q = cb.createQuery(UserToBaseClass.class);
        Root<UserToBaseClass> r = q.from(UserToBaseClass.class);

        Predicate p1 = cb.and(
                cb.or(cb.isFalse(r.get(SecurityLink_.softDelete)), r.get(SecurityLink_.softDelete).isNull()),
                cb.equal(r.get(UserToBaseClass_.leftside), user),
                cb.equal(r.get(UserToBaseClass_.value), op),
                cb.equal(r.get(UserToBaseClass_.simplevalue), IOperation.Access.deny.name())
        );

        //p1=cb.or(p1,p2);
        List<Predicate> preds = new ArrayList<>();
        preds.add(p1);
        finalizeQuery(r, q, preds, cb);
        TypedQuery<UserToBaseClass> query = em.createQuery(q);

        List<UserToBaseClass> deniedUsers = query.getResultList();
        CriteriaQuery<RoleToBaseclass> q1 = cb.createQuery(RoleToBaseclass.class);
        Root<RoleToBaseclass> r1 = q1.from(RoleToBaseclass.class);
        Join<RoleToBaseclass, Role> j1 = cb.treat(r1.join(RoleToBaseclass_.leftside, JoinType.LEFT), Role.class);
        Join<Role, RoleToUser> j2 = j1.join(Role_.roleToUser, JoinType.INNER);

        Predicate p2 = cb.and(
                cb.or(cb.isFalse(r1.get(SecurityLink_.softDelete)), r1.get(SecurityLink_.softDelete).isNull()),

                cb.equal(j2.get(RoleToUser_.rightside), user),
                cb.equal(r1.get(RoleToBaseclass_.value), op),
                cb.equal(r1.get(RoleToBaseclass_.simplevalue), IOperation.Access.deny.name())
        );
        List<Predicate> preds1 = new ArrayList<>();
        preds.add(p2);
        finalizeQuery(r1, q1, preds, cb);
        TypedQuery<RoleToBaseclass> query1 = em.createQuery(q1);
        List<RoleToBaseclass> deniedRoles = query1.getResultList();
        List<Baseclass> deniedUsersBase = new ArrayList<>();
        List<Baseclass> deniedRolesBase = new ArrayList<>();
        for (RoleToBaseclass roleToBaseclass : deniedRoles) {
            deniedRolesBase.add(roleToBaseclass.getRightside());
        }
        for (UserToBaseClass userToBaseClass : deniedUsers) {
            deniedUsersBase.add(userToBaseClass.getRightside());
        }

        return new MutablePair<>(deniedUsersBase, deniedRolesBase);
    }


    private List<Role> getAllRoles(User user) {
        Class<RoleToUser> c = RoleToUser.class;
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<RoleToUser> q = cb.createQuery(c);
        Root<RoleToUser> r = q.from(c);
        List<Predicate> preds = new ArrayList<>();
        preds.add(cb.and(cb.equal(r.get(RoleToUser_.rightside), user),cb.isFalse(r.get(RoleToUser_.softDelete))));
        finalizeQuery(r, q, preds, cb);
        TypedQuery<RoleToUser> query = em.createQuery(q);
        List<RoleToUser> links = getResultList(query);
        List<Role> roles = new ArrayList<>();
        for (RoleToUser link : links) {
            roles.add(link.getLeftside());
        }


        return roles;
    }

    @Override
    public <T extends Baseclass> void addTenantsPredicate(List<Predicate> existingPredicates, From<?, T> r,
                                                          CriteriaBuilder cb, Set<String> tenantsIds) {
        Join<T, SecurityTenant> tenantJoin = r.join(Baseclass_.tenant);
        existingPredicates.add(tenantJoin.get(Tenant_.id).in(tenantsIds));


    }

    @Override
    public <T extends Baseclass> void addClazzPredicate(List<Predicate> existingPredicates, From<?, T> r,
                                                        CriteriaBuilder cb, Set<String> clazzIds) {
        Join<T, Clazz> tenantJoin = r.join(Baseclass_.clazz);
        existingPredicates.add(tenantJoin.get(Clazz_.id).in(clazzIds));


    }



    @Override
    public void addTenantToBaseClass(Baseclass b, Tenant tenant, SecurityContext securityContext) {
        TenantToBaseClassPremission tenantToBaseClassPremission = new TenantToBaseClassPremission(b.getName(), securityContext);
        tenantToBaseClassPremission.setTenant(tenant);
        tenantToBaseClassPremission.setBaseclass(b);

    }

    @Override
    public void refresh(Object o) {
        em.refresh(o);
    }

    @Override
    public EntityManager getEm() {
        return em;
    }

    @Override
    public void setEm(EntityManager em) {
        this.em = em;
    }

    @Override
    public void refrehEntityManager() {
        em.flush();
        em.clear();
    }


    @Override
    @Transactional
    public void massMerge(List<?> toMerge,boolean updatedate) {
        List<Object> events=new ArrayList<>();
        OffsetDateTime now = OffsetDateTime.now();
        for (Object o : toMerge) {
            if(o instanceof Baseclass){
                Baseclass baseclass = (Baseclass) o;
                boolean created=baseclass.getUpdateDate()==null;
                if(updatedate){
                    baseclass.setUpdateDate(now);
                }
                if(logger.isDebugEnabled()){
                    logger.debug("merging "+ baseclass.getId() +" updateDate flag is "+updatedate +" update date is "+baseclass.getUpdateDate());
                }
                updateSearchKey(baseclass);
                if(created){
                    BaseclassCreated<?> baseclassCreated=new BaseclassCreated<>(baseclass);
                    events.add(baseclassCreated);
                }
                else{
                    BaseclassUpdated<?> baseclassUpdated=new BaseclassUpdated<>(baseclass);
                    events.add(baseclassUpdated);
                }

            }

            em.merge(o);
        }
        for (Object event : events) {
            eventPublisher.publishEvent(event);
        }
    }

    public void updateSearchKey(Baseclass b){
        try {
            if (isFreeTextSupport(b.getClass())) {
                String freeText= Stream.of( Introspector.getBeanInfo(b.getClass(), Object.class).getPropertyDescriptors()).filter(this::isPropertyForTextSearch).map(PropertyDescriptor::getReadMethod).filter(this::isIncludeMethod).map(f-> invoke(b, f)).filter(Objects::nonNull).map(f->f+"").filter(f->!f.isEmpty()).collect(Collectors.joining("|"));
                b.setSearchKey(freeText);
                logger.debug("Free Text field for "+b.getId() +" is set");

            }
        }

        catch (Exception e){
            logger.error("unable to set free text field",e);
        }
    }
    private static final Map<String, Boolean> freeTextSuportMap = new ConcurrentHashMap<>();

    private Object invoke(Baseclass b, Method f) {
        try {
            return f.invoke(b);
        } catch (IllegalAccessException | InvocationTargetException e) {
            logger.error("unable to invoke method",e);
        }
        return null;
    }

    private boolean isFreeTextSupport(Class<? extends Baseclass> aClass) {
        return freeTextSuportMap.computeIfAbsent(aClass.getCanonicalName(),f-> checkFreeTextSupport(aClass));
    }
    private boolean isIncludeMethod(Method f) {
        if(f==null||f.isAnnotationPresent(Transient.class)){
            return false;
        }
        FullTextSearchOptions fullTextSearchOptions=f.getAnnotation(FullTextSearchOptions.class);

        return fullTextSearchOptions==null||fullTextSearchOptions.include();
    }

    private boolean checkFreeTextSupport(Class<? extends Baseclass> aClass) {
        FullTextSearch annotation = aClass.getAnnotation(FullTextSearch.class);
        return annotation!=null&&annotation.supported();
    }

    private boolean isPropertyForTextSearch(PropertyDescriptor f) {
        return propertyTypes.contains(f.getPropertyType());
    }

    private static final Set<Class<?>> propertyTypes= Collections.unmodifiableSet(new HashSet<>(Arrays.asList(
            String.class,
            int.class,
            double.class,
            float.class,
            int.class,
            long.class,
            short.class,
            Double.class,
            Float.class,
            Integer.class,
            Long.class,
            Short.class

    )));

    @Override
    @Transactional
    public <T extends Baseclass> List<T> findByIds(Class<T> c, Set<String> requested) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<T> q = cb.createQuery(c);
        Root<T> r = q.from(c);
        q.select(r).where(r.get(Baseclass_.id).in(requested));
        TypedQuery<T> query = em.createQuery(q);
        return query.getResultList();
    }

    @Override
    @Transactional
    public void massDelete(MassDeleteRequest massDeleteRequest) {
        Set<String> ids = massDeleteRequest.getBaseclass().parallelStream().map(f -> f.getId()).collect(Collectors.toSet());
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaDelete<Baseclass> q = cb.createCriteriaDelete(Baseclass.class);
        Root<Baseclass> r = q.from(Baseclass.class);
        q.where(r.get(Baseclass_.id).in(ids));
        em.createQuery(q).executeUpdate();
    }

    @Override
    public List<BaseclassCount> getBaseclassCount(BaseclassCountRequest baseclassCountRequest, SecurityContext securityContext) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<BaseclassCount> q = cb.createQuery(BaseclassCount.class);
        Root<Baseclass> r = q.from(Baseclass.class);
        Join<Baseclass,Clazz> join=r.join(Baseclass_.clazz);

        List<Predicate> preds=new ArrayList<>();
        preds.add(r.get(Baseclass_.tenant).isNotNull());
        preds.add(cb.isFalse(r.get(Baseclass_.softDelete)));
        QueryInformationHolder<Baseclass> queryInformationHolder=new QueryInformationHolder<>(baseclassCountRequest,Baseclass.class,securityContext);
        prepareQuery(queryInformationHolder,preds,cb,q,r);
        Predicate[] predsArr=new Predicate[preds.size()];
        preds.toArray(predsArr);
        CriteriaQuery<BaseclassCount> select;
        List<Expression<?>> groupBy;
        if(baseclassCountRequest.isGroupByTenant()){
             select = q.select(cb.construct(BaseclassCount.class,r.get(Baseclass_.tenant), join.get(Clazz_.name), cb.count(r.get(Baseclass_.id))));
            groupBy= Arrays.asList(r.get(Baseclass_.tenant),join.get(Clazz_.name));
        }
        else{
            select = q.select(cb.construct(BaseclassCount.class, join.get(Clazz_.name), cb.count(r.get(Baseclass_.id))));
            groupBy= Arrays.asList(join.get(Clazz_.name));


        }
        select.where(predsArr)
                .groupBy(groupBy)
                .orderBy(cb.asc(join.get(Clazz_.name)));
        return em.createQuery(q).getResultList();
    }

    @Override
    @Transactional
    public void merge(Object base) {
        merge(base,true);
    }

    @Override
    @Transactional
    public void massMerge(List<?> toMerge) {
        massMerge(toMerge,true);
    }
}
