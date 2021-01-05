package com.flexicore.init;

import com.flexicore.data.IndexRepository;
import com.flexicore.interfaces.ServicePlugin;
import com.flexicore.model.Baseclass;
import com.wizzdi.flexicore.boot.base.events.PluginsLoadedEvent;
import com.wizzdi.flexicore.boot.jpa.service.EntitiesHolder;
import org.apache.commons.lang3.tuple.Pair;
import org.pf4j.Extension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.persistence.Index;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
@Extension
public class IndexCreator implements ServicePlugin {

    private static final AtomicBoolean init=new AtomicBoolean(false);
    private static final Logger logger= LoggerFactory.getLogger(IndexCreator.class);

    @Autowired
    private IndexRepository indexRepository;

    @Autowired
    private List<EntitiesHolder> entitiesHolders;

    @Value("${flexicore.inheritence.strategy:SINGLE_TABLE}")
    private String inheritanceType;
    @Async
    @EventListener
    public void init(PluginsLoadedEvent o){
        if(init.compareAndSet(false,true)){
            for (EntitiesHolder entitiesHolder : entitiesHolders) {
                for (Class<?> entity : entitiesHolder.getEntities()) {
                    try {
                        createIndex(entity);
                    }
                    catch (Exception e){
                        logger.error("Failed creating index for "+entity.getName(),e);
                    }
                }
            }

        }

    }

    private void createIndex(Class<?> claz) {
        Table table = claz.getAnnotation(Table.class);
        String tableName;
        Pair<InheritanceType, Class<?>> pair = getInheritedTableName(claz);
        boolean singleTable=false;
        if (pair != null && (singleTable=pair.getLeft().equals(InheritanceType.SINGLE_TABLE))) {
            Class<?> parent = pair.getRight();
            Table inheritedTable = parent.getAnnotation(Table.class);
            tableName = inheritedTable == null || inheritedTable.name().isEmpty() ? parent.getSimpleName() : inheritedTable.name();
        } else {
            tableName = table == null || table.name().isEmpty() ? claz.getSimpleName() : table.name();
        }
        if (table != null) {
            for (Index index : table.indexes()) {

                try {
                    indexRepository.createIndex(index, tableName,singleTable);
                }
                catch (RuntimeException rollbackException){
                    logger.debug("failed creating index",rollbackException);
                }
            }
        }
    }

    private Pair<InheritanceType, Class<?>> getInheritedTableName(Class<?> orginal) {
        for (Class<?> current = orginal; current.getSuperclass() != null; current = current.getSuperclass()) {
            Inheritance inheritance = current.getDeclaredAnnotation(Inheritance.class);
            if (inheritance != null) {
                InheritanceType strategy =  Baseclass.class.equals(current)?InheritanceType.valueOf(inheritanceType):inheritance.strategy();
                return Pair.of(strategy, current);
            }
        }
        return null;
    }
}
