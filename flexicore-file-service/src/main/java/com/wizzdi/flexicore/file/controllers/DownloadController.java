/*******************************************************************************
 *  Copyright (C) FlexiCore, Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Avishay Ben Natan And Asaf Ben Natan, October 2015
 ******************************************************************************/
package com.wizzdi.flexicore.file.controllers;

import com.flexicore.annotations.IOperation;
import com.flexicore.model.SecurityOperation;
import com.wizzdi.flexicore.security.service.SecurityOperationService;
import com.wizzdi.security.adapter.FlexiCoreAuthentication;
import com.wizzdi.security.bearer.jwt.JWTSecurityContextCreator;
import com.wizzdi.segmantix.model.Access;
import com.flexicore.annotations.OperationsInside;
import com.wizzdi.flexicore.security.configuration.SecurityContext;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.file.service.FileResourceService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.pf4j.Extension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/download")
@Extension
@Tag(name = "Download")
@OperationsInside
public class DownloadController implements Plugin {

    private static final Logger logger = LoggerFactory.getLogger(DownloadController.class);

    @Autowired
    private FileResourceService fileResourceService;
    @Autowired
    private JWTSecurityContextCreator jwtSecurityContextCreator;
    @Autowired
    private SecurityOperationService securityOperationService;
    private SecurityOperation download;


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
    @SecurityRequirements
    @GetMapping("{authenticationkey}/{id}")
    @IOperation(access = Access.allow, Name = "downloadFile", Description = "downloads file by its fileResource ID")
    public ResponseEntity<Resource> download(@PathVariable(value = "authenticationkey") String authenticationkey,
                                             @Parameter(description = "id of the FileResource Object to Download")
                                             @RequestHeader(value = "offset", defaultValue = "0", required = false) long offset,
                                             @RequestHeader(value = "size", defaultValue = "0", required = false) long size,
                                             @PathVariable("id") String id, HttpServletRequest req) {
        FlexiCoreAuthentication flexiCoreAuthentication = jwtSecurityContextCreator.getSecurityContext(authenticationkey);
        if(flexiCoreAuthentication==null|| flexiCoreAuthentication.getSecurityContext() ==null){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        try {
            SecurityContext securityContext = flexiCoreAuthentication.getSecurityContext();
             download =download==null? securityOperationService.getOperation(DownloadController.class.getMethod("download", String.class, long.class, long.class, String.class, HttpServletRequest.class)):download;
            securityContext.setOperation(download);
            return fileResourceService.download(offset, size, id, req.getRemoteAddr(), securityContext);
        } catch (NoSuchMethodException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

    }


}
