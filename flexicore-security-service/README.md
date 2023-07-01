
# ![](https://support.wizzdi.com/wp-content/uploads/2020/05/flexicore-icon-extra-small.png) FlexiCore Security Service [![Build Status](https://jenkins.wizzdi.com/buildStatus/icon?job=wizzdi+organization%2Fflexicore-security-service%2Fmaster)](https://jenkins.wizzdi.com/job/wizzdi%20organization/job/flexicore-security-service/job/master/)[![Maven Central](https://img.shields.io/maven-central/v/com.wizzdi/flexicore-security-service.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22com.wizzdi%22%20AND%20a:%22flexicore-security-service%22)


For comprehensive information about FlexiCore Security Service please visit our [site](http://wizzdi.com/).

## What it does?

FlexiCore Security Service is a FlexiCore Module and a FlexiCore Plugin.

FlexiCore Security Service defines the services required to manage  multi-tenancy access control objects and to execute a  multi-tenancy access control queries on user defined entities.
## How to use as a Module?
Add the flexicore-security-service dependency to your main app using the latest version available from maven central:

            <dependency>
                <groupId>com.wizzdi</groupId>
                <artifactId>flexicore-security-service</artifactId>
                <version>LATEST</version>
            </dependency>
            
## How to use as a Plugin?
Add the flexicore-security-service dependency to your main app using the latest version available from maven central:

            <dependency>
                <groupId>com.wizzdi</groupId>
                <artifactId>flexicore-security-service</artifactId>
                <version>LATEST</version>
                <scope>provided</scope>
            </dependency>
add the flexicore-security-service.jar to your [plugins folder.](https://github.com/wizzdi/flexicore-boot)


## Executing a Data Access Control Query
adding security predicates is easily done by calling `BaseclassRepository` `addBaseclassPredicates` method.

full example:

    @Component  
    @Extension  
    public class TestEntityRepository implements Plugin {  
       @PersistenceContext  
      private EntityManager em;  
      @Autowired  
      private BaseclassRepository baseclassRepository;  
      
      
     public List<TestEntity> listAllTestEntities(TestEntityFilter TestEntityFilter, SecurityContextBase securityContext) {  
          CriteriaBuilder cb = em.getCriteriaBuilder();  
      CriteriaQuery<TestEntity> q = cb.createQuery(TestEntity.class);  
      Root<TestEntity> r = q.from(TestEntity.class);  
      List<Predicate> predicates = new ArrayList<>();  
      addTestEntityPredicates(TestEntityFilter, cb, q, r, predicates, securityContext);  
      q.select(r).where(predicates.toArray(Predicate[]::new));  
      TypedQuery<TestEntity> query = em.createQuery(q);  
      BaseclassRepository.addPagination(TestEntityFilter, query);  
     return query.getResultList();  
      
      }  
      
       public <T extends TestEntity> void addTestEntityPredicates(TestEntityFilter TestEntityFilter, CriteriaBuilder cb, CommonAbstractCriteria q, From<?, T> r, List<Predicate> predicates, SecurityContextBase securityContext) {  
          if (securityContext != null) {  
             Join<T, Baseclass> join = r.join(TestEntity_.security);  
      baseclassRepository.addBaseclassPredicates(cb, q, join, predicates, securityContext);  
      
      }  
      
      
       }  
      
       public long countAllTestEntities(TestEntityFilter TestEntityFilter, SecurityContextBase securityContext) {  
          CriteriaBuilder cb = em.getCriteriaBuilder();  
      CriteriaQuery<Long> q = cb.createQuery(Long.class);  
      Root<TestEntity> r = q.from(TestEntity.class);  
      List<Predicate> predicates = new ArrayList<>();  
      addTestEntityPredicates(TestEntityFilter, cb, q, r, predicates, securityContext);  
      q.select(cb.count(r)).where(predicates.toArray(Predicate[]::new));  
      TypedQuery<Long> query = em.createQuery(q);  
      BaseclassRepository.addPagination(TestEntityFilter, query);  
     return query.getSingleResult();  
      
      }
    }

## Main Dependencies

[FlexiCore Boot](https://github.com/wizzdi/flexicore-boot)

[FlexiCore Security Model](https://github.com/wizzdi/flexicore-security-model)


[FlexiCore Boot Starter Data JPA](https://github.com/wizzdi/flexicore-boot-starter-data-jpa)
