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
import com.flexicore.annotations.AnnotatedClazz;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class SecurityOperation implements IOperation {
    private final String id;
    private final String name;
    private final String description;
    private final Access defaultAccess;
    private final String category;


    public SecurityOperation(String id, String name, String description, Access defaultAccess, String category) {
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
        return new SecurityOperation(operationId, null, null, null, null);
    }

    @Override
    public String getId() {
        return id;
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

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (SecurityOperation) obj;
        return Objects.equals(this.id, that.id) &&
                Objects.equals(this.name, that.name) &&
                Objects.equals(this.description, that.description) &&
                Objects.equals(this.defaultAccess, that.defaultAccess) &&
                Objects.equals(this.category, that.category);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, defaultAccess, category);
    }

    @Override
    public String toString() {
        return "SecurityOperation[" +
                "id=" + id + ", " +
                "name=" + name + ", " +
                "description=" + description + ", " +
                "defaultAccess=" + defaultAccess + ", " +
                "category=" + category + ']';
    }

}
