/*******************************************************************************
 *  Copyright (C) FlexiCore, Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Avishay Ben Natan And Asaf Ben Natan, October 2015
 ******************************************************************************/
package com.flexicore.model;

import com.fasterxml.jackson.annotation.*;
import com.flexicore.annotations.AnnotatedClazz;
import com.flexicore.interfaces.Syncable;
import com.flexicore.security.SecurityContextBase;
import com.wizzdi.flexicore.boot.rest.views.Views;
import com.wizzdi.flexicore.boot.rest.views.Views.Full;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;


@AnnotatedClazz(Category = "core", Name = "Baseclass", Description = "The root of all entities")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Entity
@Table(name = "baseclass", indexes = {@Index(name = "baseclass_id", columnList = "id", unique = true),
		@Index(name = "clazz_idx", columnList = "clazz_id"),
		@Index(name = "creator_idx", columnList = "creator_id"),
		@Index(name = "name_idx", columnList = "name"),
		@Index(name = "sort_idx", columnList = "name,id"),
		@Index(name = "search_idx", columnList = "searchKey,dtype")
})
public class Baseclass extends Basic implements Syncable {

	private static ConcurrentHashMap<String, Clazz> allclazzes = new ConcurrentHashMap<>();


	@Lob
	@JsonIgnore
	private String searchKey;



	@Column(name = "dtype", insertable = false, updatable = false)
	private String dtype;


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


	@OneToMany(targetEntity = SecurityLink.class, mappedBy = "baseclass")
	@JsonIgnore
	private List<SecurityLink> securityLinks = new ArrayList<>();


	@JsonIgnore
	@OneToMany(targetEntity = PermissionGroupToBaseclass.class, mappedBy = "baseclass")
	private List<PermissionGroupToBaseclass> permissionGroupToBaseclasses = new ArrayList<>();



	public void init() {
		init(null);
	}



	public void init(Class<?> javaClass) {
		if (id == null) {
			id = getBase64ID();
		}

		if (getCreationDate() == null) {
			setCreationDate(OffsetDateTime.now());
		}
		if(javaClass==null){
			javaClass=getClass();
		}


		String typeName = javaClass.getCanonicalName();
		Clazz clazz = allclazzes.get(typeName);
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


	@OneToMany(targetEntity = SecurityLink.class, mappedBy = "baseclass")

	public List<SecurityLink> getSecurityLinks() {
		return securityLinks;
	}

	public <T extends Baseclass> T setSecurityLinks(List<SecurityLink> securityLinks) {
		this.securityLinks = securityLinks;
		return (T) this;
	}

	public Baseclass(String name, SecurityContextBase securityContext) {
		this(name,null,securityContext);
	}


	public Baseclass(String name,Class<?> javaType, SecurityContextBase securityContext) {
		this.name = name;
		if (securityContext != null) {
			this.creator = securityContext.getUser();
			this.tenant = securityContext.getTenantToCreateIn();
		}

		init(javaType);

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
	public static String generateUUIDFromStringCompt(String input) {

		return UUID.nameUUIDFromBytes(input.getBytes()).toString()
				.replaceAll("-", "")
				.substring(0, 22);

	}
	public static String generateUUIDFromString(String input) {

		return UUID.nameUUIDFromBytes(input.getBytes()).toString();

	}




	public boolean isSystemObject() {
		return systemObject;
	}

	public Baseclass setSystemObject(boolean systemObject) {
		this.systemObject = systemObject;
		return this;
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
	@OneToMany(targetEntity = PermissionGroupToBaseclass.class, mappedBy = "baseclass")
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
