package com.wizzdi.dynamic.properties.converter.postgresql;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
public class Author {

    @Id
    private String id;

    private String name;

    private OffsetDateTime birthDate;

    private int age;

    private boolean oddYear;

    @JsonIgnore
    @OneToMany(mappedBy = "author",targetEntity = Book.class)
    private List<Book> books=new ArrayList<>();


    @Column(columnDefinition = "jsonb")
    @Convert(converter = PostgresqlJsonConverter.class)
    private Map<String, Object> dynamicProperties=new HashMap<>();

    @Id
    public String getId() {
        return id;
    }

    public <T extends Author> T setId(String id) {
        this.id = id;
        return (T) this;
    }

    @Column(columnDefinition = "jsonb")
    @Convert(converter = PostgresqlJsonConverter.class)
    public Map<String, Object> getDynamicProperties() {
        return dynamicProperties;
    }

    public <T extends Author> T setDynamicProperties(Map<String, Object> dynamicProperties) {
        this.dynamicProperties = dynamicProperties;
        return (T) this;
    }

    public String getName() {
        return name;
    }

    public <T extends Author> T setName(String name) {
        this.name = name;
        return (T) this;
    }

    public OffsetDateTime getBirthDate() {
        return birthDate;
    }

    public <T extends Author> T setBirthDate(OffsetDateTime birthDate) {
        this.birthDate = birthDate;
        return (T) this;
    }

    @JsonIgnore
    @OneToMany(mappedBy = "author",targetEntity = Book.class)
    public List<Book> getBooks() {
        return books;
    }

    public <T extends Author> T setBooks(List<Book> books) {
        this.books = books;
        return (T) this;
    }

    public int getAge() {
        return age;
    }

    public <T extends Author> T setAge(int age) {
        this.age = age;
        return (T) this;
    }

    public boolean isOddYear() {
        return oddYear;
    }

    public <T extends Author> T setOddYear(boolean post2010) {
        this.oddYear = post2010;
        return (T) this;
    }
}
