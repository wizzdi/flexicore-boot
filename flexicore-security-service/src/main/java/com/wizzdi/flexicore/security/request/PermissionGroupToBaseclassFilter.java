package com.wizzdi.flexicore.security.request;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.model.Baseclass;
import com.flexicore.model.Clazz;
import com.flexicore.model.PermissionGroup;
import com.wizzdi.flexicore.security.validation.ClazzValid;
import com.wizzdi.flexicore.security.validation.IdValid;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@IdValid.List({
        @IdValid(targetField = "permissionGroups", fieldType = PermissionGroup.class, field = "permissionGroupIds"),
})
public class PermissionGroupToBaseclassFilter extends PaginationFilter {

    private BasicPropertiesFilter basicPropertiesFilter;


    @JsonAlias("baseclassIds")
    private Set<String> securedIds =new HashSet<>();
    @JsonIgnore
    private List<PermissionGroup> permissionGroups;

    @ClazzValid
    @JsonIgnore
    private List<Clazz> clazzes;

    private Set<String> permissionGroupIds = new HashSet<>();

    private Sorting sorting;


    public BasicPropertiesFilter getBasicPropertiesFilter() {
        return basicPropertiesFilter;
    }

    public <T extends PermissionGroupToBaseclassFilter> T setBasicPropertiesFilter(BasicPropertiesFilter basicPropertiesFilter) {
        this.basicPropertiesFilter = basicPropertiesFilter;
        return (T) this;
    }

    @JsonIgnore
    public List<PermissionGroup> getPermissionGroups() {
        return permissionGroups;
    }

    public <T extends PermissionGroupToBaseclassFilter> T setPermissionGroups(List<PermissionGroup> permissionGroups) {
        this.permissionGroups = permissionGroups;
        return (T) this;
    }

    public Set<String> getPermissionGroupIds() {
        return permissionGroupIds;
    }

    public <T extends PermissionGroupToBaseclassFilter> T setPermissionGroupIds(Set<String> permissionGroupIds) {
        this.permissionGroupIds = permissionGroupIds;
        return (T) this;
    }



    public Set<String> getSecuredIds() {
        return securedIds;
    }

    public <T extends PermissionGroupToBaseclassFilter> T setSecuredIds(Set<String> securedIds) {
        this.securedIds = securedIds;
        return (T) this;
    }

    @JsonIgnore
    public List<Clazz> getClazzes() {
        return clazzes;
    }

    public <T extends PermissionGroupToBaseclassFilter> T setClazzes(List<Clazz> clazzes) {
        this.clazzes = clazzes;
        return (T) this;
    }

    public Sorting getSorting() {
        return sorting;
    }

    public <T extends PermissionGroupToBaseclassFilter> T setSorting(Sorting sorting) {
        this.sorting = sorting;
        return (T) this;
    }

    public record Sorting(SortBy sortBy, boolean asc){}
    public enum SortBy{
        BASECLASS_ID,CLAZZ_NAME,BASECLASS_NAME,BASECLASS_CREATION_DATE
    }
}
