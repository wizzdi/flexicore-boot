package com.wizzdi.flexicore.security.data;

import com.flexicore.model.Baseclass_;
import com.flexicore.model.Basic;
import com.flexicore.model.Basic_;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.security.events.BasicCreated;
import com.wizzdi.flexicore.security.events.BasicUpdated;
import com.wizzdi.flexicore.security.request.*;
import org.pf4j.Extension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import jakarta.persistence.metamodel.SingularAttribute;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Extension
@Component
public class BasicRepository implements Plugin {
    private static final Logger logger = LoggerFactory.getLogger(BasicRepository.class);
    @PersistenceContext
    private EntityManager em;
    @Autowired
    private ApplicationEventPublisher eventPublisher;

    public static <T> boolean addPagination(PaginationFilter basicFilter, TypedQuery<T> q) {
        if (basicFilter.getPageSize() != null && basicFilter.getCurrentPage() != null && basicFilter.getCurrentPage() >= 0 && basicFilter.getPageSize() > 0) {
            q.setFirstResult(basicFilter.getCurrentPage() * basicFilter.getPageSize());
            q.setMaxResults(basicFilter.getPageSize());
        }
        return false;
    }

    public static <T extends Basic> void addBasicPropertiesFilter(BasicPropertiesFilter basicPropertiesFilter, CriteriaBuilder cb, CommonAbstractCriteria q, From<?, T> r, List<Predicate> predicates) {
        basicPropertiesFilter=basicPropertiesFilter!=null?basicPropertiesFilter:new BasicPropertiesFilter().setSoftDelete(SoftDeleteOption.DEFAULT);
        if (basicPropertiesFilter.getNames() != null && !basicPropertiesFilter.getNames().isEmpty()) {
            predicates.add(r.get(Basic_.name).in(basicPropertiesFilter.getNames()));
        }
        if (basicPropertiesFilter.getNameLike() != null && !basicPropertiesFilter.getNameLike().isEmpty()) {
            if (basicPropertiesFilter.isNameLikeCaseSensitive()) {
                predicates.add(cb.like(r.get(Basic_.name), basicPropertiesFilter.getNameLike()));
            } else {
                predicates.add(cb.like(cb.lower(r.get(Basic_.name)), basicPropertiesFilter.getNameLike().toLowerCase()));

            }
        }
        if (basicPropertiesFilter.getSoftDelete() == null) {
            basicPropertiesFilter.setSoftDelete(SoftDeleteOption.DEFAULT);
        }
        switch (basicPropertiesFilter.getSoftDelete()) {
            case DEFAULT:
            case NON_DELETED_ONLY:
                predicates.add(cb.equal(r.get(Basic_.softDelete), false));
                break;
            case DELETED_ONLY:
                predicates.add(cb.equal(r.get(Basic_.softDelete), true));
            case BOTH:
                break;
        }
        if (basicPropertiesFilter.getCreationDateFilter() != null) {
            addDateFilter(basicPropertiesFilter.getCreationDateFilter(), cb, q, r.get(Basic_.creationDate), predicates);
        }
        if (basicPropertiesFilter.getUpdateDateFilter() != null) {
            addDateFilter(basicPropertiesFilter.getCreationDateFilter(), cb, q, r.get(Basic_.updateDate), predicates);
        }
        if (basicPropertiesFilter.getOnlyIds() != null && !basicPropertiesFilter.getOnlyIds().isEmpty()) {
            predicates.add(r.get(Basic_.id).in(basicPropertiesFilter.getOnlyIds()));
        }
    }

    public static void addDateFilter(DateFilter dateFilter, CriteriaBuilder cb, CommonAbstractCriteria q, Path<OffsetDateTime> r, List<Predicate> predicates) {
        if (dateFilter == null) {
            return;
        }
        if (dateFilter.getStart() != null) {
            predicates.add(cb.greaterThanOrEqualTo(r, dateFilter.getStart()));
        }
        if (dateFilter.getEnd() != null) {
            predicates.add(cb.lessThanOrEqualTo(r, dateFilter.getEnd()));
        }
    }

    public static List<Order> getSorting(List<SortParameter> sortParameters, CriteriaBuilder cb, From<?, ?> r) {
        return sortParameters.stream().map(f -> getSorting(f, cb, r)).collect(Collectors.toList());
    }

    public static Order getSorting(SortParameter f, CriteriaBuilder cb, From<?, ?> r) {
        return f.getOrderDirection() == OrderDirection.DESC ? cb.desc(r.get(f.getName())) : cb.asc(r.get(f.getName()));
    }


    public <D extends Basic, T extends D> List<T> findByIds(Class<T> c, Set<String> ids, SingularAttribute<D, String> idAttribute) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<T> q = cb.createQuery(c);
        Root<T> r = q.from(c);
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(r.get(idAttribute).in(ids));
        q.select(r).where(predicates.toArray(Predicate[]::new));
        TypedQuery<T> query = em.createQuery(q);
        return query.getResultList();
    }


    public <T extends Basic> List<T> findByIds(Class<T> c, Set<String> requested) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<T> q = cb.createQuery(c);
        Root<T> r = q.from(c);
        q.select(r).where(r.get(Baseclass_.id).in(requested));
        TypedQuery<T> query = em.createQuery(q);
        return query.getResultList();
    }

    public <T> T findByIdOrNull(Class<T> type, String id) {
        try {
            return em.find(type, id);

        } catch (NoResultException e) {
            return null;
        }
    }

    @Transactional
    public <T> T merge(T base) {
        return merge(base, true);
    }

    @Transactional
    public <T> T merge(T base, boolean updateDate) {
        return merge(base, updateDate, true);
    }


    @Transactional
    public <T> T merge(T base, boolean updateDate, boolean propagateEvents) {
        Basic base1 = null;
        boolean created = false;
        if (base instanceof Basic) {
            OffsetDateTime now = OffsetDateTime.now();
            base1 = (Basic) base;
            created = base1.getUpdateDate() == null;
            if (updateDate) {
                base1.setUpdateDate(now);
            }
            if (created) {
                base1.setCreationDate(now);
            }
            if (logger.isDebugEnabled()) {
                logger.debug("merging " + base1.getId() + " updateDate flag is " + updateDate + " update date " + base1.getUpdateDate());
            }


        }

        T merged = em.merge(base);
        if (propagateEvents) {
            if (base1 != null) {
                if (created) {
                    eventPublisher.publishEvent(new BasicCreated<>(base1));
                } else {
                    eventPublisher.publishEvent(new BasicUpdated<>(base1));

                }
            }
        }

        return merged;

    }

    @Transactional
    public void massMerge(List<?> toMerge) {
        massMerge(toMerge, true);
    }

    @Transactional
    public void massMerge(List<?> toMerge, boolean updatedate) {
        massMerge(toMerge, updatedate, true);
    }

    @Transactional
    public void massMerge(List<?> toMerge, boolean updatedate, boolean propagateEvents) {
        List<Object> events = new ArrayList<>();
        OffsetDateTime now = OffsetDateTime.now();
        for (Object o : toMerge) {
            if (o instanceof Basic) {
                Basic baseclass = (Basic) o;
                boolean created = baseclass.getUpdateDate() == null;
                if (updatedate) {
                    baseclass.setUpdateDate(now);
                }
                if (created) {
                    baseclass.setCreationDate(now);
                }
                if (logger.isDebugEnabled()) {
                    logger.debug("merging " + baseclass.getId() + " updateDate flag is " + updatedate + " update date is " + baseclass.getUpdateDate());
                }
                if (created) {
                    BasicCreated<?> baseclassCreated = new BasicCreated<>(baseclass);
                    events.add(baseclassCreated);
                } else {
                    BasicUpdated<?> baseclassUpdated = new BasicUpdated<>(baseclass);
                    events.add(baseclassUpdated);
                }

            }

            em.merge(o);
        }
        if (propagateEvents) {
            for (Object event : events) {
                eventPublisher.publishEvent(event);
            }
        }

    }

}
