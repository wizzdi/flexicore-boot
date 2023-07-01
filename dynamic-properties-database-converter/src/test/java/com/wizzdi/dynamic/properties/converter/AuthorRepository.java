package com.wizzdi.dynamic.properties.converter;

import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class AuthorRepository {

    @Autowired
    private EntityManager em;


    @Transactional
    public Author merge(Author author){
       return em.merge(author);
    }

}
