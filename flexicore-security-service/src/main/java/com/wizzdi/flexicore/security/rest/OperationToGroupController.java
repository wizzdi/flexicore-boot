package com.wizzdi.flexicore.security.rest;

import com.flexicore.annotations.IOperation;
import com.flexicore.annotations.OperationsInside;
import com.flexicore.model.OperationToGroup;
import com.flexicore.model.SecurityOperation;
import com.wizzdi.flexicore.security.configuration.SecurityContext;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.security.request.BasicPropertiesFilter;
import com.wizzdi.flexicore.security.request.OperationToGroupCreate;
import com.wizzdi.flexicore.security.request.OperationToGroupFilter;
import com.wizzdi.flexicore.security.request.OperationToGroupUpdate;
import com.wizzdi.flexicore.security.request.SecurityOperationFilter;
import com.wizzdi.flexicore.security.response.OperationToGroupContainer;
import com.wizzdi.flexicore.security.response.PaginationResponse;
import com.wizzdi.flexicore.security.service.OperationToGroupService;
import com.wizzdi.flexicore.security.service.SecurityOperationService;
import com.wizzdi.flexicore.security.validation.Create;
import com.wizzdi.flexicore.security.validation.Update;
import jakarta.validation.Valid;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Set;

@RestController
@OperationsInside
@RequestMapping("/operationToGroup")
@Extension
public class OperationToGroupController implements Plugin {

    @Autowired
    private OperationToGroupService operationService;
    @Autowired
    private SecurityOperationService securityOperationService;

    @IOperation(Name = "creates security operation group", Description = "creates security operation group")
    @PostMapping("/create")
    public OperationToGroup create(@RequestBody @Validated(Create.class) OperationToGroupCreate operationToGroupCreate, @RequestAttribute SecurityContext securityContext) {
        String operationId = operationToGroupCreate.getOperationId();
        SecurityOperation securityOperation = securityOperationService.listAllOperations(new SecurityOperationFilter().setBasicPropertiesFilter(new BasicPropertiesFilter().setOnlyIds(Set.of(operationId)))).stream().findFirst().orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "no operation with id %s".formatted(operationId)));
        operationToGroupCreate.setOperation(securityOperation);
        return operationService.createOperationToGroup(operationToGroupCreate, securityContext);
    }

    @IOperation(Name = "returns security operation group", Description = "returns security operation group")
    @PostMapping("/getAll")
    public PaginationResponse<OperationToGroup> getAll(@RequestBody @Valid OperationToGroupFilter operationToGroupFilter, @RequestAttribute SecurityContext securityContext) {

        if(operationService.setOperations(operationToGroupFilter)){
            return new PaginationResponse<>(new ArrayList<>(),operationToGroupFilter.getPageSize(),0L);
        }


        return operationService.getAllOperationToGroups(operationToGroupFilter, securityContext);
    }

    @IOperation(Name = "returns security operation group containers", Description = "returns security operation group containers")
    @PostMapping("/getAllContainers")
    public PaginationResponse<OperationToGroupContainer> getAllContainers(@RequestBody @Valid OperationToGroupFilter operationToGroupFilter, @RequestAttribute SecurityContext securityContext) {

        if(operationService.setOperations(operationToGroupFilter)){
            return new PaginationResponse<>(new ArrayList<>(),operationToGroupFilter.getPageSize(),0L);
        }

        return operationService.getAllOperationToGroupsContainers(operationToGroupFilter, securityContext);
    }

    @IOperation(Name = "updates security operation group", Description = "updates security operation group")
    @PutMapping("/update")
    public OperationToGroup update(@RequestBody @Validated(Update.class) OperationToGroupUpdate operationToGroupUpdate, @RequestAttribute SecurityContext securityContext) {

        if(operationToGroupUpdate.getOperationId()!=null){
            String operationId = operationToGroupUpdate.getOperationId();
            SecurityOperation securityOperation = securityOperationService.listAllOperations(new SecurityOperationFilter().setBasicPropertiesFilter(new BasicPropertiesFilter().setOnlyIds(Set.of(operationId)))).stream().findFirst().orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "no operation with id %s".formatted(operationId)));
            operationToGroupUpdate.setOperation(securityOperation);
        }
        return operationService.updateOperationToGroup(operationToGroupUpdate, securityContext);
    }
}
