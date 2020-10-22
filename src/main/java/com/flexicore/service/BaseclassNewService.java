package com.flexicore.service;

import com.flexicore.interfaces.FlexiCoreService;
import com.flexicore.model.Baseclass;
import com.flexicore.model.FilteringInformationHolder;
import com.flexicore.request.BaseclassCreate;
import com.flexicore.security.SecurityContext;

import java.util.List;
import java.util.Set;

public interface BaseclassNewService extends FlexiCoreService {
    void populate(BaseclassCreate baseclassCreate, SecurityContext securityContext);

    void validate(BaseclassCreate baseclassCreate, SecurityContext securityContext);

    void validateFilter(FilteringInformationHolder filteringInformationHolder, SecurityContext securityContext);

    void validateCreate(BaseclassCreate baseclassCreate, SecurityContext securityContext);

    /**
     * updates baseclass
     * @param baseclassCreate object used to update the baseclass
     * @param baseclass baseclass to update
     * @return true if update, false otherwise
     */
    boolean updateBaseclassNoMerge(BaseclassCreate baseclassCreate, Baseclass baseclass);

    <T extends Baseclass> T getByIdOrNull(String id, Class<T> c, List<String> batchString, SecurityContext securityContext);

    <T extends Baseclass> List<T> listByIds(Class<T> aClass, Set<String> set, SecurityContext securityContext);
    void massMerge(List<?> toMerge);
}
