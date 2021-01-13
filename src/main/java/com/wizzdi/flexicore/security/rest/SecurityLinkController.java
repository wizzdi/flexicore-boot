package com.wizzdi.flexicore.security.rest;

import com.flexicore.annotations.IOperation;
import com.flexicore.annotations.OperationsInside;
import com.flexicore.model.SecurityLink;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.flexicore.security.SecurityContextBase;
import com.wizzdi.flexicore.security.request.SecurityLinkCreate;
import com.wizzdi.flexicore.security.request.SecurityLinkFilter;
import com.wizzdi.flexicore.security.request.SecurityLinkUpdate;
import com.wizzdi.flexicore.security.response.PaginationResponse;
import com.wizzdi.flexicore.security.service.SecurityLinkService;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@OperationsInside
@RequestMapping("/securityLink")
@Extension
public class SecurityLinkController implements Plugin {

	@Autowired
	private SecurityLinkService securityLinkService;

	@IOperation(Name = "creates SecurityLink",Description = "creates SecurityLink")
	@PostMapping("/create")
	public SecurityLink create(@RequestBody SecurityLinkCreate securityLinkCreate, @RequestAttribute SecurityContextBase securityContext){
		securityLinkService.validate(securityLinkCreate,securityContext);
		return securityLinkService.createSecurityLink(securityLinkCreate,securityContext);
	}

	@IOperation(Name = "returns SecurityLink",Description = "returns SecurityLink")
	@PostMapping("/getAll")
	public PaginationResponse<SecurityLink> getAll(@RequestBody SecurityLinkFilter securityLinkFilter, @RequestAttribute SecurityContextBase securityContext){
		securityLinkService.validate(securityLinkFilter,securityContext);
		return securityLinkService.getAllSecurityLinks(securityLinkFilter,securityContext);
	}

	@IOperation(Name = "updates SecurityLink",Description = "updates SecurityLink")
	@PutMapping("/update")
	public SecurityLink update(@RequestBody SecurityLinkUpdate securityLinkUpdate, @RequestAttribute SecurityContextBase securityContext){
		String id=securityLinkUpdate.getId();
		SecurityLink securityLink=id!=null?securityLinkService.getByIdOrNull(id,SecurityLink.class,securityContext):null;
		if(securityLink==null){
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"no security user with id "+id);
		}
		securityLinkUpdate.setSecurityLink(securityLink);
		securityLinkService.validate(securityLinkUpdate,securityContext);
		return securityLinkService.updateSecurityLink(securityLinkUpdate,securityContext);
	}
}
