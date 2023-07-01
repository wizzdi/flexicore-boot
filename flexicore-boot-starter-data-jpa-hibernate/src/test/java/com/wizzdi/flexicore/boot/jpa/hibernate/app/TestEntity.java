package com.wizzdi.flexicore.boot.jpa.hibernate.app;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;

@Entity
public class TestEntity extends BaseEntity{



	private String name;
	@Column(columnDefinition = "bytea")
	private String longText;


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
