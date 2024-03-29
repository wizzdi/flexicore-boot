package com.wizzdi.flexicore.boot.data.rest.app;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Book {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)

	private Long id;
	private String name;


	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}

	public <T extends Book> T setId(Long id) {
		this.id = id;
		return (T) this;
	}

	public String getName() {
		return name;
	}

	public <T extends Book> T setName(String name) {
		this.name = name;
		return (T) this;
	}

	@Override
	public String toString() {
		return "Book{" +
				"id=" + id +
				", name='" + name + '\'' +
				'}';
	}
}
