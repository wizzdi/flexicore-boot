/*******************************************************************************
 *  Copyright (C) FlexiCore, Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Avishay Ben Natan And Asaf Ben Natan, October 2015
 ******************************************************************************/
package com.flexicore.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.data.jsoncontainers.SortingOrder;
import com.flexicore.interfaces.dynamic.FieldInfo;
import com.flexicore.interfaces.dynamic.IdRefFieldInfo;
import com.flexicore.interfaces.dynamic.ListFieldInfo;
import com.flexicore.model.dynamic.ExecutionParametersHolder;
import com.wizzdi.dynamic.annotations.service.TransformAnnotations;
import com.wizzdi.dynamic.properties.converter.DynamicColumnDefinition;
import com.wizzdi.dynamic.properties.converter.JsonConverter;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A JSON container to send:
 * List of Types
 *
 * @author Asaf
 */
@Entity
@Schema(description = "This class is used to filter a list. There are many extenders of this class adding information pertinent to some class, for example the EquipmentShort class has an extender that includes additional information such as location area on the map, street etc.")
@TransformAnnotations
public class FilteringInformationHolder extends ExecutionParametersHolder  {


    /**
     *
     */
    private static final long serialVersionUID = 839603578576323956L;
    @OneToMany(targetEntity = SortParameter.class, mappedBy = "filteringInformationHolder")
    @ListFieldInfo(displayName = "sort", description = "list of sorting options", listType = SortParameter.class)
    private List<SortParameter> sort = new ArrayList<>();
    @FieldInfo(displayName = "nameLike", description = "string used to search with like on name field")
    private String nameLike;
    @FieldInfo(displayName = "fullTextLike", description = "string used to search full text")
    private String fullTextLike;
    @FieldInfo(displayName = "likeCaseSensitive", description = "if nameLike is used , using case sensetive or not")
    private boolean likeCaseSensitive;
    @FieldInfo(displayName = "fromDate", description = "results from date")
    @Column(columnDefinition = "timestamp with time zone")
    private OffsetDateTime fromDate;
    @FieldInfo(displayName = "toDate", description = "results to date")
    @Column(columnDefinition = "timestamp with time zone")
    private OffsetDateTime toDate;
    @FieldInfo(displayName = "pageSize", description = "max results returned")

    private Integer pageSize;
    @FieldInfo(displayName = "currentPage", description = "page to return")

    private Integer currentPage;
    @FieldInfo(displayName = "resultType", description = "result type")

    private String resultType;

    @OneToMany(targetEntity = BaseclassNotIdFiltering.class, mappedBy = "filteringInformationHolder")
    @FieldInfo(displayName = "excludingIds", description = "excluding specific ids")
    private List<BaseclassNotIdFiltering> excludingIds = new ArrayList<>();

    @OneToMany(targetEntity = BaseclassOnlyIdFiltering.class, mappedBy = "filteringInformationHolder")
    @FieldInfo(displayName = "onlyIds", description = "specific ids",actionIdHolder = true)
    private List<BaseclassOnlyIdFiltering> onlyIds = new ArrayList<>();

    @OneToMany(targetEntity = TenantIdFiltering.class, mappedBy = "filteringInformationHolder")
    @IdRefFieldInfo(displayName = "tenants", description = "tenants to filter by", refType = Tenant.class)
    private List<TenantIdFiltering> tenantIds = new ArrayList<>();

    @OneToMany(targetEntity = ClazzIdFiltering.class, mappedBy = "filteringInformationHolder")
    @IdRefFieldInfo(displayName = "classes", description = "classes to filter by", refType = Clazz.class)
    private List<ClazzIdFiltering> clazzIds = new ArrayList<>();


    @FieldInfo(displayName = "fetchSoftDelete", description = "fetch soft delete")
    private boolean fetchSoftDelete;

    @FieldInfo(displayName = "permissionContextLike", description = "Search Permission Context")
    private String permissionContextLike;

    @Convert(converter = JsonConverter.class)
    @JsonIgnore
    private Map<String, Object> genericPredicates=new HashMap<>();

    @DynamicColumnDefinition
    @Convert(converter = JsonConverter.class)
    @JsonIgnore
    public Map<String, Object> getGenericPredicates() {
        return genericPredicates;
    }

    public <T extends FilteringInformationHolder> T setGenericPredicates(Map<String, Object> genericPredicates) {
        this.genericPredicates = genericPredicates;
        return (T) this;
    }

    @JsonAnyGetter
    public Map<String, Object> any() {
        return genericPredicates;
    }

    @JsonIgnore
    public boolean supportingDynamic() {
        return false;
    }

    @JsonAnySetter
    public void set(final String name, final Object value) {
        genericPredicates.put(name, value);
    }


    public FilteringInformationHolder() {
        super();
    }

    public FilteringInformationHolder(FilteringInformationHolder other) {
        this.sort = other.sort;
        this.nameLike = other.nameLike;
        this.fullTextLike = other.fullTextLike;
        this.likeCaseSensitive = other.likeCaseSensitive;
        this.fromDate = other.fromDate;
        this.toDate = other.toDate;
        this.pageSize = other.pageSize;
        this.currentPage = other.currentPage;
        this.resultType = other.resultType;
        this.excludingIds = other.excludingIds;
        this.onlyIds = other.onlyIds;
        this.tenantIds = other.tenantIds;
        this.clazzIds = other.clazzIds;
        this.fetchSoftDelete = other.fetchSoftDelete;
        this.permissionContextLike = other.permissionContextLike;
        this.genericPredicates=other.genericPredicates;
    }

    public void prepareForSave() {
        super.prepareForSave();
        if (sort != null) {
            for (SortParameter sortParameter : sort) {
                sortParameter.prepareForSave(this);
            }
        }
        if (tenantIds != null) {
            for (TenantIdFiltering tenantId : tenantIds) {
                tenantId.prepareForSave(this);
            }
        }
        if (clazzIds != null) {
            for (ClazzIdFiltering clazzIdFiltering : clazzIds) {
                clazzIdFiltering.prepareForSave(this);
            }
        }
        if (onlyIds != null) {
            for (BaseclassOnlyIdFiltering baseclassOnlyIdFiltering : onlyIds) {
                baseclassOnlyIdFiltering.prepareForSave(this);
            }
        }
        if (excludingIds != null) {
            for (BaseclassNotIdFiltering baseclassNotIdFiltering : excludingIds) {
                baseclassNotIdFiltering.prepareForSave(this);
            }
        }

    }

    public FilteringInformationHolder(List<SortParameter> sort) {
        this();
        this.sort = sort;
        if (this.sort == null) {
            this.sort = new ArrayList<>();
            this.sort.add(new SortParameter("name", SortingOrder.ASCENDING));
        }
    }



    @Schema(description = "provides sorting information for the returned collection" +
            "Add more sort parameters if you need to sort by more than 1 field.  " +
            "The first sort parameter has highest sort priority and so on")
    @OneToMany(targetEntity = SortParameter.class, mappedBy = "filteringInformationHolder")
    public List<SortParameter> getSort() {
        return sort;
    }

    @Schema(description = "Provide soring information for the returned collection, see sort parameter")

    public void setSort(List<SortParameter> sort) {
        this.sort = sort;
    }



    @Schema(description = "provide filtering on name, for example: %myname% will retrieve all instances having myname anywhere inside their name ", example = "%John Smith%")

    public String getNameLike() {

        return nameLike;
    }

    public void setNameLike(String nameLike) {
        this.nameLike = nameLike;
    }

    @Schema(description = "if true, like strings are case sensitive.")
    public boolean isLikeCaseSensitive() {
        return likeCaseSensitive;
    }

    ////@ApiModelProperty(value = "Set case sensitivity for name like")
    public void setLikeCaseSensitive(boolean likeCaseSensitive) {
        this.likeCaseSensitive = likeCaseSensitive;
    }

    ////@ApiModelProperty(value = "provide from time filtering, provide epoch time in ISODate Format expecting UTC. leave empty for any instance not limited by ToDate field ")
    public OffsetDateTime getFromDate() {
        return fromDate;
    }

    @Schema(description = "The creation date before which no instances will be retrieved.")
    public void setFromDate(OffsetDateTime fromDate) {
        this.fromDate = fromDate;
    }

    ////@ApiModelProperty(value = "provide to time filtering, provide epoch time in ISODate Format expecting UTC. leave empty for any instance not limited by FromDate field ")
    @Schema(description = "The creation date after which no instances will be retrieved.")
    public OffsetDateTime getToDate() {
        return toDate;
    }

    public void setToDate(OffsetDateTime toDate) {
        this.toDate = toDate;
    }

    ////@ApiModelProperty(value = "define the page size for paged data, set to -1 or dont specify if all records are needed at once, paging by this property is the most efficient method as paging is carried out by the database engine.")
    public Integer getPageSize() {
        return pageSize;
    }


    @Schema(description = "The current page to retrieve, see pagesize, the first page is 0, if -1 is used, all data is returned, this is not recommended.")
    public Integer getCurrentPage() {
        return currentPage;
    }

    public <T extends FilteringInformationHolder> T setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
        return (T) this;
    }

    public <T extends FilteringInformationHolder> T setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
        return (T) this;
    }


    public String getResultType() {
        return resultType;
    }

    public FilteringInformationHolder setResultType(String resultType) {
        this.resultType = resultType;
        return this;
    }

    @OneToMany(targetEntity = TenantIdFiltering.class, mappedBy = "filteringInformationHolder")
    @IdRefFieldInfo(displayName = "tenants", description = "tenants to filter by", refType = Tenant.class)
    @Schema(description = "List of ids of tenants , these must be accessible by the current user.")
    public List<TenantIdFiltering> getTenantIds() {
        return tenantIds;
    }

    public <T extends FilteringInformationHolder> T setTenantIds(List<TenantIdFiltering> tenantIds) {
        this.tenantIds = tenantIds;
        return (T) this;
    }

    @OneToMany(targetEntity = ClazzIdFiltering.class, mappedBy = "filteringInformationHolder")
    @IdRefFieldInfo(displayName = "classes", description = "classes to filter by", refType = Clazz.class)
    @Schema(description = "List of ids of clazz , these must be accessible by the current user.")

    public List<ClazzIdFiltering> getClazzIds() {
        return clazzIds;
    }

    public <T extends FilteringInformationHolder> T setClazzIds(List<ClazzIdFiltering> clazzIds) {
        this.clazzIds = clazzIds;
        return (T) this;
    }

    @Schema(description = "fetch soft delete objects as well as non soft deleted objects")

    public boolean isFetchSoftDelete() {
        return fetchSoftDelete;
    }

    public <T extends FilteringInformationHolder> T setFetchSoftDelete(boolean fetchSoftDelete) {
        this.fetchSoftDelete = fetchSoftDelete;
        return (T) this;
    }

    @Schema(description = "field to search free text ")
    public String getFullTextLike() {
        return fullTextLike;
    }

    public <T extends FilteringInformationHolder> T setFullTextLike(String fullTextLike) {
        this.fullTextLike = fullTextLike;
        return (T) this;
    }

    @FieldInfo(displayName = "permissionContextLike", description = "Search Permission Context")
    @Schema(description = "Search Permission Context")
    public String getPermissionContextLike() {
        return permissionContextLike;
    }

    public <T extends FilteringInformationHolder> T setPermissionContextLike(String permissionContextLike) {
        this.permissionContextLike = permissionContextLike;
        return (T) this;
    }


    @OneToMany(targetEntity = BaseclassNotIdFiltering.class, mappedBy = "filteringInformationHolder")
    public List<BaseclassNotIdFiltering> getExcludingIds() {
        return excludingIds;
    }

    public <T extends FilteringInformationHolder> T setExcludingIds(List<BaseclassNotIdFiltering> excludingIds) {
        this.excludingIds = excludingIds;
        return (T) this;
    }

    @OneToMany(targetEntity = BaseclassOnlyIdFiltering.class, mappedBy = "filteringInformationHolder")
    public List<BaseclassOnlyIdFiltering> getOnlyIds() {
        return onlyIds;
    }

    public <T extends FilteringInformationHolder> T setOnlyIds(List<BaseclassOnlyIdFiltering> onlyIds) {
        this.onlyIds = onlyIds;
        return (T) this;
    }
}
