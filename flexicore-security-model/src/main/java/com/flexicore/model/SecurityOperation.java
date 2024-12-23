/*******************************************************************************
 *  Copyright (C) FlexiCore, Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Avishay Ben Natan And Asaf Ben Natan, October 2015
 ******************************************************************************/
package com.flexicore.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wizzdi.segmantix.api.model.IOperation;
import com.wizzdi.segmantix.model.Access;

import java.lang.reflect.Method;
import java.util.Objects;


public class SecurityOperation implements IOperation {
    private final Method method;
    private final Class<?> clazz;
    private final String id;
    private final String name;
    private final String description;
    private final Access defaultAccess;
    private final String category;

    public SecurityOperation(Method method, Class<?> clazz, String id, String name, String description,
                             Access defaultAccess, String category) {
        this.method = method;
        this.clazz = clazz;
        this.id = id;
        this.name = name;
        this.description = description;
        this.defaultAccess = defaultAccess;
        this.category = category;
    }

    public static IOperation ofId(String operationId) {
        if (operationId == null) {
            return null;
        }
        return new SecurityOperation(null, null, operationId, null, null, null, null);
    }

    public static SecurityOperation ofMethod(Method method, String id, String name, String description, Access defaultAccess, String category) {
        return new SecurityOperation(method, null, id, name, description, defaultAccess, category);
    }

    public static SecurityOperation ofStandardAccess(Class<?> clazz, String id, String name, String description, Access defaultAccess, String category) {
        return new SecurityOperation(null, clazz, id, name, description, defaultAccess, category);
    }

    @Override
    public String getId() {
        return id;
    }

    @JsonIgnore
    public Method method() {
        return method;
    }

    public Class<?> clazz() {
        return clazz;
    }

    public String id() {
        return id;
    }

    public String name() {
        return name;
    }

    public String description() {
        return description;
    }

    public Access defaultAccess() {
        return defaultAccess;
    }

    public String category() {
        return category;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Access getDefaultAccess() {
        return defaultAccess;
    }

    public String getCategory() {
        return category;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (SecurityOperation) obj;
        return Objects.equals(this.method, that.method) &&
                Objects.equals(this.clazz, that.clazz) &&
                Objects.equals(this.id, that.id) &&
                Objects.equals(this.name, that.name) &&
                Objects.equals(this.description, that.description) &&
                Objects.equals(this.defaultAccess, that.defaultAccess) &&
                Objects.equals(this.category, that.category);
    }

    @Override
    public int hashCode() {
        return Objects.hash(method, clazz, id, name, description, defaultAccess, category);
    }

    @Override
    public String toString() {
        return "SecurityOperation[" +
                "method=" + method + ", " +
                "clazz=" + clazz + ", " +
                "id=" + id + ", " +
                "name=" + name + ", " +
                "description=" + description + ", " +
                "defaultAccess=" + defaultAccess + ", " +
                "category=" + category + ']';
    }


}
