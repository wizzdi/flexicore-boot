package com.wizzdi.flexicore.boot.dynamic.invokers.response;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.flexicore.model.Baseclass;
import com.wizzdi.flexicore.boot.dynamic.invokers.annotations.VirtualField;
import com.wizzdi.flexicore.security.service.ClazzService;
import com.wizzdi.flexicore.security.service.SecurityOperationService;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@JsonIdentityInfo(generator = ObjectIdGenerators.UUIDGenerator.class, property = "json-id")
public class InvokerMethodInfo {


    private String name;
    private String id;
    private String description;
    private String displayName;
    private Set<String> categories;
    private Set<String> relatedMethodNames;
    @JsonIgnore
    private Class<?> returnTypeClass;

    private String parameterHolderType;
    private List<ParameterInfo> parameters=new ArrayList<>();
    private List<VirtualProperty> returnTypeVirtualProperties = new ArrayList<>();
    private List<Constructor> constructors=new ArrayList<>();

    public InvokerMethodInfo() {
    }

    public InvokerMethodInfo(Method method, com.wizzdi.flexicore.boot.dynamic.invokers.annotations.InvokerMethodInfo invokerMethodInfo) {

        name=method.getName();
        id= SecurityOperationService.generateUUIDFromStringCompt(method.toString());
        displayName=invokerMethodInfo!=null&&!invokerMethodInfo.displayName().isEmpty()?invokerMethodInfo.displayName():name;
        description=invokerMethodInfo!=null&&!invokerMethodInfo.description().isEmpty()?invokerMethodInfo.description():"No Description";
        returnTypeClass= method.getReturnType();
        this.categories=invokerMethodInfo!=null? Stream.of(invokerMethodInfo.categories()).collect(Collectors.toSet()) : Collections.emptySet();
        this.relatedMethodNames=invokerMethodInfo!=null? Stream.of(invokerMethodInfo.relatedMethodNames()).collect(Collectors.toSet()) : Collections.emptySet();
        this.returnTypeVirtualProperties = getVirtualProperties(returnTypeClass);


    }

    private List<VirtualProperty> getVirtualProperties(Class<?> returnTypeClass) {
        if (returnTypeClass == null) {
            return Collections.emptyList();
        }
        List<VirtualProperty> virtualProperties = new ArrayList<>();
        for (Class<?> current = returnTypeClass; current != null; current = current.getSuperclass()) {
            List<VirtualProperty> virtualFields = Arrays.stream(current.getAnnotationsByType(VirtualField.class)).map(f -> toVirtualProperty(f)).toList();
            virtualProperties.addAll(virtualFields);

        }
        return virtualProperties;
    }

    public void addConstructor(Constructor constructor) {
        this.constructors.add(constructor);
    }
    private VirtualProperty toVirtualProperty(VirtualField field) {

        return new VirtualProperty(field.name(), field.list(), field.type(), field.mappedBy());
    }

    public void addParameterInfo(ParameterInfo parameterInfo){
        this.parameters.add(parameterInfo);
    }



    public String getName() {
        return name;
    }

    public InvokerMethodInfo setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public InvokerMethodInfo setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getDisplayName() {
        return displayName;
    }

    public InvokerMethodInfo setDisplayName(String displayName) {
        this.displayName = displayName;
        return this;
    }

    public String getReturnType() {
        return returnTypeClass.getCanonicalName();
    }

    @JsonIgnore
    public Class<?> getReturnTypeClass() {
        return returnTypeClass;
    }

    public List<ParameterInfo> getParameters() {
        return parameters;
    }

    public InvokerMethodInfo setParameters(List<ParameterInfo> parameters) {
        this.parameters = parameters;
        return this;
    }

    public String getParameterHolderType() {
        return parameterHolderType;
    }

    public InvokerMethodInfo setParameterHolderType(String parameterHolderType) {
        this.parameterHolderType = parameterHolderType;
        return this;
    }

    public String getId() {
        return id;
    }

    public InvokerMethodInfo setId(String id) {
        this.id = id;
        return this;
    }

    public Set<String> getCategories() {
        return categories;
    }

    public <T extends InvokerMethodInfo> T setCategories(Set<String> categories) {
        this.categories = categories;
        return (T) this;
    }

    public Set<String> getRelatedMethodNames() {
        return relatedMethodNames;
    }

    public <T extends InvokerMethodInfo> T setRelatedMethodNames(Set<String> relatedMethodNames) {
        this.relatedMethodNames = relatedMethodNames;
        return (T) this;
    }

    public List<VirtualProperty> getReturnTypeVirtualProperties() {
        return returnTypeVirtualProperties;
    }

    public <T extends InvokerMethodInfo> T setReturnTypeVirtualProperties(List<VirtualProperty> returnTypeVirtualProperties) {
        this.returnTypeVirtualProperties = returnTypeVirtualProperties;
        return (T) this;
    }

    public List<Constructor> getConstructors() {
        return constructors;
    }

    public <T extends InvokerMethodInfo> T setConstructors(List<Constructor> constructors) {
        this.constructors = constructors;
        return (T) this;
    }

    @Override
    public String toString() {
        return "InvokerMethodInfo{" +
                "name='" + name + '\'' +
                ", id='" + id + '\'' +
                ", description='" + description + '\'' +
                ", displayName='" + displayName + '\'' +
                ", categories=" + categories +
                ", relatedMethodNames=" + relatedMethodNames +
                ", returnTypeClass=" + returnTypeClass +
                ", parameterHolderType='" + parameterHolderType + '\'' +
                ", parameters=" + parameters +
                '}';
    }

}
