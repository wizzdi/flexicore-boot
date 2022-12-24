package com.flexicore.model.dynamic;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver;
import com.fasterxml.jackson.databind.annotation.JsonTypeResolver;
import com.wizzdi.flexicore.boot.rest.resolvers.CrossLoaderResolver;
import com.flexicore.data.jsoncontainers.FCTypeResolver;
import com.flexicore.model.Baseclass;
import com.flexicore.security.SecurityContext;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS,property = "type" )
@JsonTypeIdResolver(CrossLoaderResolver.class)
@JsonTypeResolver(FCTypeResolver.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.UUIDGenerator.class, property = "json-id")
@Entity
public class ExecutionParametersHolder {

    @Id
    private String id;



    @JsonIgnore
    @Transient
    private SecurityContext securityContext;

    @JsonIgnore
    @Transient
    public SecurityContext getSecurityContext() {
        return securityContext;
    }

    public ExecutionParametersHolder setSecurityContext(SecurityContext securityContext) {
        this.securityContext = securityContext;
        return this;
    }
    @Id
    public String getId() {
        return id;
    }

    public ExecutionParametersHolder setId(String id) {
        this.id = id;
        return this;
    }

    public void prepareForSave(){
        this.id= Baseclass.getBase64ID();
    }


}
