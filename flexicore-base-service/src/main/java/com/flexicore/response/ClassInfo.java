package com.flexicore.response;

import com.flexicore.annotations.AnnotatedClazz;
import com.flexicore.model.Baseclass;
import com.flexicore.model.Clazz;

import java.util.Objects;

public class ClassInfo {

    private Class<?> clazz;
    private String clazzId;
    private String displayName;
    private String description;

    public ClassInfo() {
    }

    public ClassInfo(Class<?> clazz, AnnotatedClazz annotatedClazz) {
        this.clazz = clazz;
        Clazz clazzbyname = Baseclass.getClazzByName(clazz.getCanonicalName());
        this.clazzId= clazzbyname!=null?clazzbyname.getId():null;
        this.displayName=annotatedClazz!=null&&!annotatedClazz.DisplayName().isEmpty()?annotatedClazz.DisplayName():clazz.getSimpleName();
        this.description=annotatedClazz!=null?annotatedClazz.Description():"No Description";
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public ClassInfo setClazz(Class<?> clazz) {
        this.clazz = clazz;
        return this;
    }

    public String getDisplayName() {
        return displayName;
    }

    public ClassInfo setDisplayName(String displayName) {
        this.displayName = displayName;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public ClassInfo setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getClazzId() {
        return clazzId;
    }

    public <T extends ClassInfo> T setClazzId(String clazzId) {
        this.clazzId = clazzId;
        return (T) this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClassInfo classInfo = (ClassInfo) o;
        return Objects.equals(clazz, classInfo.clazz);
    }

    @Override
    public int hashCode() {

        return Objects.hash(clazz);
    }
}
