package com.wizzdi.dynamic.properties.converter.postgresql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
public class AuthorService {

    @Autowired
    private AuthorRepository authorRepository;


    public Author createAuthor(String name, Map<String,Object> props){
        Author author=new Author();
        author.setId(UUID.randomUUID().toString());
        author.setName(name);
        author.setDynamicProperties(props);
        return authorRepository.merge(author);
    }

    public List<Author> getAuthors(Map<String,DynamicFilterItem> filter){
        return authorRepository.getAuthors(filter);
    }
}
