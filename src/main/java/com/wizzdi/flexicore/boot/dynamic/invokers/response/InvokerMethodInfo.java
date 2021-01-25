package com.wizzdi.flexicore.boot.dynamic.invokers.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wizzdi.flexicore.boot.dynamic.invokers.annotations.FieldInfo;
import com.wizzdi.flexicore.boot.dynamic.invokers.annotations.IdRefFieldInfo;
import com.wizzdi.flexicore.boot.dynamic.invokers.annotations.ListFieldInfo;
import com.flexicore.model.Baseclass;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
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
            List<Field> allFields = ParameterInfo.getAllFields(parameter.getType());
            for (Field field : allFields) {
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
                        else{

                            this.parameters.add(detectAutomatically(field,allFields,parameter.getType().getDeclaredFields()));
                        }

                    }
                }

            }
        }

    }

    private ParameterInfo detectAutomatically(Field field, List<Field> fields, Field[] declaredFields) {
        String fieldName = field.getName();
        if(fieldName.toLowerCase().endsWith("id")){
            String refName= fieldName.substring(0,fieldName.length()-2);
            Optional<IdRefFieldInfo> related = fields.stream().filter(f -> f.getName().equals(refName)).findFirst().map(f->getIdRefInfo(field,f.getType(),false)).or(()->fromDeclared(field,declaredFields).map(f->getIdRefInfo(field,f.getType(),true)));

            if(related.isPresent()){
                IdRefFieldInfo idRefFieldInfo=related.get();
                return new ParameterInfo(field,idRefFieldInfo);
            }
        }
        FieldInfo fieldInfo=getFieldInfo(field);
        return new ParameterInfo(field,fieldInfo);

    }

    private FieldInfo getFieldInfo(Field field) {
        return new FieldInfo(){
            @Override
            public String displayName() {
                return field.getName();
            }

            @Override
            public String description() {
                return field.getName();
            }

            @Override
            public boolean mandatory() {
                return false;
            }

            @Override
            public String defaultValue() {
                return "";
            }

            @Override
            public boolean enableRegexValidation() {
                return false;
            }

            @Override
            public String regexValidation() {
                return "";
            }

            @Override
            public boolean rangeEnabled() {
                return false;
            }

            @Override
            public double rangeMin() {
                return Double.NEGATIVE_INFINITY;
            }

            @Override
            public double rangeMax() {
                return Double.POSITIVE_INFINITY;
            }

            @Override
            public double valueSteps() {
                return Double.MIN_VALUE;
            }

            @Override
            public Class<? extends Annotation> annotationType() {
                return FieldInfo.class;
            }
        };
    }

    private Optional<? extends Field> fromDeclared(Field field,Field[] declaredFields) {
        if(declaredFields.length==2){
            return Arrays.stream(declaredFields).filter(f->!f.equals(field)).findFirst();
        }
        return Optional.empty();
    }

    private IdRefFieldInfo getIdRefInfo(Field field,Class<?> type,boolean action) {
        return new IdRefFieldInfo(){
            @Override
            public String displayName() {
                return field.getName();
            }

            @Override
            public String description() {
                return field.getName();
            }

            @Override
            public boolean mandatory() {
                return false;
            }

            @Override
            public Class<?> refType() {
                return type;
            }

            @Override
            public boolean list() {
                return false;
            }

            @Override
            public boolean actionId() {
                return action;
            }

            @Override
            public Class<? extends Annotation> annotationType() {
                return IdRefFieldInfo.class;
            }
        };
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
