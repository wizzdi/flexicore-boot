package com.flexicore.response;

import com.flexicore.interfaces.dynamic.FieldInfo;
import com.flexicore.interfaces.dynamic.IdRefFieldInfo;
import com.flexicore.interfaces.dynamic.ListFieldInfo;

import java.lang.reflect.Field;
import java.util.*;

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
    private Class<?> idRefType;
    private List<ParameterInfo> subParameters;
    private static Set<Class<?>> wrappers = new HashSet<>();

    static {
        wrappers.add(Boolean.class);
        wrappers.add(Character.class);
        wrappers.add(Byte.class);
        wrappers.add(Short.class);
        wrappers.add(Integer.class);
        wrappers.add(Long.class);
        wrappers.add(Float.class);
        wrappers.add(Double.class);
        wrappers.add(Void.class);
        wrappers.add(String.class);
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
            if (fieldInfo.rangeEnabled()) {
                this.rangeMin = fieldInfo.rangeMin();
                this.rangeMax = fieldInfo.rangeMax();
                this.valueSteps = fieldInfo.valueSteps();
            }

        }


        Class<?> type = parameter.getType();
        iterateFields(type);


    }

    public ParameterInfo(Field parameter, ListFieldInfo fieldInfo) {
        this.name = parameter.getName();
        this.displayName = fieldInfo != null && !fieldInfo.displayName().isEmpty() ? fieldInfo.displayName() : name;
        this.description = fieldInfo != null && !fieldInfo.description().isEmpty() ? fieldInfo.description() : "No Description";
        this.list = true;
        this.mandatory = fieldInfo != null && fieldInfo.mandatory();

        Class<?> type = fieldInfo != null ? fieldInfo.listType() : null;
        this.type = type != null ? type.getCanonicalName() : null;
        if (type != null) {
            iterateFields(type);

        }


    }

    private void iterateFields(Class<?> type) {
        if (!type.isPrimitive() || !wrappers.contains(type)) {
            subParameters = new ArrayList<>();
            for (Field field : getAllFields(type)) {

                IdRefFieldInfo subFieldInfo = field.getDeclaredAnnotation(IdRefFieldInfo.class);
                if (subFieldInfo != null) {
                    this.subParameters.add(new ParameterInfo(field, subFieldInfo));

                } else {
                    FieldInfo fieldInfo = field.getDeclaredAnnotation(FieldInfo.class);
                    if (fieldInfo != null) {
                        this.subParameters.add(new ParameterInfo(field, fieldInfo));
                    } else {
                        ListFieldInfo listFieldInfo = field.getDeclaredAnnotation(ListFieldInfo.class);
                        if (listFieldInfo != null) {
                            this.subParameters.add(new ParameterInfo(field, listFieldInfo));
                        }
                    }
                }

            }
        }
    }

    public static List<Field> getAllFields(Class<?> type) {
        List<Field> fields = new ArrayList<>();
        for (Class<?> current = type; current != null; current = current.getSuperclass()) {
            fields.addAll(Arrays.asList(current.getDeclaredFields()));
        }

        return fields;
    }

    public ParameterInfo(Field parameter, IdRefFieldInfo fieldInfo) {
        this.name = parameter.getName();
        this.displayName = fieldInfo != null && !fieldInfo.displayName().isEmpty() ? fieldInfo.displayName() : name;
        this.description = fieldInfo != null && !fieldInfo.description().isEmpty() ? fieldInfo.description() : "No Description";
        this.list = fieldInfo == null || fieldInfo.list();
        this.mandatory = fieldInfo != null && fieldInfo.mandatory();
        this.type = "com.flexicore.model.BaseclassIdFiltering";
        idRef = true;
        idRefType = fieldInfo != null ? fieldInfo.refType() : null;
        this.actionId= fieldInfo != null && fieldInfo.actionId();



    }

    public ParameterInfo(Class<?> c) {
        this.displayName = c.getSimpleName();
        this.name = c.getCanonicalName();
        this.type = c.getCanonicalName();
        iterateFields(c);
    }

    public ParameterInfo(ClassInfo c) {
        this.displayName = c.getDisplayName();
        this.name = c.getClazz().getCanonicalName();
        this.type = c.getClazz().getCanonicalName();
        iterateFields(c.getClazz());
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

    public static Set<Class<?>> getWrappers() {
        return wrappers;
    }

    public static void setWrappers(Set<Class<?>> wrappers) {
        ParameterInfo.wrappers = wrappers;
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
}
