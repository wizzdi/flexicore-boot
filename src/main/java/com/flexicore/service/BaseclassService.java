package com.flexicore.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.flexicore.annotations.AnnotatedClazz;
import com.flexicore.data.jsoncontainers.CrossLoaderResolver;
import com.flexicore.data.jsoncontainers.PaginationResponse;
import com.flexicore.data.jsoncontainers.SetBaseclassTenantRequest;
import com.flexicore.interfaces.FlexiCoreService;
import com.flexicore.model.*;
import com.flexicore.request.*;
import com.flexicore.response.BaseclassCount;
import com.flexicore.response.ClassInfo;
import com.flexicore.security.SecurityContext;
import com.flexicore.utils.InheritanceUtils;
import com.wizzdi.flexicore.boot.dynamic.invokers.response.ParameterInfo;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public interface BaseclassService extends FlexiCoreService {
    static Map<String, Set<ClassInfo>> filterClass = new ConcurrentHashMap<>();

    /**
     * registers filter class , for example (registerFilterClass(Category.class,CategoryFilter.class))
     * @param filter the filter class
     * @param handling the class the filter filters
     */
    static void registerFilterClass(Class<?> filter, Class<?> handling) {
        Set<ClassInfo> filters = filterClass.computeIfAbsent(handling.getCanonicalName(), f -> new HashSet<>());
        AnnotatedClazz annotatedClazz = filter.getDeclaredAnnotation(AnnotatedClazz.class);

        filters.add(new ClassInfo(filter, annotatedClazz));
        CrossLoaderResolver.registerClass(filter);
        CrossLoaderResolver.registerClass(handling);

    }

    /**
     * get class filters
     * @param className
     * @return
     */
    static Set<ClassInfo> getFilterClass(String className) {
        return filterClass.get(className);
    }

    static Date longToDate(long time) {
        if (time == -1) {
            return new Date();
        }
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(time);
        return c.getTime();
    }

    /**
     * returns class parent
     * @param name
     * @return
     */
    static String getSuperSimple(String name) {
        return InheritanceUtils.simpleToSimpleInhereting.get(name);
    }

    <T extends Baseclass> long count(Class<T> clazz, FilteringInformationHolder filteringInformationHolder, SecurityContext securityContext);

    void persist(Baseclass base);

    <T extends Baseclass> T find(Class<T> type, String id);

    <T extends Baseclass> T findByIdOrNull(Class<T> type, String id);

    <T extends Baseclass> List<T> listByIds(Class<T> c, Set<String> ids, SecurityContext securityContext);

    boolean remove(Baseclass base);

    <T extends Baseclass> int removeById(String id, QueryInformationHolder<T> queryInformationHolder);

    <T extends Baseclass> T getByIdOrNull(String id, Class<T> c, List<String> batchString, SecurityContext securityContext);

    <T extends Baseclass> List<T> getAllByKeyWordAndCategory(QueryInformationHolder<T> queryInformationHolder);

    <T extends Baseclass> List<T> getAllFiltered(QueryInformationHolder<T> queryInformationHolder);

    <T extends Baseclass> List<T> getAllFiltered(FilteringInformationHolder filteringInformationHolder, Class<T> c, SecurityContext securityContext);

    <T extends Baseclass> long countAllFiltered(QueryInformationHolder<T> queryInformationHolder);

    <T extends Baseclass> long countAllFiltered(FilteringInformationHolder filteringInformationHolder, Class<T> c, SecurityContext securityContext);

    <T extends Baseclass> String serializeBaseclssForExport(T baseclass, SecurityContext securityContext) throws JsonProcessingException;

    <T extends Baseclass> T deserializeBaseclassForImport(String json, Class<T> type, SecurityContext securityContext) throws IOException;

    void validate(MassDeleteRequest massDeleteRequest, SecurityContext securityContext);

    List<BaseclassCount> getBaseclassCount(BaseclassCountRequest baseclassCountRequest, SecurityContext securityContext);
    void massDelete(MassDeleteRequest massDeleteRequest, SecurityContext securityContext);

    @SuppressWarnings("unchecked")
    <T extends Baselink, E extends Baseclass> List<String> getConnectedClasses(Clazz c, String id, Clazz linkClazz,
                                                                               FilteringInformationHolder filteringInformationHolder, int pagesize, int currentPage, Baseclass value, String simpleValue, SecurityContext securityContext);

    ParameterInfo getClassInfo(GetClassInfo filteringInformationHolder);

    Object getExample(GetClassInfo filteringInformationHolder);

    <T ,E extends FilteringInformationHolder> FileResource exportBaseclassGeneric(ExportBaseclassGeneric<E> baseclassGeneric, SecurityContext securityContext);

    void validate(GetDisconnected getDisconnected, SecurityContext securityContext);

    void validate(GetConnected getConnected, SecurityContext securityContext);

    <T extends Baselink, E extends Baseclass> PaginationResponse<E> getDisconnected(GetDisconnected getDisconnected, SecurityContext securityContext);

    <T extends Baselink, E extends Baseclass> PaginationResponse<E> getConnected(GetConnected getConnected, SecurityContext securityContext);

    <T extends Baselink> Boolean onRight(Class<?> wanted, Class<T> link);

    @SuppressWarnings("unchecked")
    <T extends Baselink> List<Baseclass> getDisconnected(Clazz c, String id, Clazz linkClazz,
                                                         FilteringInformationHolder filteringInformationHolder, int pagesize, int currentPage, Baseclass value, String simpleValue, SecurityContext securityContext) throws Exception;

    @SuppressWarnings("unchecked")
    <T extends Baselink> long countDisconnected(Clazz c, String id, Clazz linkClazz,
                                                FilteringInformationHolder filteringInformationHolder, int pagesize, int currentPage, Baseclass value, String simpleValue, SecurityContext securityContext) throws Exception;

    @SuppressWarnings("unchecked")
    <T extends Baselink, E extends Baseclass> List<E> getConnected(Clazz c, String id, Clazz linkClazz,
                                                                   FilteringInformationHolder filteringInformationHolder, int pagesize, int currentPage, Baseclass value, String simpleValue, SecurityContext securityContext);

    @SuppressWarnings("unchecked")
    <T extends Baselink, E extends Baseclass> long countConnected(Clazz c, String id, Clazz linkClazz,
                                                                  FilteringInformationHolder filteringInformationHolder, int pagesize, int currentPage, Baseclass value, String simpleValue, SecurityContext securityContext);

    boolean updateInfo(String id, String name, String description, SecurityContext securityContext);

    <T extends Baseclass> List<T> getByNameLike(String name, Class<T> c, List<String> batchString,
                                                SecurityContext securityContext);

    <T extends Baseclass> List<T> getAllUnsecure(Class<T> c);

    void merge(Object base);

    void softDelete(Baseclass baseclass, SecurityContext securityContext);

    long setBaseclassTenant(SetBaseclassTenantRequest setBaseclassTenantRequest, SecurityContext securityContext);

    void refrehEntityManager();

    <T, E extends FilteringInformationHolder> PaginationResponse<T> listAllBaseclassGeneric(E filteringInformationHolder, SecurityContext securityContext);
}
