/*******************************************************************************
 *  Copyright (C) FlexiCore, Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Avishay Ben Natan And Asaf Ben Natan, October 2015
 ******************************************************************************/
package com.flexicore.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.model.User;

@Deprecated
public class NewUser<T extends User> {

    private String email;
    private String name;
    private String surname;
    private String apikey;
    private String newTenantName;
    private String password;
    private String phonenumber;
    private String type;
    @JsonIgnore
    private Class<T> clazz;

    public NewUser() {
        // TODO Auto-generated constructor stub
    }


    public NewUser(String email, String name, String surname, String apikey, String password, String phonenumber,
                   Class<T> clazz) {
        super();
        this.email = email;
        this.name = name;
        this.surname = surname;
        this.apikey = apikey;
        this.password = password;
        this.phonenumber = phonenumber;
        this.clazz = clazz;
        type = clazz.getCanonicalName();
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getApikey() {
        return apikey;
    }

    public void setApikey(String apikey) {
        this.apikey = apikey;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    @JsonIgnore
    public Class<T> getClazz() {
        return clazz;
    }

    public void setClazz(Class<T> clazz) {
        this.clazz = clazz;
    }

    public String getNewTenantName() {
        return newTenantName;
    }

    public void setNewTenantName(String newTenantName) {
        this.newTenantName = newTenantName;
    }

    public String getType() {
        return type;
    }

    public NewUser<T> setType(String type) {
        this.type = type;
        return this;
    }
}
