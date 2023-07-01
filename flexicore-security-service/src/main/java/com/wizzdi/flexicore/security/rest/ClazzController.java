package com.wizzdi.flexicore.security.rest;

import com.flexicore.annotations.IOperation;
import com.flexicore.annotations.OperationsInside;
import com.flexicore.model.Clazz;
import com.flexicore.security.SecurityContextBase;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.security.request.ClazzCreate;
import com.wizzdi.flexicore.security.request.ClazzFilter;
import com.wizzdi.flexicore.security.request.ClazzUpdate;
import com.wizzdi.flexicore.security.response.PaginationResponse;
import com.wizzdi.flexicore.security.service.ClazzService;
import com.wizzdi.flexicore.security.validation.Create;
import com.wizzdi.flexicore.security.validation.Update;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@OperationsInside
@RequestMapping("/clazz")
@Extension
public class ClazzController implements Plugin {

    @Autowired
    private ClazzService ClazzService;

    @IOperation(Name = "creates Clazz", Description = "creates Clazz")
    @PostMapping("/create")
    public Clazz create(@RequestBody @Validated(Create.class) ClazzCreate ClazzCreate, @RequestAttribute SecurityContextBase securityContext) {

        return ClazzService.createClazz(ClazzCreate, securityContext);
    }

    @IOperation(Name = "returns Clazz", Description = "returns Clazz")
    @PostMapping("/getAll")
    public PaginationResponse<Clazz> getAll(@RequestBody @Valid ClazzFilter ClazzFilter, @RequestAttribute SecurityContextBase securityContext) {

        return ClazzService.getAllClazzs(ClazzFilter, securityContext);
    }

    @IOperation(Name = "updates Clazz", Description = "updates Clazz")
    @PutMapping("/update")
    public Clazz update(@RequestBody @Validated(Update.class) ClazzUpdate clazzUpdate, @RequestAttribute SecurityContextBase securityContext) {

        return ClazzService.updateClazz(clazzUpdate, securityContext);
    }
}
