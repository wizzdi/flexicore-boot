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
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.file.service.FileResourceService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.pf4j.Extension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("/downloadUnsecure")
@Extension
@Tag(name = "Download")
@OperationsInside
public class DownloadUnsecureController implements Plugin {

	private static final Logger logger = LoggerFactory.getLogger(DownloadUnsecureController.class);

	@Autowired
	private FileResourceService fileResourceService;


    @GetMapping("{id}")
    @IOperation(access = Access.allow, Name = "downloadFile", Description = "downloads file by its fileResource ID")
    public ResponseEntity<Resource> download(@Parameter(description = "id of the FileResource Object to Download")
            @RequestHeader(value = "offset",defaultValue = "0")  long offset,
											 @RequestHeader(value = "size",defaultValue = "0")  long size,
											 @PathVariable("id") String id, HttpServletRequest req) {
        return fileResourceService.download(offset, size, id, req.getRemoteAddr(), null);

    }
}
