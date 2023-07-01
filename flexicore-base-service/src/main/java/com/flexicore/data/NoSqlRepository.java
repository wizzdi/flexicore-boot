package com.flexicore.data;

import com.flexicore.interfaces.ServicePlugin;
import org.pf4j.Extension;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by Asaf on 01/12/2016.
 */
@Component
@Extension
public class NoSqlRepository implements ServicePlugin {



    public void refreshEntityManager(){

    }



    public void persist(Object o){


    }

    public void merge(Object o){


    }


    public void batchMerge(List<Object> o){

    }

    public void batchPersist(List<?> o){

    }



}
