/*******************************************************************************
 *  Copyright (C) FlexiCore, Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Avishay Ben Natan And Asaf Ben Natan, October 2015
 ******************************************************************************/
package com.flexicore.model;


import com.fasterxml.jackson.annotation.JsonIgnore;

public record Clazz(@JsonIgnore Class<?> c, String name) {
    public static Clazz ofClass(Class<?> c){
        return new Clazz(c,c.getSimpleName());
    }
    public static Clazz ofName(String name){
        return new Clazz(null,name);
    }

    @JsonIgnore
    @Override
    public Class<?> c() {
        return c;
    }

    @Override
    public String name() {
        return name;
    }
}
