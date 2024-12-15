package com.wizzdi.flexicore.security.rest;

import com.flexicore.annotations.IOperation;
import com.flexicore.annotations.OperationsInside;
import com.flexicore.model.SecurityLink;
import com.wizzdi.segmantix.model.SecurityContext;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.security.request.SecurityLinkFilter;
import com.wizzdi.flexicore.security.request.SecurityLinkUpdate;
import com.wizzdi.flexicore.security.response.PaginationResponse;
import com.wizzdi.flexicore.security.service.SecurityLinkService;
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


    @IOperation(Name = "returns SecurityLink", Description = "returns SecurityLink")
    @PostMapping("/getAll")
    public PaginationResponse<SecurityLink> getAll(@RequestBody @Valid SecurityLinkFilter securityLinkFilter, @RequestAttribute SecurityContext securityContext) {
        securityLinkService.setRelevant(securityLinkFilter,securityContext);
        return securityLinkService.getAllSecurityLinks(securityLinkFilter, securityContext);
    }

    @IOperation(Name = "updates SecurityLink", Description = "updates SecurityLink")
    @PutMapping("/update")
    public SecurityLink update(@RequestBody @Validated(Update.class) SecurityLinkUpdate securityLinkUpdate, @RequestAttribute SecurityContext securityContext) {

        return securityLinkService.updateSecurityLink(securityLinkUpdate, securityContext);
    }
}
