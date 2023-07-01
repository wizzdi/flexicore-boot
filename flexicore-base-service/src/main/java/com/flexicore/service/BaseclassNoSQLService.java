package com.flexicore.service;

import com.flexicore.interfaces.FlexiCoreService;
import com.flexicore.model.nosql.BaseclassNoSQL;
import com.flexicore.request.BaseclassNoSQLCreate;
import com.flexicore.request.BaseclassNoSQLUpdate;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface BaseclassNoSQLService extends FlexiCoreService {
    <T extends BaseclassNoSQL> T getByIdOrNull(Class<T> c, String id);

    <T extends BaseclassNoSQL> List<T> listByIds(Class<T> c, Set<String> ids);

    <T extends BaseclassNoSQL> T getByIdOrNull(Class<T> c, String collectionName, String id);

    <T extends BaseclassNoSQL> List<T> listByIds(Class<T> c, String collectionName, Set<String> ids);

    void mergeBaseclassNoSQLByCollection(Map<String, String> noSQLNodesCollections, List<? extends BaseclassNoSQL> o);

    <T extends BaseclassNoSQL> List<T> getBaseclassNoSQLByCollection(Map<String, String> noSQLNodesCollections, Class<T> c, Set<String> noSQLIds);

    /**
     * creates a baseclassNoSQL
     *
     * @param baseclassNoSQLCreate object used to create the baseclassNoSQL
     * @return created baseclassNoSQL
     */
    BaseclassNoSQL createBaseclassNoSQL(BaseclassNoSQLCreate baseclassNoSQLCreate);

    /**
     * creates a baseclassNoSQL
     *
     * @param baseclassNoSQLCreate object used to create the baseclassNoSQL
     * @return created baseclassNoSQL
     */
    BaseclassNoSQL createBaseclassNoSQLNoMerge(BaseclassNoSQLCreate baseclassNoSQLCreate);

    /**
     * updtes a baseclassNoSQL
     *
     * @param baseclassNoSQLUpdate object used to update the baseclassNoSQL
     * @return updated baseclassNoSQL
     */
    BaseclassNoSQL updateBaseclassNoSQL(BaseclassNoSQLUpdate baseclassNoSQLUpdate);

    boolean updateBaseclassNoSQLNoMerge(BaseclassNoSQL baseclassNoSQL, BaseclassNoSQLCreate create);

    void mergeBaseclassNoSQL(BaseclassNoSQL o);

    void massMergeBaseclassNoSQL(List<? extends BaseclassNoSQL> o);

    void mergeBaseclassNoSQL(BaseclassNoSQL o, String collectionName);

    void massMergeBaseclassNoSQL(List<? extends BaseclassNoSQL> o, String collectionName);
}
