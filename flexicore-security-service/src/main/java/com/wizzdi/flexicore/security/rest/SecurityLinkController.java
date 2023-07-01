package com.wizzdi.flexicore.security.rest;

import com.flexicore.annotations.IOperation;
import com.flexicore.annotations.OperationsInside;
import com.flexicore.model.SecurityLink;
import com.flexicore.security.SecurityContextBase;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.security.request.SecurityLinkCreate;
import com.wizzdi.flexicore.security.request.SecurityLinkFilter;
import com.wizzdi.flexicore.security.request.SecurityLinkUpdate;
import com.wizzdi.flexicore.security.response.PaginationResponse;
import com.wizzdi.flexicore.security.service.SecurityLinkService;
import com.wizzdi.flexicore.security.validation.Create;
import com.wizzdi.flexicore.security.validation.Update;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@OperationsInside
@RequestMapping("/securityLink")
@Extension
public class SecurityLinkController implements Plugin {

    @Autowired
    private SecurityLinkService securityLinkService;

    @IOperation(Name = "creates SecurityLink", Description = "creates SecurityLink")
    @PostMapping("/create")
    public SecurityLink create(@RequestBody @Validated(Create.class) SecurityLinkCreate securityLinkCreate, @RequestAttribute SecurityContextBase securityContext) {

        return securityLinkService.createSecurityLink(securityLinkCreate, securityContext);
    }

    @IOperation(Name = "returns SecurityLink", Description = "returns SecurityLink")
    @PostMapping("/getAll")
    public PaginationResponse<SecurityLink> getAll(@RequestBody @Valid SecurityLinkFilter securityLinkFilter, @RequestAttribute SecurityContextBase securityContext) {

        return securityLinkService.getAllSecurityLinks(securityLinkFilter, securityContext);
    }

    @IOperation(Name = "updates SecurityLink", Description = "updates SecurityLink")
    @PutMapping("/update")
    public SecurityLink update(@RequestBody @Validated(Update.class) SecurityLinkUpdate securityLinkUpdate, @RequestAttribute SecurityContextBase securityContext) {

        return securityLinkService.updateSecurityLink(securityLinkUpdate, securityContext);
    }
}
