package com.wizzdi.flexicore.security.service;

import com.flexicore.model.Baseclass;
import com.flexicore.model.Role;
import com.flexicore.model.SecurityTenant;
import com.flexicore.model.security.SecurityPolicy;
import com.flexicore.security.SecurityContextBase;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.security.data.SecurityPolicyRepository;
import com.wizzdi.flexicore.security.request.SecurityPolicyCreate;
import com.wizzdi.flexicore.security.request.SecurityPolicyFilter;
import com.wizzdi.flexicore.security.request.SecurityPolicyUpdate;
import com.wizzdi.flexicore.security.response.PaginationResponse;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

@Extension
@Component
public class SecurityPolicyService implements Plugin {

	@Autowired
	private SecurityPolicyRepository SecurityPolicyRepository;


	public SecurityPolicy createSecurityPolicy(SecurityPolicyCreate securityPolicyCreate, SecurityContextBase securityContext) {
		SecurityPolicy SecurityPolicy = createSecurityPolicyNoMerge(securityPolicyCreate, securityContext);
		SecurityPolicyRepository.merge(SecurityPolicy);
		return SecurityPolicy;
	}

	public void merge(Object o) {
		SecurityPolicyRepository.merge(o);
	}

	public void massMerge(List<Object> list) {
		SecurityPolicyRepository.massMerge(list);
	}

	public <T extends Baseclass> List<T> listByIds(Class<T> c, Set<String> ids, SecurityContextBase securityContext) {
		return SecurityPolicyRepository.listByIds(c, ids, securityContext);
	}

	public SecurityPolicy createSecurityPolicyNoMerge(SecurityPolicyCreate securityPolicyCreate, SecurityContextBase securityContext) {
		SecurityPolicy securityPolicy = new SecurityPolicy();
		securityPolicy.setId(Baseclass.getBase64ID());
		updateSecurityPolicyNoMerge(securityPolicyCreate, securityPolicy);
		SecurityPolicyRepository.merge(securityPolicy);
		return securityPolicy;
	}

	public boolean updateSecurityPolicyNoMerge(SecurityPolicyCreate securityPolicyCreate, SecurityPolicy securityPolicy) {
		boolean update = false;
		if (securityPolicyCreate.getName() != null && !securityPolicyCreate.getName().equals(securityPolicy.getName())) {
			securityPolicy.setName(securityPolicyCreate.getName());
			update = true;
		}

		if (securityPolicyCreate.getDescription() != null && !securityPolicyCreate.getDescription().equals(securityPolicy.getDescription())) {
			securityPolicy.setDescription(securityPolicyCreate.getDescription());
			update = true;
		}

		if (securityPolicyCreate.getStartTime() != null && !securityPolicyCreate.getStartTime().equals(securityPolicy.getStartTime())) {
			securityPolicy.setStartTime(securityPolicyCreate.getStartTime());
			update = true;
		}

		if (securityPolicyCreate.getEnabled() != null && !securityPolicyCreate.getEnabled().equals(securityPolicy.isEnabled())) {
			securityPolicy.setEnabled(securityPolicyCreate.getEnabled());
			update = true;
		}

		if (securityPolicyCreate.getPolicyRole() != null && (securityPolicy.getPolicyRole() == null || !securityPolicyCreate.getPolicyRole().getId().equals(securityPolicy.getPolicyRole().getId()))) {
			securityPolicy.setPolicyRole(securityPolicyCreate.getPolicyRole());
			update = true;
		}

		if (securityPolicyCreate.getPolicyTenant() != null && (securityPolicy.getPolicyTenant() == null || !securityPolicyCreate.getPolicyTenant().getId().equals(securityPolicy.getPolicyTenant().getId()))) {
			securityPolicy.setPolicyTenant(securityPolicyCreate.getPolicyTenant());
			update = true;
		}

		return update;
	}

	public SecurityPolicy updateSecurityPolicy(SecurityPolicyUpdate securityPolicyUpdate, SecurityContextBase securityContext) {
		SecurityPolicy SecurityPolicy = securityPolicyUpdate.getSecurityPolicy();
		if (updateSecurityPolicyNoMerge(securityPolicyUpdate, SecurityPolicy)) {
			SecurityPolicyRepository.merge(SecurityPolicy);
		}
		return SecurityPolicy;
	}

	public void validate(SecurityPolicyCreate securityPolicyCreate, SecurityContextBase securityContext) {
		String policyRoleId=securityPolicyCreate.getPolicyRoleId();
		Role role=policyRoleId!=null?getByIdOrNull(policyRoleId,Role.class,securityContext):null;
		if(policyRoleId!=null&&role==null){
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"no role with id "+policyRoleId);
		}
		securityPolicyCreate.setPolicyRole(role);

		String policyTenantId=securityPolicyCreate.getPolicyTenantId();
		SecurityTenant securityTenant=policyTenantId!=null?getByIdOrNull(policyTenantId,SecurityTenant.class,securityContext):null;
		if(policyTenantId!=null&&securityTenant==null){
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"no SecurityTenant with id "+policyTenantId);
		}
		securityPolicyCreate.setPolicyTenant(securityTenant);

		String securityId=securityPolicyCreate.getSecurityId();
		Baseclass baseclass=securityId!=null?getByIdOrNull(securityId,Baseclass.class,securityContext):null;
		if(securityId!=null&&baseclass==null){
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"no Baseclass with id "+securityId);
		}
		securityPolicyCreate.setSecurity(baseclass);
	}

	public void validate(SecurityPolicyFilter securityPolicyFilter, SecurityContextBase securityContext) {
		Set<String> securityTenantsIds=securityPolicyFilter.getSecurityTenantsIds();
		Map<String,SecurityTenant> securityTenantMap=securityTenantsIds.isEmpty()?new HashMap<>():listByIds(SecurityTenant.class,securityTenantsIds,securityContext).stream().collect(Collectors.toMap(f->f.getId(),f->f));
		securityTenantsIds.removeAll(securityTenantMap.keySet());
		if(!securityTenantsIds.isEmpty()){
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"no security tenants with ids "+securityTenantsIds);
		}
		securityPolicyFilter.setSecurityTenants(new ArrayList<>(securityTenantMap.values()));

		Set<String> rolesIds=securityPolicyFilter.getRolesIds();
		Map<String,Role> roleMap=rolesIds.isEmpty()?new HashMap<>():listByIds(Role.class,rolesIds,securityContext).stream().collect(Collectors.toMap(f->f.getId(),f->f));
		rolesIds.removeAll(roleMap.keySet());
		if(!rolesIds.isEmpty()){
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"no Roles with ids "+rolesIds);
		}
		securityPolicyFilter.setRoles(new ArrayList<>(roleMap.values()));



	}

	public <T extends Baseclass> T getByIdOrNull(String id, Class<T> c, SecurityContextBase securityContext) {
		return SecurityPolicyRepository.getByIdOrNull(id, c, securityContext);
	}

	public <T extends SecurityPolicy> T getSecurityPolicyByIdOrNull(String id, Class<T> c, SecurityContextBase securityContext) {
		return SecurityPolicyRepository.getSecurityPolicyByIdOrNull(id, c, securityContext);
	}

	public PaginationResponse<SecurityPolicy> getAllSecurityPolicies(SecurityPolicyFilter SecurityPolicyFilter, SecurityContextBase securityContext) {
		List<SecurityPolicy> list = listAllSecurityPolicies(SecurityPolicyFilter, securityContext);
		long count = SecurityPolicyRepository.countAllSecurityPolicies(SecurityPolicyFilter, securityContext);
		return new PaginationResponse<>(list, SecurityPolicyFilter, count);
	}

	public List<SecurityPolicy> listAllSecurityPolicies(SecurityPolicyFilter SecurityPolicyFilter, SecurityContextBase securityContext) {
		return SecurityPolicyRepository.listAllSecurityPolicies(SecurityPolicyFilter, securityContext);
	}

	public <T extends Baseclass> List<T> findByIds(Class<T> c, Set<String> requested) {
		return SecurityPolicyRepository.findByIds(c, requested);
	}
}
