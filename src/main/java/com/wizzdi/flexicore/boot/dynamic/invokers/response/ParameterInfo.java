package com.wizzdi.flexicore.boot.dynamic.invokers.response;

import com.wizzdi.flexicore.boot.dynamic.invokers.annotations.FieldInfo;
import com.wizzdi.flexicore.boot.dynamic.invokers.annotations.IdRefFieldInfo;
import com.wizzdi.flexicore.boot.dynamic.invokers.annotations.ListFieldInfo;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.wizzdi.flexicore.boot.dynamic.invokers.utils.InvokerUtils.getAllFields;

public class ParameterInfo {


    private String name;
    private String description;
    private String displayName;
    private String type;
    private String defaultValue;
    private String regexValidation;
    private Double rangeMax;
    private Double rangeMin;
    private Double valueSteps;
    private boolean idRef;
    private boolean list;
    private boolean mandatory;
    private boolean actionId;
    private boolean actionIdHolder;
    private Class<?> idRefType;
    private List<ParameterInfo> subParameters;
    private Set<String> possibleValues;
    private Class<?> iterationType;
    private boolean ignoreSubParameters;

    public ParameterInfo() {
    }

    public ParameterInfo(Field parameter, FieldInfo fieldInfo) {
        this.name = parameter.getName();
        if (fieldInfo != null) {
            this.displayName = !fieldInfo.displayName().isEmpty() ? fieldInfo.displayName() : name;
            this.description = fieldInfo.description();
            this.type = parameter.getGenericType().getTypeName();
            this.mandatory = fieldInfo.mandatory();
            this.defaultValue = fieldInfo.defaultValue();
            this.regexValidation = fieldInfo.regexValidation();
            this.actionIdHolder = fieldInfo.actionIdHolder();
            this.ignoreSubParameters=fieldInfo.ignoreSubParameters();
            if (fieldInfo.rangeEnabled()) {
                this.rangeMin = fieldInfo.rangeMin();
                this.rangeMax = fieldInfo.rangeMax();
                this.valueSteps = fieldInfo.valueSteps();
            }

        }


        iterationType= parameter.getType();
        if (iterationType.isEnum()) {
            Class<? extends Enum> enumType = (Class<? extends Enum>) iterationType;
            EnumSet enumSet = EnumSet.allOf(enumType);
            Stream<Enum<?>> stream = enumSet.stream();
            possibleValues = stream.map(f -> f.name()).collect(Collectors.toSet());
        }


    }

    public ParameterInfo(Field parameter, ListFieldInfo fieldInfo) {
        this.name = parameter.getName();
        this.displayName = name;
        this.description = "No Description";
        this.list = true;

        if(fieldInfo!=null){
            this.displayName = !fieldInfo.displayName().isEmpty() ? fieldInfo.displayName() : name;
            this.description = !fieldInfo.description().isEmpty() ? fieldInfo.description() : "No Description";
            this.mandatory = fieldInfo.mandatory();
            iterationType = fieldInfo.listType();
            this.type = iterationType.getCanonicalName();
            this.ignoreSubParameters=fieldInfo.ignoreSubParameters();
        }




    }


    public ParameterInfo(Field parameter, IdRefFieldInfo fieldInfo) {
        this.name = parameter.getName();
        this.displayName = name;
        this.description = "No Description";
        this.list = true;
        this.type = "com.flexicore.model.BaseclassIdFiltering";
        idRef = true;
        if(fieldInfo!=null){
            this.displayName = !fieldInfo.displayName().isEmpty() ? fieldInfo.displayName() : name;
            this.description = !fieldInfo.description().isEmpty() ? fieldInfo.description() : description;
            this.list = fieldInfo.list();
            this.mandatory = fieldInfo.mandatory();
            idRefType = fieldInfo.refType();
            this.actionId = fieldInfo.actionId();

        }



    }

    public ParameterInfo(Class<?> c) {
        this(c, c.getSimpleName());
    }

    public ParameterInfo(Class<?> c, String displayName) {
        this.displayName = displayName;
        this.name = c.getCanonicalName();
        this.type = c.getCanonicalName();
        this.iterationType=c;

    }

    public String getName() {
        return name;
    }

    public ParameterInfo setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public ParameterInfo setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getDisplayName() {
        return displayName;
    }

    public ParameterInfo setDisplayName(String displayName) {
        this.displayName = displayName;
        return this;
    }

    public String getType() {
        return type;
    }

    public ParameterInfo setType(String type) {
        this.type = type;
        return this;
    }

    public boolean isIdRef() {
        return idRef;
    }

    public ParameterInfo setIdRef(boolean idRef) {
        this.idRef = idRef;
        return this;
    }

    public List<ParameterInfo> getSubParameters() {
        return subParameters;
    }

    public ParameterInfo setSubParameters(List<ParameterInfo> subParameters) {
        this.subParameters = subParameters;
        return this;
    }

    public Class<?> getIdRefType() {
        return idRefType;
    }

    public ParameterInfo setIdRefType(Class<?> idRefType) {
        this.idRefType = idRefType;
        return this;
    }


    public boolean isList() {
        return list;
    }

    public ParameterInfo setList(boolean list) {
        this.list = list;
        return this;
    }

    public boolean isMandatory() {
        return mandatory;
    }

    public ParameterInfo setMandatory(boolean mandatory) {
        this.mandatory = mandatory;
        return this;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public <T extends ParameterInfo> T setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
        return (T) this;
    }

    public String getRegexValidation() {
        return regexValidation;
    }

    public <T extends ParameterInfo> T setRegexValidation(String regexValidation) {
        this.regexValidation = regexValidation;
        return (T) this;
    }

    public Double getRangeMax() {
        return rangeMax;
    }

    public <T extends ParameterInfo> T setRangeMax(Double rangeMax) {
        this.rangeMax = rangeMax;
        return (T) this;
    }

    public Double getRangeMin() {
        return rangeMin;
    }

    public <T extends ParameterInfo> T setRangeMin(Double rangeMin) {
        this.rangeMin = rangeMin;
        return (T) this;
    }

    public Double getValueSteps() {
        return valueSteps;
    }

    public <T extends ParameterInfo> T setValueSteps(Double valueSteps) {
        this.valueSteps = valueSteps;
        return (T) this;
    }

    public boolean isActionId() {
        return actionId;
    }

    public <T extends ParameterInfo> T setActionId(boolean actionId) {
        this.actionId = actionId;
        return (T) this;
    }

    public boolean isActionIdHolder() {
        return actionIdHolder;
    }

    public <T extends ParameterInfo> T setActionIdHolder(boolean actionIdHolder) {
        this.actionIdHolder = actionIdHolder;
        return (T) this;
    }

    public Set<String> getPossibleValues() {
        return possibleValues;
    }

    public <T extends ParameterInfo> T setPossibleValues(Set<String> possibleValues) {
        this.possibleValues = possibleValues;
        return (T) this;
    }

    public Class<?> getIterationType() {
        return iterationType;
    }

    public boolean isIgnoreSubParameters() {
        return ignoreSubParameters;
    }

    @Override
    public String toString() {
        return "ParameterInfo{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", displayName='" + displayName + '\'' +
                ", type='" + type + '\'' +
                ", defaultValue='" + defaultValue + '\'' +
                ", regexValidation='" + regexValidation + '\'' +
                ", rangeMax=" + rangeMax +
                ", rangeMin=" + rangeMin +
                ", valueSteps=" + valueSteps +
                ", idRef=" + idRef +
                ", list=" + list +
                ", mandatory=" + mandatory +
                ", actionId=" + actionId +
                ", actionIdHolder=" + actionIdHolder +

                ", idRefType=" + idRefType +
                ", subParameters=" + subParameters +
                '}';
    }
}
