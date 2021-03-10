package com.wizzdi.flexicore.boot.jpa.hibernate.app;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;

@Entity
public class TestEntity {

	@Id
	private String id;

	private String name;
	@Column(columnDefinition = "bytea")
	private String longText;

	@Id
	public String getId() {
		return id;
	}

	public <T extends TestEntity> T setId(String id) {
		this.id = id;
		return (T) this;
	}

	public String getName() {
		return name;
	}

	public <T extends TestEntity> T setName(String name) {
		this.name = name;
		return (T) this;
	}

	@Column(columnDefinition = "bytea")
	public String getLongText() {
		return longText;
	}

	public <T extends TestEntity> T setLongText(String longText) {
		this.longText = longText;
		return (T) this;
	}
}
