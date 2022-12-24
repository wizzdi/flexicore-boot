package com.flexicore.data;

import com.flexicore.annotations.InheritedComponent;
import com.flexicore.interfaces.FlexiCoreService;
import com.flexicore.model.Clazz;
import org.pf4j.Extension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;


@InheritedComponent
@Extension
public class ClazzRegistration implements FlexiCoreService {


    @PersistenceContext
    private EntityManager em;


    private static final Logger logger = LoggerFactory.getLogger(ClazzRegistration.class);

    public void register(Clazz clazz) {
        em.persist(clazz);
    }


}