/*******************************************************************************
 *  Copyright (C) FlexiCore, Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Avishay Ben Natan And Asaf Ben Natan, October 2015
 ******************************************************************************/
package com.wizzdi.flexicore.file.controllers;


import com.flexicore.annotations.OperationsInside;
import com.wizzdi.segmantix.model.SecurityContext;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.file.model.ZipFile;
import com.wizzdi.flexicore.file.request.ZipAndDownloadRequest;
import com.wizzdi.flexicore.file.request.ZipFileFilter;
import com.wizzdi.flexicore.file.service.FileResourceService;
import com.wizzdi.flexicore.file.service.ZipFileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.pf4j.Extension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/download")
@Extension
@Tag(name = "Download")
@OperationsInside
public class ZipFileController implements Plugin {

    private static final Logger logger = LoggerFactory.getLogger(ZipFileController.class);

    @Autowired
    private FileResourceService fileResourceService;

    @Autowired
    private ZipFileService zipFileService;



    /**
     * zips list of fileResources and sends it
     *
     * @param zipAndDownload zip and download request
     * @param securityContext security context
     * @return binary zip data
     */
    @PostMapping("zipAndDownload")
    @Operation(summary = "zipAndDownload", description = "Mass Download")
    public ResponseEntity<Resource> zipAndDownload(@RequestBody ZipAndDownloadRequest zipAndDownload, @RequestAttribute SecurityContext securityContext) {
        zipFileService.validate(zipAndDownload, securityContext);
        ZipFile zipFile = zipFileService.zipAndDownload(zipAndDownload, securityContext);

        return fileResourceService.prepareFileResourceForDownload(zipFile, zipAndDownload.getOffset(), 0);


    }

    @PostMapping("getOrCreateZipFile")
    @Operation(summary = "getOrCreateZipFile", description = "getOrCreateZipFile")
    public ZipFile getOrCreateZipFile(@RequestBody   ZipAndDownloadRequest zipAndDownload, @RequestAttribute SecurityContext securityContext) {
        zipFileService.validate(zipAndDownload, securityContext);
        return zipFileService.zipAndDownload(zipAndDownload, securityContext);
    }


    @PostMapping("calculateZipsMd5")
    @Operation(summary = "calculateZipsMd5", description = "calculateZipsMd5")
    public void calculateZipsMd5(@RequestBody ZipFileFilter zipFileFilter, @RequestAttribute SecurityContext securityContext) {
        zipFileService.validate(zipFileFilter, securityContext);
        zipFileService.calculateZipsMd5(zipFileFilter, securityContext);
    }






}
