package com.wizzdi.security.bearer.jwt;

import com.flexicore.annotations.IOperation;
import com.flexicore.annotations.OperationsInside;
import com.flexicore.model.Role;
import com.flexicore.security.SecurityContextBase;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.security.response.PaginationResponse;
import org.pf4j.Extension;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@OperationsInside
@RequestMapping("/test")
@Extension
public class TestController implements Plugin {


    @IOperation(Name = "returns Test", Description = "returns Test")
    @GetMapping("/getAll")
    public PaginationResponse<Role> getAll(@RequestAttribute SecurityContextBase securityContext) {
        return new PaginationResponse<>();
    }

}
