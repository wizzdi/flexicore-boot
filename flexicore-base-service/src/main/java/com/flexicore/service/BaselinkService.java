package com.flexicore.service;

import com.flexicore.data.jsoncontainers.PaginationResponse;
import com.flexicore.interfaces.FlexiCoreService;
import com.flexicore.model.Baseclass;
import com.flexicore.model.Baselink;
import com.flexicore.model.FilteringInformationHolder;
import com.flexicore.request.BaselinkCreate;
import com.flexicore.request.BaselinkFilter;
import com.flexicore.request.BaselinkMassCreate;
import com.flexicore.request.BaselinkUpdate;
import com.flexicore.security.SecurityContext;

import java.util.List;
import java.util.Set;

public interface BaselinkService extends FlexiCoreService {
    <T extends Baseclass> List<T> listByIds(Class<T> c, Set<String> ids, SecurityContext securityContext);

    <T extends Baseclass> T getById(String id, Class<T> c, List<String> betch, SecurityContext securityContext);

    <T extends Baseclass> T getByIdOrNull(String id, Class<T> c, List<String> batchString, SecurityContext securityContext);

    <T extends Baselink> T linkEntities(Baseclass left, Baseclass right, Class<T> clazz);

    <T extends Baselink> T linkEntitiesNoCheck(Baseclass left, Baseclass right, Class<T> clazz, SecurityContext securityContext);

    <T extends Baselink> T linkEntitiesNoCheck(Baseclass left, Baseclass right, Baseclass value, String simpleVal, Class<T> clazz, SecurityContext securityContext);

    <T extends Baselink> T linkEntities(Baseclass left, Baseclass right, Class<T> clazz, Baseclass value, String simpleVal);

    <T extends Baselink> T findBySides(Class<T> clazz, Baseclass left, Baseclass right);

    void remove(Baseclass object);

    <T extends Baseclass> T findById(String right);

    <T extends Baselink> T findBySidesAndValue(Baseclass leftside, Baseclass rightside, Baseclass value, Class<T> c);

    Baselink findBySidesAndValue(Baseclass leftside, Baseclass rightside, String simplevalue);

    <T extends Baselink> T findBySidesAndValue(Baseclass leftside, Baseclass rightside, Baseclass value, String simpleValue, Class<T> c);

    void merge(Object o);

    <T extends Baselink> T findBySides(Baseclass leftside, Baseclass rightside);

    <T extends Baselink> List<T> findAllBySides(Class<T> type, Baseclass left, Baseclass right,
                                                SecurityContext securityContext);

    <T extends Baselink> List<T> findAllBySidesAndValue(Class<T> type, Baseclass left, Baseclass right, Baseclass value, String simpleValue, FilteringInformationHolder filter,
                                                        int pagesize, int current, SecurityContext securityContext);

    <T extends Baselink> List<Baseclass> getAllValues(Class<T> type, Baseclass left, Baseclass right, Baseclass value, String simpleValue, FilteringInformationHolder filter,
                                                      int pagesize, int current, SecurityContext securityContext);

    <T extends Baselink> List<T> findAllBySide(Class<T> linkType, Baseclass base, boolean right, SecurityContext securityContext);

    void flush();

    void refrehEntityManager();

    void validate(BaselinkMassCreate createBaselinkRequest, SecurityContext securityContext);

    void validate(BaselinkFilter createBaselinkRequest, SecurityContext securityContext);

    List<Baselink> massCreateBaselink(BaselinkMassCreate createBaselinkRequest, SecurityContext securityContext);

    <T extends Baselink> T createBaselink(BaselinkCreate baselinkCreate, SecurityContext securityContext);

    Baselink updateBaselink(BaselinkUpdate baselinkUpdate, SecurityContext securityContext);

    <T extends Baselink> T createBaselinkNoMerge(BaselinkCreate baselinkCreate, SecurityContext securityContext);

    PaginationResponse<Baselink> getAllBaselinks(BaselinkFilter baselinkFilter, SecurityContext securityContext);

    <T extends Baselink> List<T> listAllBaselinks(BaselinkFilter baselinkFilter, SecurityContext securityContext);

    void validate(BaselinkCreate baselinkCreate, SecurityContext securityContext);
}
