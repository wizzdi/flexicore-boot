package com.flexicore.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.interfaces.dynamic.FieldInfo;
import com.flexicore.interfaces.dynamic.IdRefFieldInfo;
import com.flexicore.interfaces.dynamic.ListFieldInfo;
import com.flexicore.model.AccessRef;
import com.flexicore.model.DynamicInvokerRef;
import com.flexicore.model.FilteringInformationHolder;
import com.flexicore.model.dynamic.DynamicInvoker;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Transient;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class OperationFiltering extends FilteringInformationHolder {

    @FieldInfo
    private Boolean auditable;
    @ListFieldInfo(listType = AccessRef.class)
    @OneToMany(targetEntity = AccessRef.class,mappedBy = "filteringInformationHolder")
    private Set<AccessRef> defaultaccess=new HashSet<>();

    @OneToMany(targetEntity = DynamicInvokerRef.class,mappedBy = "filteringInformationHolder")
    @IdRefFieldInfo(refType = DynamicInvoker.class)
    private Set<DynamicInvokerRef> dynamicInvokerIds=new HashSet<>();
    @JsonIgnore
    @Transient
    private List<DynamicInvoker> dynamicInvokers;

    public Boolean getAuditable() {
        return auditable;
    }

    public <T extends OperationFiltering> T setAuditable(Boolean auditable) {
        this.auditable = auditable;
        return (T) this;
    }

    @OneToMany(targetEntity = AccessRef.class,mappedBy = "filteringInformationHolder")
    public Set<AccessRef> getDefaultaccess() {
        return defaultaccess;
    }

    public <T extends OperationFiltering> T setDefaultaccess(Set<AccessRef> defaultaccess) {
        this.defaultaccess = defaultaccess;
        return (T) this;
    }

    @OneToMany(targetEntity = DynamicInvokerRef.class,mappedBy = "filteringInformationHolder")
    public Set<DynamicInvokerRef> getDynamicInvokerIds() {
        return dynamicInvokerIds;
    }

    public <T extends OperationFiltering> T setDynamicInvokerIds(Set<DynamicInvokerRef> dynamicInvokerIds) {
        this.dynamicInvokerIds = dynamicInvokerIds;
        return (T) this;
    }

    @JsonIgnore
    @Transient
    public List<DynamicInvoker> getDynamicInvokers() {
        return dynamicInvokers;
    }

    public <T extends OperationFiltering> T setDynamicInvokers(List<DynamicInvoker> dynamicInvokers) {
        this.dynamicInvokers = dynamicInvokers;
        return (T) this;
    }

    @Override
    public void prepareForSave() {
        super.prepareForSave();
        for (AccessRef accessRef : defaultaccess) {
            accessRef.prepareForSave(this);
        }
        for (DynamicInvokerRef dynamicInvokerId : dynamicInvokerIds) {
            dynamicInvokerId.prepareForSave(this);
        }
    }
}
