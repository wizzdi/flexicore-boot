package com.wizzdi.flexicore.security.rest;

import com.flexicore.annotations.IOperation;
import com.flexicore.annotations.OperationsInside;
import com.flexicore.model.SecurityLinkGroup;
import com.wizzdi.segmantix.model.SecurityContext;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.security.request.*;
import com.wizzdi.flexicore.security.response.PaginationResponse;
import com.wizzdi.flexicore.security.response.SecurityLinkGroupContainer;
import com.wizzdi.flexicore.security.service.SecurityLinkGroupService;
import com.wizzdi.flexicore.security.validation.Create;
import com.wizzdi.flexicore.security.validation.Update;
import jakarta.validation.Valid;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@OperationsInside
@RequestMapping("/securityLinkGroup")
@Extension
public class SecurityLinkGroupController implements Plugin {

    @Autowired
    private SecurityLinkGroupService securityLinkGroupService;


    @IOperation(Name = "returns SecurityLinkGroup", Description = "returns SecurityLinkGroup")
    @PostMapping("/getAll")
    public PaginationResponse<SecurityLinkGroup> getAll(@RequestBody @Valid SecurityLinkGroupFilter securityLinkGroupFilter, @RequestAttribute SecurityContext securityContext) {
        return securityLinkGroupService.getAllSecurityLinkGroups(securityLinkGroupFilter, securityContext);
    }

    @IOperation(Name = "returns SecurityLinkGroupContainers", Description = "returns SecurityLinkGroupContainers")
    @PostMapping("/getAllContainers")
    public PaginationResponse<SecurityLinkGroupContainer> getAllContainers(@RequestBody @Valid SecurityLinkGroupFilter securityLinkGroupFilter, @RequestAttribute SecurityContext securityContext) {
        return securityLinkGroupService.getAllSecurityLinkGroupContainers(securityLinkGroupFilter, securityContext);
    }

    @IOperation(Name = "updates SecurityLinkGroup", Description = "updates SecurityLinkGroup")
    @PutMapping("/update")
    public SecurityLinkGroup update(@RequestBody @Validated(Update.class) SecurityLinkGroupUpdate securityLinkGroupUpdate, @RequestAttribute SecurityContext securityContext) {

        return securityLinkGroupService.updateSecurityLinkGroup(securityLinkGroupUpdate, securityContext);
    }

    @IOperation(Name = "creates SecurityLinkGroup", Description = "creates SecurityLinkGroup")
    @PostMapping("/create")
    public SecurityLinkGroup create(@RequestBody @Validated(Create.class) SecurityLinkGroupCreate securityLinkGroupCreate, @RequestAttribute SecurityContext securityContext) {

        return securityLinkGroupService.createSecurityLinkGroup(securityLinkGroupCreate, securityContext);
    }


}
