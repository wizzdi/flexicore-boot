/*******************************************************************************
 *  Copyright (C) FlexiCore, Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Avishay Ben Natan And Asaf Ben Natan, October 2015
 ******************************************************************************/
package com.wizzdi.flexicore.file.controllers;

import com.flexicore.annotations.IOperation;
import com.flexicore.annotations.IOperation.Access;
import com.flexicore.annotations.OperationsInside;
import com.flexicore.security.SecurityContextBase;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.file.model.FileResource;
import com.wizzdi.flexicore.file.service.FileResourceService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.pf4j.Extension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.InputStream;

@RestController
@RequestMapping("/upload")
@Extension
@Tag(name = "Upload")
@OperationsInside
public class FileUploadController implements Plugin {

    private static final Logger logger = LoggerFactory.getLogger(FileUploadController.class);

    @Autowired
    private FileResourceService fileResourceService;





    @PostMapping(consumes = MediaType.APPLICATION_OCTET_STREAM_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    @IOperation(access = Access.allow, Name = "uploadOctet", Description = "uploads a file octat way")
    public FileResource uploadFile(@RequestHeader("md5") String md5, @RequestHeader(value = "name",required = false) String name,
                                   @RequestHeader(value = "chunkMd5",required = false) String chunkMd5,
                                   @RequestHeader(value = "lastChunk",required = false) boolean lastChunk,
                                   InputStream stream, @RequestAttribute SecurityContextBase securityContext) {
        return fileResourceService.uploadFileResource(name, securityContext, md5,chunkMd5,lastChunk, stream);

    }



}
