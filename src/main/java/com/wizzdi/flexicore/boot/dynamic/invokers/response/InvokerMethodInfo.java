package com.wizzdi.flexicore.boot.dynamic.invokers.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wizzdi.flexicore.boot.dynamic.invokers.annotations.FieldInfo;
import com.wizzdi.flexicore.boot.dynamic.invokers.annotations.IdRefFieldInfo;
import com.wizzdi.flexicore.boot.dynamic.invokers.annotations.ListFieldInfo;
import com.flexicore.model.Baseclass;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    public InvokerMethodInfo() {
    }

    public InvokerMethodInfo(Method method, com.wizzdi.flexicore.boot.dynamic.invokers.annotations.InvokerMethodInfo invokerMethodInfo) {

        name=method.getName();
        id= Baseclass.generateUUIDFromString(method.toString());
        displayName=invokerMethodInfo!=null&&!invokerMethodInfo.displayName().isEmpty()?invokerMethodInfo.displayName():name;
        description=invokerMethodInfo!=null&&!invokerMethodInfo.description().isEmpty()?invokerMethodInfo.description():"No Description";
        returnTypeClass= method.getReturnType();
        this.categories=invokerMethodInfo!=null? Stream.of(invokerMethodInfo.categories()).collect(Collectors.toSet()) : Collections.emptySet();
        this.relatedMethodNames=invokerMethodInfo!=null? Stream.of(invokerMethodInfo.relatedMethodNames()).collect(Collectors.toSet()) : Collections.emptySet();

        Parameter[] parameters=method.getParameters();
        if(parameters.length>0){
            Parameter parameter=parameters[0];
            parameterHolderType=parameter.getType().getCanonicalName();
            for (Field field : ParameterInfo.getAllFields(parameter.getType())) {
                IdRefFieldInfo idRefFieldInfo = field.getDeclaredAnnotation(IdRefFieldInfo.class);
                if(idRefFieldInfo !=null){
                    this.parameters.add(new ParameterInfo(field, idRefFieldInfo));
                }
                else{
                    FieldInfo fieldInfo = field.getDeclaredAnnotation(FieldInfo.class);
                    if(fieldInfo !=null){
                        this.parameters.add(new ParameterInfo(field, fieldInfo));
                    }
                    else{
                        ListFieldInfo listFieldInfo = field.getDeclaredAnnotation(ListFieldInfo.class);
                        if(listFieldInfo !=null){
                            this.parameters.add(new ParameterInfo(field, listFieldInfo));
                        }

                    }
                }

            }
        }

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
