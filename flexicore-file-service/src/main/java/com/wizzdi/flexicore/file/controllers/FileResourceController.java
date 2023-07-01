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
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/fileResource")
@Extension
@Tag(name = "FileResource")
@OperationsInside
public class FileResourceController implements Plugin {

    private static final Logger logger = LoggerFactory.getLogger(FileResourceController.class);

    @Autowired
    private FileResourceService fileResourceService;


    /**
     * retreives file resource by md5
     *
     * @param md5               md5 of requested file
     * @param securityContext security context
     * @return FileResource requested file
     */
    @GetMapping("{md5}")
    @IOperation(access = Access.allow, Name = "gets file resource", Description = "gets a fileResource by MD5")

    public FileResource getFileResource(     @PathVariable("md5") String md5, @RequestAttribute SecurityContextBase securityContext) {
        return fileResourceService.getFileResourceByMd5(md5, securityContext);

    }



}
