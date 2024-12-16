package com.wizzdi.flexicore.security.rest;

import com.flexicore.annotations.IOperation;
import com.flexicore.annotations.OperationsInside;
import com.flexicore.model.OperationToClazz;
import com.wizzdi.flexicore.security.configuration.SecurityContext;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.security.request.OperationToClazzCreate;
import com.wizzdi.flexicore.security.request.OperationToClazzFilter;
import com.wizzdi.flexicore.security.response.PaginationResponse;
import com.wizzdi.flexicore.security.service.OperationToClazzService;
import com.wizzdi.flexicore.security.validation.Create;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@OperationsInside
@RequestMapping("/operationToClazz")
@Extension
public class OperationToClazzController implements Plugin {

    @Autowired
    private OperationToClazzService operationToClazzService;

    @IOperation(Name = "creates OperationToClazz", Description = "creates OperationToClazz")
    @PostMapping("/create")
    public OperationToClazz create(@RequestBody @Validated(Create.class) OperationToClazzCreate operationToClazzCreate, @RequestAttribute SecurityContext securityContext) {

        return operationToClazzService.addOperationToClazz(operationToClazzCreate);
    }

    @IOperation(Name = "returns OperationToClazz", Description = "returns OperationToClazz")
    @PostMapping("/getAll")
    public PaginationResponse<OperationToClazz> getAll(@RequestBody @Valid OperationToClazzFilter operationToClazzFilter, @RequestAttribute SecurityContext securityContext) {

        return operationToClazzService.getAllOperationToClazz(operationToClazzFilter);
    }


}
