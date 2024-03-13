package com.wizzdi.dynamic.properties.converter.postgresql;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Book {
    @Id
    private String id;

    private String title;
    @ManyToOne(targetEntity = Author.class)
    private Author author;

    @Id
    public String getId() {
        return id;
    }

    public <T extends Book> T setId(String id) {
        this.id = id;
        return (T) this;
    }

    public String getTitle() {
        return title;
    }

    public <T extends Book> T setTitle(String title) {
        this.title = title;
        return (T) this;
    }

    @ManyToOne(targetEntity = Author.class)
    public Author getAuthor() {
        return author;
    }

    public <T extends Book> T setAuthor(Author author) {
        this.author = author;
        return (T) this;
    }
}
