package com.wizzdi.flexicore.security.rest;

import com.flexicore.annotations.IOperation;
import com.flexicore.annotations.OperationsInside;
import com.flexicore.model.Baselink;
import com.flexicore.security.SecurityContextBase;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.security.request.BaselinkCreate;
import com.wizzdi.flexicore.security.request.BaselinkFilter;
import com.wizzdi.flexicore.security.request.BaselinkUpdate;
import com.wizzdi.flexicore.security.response.PaginationResponse;
import com.wizzdi.flexicore.security.service.BaselinkService;
import com.wizzdi.flexicore.security.validation.Create;
import com.wizzdi.flexicore.security.validation.Update;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@OperationsInside
@RequestMapping("/baselink")
@Extension
public class BaselinkController implements Plugin {

    @Autowired
    private BaselinkService baselinkService;

    @IOperation(Name = "creates Baselink", Description = "creates Baselink")
    @PostMapping("/create")
    public Baselink create(@RequestBody @Validated(Create.class) BaselinkCreate baselinkCreate, @RequestAttribute SecurityContextBase securityContext) {

        return baselinkService.createBaselink(baselinkCreate, securityContext);
    }

    @IOperation(Name = "returns Baselink", Description = "returns Baselink")
    @PostMapping("/getAll")
    public PaginationResponse<Baselink> getAll(@RequestBody @Valid BaselinkFilter baselinkFilter, @RequestAttribute SecurityContextBase securityContext) {

        return baselinkService.getAllBaselinks(baselinkFilter, securityContext);
    }

    @IOperation(Name = "updates Baselink", Description = "updates Baselink")
    @PutMapping("/update")
    public Baselink update(@RequestBody @Validated(Update.class) BaselinkUpdate baselinkUpdate, @RequestAttribute SecurityContextBase securityContext) {

        return baselinkService.updateBaselink(baselinkUpdate, securityContext);
    }
}
