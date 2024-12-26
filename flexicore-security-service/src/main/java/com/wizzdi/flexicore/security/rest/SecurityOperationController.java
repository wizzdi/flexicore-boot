package com.wizzdi.flexicore.security.rest;

import com.flexicore.annotations.IOperation;
import com.flexicore.annotations.OperationsInside;
import com.flexicore.model.OperationGroup;
import com.flexicore.model.SecurityOperation;
import com.wizzdi.flexicore.security.configuration.SecurityContext;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.security.request.BasicPropertiesFilter;
import com.wizzdi.flexicore.security.request.OperationToGroupFilter;
import com.wizzdi.flexicore.security.request.SecurityOperationFilter;
import com.wizzdi.flexicore.security.response.PaginationResponse;
import com.wizzdi.flexicore.security.service.OperationToGroupService;
import com.wizzdi.flexicore.security.service.SecurityOperationService;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@OperationsInside
@RequestMapping("/securityOperation")
@Extension
public class SecurityOperationController implements Plugin {

    @Autowired
    private SecurityOperationService operationService;
    @Autowired
    private OperationToGroupService operationToGroupService;

    @IOperation(Name = "returns security operation", Description = "returns security operation")
    @PostMapping("/getAll")
    public PaginationResponse<SecurityOperation> getAll(@RequestBody @Valid SecurityOperationFilter operationFilter, @RequestAttribute SecurityContext securityContext) {

        List<OperationGroup> operationGroups = operationFilter.getOperationGroups();
        if(operationGroups !=null&&!operationGroups.isEmpty()){
            BasicPropertiesFilter basicPropertiesFilter= Optional.ofNullable(operationFilter.getBasicPropertiesFilter()).orElseGet(()->new BasicPropertiesFilter());
            Set<String> onlyIds=Optional.of(basicPropertiesFilter).map(f->f.getOnlyIds()).orElseGet(()->new HashSet<>());
           onlyIds.addAll(operationToGroupService.listAllOperationToGroups(new OperationToGroupFilter().setOperationGroups(operationGroups),null).stream().map(f->f.getOperationId()).collect(Collectors.toSet()));
           basicPropertiesFilter.setOnlyIds(onlyIds);
           operationFilter.setBasicPropertiesFilter(basicPropertiesFilter);
        }
        return operationService.getAllOperations(operationFilter);
    }

}
