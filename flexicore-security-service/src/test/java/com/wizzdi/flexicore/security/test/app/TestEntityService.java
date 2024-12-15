package com.wizzdi.flexicore.security.test.app;

import com.flexicore.model.Baseclass;
import com.flexicore.model.Basic;
import com.wizzdi.segmantix.model.SecurityContext;
import com.wizzdi.flexicore.security.service.BaseclassService;
import com.wizzdi.flexicore.security.service.BasicService;
import jakarta.persistence.metamodel.SingularAttribute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Component
public class TestEntityService {

    @Autowired
    private TestEntityRepository testEntityRepository;
    @Autowired
    private BasicService basicService;

    public TestEntity createTestEntity(TestEntityCreate testEntityCreate, SecurityContext securityContext){
        TestEntity testEntity=createTestEntityNoMerge(testEntityCreate, securityContext);
        testEntityRepository.merge(testEntity);
        return testEntity;

    }

    public TestEntity createTestEntityNoMerge(TestEntityCreate testEntityCreate, SecurityContext securityContext) {
        TestEntity testEntity = new TestEntity();
        testEntity.setId(UUID.randomUUID().toString());
        updateTestEntityNoMerge(testEntityCreate, testEntity);

        BaseclassService.createSecurityObjectNoMerge(testEntity, securityContext);
        return testEntity;
    }

    public boolean updateTestEntityNoMerge(TestEntityCreate testEntityCreate, TestEntity testEntity) {
        return basicService.updateBasicNoMerge(testEntityCreate,testEntity);
    }

    public List<TestEntity> listAllTestEntities(TestEntityFilter filtering, SecurityContext securityContext) {
        return testEntityRepository.listAllTestEntities(filtering, securityContext);
    }

    @Transactional
    public <T> T merge(T base, boolean updateDate, boolean propagateEvents) {
        return testEntityRepository.merge(base, updateDate, propagateEvents);
    }

    @Transactional
    public void massMerge(List<?> toMerge, boolean updatedate, boolean propagateEvents) {
        testEntityRepository.massMerge(toMerge, updatedate, propagateEvents);
    }

    public <T extends Baseclass> List<T> listByIds(Class<T> c, Set<String> ids, SecurityContext securityContext) {
        return testEntityRepository.listByIds(c, ids, securityContext);
    }

    public <T extends Baseclass> T getByIdOrNull(String id, Class<T> c, SecurityContext securityContext) {
        return testEntityRepository.getByIdOrNull(id, c, securityContext);
    }

    public <D extends Basic, E extends Baseclass, T extends D> T getByIdOrNull(String id, Class<T> c, SingularAttribute<D, E> baseclassAttribute, SecurityContext securityContext) {
        return testEntityRepository.getByIdOrNull(id, c, baseclassAttribute, securityContext);
    }

    public <D extends Basic, E extends Baseclass, T extends D> List<T> listByIds(Class<T> c, Set<String> ids, SingularAttribute<D, E> baseclassAttribute, SecurityContext securityContext) {
        return testEntityRepository.listByIds(c, ids, baseclassAttribute, securityContext);
    }

    public <D extends Basic, T extends D> List<T> findByIds(Class<T> c, Set<String> ids, SingularAttribute<D, String> idAttribute) {
        return testEntityRepository.findByIds(c, ids, idAttribute);
    }

    public <T extends Basic> List<T> findByIds(Class<T> c, Set<String> requested) {
        return testEntityRepository.findByIds(c, requested);
    }

    public <T> T findByIdOrNull(Class<T> type, String id) {
        return testEntityRepository.findByIdOrNull(type, id);
    }

    @Transactional
    public void merge(Object base) {
        testEntityRepository.merge(base);
    }

    @Transactional
    public void massMerge(List<?> toMerge) {
        testEntityRepository.massMerge(toMerge);
    }
}
