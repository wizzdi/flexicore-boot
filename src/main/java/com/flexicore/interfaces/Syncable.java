package com.flexicore.interfaces;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver;
import com.wizzdi.flexicore.boot.rest.resolvers.CrossLoaderResolver;


import java.time.OffsetDateTime;

@JsonIdentityInfo(generator = ObjectIdGenerators.UUIDGenerator.class, property = "json-id")
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS,property = "json-type")
@JsonTypeIdResolver(CrossLoaderResolver.class)
public interface Syncable {

    String getId();
    OffsetDateTime getUpdateDate();
    OffsetDateTime getCreationDate();
    boolean isNoSQL();
}
