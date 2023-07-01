package com.flexicore.data;

import com.flexicore.interfaces.ServicePlugin;
import org.pf4j.Extension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Index;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Extension
public class IndexRepository implements ServicePlugin {

    private static final Logger logger= LoggerFactory.getLogger(IndexRepository.class);
    @PersistenceContext
    private EntityManager em;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void createIndex(Index index, String tableName, boolean singleTable) {

        String columnsList = singleTable?index.columnList():Arrays.stream(index.columnList().split(",")).filter(f->!"dtype".equalsIgnoreCase(f)).collect(Collectors.joining(","));
        if(columnsList.isEmpty()){
            return;
        }
        try {
            Query existing = em.createNativeQuery("select indexname from pg_indexes where tablename=? and indexname=?");
            existing.setParameter(1,tableName);
            existing.setParameter(2,index.name());
            List<?> results=existing.getResultList();
            if(results.isEmpty()){
                Query query = em.createNativeQuery("create " + (index.unique() ? "unique " : "") + "index " + index.name() + " on " + tableName + "(" + columnsList + ")");
                query.executeUpdate();
                logger.info("created index " + (index.unique() ? "unique " : "") + index.name() + " on table " + tableName + "(" + columnsList + ")");
            }
            else{
                logger.debug("index " + (index.unique() ? "unique " : "") + index.name() + " on table " + tableName + "(" + columnsList + ") , already exists");
            }

        } catch (Exception e) {
            if (e.getMessage().toLowerCase().contains("already exists")) {
                logger.debug("index " + (index.unique() ? "unique " : "") + index.name() + " on table " + tableName + "(" + columnsList + ") , already exists");

            } else {
                logger.error("unable to create index", e);

            }
        }
    }
}
