/*******************************************************************************
 *  Copyright (C) FlexiCore, Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Avishay Ben Natan And Asaf Ben Natan, October 2015
 ******************************************************************************/
package com.flexicore.rest;

import com.flexicore.annotations.IOperation;
import com.flexicore.annotations.IOperation.Access;
import com.flexicore.annotations.LogExecutionTime;
import com.flexicore.annotations.OperationsInside;
import com.flexicore.interfaces.RESTService;
import com.flexicore.model.Baseclass;
import com.flexicore.service.impl.BaseclassService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.pf4j.Extension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

/**
 * no need to intercept for security here.
 *
 * @author Avishay Ben Natan
 */
@Path("/ping")
@RequestScoped
@Component
@OperationsInside
@Tag(name = "Core")
@Extension
public class PingRESTService implements RESTService {

    @Autowired
    private BaseclassService baseclassService;

   private static final Logger logger = LoggerFactory.getLogger(PingRESTService.class);


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @IOperation(access = Access.allow, Name = "pingServer", Description = "ping Server for availability")
    @LogExecutionTime
    public boolean ping() {
        //return Initializator.getInit();
        return true;

    }


    @GET
    @Path("checkDBAvailable")
    @Produces(MediaType.APPLICATION_JSON)
    @IOperation(access = Access.allow, Name = "checkDBAvailable", Description = "ping db for availability")
    @LogExecutionTime
    public boolean checkDBAvailable() {
        try {
            String id = Baseclass.generateUUIDFromString("dbPingCheck");
            Baseclass baseclass = baseclassService.findByIdOrNull(Baseclass.class, id);
            if (baseclass == null) {
                baseclass = new Baseclass("pingCheck", null);
                baseclass.setId(id);
                baseclass.setDescription(System.currentTimeMillis() + "");
                baseclassService.persist(baseclass);

            } else {
                baseclass.setDescription(System.currentTimeMillis() + "");
                baseclassService.merge(baseclass);
            }
            return true;

        } catch (Exception e) {
            logger.error( "DB ISSUE", e);
        }
        return false;


    }

}
