package com.wizzdi.flexicore.security.rest;

import com.flexicore.annotations.IOperation;
import com.flexicore.annotations.OperationsInside;
import com.flexicore.model.OperationGroup;
import com.wizzdi.segmantix.model.SecurityContext;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.security.request.OperationGroupCreate;
import com.wizzdi.flexicore.security.request.OperationGroupFilter;
import com.wizzdi.flexicore.security.request.OperationGroupUpdate;
import com.wizzdi.flexicore.security.response.PaginationResponse;
import com.wizzdi.flexicore.security.service.OperationGroupService;
import com.wizzdi.flexicore.security.validation.Create;
import com.wizzdi.flexicore.security.validation.Update;
import jakarta.validation.Valid;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@OperationsInside
@RequestMapping("/operationGroup")
@Extension
public class OperationGroupController implements Plugin {

    @Autowired
    private OperationGroupService operationService;

    @IOperation(Name = "creates security operation group", Description = "creates security operation group")
    @PostMapping("/create")
    public OperationGroup create(@RequestBody @Validated(Create.class) OperationGroupCreate operationCreate, @RequestAttribute SecurityContext securityContext) {

        return operationService.createOperationGroup(operationCreate, securityContext);
    }

    @IOperation(Name = "returns security operation group", Description = "returns security operation group")
    @PostMapping("/getAll")
    public PaginationResponse<OperationGroup> getAll(@RequestBody @Valid OperationGroupFilter operationFilter, @RequestAttribute SecurityContext securityContext) {

        return operationService.getAllOperationGroups(operationFilter, securityContext);
    }

    @IOperation(Name = "updates security operation group", Description = "updates security operation group")
    @PutMapping("/update")
    public OperationGroup update(@RequestBody @Validated(Update.class) OperationGroupUpdate operationUpdate, @RequestAttribute SecurityContext securityContext) {

        return operationService.updateOperationGroup(operationUpdate, securityContext);
    }
}
