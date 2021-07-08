package com.wizzdi.flexicore.security.rest;

import com.flexicore.annotations.IOperation;
import com.flexicore.annotations.OperationsInside;
import com.flexicore.security.SecurityContextBase;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.security.request.DeleteObjectsRequest;
import com.wizzdi.flexicore.security.response.DeleteResponse;
import com.wizzdi.flexicore.security.service.GenericDeleteService;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@OperationsInside
@RequestMapping("/generic")
@Extension
public class GenericDeleteController implements Plugin {

	@Autowired
	private GenericDeleteService genericDeleteService;

	@IOperation(Name = "soft deletes Objects",Description = "soft deletes Objects")
	@DeleteMapping("/softDelete")
	public DeleteResponse softDelete(@RequestHeader("authenticationKey") String authenticationKey, @RequestBody DeleteObjectsRequest deleteObjectsRequest, @RequestAttribute SecurityContextBase securityContext){
		genericDeleteService.validate(deleteObjectsRequest,securityContext);
		return genericDeleteService.softDelete(deleteObjectsRequest,securityContext);
	}

}
