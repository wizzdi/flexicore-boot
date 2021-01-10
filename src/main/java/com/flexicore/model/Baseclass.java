/*******************************************************************************
 *  Copyright (C) FlexiCore, Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Avishay Ben Natan And Asaf Ben Natan, October 2015
 ******************************************************************************/
package com.flexicore.model;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonTypeResolver;
import com.flexicore.annotations.AnnotatedClazz;
import com.flexicore.data.jsoncontainers.FCTypeResolver;
import com.wizzdi.flexicore.boot.rest.views.Views;
import com.wizzdi.flexicore.boot.rest.views.Views.Full;
import com.flexicore.interfaces.Syncable;
import com.flexicore.security.SecurityContextBase;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;


import javax.persistence.*;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;


@AnnotatedClazz(Category = "core", Name = "Baseclass", Description = "The root of all entities")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.UUIDGenerator.class, property = "json-id")
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "json-type", visible = true)
@JsonTypeResolver(FCTypeResolver.class)
@Table(name = "baseclass", indexes = {@Index(name = "baseclass_id", columnList = "id", unique = true),
        @Index(name = "clazz_idx", columnList = "clazz_id"),
        @Index(name = "creator_idx", columnList = "creator_id"),
        @Index(name = "name_idx", columnList = "name"),
        @Index(name = "sort_idx", columnList = "name,id"),
        @Index(name = "search_idx", columnList = "searchKey,dtype")
})
public class Baseclass implements Syncable {

    private static ConcurrentHashMap<String, Clazz> allclazzes = new ConcurrentHashMap<>();


    @Lob
    @JsonIgnore
    private String searchKey;
    private boolean softDelete;


    @Column(name = "dtype", insertable = false, updatable = false)
    private String dtype;

    @Id
    protected String id;
    protected String name;
    protected String description;

    @ManyToOne(targetEntity = SecurityUser.class)
    @JsonView(Views.ForSwaggerOnly.class)
    protected SecurityUser creator;

    @ManyToOne(targetEntity = SecurityTenant.class)

    private SecurityTenant tenant;

    private boolean systemObject;

    @ManyToOne(targetEntity = SecurityTenant.class)

    public SecurityTenant getTenant() {
        return tenant;
    }

    public void setTenant(SecurityTenant tenant) {
        this.tenant = tenant;
    }

    @ManyToOne(targetEntity = Clazz.class)
    @JsonView(Full.class)
    protected Clazz clazz;
    // this field is added to to trigger metadatamodel generation , sometimes _.clazz field isn't created.


    @Column(columnDefinition = "timestamp with time zone")
    private OffsetDateTime creationDate;
    @Column(columnDefinition = "timestamp with time zone")
    private OffsetDateTime updateDate;

    @JsonIgnore
    @OneToMany(targetEntity = PermissionGroupToBaseclass.class, mappedBy = "rightside")
    private List<PermissionGroupToBaseclass> permissionGroupToBaseclasses = new ArrayList<>();


    public boolean isSoftDelete() {
        return softDelete;
    }

    public void setSoftDelete(boolean softDelete) {
        this.softDelete = softDelete;
    }


    public OffsetDateTime getCreationDate() {
        return creationDate;
    }


    public void setCreationDate(OffsetDateTime timestampField) {
        this.creationDate = timestampField;
    }


    public void init() {
        if (id == null) {
            id = getBase64ID();
        }

        if (creationDate == null) {
            creationDate = OffsetDateTime.now();
        }


        Class<? extends Baseclass> javaClass = this.getClass();
        String typeName = javaClass.getCanonicalName();
        Clazz clazz = Clazz.class.equals(javaClass) && this.name.equals("com.flexicore.model.Clazz") ? (Clazz) this : allclazzes.get(typeName);
        this.setClazz(clazz);

    }


    public Baseclass() {
        super();

    }

    public static String getBase64ID() {
        String result;
        try {
            result = new String(Base64.encodeBase64(Hex.decodeHex(UUID.randomUUID().toString().replaceAll("-", "")
                    .toCharArray())));
            result = result.replace("/", "-"); // we cannot afford a slash
            result = result.substring(0, 22); //we don't need the trailing ==

        } catch (DecoderException e) {
            result = "errorinid";
        }

        return result;
    }


    @Id
    public String getId() {

        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @JsonView(Full.class)
    @ManyToOne(targetEntity = Clazz.class)
    public Clazz getClazz() {
        return clazz;
    }

    public void setClazz(Clazz clazz) {
        this.clazz = clazz;
    }


    @JsonView(Views.ForSwaggerOnly.class)
    @ManyToOne(targetEntity = SecurityUser.class)
    public SecurityUser getCreator() {
        return creator;
    }

    public void setCreator(SecurityUser creator) {
        this.creator = creator;
    }


    @JsonIgnore
    @OneToMany(targetEntity = SecurityLink.class, mappedBy = "rightside")
    public List<SecurityLink> securityLinks = new ArrayList<>();


    public Baseclass(String name, SecurityContextBase securityContext) {
        this.name = name;
        if (securityContext != null) {
            this.creator = securityContext.getUser();
            this.tenant = securityContext.getTenantToCreateIn();
        }

        init();

    }


    public static void addClazz(Clazz clazz) {
        String key = clazz.getName();
        if (!allclazzes.containsKey(key)) {
            allclazzes.put(key, clazz);
        }
    }


    public static Clazz getClazzByName(String canonicalName) {
        return allclazzes.get(canonicalName);

    }


    public OffsetDateTime getUpdateDate() {
        return updateDate;
    }


    public void setUpdateDate(OffsetDateTime updateDate) {
        this.updateDate = updateDate;
    }


    public static List<Clazz> getAllClazz() {
        return new ArrayList<>(allclazzes.values());
    }


    /**
     * generates UUID from input string , for every run generateUUIDFromString(s)=generateUUIDFromString(s),
     * id is cached to improve performance
     *
     * @param input
     * @return generated/cached id
     */
    public static String generateUUIDFromString(String input) {

        return UUID.nameUUIDFromBytes(input.getBytes()).toString()
                .replaceAll("-", "")
                .substring(0, 22);

    }

    @JsonIgnore
    @OneToMany(targetEntity = SecurityLink.class, mappedBy = "rightside")
    public List<SecurityLink> getSecurityLinks() {
        return securityLinks;
    }

    public void setSecurityLinks(List<SecurityLink> securityLinks) {
        this.securityLinks = securityLinks;
    }

    public boolean isSystemObject() {
        return systemObject;
    }

    public Baseclass setSystemObject(boolean systemObject) {
        this.systemObject = systemObject;
        return this;
    }


    @Column(name = "dtype", insertable = false, updatable = false)

    public String getDtype() {
        return dtype;
    }

    public <T extends Baseclass> T setDtype(String dtype) {
        this.dtype = dtype;
        return (T) this;
    }

    @Lob
    @JsonIgnore
    public String getSearchKey() {
        return searchKey;
    }

    public <T extends Baseclass> T setSearchKey(String searchKey) {
        this.searchKey = searchKey;
        return (T) this;
    }

    @JsonIgnore
    @OneToMany(targetEntity = PermissionGroupToBaseclass.class, mappedBy = "rightside")
    public List<PermissionGroupToBaseclass> getPermissionGroupToBaseclasses() {
        return permissionGroupToBaseclasses;
    }

    public Baseclass setPermissionGroupToBaseclasses(List<PermissionGroupToBaseclass> permissionGroupToBaseclasses) {
        this.permissionGroupToBaseclasses = permissionGroupToBaseclasses;
        return this;
    }

    @Override
    @Transient
    public boolean isNoSQL() {
        return false;
    }


}
