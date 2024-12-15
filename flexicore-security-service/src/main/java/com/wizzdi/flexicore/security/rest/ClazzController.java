package com.wizzdi.flexicore.security.rest;

import com.flexicore.annotations.IOperation;
import com.flexicore.annotations.OperationsInside;
import com.flexicore.model.Clazz;
import com.wizzdi.segmantix.model.SecurityContext;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.security.request.ClazzFilter;
import com.wizzdi.flexicore.security.response.PaginationResponse;
import com.wizzdi.flexicore.security.service.ClazzService;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@OperationsInside
@RequestMapping("/clazz")
@Extension
public class ClazzController implements Plugin {

    @Autowired
    private ClazzService ClazzService;


    @IOperation(Name = "returns Clazz", Description = "returns Clazz")
    @PostMapping("/getAll")
    public PaginationResponse<Clazz> getAll(@RequestBody @Valid ClazzFilter ClazzFilter, @RequestAttribute SecurityContext securityContext) {

        return ClazzService.getAllClazzs(ClazzFilter, securityContext);
    }
}
