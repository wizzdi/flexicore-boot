package com.wizzdi.dynamic.properties.converter.postgresql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
public class AuthorService {

    @Autowired
    private AuthorRepository authorRepository;


    public Author createAuthor(String name, OffsetDateTime birthDate) {
        return createAuthor(name, birthDate, new HashMap<>());
    }

    public Author createAuthor(String name, OffsetDateTime birthDate, Map<String, Object> props) {
        Author author=new Author();
        author.setId(UUID.randomUUID().toString());
        author.setName(name);
        author.setDynamicProperties(props);
        author.setBirthDate(birthDate);
        author.setAge(OffsetDateTime.now().getYear()-birthDate.getYear());
        author.setOddYear(birthDate.getYear() % 2 != 0);
        return authorRepository.merge(author);
    }

    public Author createAuthorWithBooks(String name, OffsetDateTime birthDate,int numberOfBooks) {
        Author author=createAuthor(name,birthDate);
        for (int i = 0; i < numberOfBooks; i++) {
            Book book=new Book();
            book.setId(UUID.randomUUID().toString());
            book.setTitle(author.getName() +" title "+i);
            book.setAuthor(author);
            authorRepository.mergeBook(book);
        }
        return author;
    }


    public List<Author> getAuthors(AuthorFilter filter) {
        return authorRepository.getAuthors(filter);
    }

    public List<Book> getAuthorsBooks(Author author) {
        return authorRepository.getAuthorsBooks(author);
    }

    public List<Author> getAuthorsDynamicFilter(Map<String, DynamicFilterItem> filter) {
        return authorRepository.getAuthors(new AuthorFilter().setDynamicPropertiesFilter(filter));
    }

    public List<Author> getAuthorsStaticFilter(Map<String, DynamicFilterItem> filter) {
        return authorRepository.getAuthors(new AuthorFilter().setStaticPropertiesFilter(filter));
    }
}
