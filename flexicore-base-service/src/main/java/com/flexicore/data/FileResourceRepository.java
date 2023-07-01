/*******************************************************************************
 *  Copyright (C) FlexiCore, Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Avishay Ben Natan And Asaf Ben Natan, October 2015
 ******************************************************************************/
package com.flexicore.data;

import com.flexicore.annotations.InheritedComponent;
import com.flexicore.model.*;
import com.flexicore.request.FileResourceFilter;
import com.flexicore.request.ZipFileFilter;
import com.flexicore.request.ZipFileToFileResourceFilter;
import com.flexicore.security.SecurityContext;
import com.flexicore.security.SecurityContextBase;
import com.wizzdi.flexicore.file.model.*;
import com.wizzdi.flexicore.security.data.BaseclassRepository;
import com.wizzdi.flexicore.security.data.BasicRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import jakarta.persistence.metamodel.SingularAttribute;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.flexicore.service.impl.BaseclassNewService.getCompatiblePagination;


@InheritedComponent
@Component("fileResourceRepositoryOld")
@Transactional
public class FileResourceRepository  {

    @PersistenceContext
    private EntityManager em;
    @Autowired
    private BaseclassRepository baseclassRepository;


   private static final Logger logger = LoggerFactory.getLogger(FileResourceRepository.class);

    public void persist(Object o) {
        em.persist(o);
    }

    public List<FileResource> getFileResourceScheduledForDelete(OffsetDateTime date) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<FileResource> q = cb.createQuery(FileResource.class);
        Root<FileResource> r = q.from(FileResource.class);
        Predicate predicate = cb.and(
                cb.lessThanOrEqualTo(r.get(FileResource_.keepUntil), date)
                , cb.or(
                        cb.isFalse(r.get(FileResource_.softDelete))
                        , cb.isNull(r.get(FileResource_.softDelete))));
        q.select(r).where(predicate).distinct(true);
        TypedQuery<FileResource> query = em.createQuery(q);
        return query.getResultList();
    }

    public List<FileResource> listAllFileResources(FileResourceFilter fileResourceFilter, SecurityContext securityContext) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<FileResource> q = cb.createQuery(FileResource.class);
        Root<FileResource> r = q.from(FileResource.class);
        List<Predicate> preds = new ArrayList<>();
        addFileResourcePredicates(fileResourceFilter, r, q, cb, preds);
        if(securityContext!=null){
            Join<FileResource,Baseclass> security=r.join(FileResource_.security);
            baseclassRepository.addBaseclassPredicates(cb,q,security,preds,securityContext);
        }
        q.select(r).where(preds.toArray(Predicate[]::new));
        TypedQuery<FileResource> query=em.createQuery(q);
        BasicRepository.addPagination(getCompatiblePagination(fileResourceFilter),query);
        return query.getResultList();
    }

    private void addFileResourcePredicates(FileResourceFilter fileResourceFilter, Root<FileResource> r, CriteriaQuery<?> q, CriteriaBuilder cb, List<Predicate> preds) {
        if (fileResourceFilter.getMd5s() != null && !fileResourceFilter.getMd5s().isEmpty()) {
            preds.add(r.get(FileResource_.md5).in(fileResourceFilter.getMd5s()));
        }
    }

    public List<ZipFileToFileResource> listZipFileToFileResource(ZipFileToFileResourceFilter zipFileToFileResourceFilter, SecurityContext securityContext) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<ZipFileToFileResource> q = cb.createQuery(ZipFileToFileResource.class);
        Root<ZipFileToFileResource> r = q.from(ZipFileToFileResource.class);
        List<Predicate> preds = new ArrayList<>();
        addZipFileToFileResourcePredicates(zipFileToFileResourceFilter, r, q, cb, preds);
        TypedQuery<ZipFileToFileResource> query=em.createQuery(q);
        BasicRepository.addPagination(getCompatiblePagination(zipFileToFileResourceFilter),query);
        return query.getResultList();
    }

    private void addZipFileToFileResourcePredicates(ZipFileToFileResourceFilter zipFileToFileResourceFilter, Root<ZipFileToFileResource> r, CriteriaQuery<ZipFileToFileResource> q, CriteriaBuilder cb, List<Predicate> preds) {

        Join<ZipFileToFileResource, ZipFile> zipFileJoin = r.join(ZipFileToFileResource_.zipFile);
        if (zipFileToFileResourceFilter.getZipFiles() != null && !zipFileToFileResourceFilter.getZipFiles().isEmpty()) {
            Set<String> ids = zipFileToFileResourceFilter.getZipFiles().parallelStream().map(f -> f.getId()).collect(Collectors.toSet());

            preds.add(zipFileJoin.get(ZipFile_.id).in(ids));
        }

        if (zipFileToFileResourceFilter.getFileResources() != null && !zipFileToFileResourceFilter.getFileResources().isEmpty()) {
            Set<String> ids = zipFileToFileResourceFilter.getFileResources().parallelStream().map(f -> f.getId()).collect(Collectors.toSet());
            Join<ZipFileToFileResource, FileResource> join = r.join(ZipFileToFileResource_.zippedFile);
            preds.add(join.get(FileResource_.id).in(ids));
        }
        preds.add(cb.isFalse(zipFileJoin.get(ZipFile_.softDelete)));
    }

    public List<ZipFile> listAllZipFiles(ZipFileFilter zipFileFilter, SecurityContext securityContext) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<ZipFile> q = cb.createQuery(ZipFile.class);
        Root<ZipFile> r = q.from(ZipFile.class);
        List<Predicate> preds = new ArrayList<>();
        addZipFilePredicates(zipFileFilter, r, q, cb, preds);
        if(securityContext!=null){
            Join<ZipFile,Baseclass> security=r.join(ZipFile_.security);
            baseclassRepository.addBaseclassPredicates(cb,q,security,preds,securityContext);
        }
        q.select(r).where(preds.toArray(Predicate[]::new));
        TypedQuery<ZipFile> query=em.createQuery(q);
        BasicRepository.addPagination(getCompatiblePagination(zipFileFilter),query);
        return query.getResultList();
    }

    private void addZipFilePredicates(ZipFileFilter zipFileFilter, Root<ZipFile> r, CriteriaQuery<ZipFile> q, CriteriaBuilder cb, List<Predicate> preds) {

        if (zipFileFilter.getFileResources() != null && !zipFileFilter.getFileResources().isEmpty()) {
            Set<String> ids = zipFileFilter.getFileResources().parallelStream().map(f -> f.getId()).collect(Collectors.toSet());
            Join<ZipFile, ZipFileToFileResource> join = r.join(ZipFile_.zippedFilesToFileResourceList);
            Join<ZipFileToFileResource, FileResource> join2 = join.join(ZipFileToFileResource_.zippedFile);
            preds.add(join2.get(FileResource_.id).in(ids));
        }
    }


    public long countAllFileResources(FileResourceFilter fileResourceFilter, SecurityContext securityContext) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> q = cb.createQuery(Long.class);
        Root<FileResource> r = q.from(FileResource.class);
        List<Predicate> preds = new ArrayList<>();
        addFileResourcePredicates(fileResourceFilter, r, q, cb, preds);
        if(securityContext!=null){
            Join<FileResource,Baseclass> security=r.join(FileResource_.security);
            baseclassRepository.addBaseclassPredicates(cb,q,security,preds,securityContext);
        }
        q.select(cb.count(r)).where(preds.toArray(Predicate[]::new));
        TypedQuery<Long> query=em.createQuery(q);
        BasicRepository.addPagination(getCompatiblePagination(fileResourceFilter),query);
        return query.getSingleResult();
    }


    public <T extends Baseclass> List<T> listByIds(Class<T> c, Set<String> ids, SecurityContextBase securityContext) {
        return baseclassRepository.listByIds(c, ids, securityContext);
    }

    public <T extends Baseclass> T getByIdOrNull(String id, Class<T> c, SecurityContextBase securityContext) {
        return baseclassRepository.getByIdOrNull(id, c, securityContext);
    }

    public <D extends Basic, E extends Baseclass, T extends D> T getByIdOrNull(String id, Class<T> c, SingularAttribute<D, E> baseclassAttribute, SecurityContextBase securityContext) {
        return baseclassRepository.getByIdOrNull(id, c, baseclassAttribute, securityContext);
    }

    public <D extends Basic, E extends Baseclass, T extends D> List<T> listByIds(Class<T> c, Set<String> ids, SingularAttribute<D, E> baseclassAttribute, SecurityContextBase securityContext) {
        return baseclassRepository.listByIds(c, ids, baseclassAttribute, securityContext);
    }

    public <D extends Basic, T extends D> List<T> findByIds(Class<T> c, Set<String> ids, SingularAttribute<D, String> idAttribute) {
        return baseclassRepository.findByIds(c, ids, idAttribute);
    }

    public <T extends Basic> List<T> findByIds(Class<T> c, Set<String> requested) {
        return baseclassRepository.findByIds(c, requested);
    }

    public <T> T findByIdOrNull(Class<T> type, String id) {
        return baseclassRepository.findByIdOrNull(type, id);
    }

    @Transactional
    public void merge(Object base) {
        baseclassRepository.merge(base);
    }

    @Transactional
    public void merge(Object base, boolean updateDate) {
        baseclassRepository.merge(base, updateDate);
    }

    @Transactional
    public void massMerge(List<?> toMerge) {
        baseclassRepository.massMerge(toMerge);
    }

    @Transactional
    public void massMerge(List<?> toMerge, boolean updatedate) {
        baseclassRepository.massMerge(toMerge, updatedate);
    }
}
