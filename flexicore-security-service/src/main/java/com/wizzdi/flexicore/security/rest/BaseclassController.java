package com.wizzdi.flexicore.security.rest;

import com.flexicore.annotations.IOperation;
import com.flexicore.annotations.OperationsInside;
import com.flexicore.model.Baseclass;
import com.flexicore.model.Clazz;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.security.configuration.SecurityContext;
import com.wizzdi.flexicore.security.request.BaseclassFilter;
import com.wizzdi.flexicore.security.response.PaginationResponse;
import com.wizzdi.flexicore.security.service.BaseclassService;
import jakarta.validation.Valid;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@OperationsInside
@RequestMapping("/baseclass")
@Extension
public class BaseclassController implements Plugin {

    @Autowired
    private BaseclassService baseclassService;


    @IOperation(Name = "returns Baseclass", Description = "returns Baseclass")
    @PostMapping("/getAll")
    public PaginationResponse<?> getAll(@RequestBody @Valid BaseclassFilter baseclassFilter, @RequestAttribute SecurityContext securityContext) {
        if(baseclassFilter.getClazzes()!=null){
            Clazz baseclazz=Clazz.ofClass(Baseclass.class);
            baseclassFilter.setClazzes(baseclassFilter.getClazzes().stream().filter(f->!baseclazz.equals(f)).toList());
        }
        return baseclassService.getAllBaseclass(baseclassFilter, securityContext);
    }

}
