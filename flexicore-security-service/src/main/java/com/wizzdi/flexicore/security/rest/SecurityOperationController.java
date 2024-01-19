package com.wizzdi.flexicore.security.rest;

import com.flexicore.annotations.IOperation;
import com.flexicore.annotations.OperationsInside;
import com.flexicore.model.SecurityOperation;
import com.flexicore.security.SecurityContextBase;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.security.request.SecurityOperationCreate;
import com.wizzdi.flexicore.security.request.SecurityOperationFilter;
import com.wizzdi.flexicore.security.request.SecurityOperationUpdate;
import com.wizzdi.flexicore.security.response.PaginationResponse;
import com.wizzdi.flexicore.security.service.SecurityOperationService;
import com.wizzdi.flexicore.security.validation.Create;
import com.wizzdi.flexicore.security.validation.Update;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@OperationsInside
@RequestMapping("/securityOperation")
@Extension
public class SecurityOperationController implements Plugin {

    @Autowired
    private SecurityOperationService operationService;

    @IOperation(Name = "returns security operation", Description = "returns security operation")
    @PostMapping("/getAll")
    public PaginationResponse<SecurityOperation> getAll(@RequestBody @Valid SecurityOperationFilter operationFilter, @RequestAttribute SecurityContextBase securityContext) {

        return operationService.getAllOperations(operationFilter, securityContext);
    }

}
