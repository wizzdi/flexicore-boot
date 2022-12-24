/*******************************************************************************
 *  Copyright (C) FlexiCore, Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Avishay Ben Natan And Asaf Ben Natan, October 2015
 ******************************************************************************/
package com.flexicore.rest;

import com.flexicore.annotations.IOperation;
import com.flexicore.annotations.IOperation.Access;
import com.flexicore.annotations.OperationsInside;
import com.flexicore.annotations.Protected;
import com.flexicore.interfaces.RESTService;
import com.flexicore.request.ZipAndDownloadRequest;
import com.flexicore.security.SecurityContext;
import com.flexicore.service.impl.FileResourceService;
import com.wizzdi.flexicore.file.model.ZipFile;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.pf4j.Extension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.enterprise.context.RequestScoped;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/download")
@RequestScoped
@Component
@Extension
@OperationsInside
@Protected
@Tag(name = "Download")
@Tag(name = "Core")

public class DownloadRESTService implements RESTService {

    @Autowired
    private FileResourceService fileResourceService;

    private static final Logger logger = LoggerFactory.getLogger(DownloadRESTService.class);

    /**
     * download a file by its fileResource ID
     *
     * @param authenticationkey authentication key
     * @param id id
     * @param securityContext security context
     * @param offset offset to start reading from
     * @param req request context
     * @param size length to read
     * @return binary file data
     */
    @GET
    @Path("{authenticationkey}/{id}")
    @IOperation(access = Access.allow, Name = "downloadFile", Description = "downloads file by its fileResource ID")
    public Response download(@PathParam("authenticationkey") String authenticationkey,
                             @Parameter(description = "id of the FileResource Object to Download")
                             @HeaderParam("offset") @DefaultValue("0") long offset,
                             @HeaderParam("size") @DefaultValue("0") long size,
                             @PathParam("id") String id, @Context HttpServletRequest req, @Context SecurityContext securityContext) {
        return fileResourceService.download(offset, size, id, req.getRemoteAddr(), securityContext);

    }




    /**
     * zips list of fileResources and sends it
     *
     * @param authenticationkey authentication key
     * @param zipAndDownload zip and download request
     * @param securityContext security context
     * @return binary zip data
     */
    @POST
    @Path("zipAndDownload")
    @Operation(summary = "zipAndDownload", description = "Mass Download")
    public Response zipAndDownload(@HeaderParam("authenticationKey") String authenticationkey,
                                   ZipAndDownloadRequest zipAndDownload, @Context SecurityContext securityContext) {
        fileResourceService.validate(zipAndDownload, securityContext);
        ZipFile zipFile = fileResourceService.zipAndDownload(zipAndDownload, securityContext);

        return fileResourceService.prepareFileResourceForDownload(zipFile, zipAndDownload.getOffset(), 0);


    }

    @POST
    @Path("getOrCreateZipFile")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "getOrCreateZipFile", description = "getOrCreateZipFile")
    public ZipFile getOrCreateZipFile(@HeaderParam("authenticationKey") String authenticationkey,
                                      ZipAndDownloadRequest zipAndDownload, @Context SecurityContext securityContext) {
        fileResourceService.validate(zipAndDownload, securityContext);
        return fileResourceService.zipAndDownload(zipAndDownload, securityContext);
    }






}
