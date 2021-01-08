package com.wizzdi.flexicore.security.rest;

import com.flexicore.model.SecurityLink;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.flexicore.security.SecurityContext;
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
@RequestMapping("/securityLink")
@Extension
public class SecurityLinkController implements Plugin {

	@Autowired
	private SecurityLinkService securityLinkService;

	@PostMapping("/create")
	public SecurityLink create(@RequestBody SecurityLinkCreate securityLinkCreate, @RequestAttribute SecurityContext securityContext){
		securityLinkService.validate(securityLinkCreate,securityContext);
		return securityLinkService.createSecurityLink(securityLinkCreate,securityContext);
	}

	@PostMapping("/getAll")
	public PaginationResponse<SecurityLink> getAll(@RequestBody SecurityLinkFilter securityLinkFilter, @RequestAttribute SecurityContext securityContext){
		securityLinkService.validate(securityLinkFilter,securityContext);
		return securityLinkService.getAllSecurityLinks(securityLinkFilter,securityContext);
	}

	@PutMapping("/update")
	public SecurityLink update(@RequestBody SecurityLinkUpdate securityLinkUpdate, @RequestAttribute SecurityContext securityContext){
		String id=securityLinkUpdate.getId();
		SecurityLink securityLink=id!=null?securityLinkService.getByIdOrNull(id,SecurityLink.class,securityContext):null;
		if(securityLink==null){
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"no security user with id "+id);
		}
		securityLinkService.validate(securityLinkUpdate,securityContext);
		return securityLinkService.updateSecurityLink(securityLinkUpdate,securityContext);
	}
}
