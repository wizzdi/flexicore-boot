package com.wizzdi.flexicore.security.rest;

import com.flexicore.annotations.IOperation;
import com.flexicore.annotations.OperationsInside;
import com.flexicore.model.OperationToGroup;
import com.wizzdi.segmantix.model.SecurityContext;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.security.request.OperationToGroupCreate;
import com.wizzdi.flexicore.security.request.OperationToGroupFilter;
import com.wizzdi.flexicore.security.request.OperationToGroupUpdate;
import com.wizzdi.flexicore.security.response.PaginationResponse;
import com.wizzdi.flexicore.security.service.OperationToGroupService;
import com.wizzdi.flexicore.security.validation.Create;
import com.wizzdi.flexicore.security.validation.Update;
import jakarta.validation.Valid;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@OperationsInside
@RequestMapping("/operationToGroup")
@Extension
public class OperationToGroupController implements Plugin {

    @Autowired
    private OperationToGroupService operationService;

    @IOperation(Name = "creates security operation group", Description = "creates security operation group")
    @PostMapping("/create")
    public OperationToGroup create(@RequestBody @Validated(Create.class) OperationToGroupCreate operationToGroupCreate, @RequestAttribute SecurityContext securityContext) {

        return operationService.createOperationToGroup(operationToGroupCreate, securityContext);
    }

    @IOperation(Name = "returns security operation group", Description = "returns security operation group")
    @PostMapping("/getAll")
    public PaginationResponse<OperationToGroup> getAll(@RequestBody @Valid OperationToGroupFilter operationToGroupFilter, @RequestAttribute SecurityContext securityContext) {

        return operationService.getAllOperationToGroups(operationToGroupFilter, securityContext);
    }

    @IOperation(Name = "updates security operation group", Description = "updates security operation group")
    @PutMapping("/update")
    public OperationToGroup update(@RequestBody @Validated(Update.class) OperationToGroupUpdate operationToGroupUpdate, @RequestAttribute SecurityContext securityContext) {

        return operationService.updateOperationToGroup(operationToGroupUpdate, securityContext);
    }
}
