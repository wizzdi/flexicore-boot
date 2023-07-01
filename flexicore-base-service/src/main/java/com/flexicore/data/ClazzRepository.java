/*******************************************************************************
 *  Copyright (C) FlexiCore, Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Avishay Ben Natan And Asaf Ben Natan, October 2015
 ******************************************************************************/
package com.flexicore.data;

import com.flexicore.data.impl.BaseclassRepository;
import com.flexicore.model.Baseclass;
import com.flexicore.model.Clazz;
import com.flexicore.model.QueryInformationHolder;
import org.pf4j.Extension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;


@Primary
@Component("ClazzRepositoryBase")
@Extension
public class ClazzRepository extends BaseclassRepository {

   private static final Logger logger = LoggerFactory.getLogger(ClazzRepository.class);

    @Override
    public Clazz findById(String id) {
        // TODO Auto-generated method stub
        return em.find(Clazz.class, id);
    }

    @Override
    public void massMerge(List<?> toMerge) {
        super.massMerge(toMerge);
    }

    @Override
    public <T extends Baseclass> List<T> findByIds(Class<T> c, Set<String> requested) {
        return super.findByIds(c, requested);
    }

    public List<Clazz> findAllOrderedByName(QueryInformationHolder<Clazz> queryInformationHolder) {
        return getAllFiltered(queryInformationHolder);
    }



}
