package com.flexicore.interfaces;

import com.flexicore.data.BaseclassNoSQLRepository;
import com.flexicore.model.nosql.BaseclassNoSQL;
import com.flexicore.request.BaseclassNoSQLFilter;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Set;

public abstract class AbstractNoSqlRepositoryPlugin implements PluginRepository {

    @Autowired
    private BaseclassNoSQLRepository baseclassNoSQLRepository;

    public static CodecRegistry getPojoCodecRegistry() {
        return BaseclassNoSQLRepository.getPojoCodecRegistry();
    }

    public static void addClassForMongoCodec(Class<?>... c) {
        BaseclassNoSQLRepository.addClassForMongoCodec(c);
    }

    public static void addClassForMongoCodec(Class<?> c) {
        BaseclassNoSQLRepository.addClassForMongoCodec(c);
    }

    public static Bson getBaseclassNoSQLPredicates(BaseclassNoSQLFilter baseclassNoSQLFilter) {
        return BaseclassNoSQLRepository.getBaseclassNoSQLPredicates(baseclassNoSQLFilter);
    }

    public <T extends BaseclassNoSQL> T getByIdOrNull(Class<T> c, String id) {
        return baseclassNoSQLRepository.getByIdOrNull(c, id);
    }

    public <T extends BaseclassNoSQL> List<T> listByIds(Class<T> c, Set<String> ids) {
        return baseclassNoSQLRepository.listByIds(c, ids);
    }

    public void mergeBaseclassNoSQL(BaseclassNoSQL o) {
        baseclassNoSQLRepository.mergeBaseclassNoSQL(o);
    }

    public void massMergeBaseclassNoSQL(List<? extends BaseclassNoSQL> o) {
        baseclassNoSQLRepository.massMergeBaseclassNoSQL(o);
    }

    public void refreshEntityManager() {
        baseclassNoSQLRepository.refreshEntityManager();
    }

    public void persist(Object o) {
        baseclassNoSQLRepository.persist(o);
    }

    public void merge(Object o) {
        baseclassNoSQLRepository.merge(o);
    }

    public void batchMerge(List<Object> o) {
        baseclassNoSQLRepository.batchMerge(o);
    }

    public void batchPersist(List<?> o) {
        baseclassNoSQLRepository.batchPersist(o);
    }
}
