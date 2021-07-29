package com.flexicore.service.impl;

import com.flexicore.annotations.Baseclassroot;
import com.flexicore.data.BaseclassRepository;
import com.flexicore.model.Baseclass;
import com.flexicore.model.FilteringInformationHolder;
import com.flexicore.model.Tenant;
import com.flexicore.request.BaseclassCreate;
import com.flexicore.request.ZipFileFilter;
import com.flexicore.security.SecurityContext;
import com.wizzdi.flexicore.security.request.BasicPropertiesFilter;
import com.wizzdi.flexicore.security.request.DateFilter;
import com.wizzdi.flexicore.security.request.PaginationFilter;
import com.wizzdi.flexicore.security.request.SoftDeleteOption;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.BadRequestException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Primary
@Component
@Extension
public class BaseclassNewService implements com.flexicore.service.BaseclassNewService {
    @Autowired
    @Baseclassroot
    private BaseclassRepository baseclassRepository;

    public static BasicPropertiesFilter getCompatible(FilteringInformationHolder zipFileFilter) {
        return new BasicPropertiesFilter()
                .setNameLike(zipFileFilter.getNameLike())
                .setSoftDelete(zipFileFilter.isFetchSoftDelete()? SoftDeleteOption.BOTH:SoftDeleteOption.NON_DELETED_ONLY)
                .setCreationDateFilter(new DateFilter().setStart(zipFileFilter.getFromDate()).setEnd(zipFileFilter.getToDate()));
    }

    @Override
    public void populate(BaseclassCreate baseclassCreate, SecurityContext securityContext) {
        String tenantId = baseclassCreate.getTenantId();
        Tenant tenant = tenantId != null ? baseclassRepository.getByIdOrNull(tenantId, Tenant.class, null, securityContext) : null;
        baseclassCreate.setTenant(tenant);

    }

    public static PaginationFilter getCompatiblePagination(FilteringInformationHolder filteringInformationHolder){
        return new PaginationFilter().setCurrentPage(filteringInformationHolder.getCurrentPage()).setPageSize(filteringInformationHolder.getPageSize());
    }

    @Override
    public void validate(BaseclassCreate baseclassCreate, SecurityContext securityContext) {
        populate(baseclassCreate, securityContext);
        if (baseclassCreate.getTenant() == null && baseclassCreate.getTenantId() != null) {
            throw new BadRequestException("No Tenant with id " + baseclassCreate.getTenantId());
        }

    }


    /**
     * keeping compatibility with code gen
     *
     * @param filteringInformationHolder filteringinformationholder
     * @param securityContext            security context
     */
    @Override
    public void validateFilter(FilteringInformationHolder filteringInformationHolder, SecurityContext securityContext) {

    }

    /**
     * keeping compatibility with code gen
     *
     * @param baseclassCreate baseclass create
     * @param securityContext security context
     */
    @Override
    public void validateCreate(BaseclassCreate baseclassCreate, SecurityContext securityContext) {
        validate(baseclassCreate, securityContext);
    }


    @Override
    public boolean updateBaseclassNoMerge(BaseclassCreate baseclassCreate, Baseclass baseclass) {
        boolean update = false;
        if (baseclassCreate.getName() != null && !baseclassCreate.getName().equals(baseclass.getName())) {
            baseclass.setName(baseclassCreate.getName());
            update = true;
        }
        if (baseclassCreate.getSoftDelete() != null && !baseclassCreate.getSoftDelete().equals(baseclass.isSoftDelete())) {
            baseclass.setSoftDelete(baseclassCreate.getSoftDelete());
            update = true;
        }
        if (baseclassCreate.getDescription() != null && !baseclassCreate.getDescription().equals(baseclass.getDescription())) {
            baseclass.setDescription(baseclassCreate.getDescription());
            update = true;
        }
        if (baseclassCreate.getTenant() != null && (baseclass.getTenant() == null || !baseclassCreate.getTenant().getId().equals(baseclass.getTenant().getId()))) {
            baseclass.setTenant(baseclassCreate.getTenant());
            update = true;
        }
        return update;

    }

    /**
     * returns a set of merged values from two maps if there was an update otherwise null
     * @param newVals incoming values
     * @param current existing values
     * @return merged map or null
     */
    public Map<String, Object> updateDynamic(Map<String, Object> newVals, Map<String, Object> current) {
        boolean update = false;
        if (newVals != null && !newVals.isEmpty()) {
            if (current == null) {
                return newVals;
            } else {
                Map<String, Object> copy = new HashMap<>(newVals);
                for (Map.Entry<String, Object> entry : newVals.entrySet()) {
                    String key = entry.getKey();
                    Object newVal = entry.getValue();
                    Object val = current.get(key);
                    if (newVal != null && !newVal.equals(val)) {
                        copy.put(key, newVal);
                        update = true;
                    }
                }
                if (update) {
                    return copy;
                }
            }


        }
        return null;
    }

    @Override
    public <T extends Baseclass> T getByIdOrNull(String id, Class<T> c, List<String> batchString, SecurityContext securityContext) {
        return baseclassRepository.getByIdOrNull(id, c, batchString, securityContext);
    }

    @Override
    public <T extends Baseclass> List<T> listByIds(Class<T> aClass, Set<String> set, SecurityContext securityContext) {
        return baseclassRepository.listByIds(aClass, set, securityContext);
    }

    @Override
    @Transactional
    public void massMerge(List<?> toMerge) {
        baseclassRepository.massMerge(toMerge);
    }
}
