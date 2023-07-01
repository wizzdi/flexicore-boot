package com.flexicore.service;

import com.flexicore.data.TotpSecurityPolicyRepository;
import com.flexicore.model.Baseclass;
import com.flexicore.model.security.TotpSecurityPolicy;
import com.flexicore.request.TotpSecurityPolicyCreate;
import com.flexicore.request.TotpSecurityPolicyFilter;
import com.flexicore.request.TotpSecurityPolicyUpdate;
import com.flexicore.security.SecurityContextBase;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.security.response.PaginationResponse;
import com.wizzdi.flexicore.security.service.SecurityPolicyService;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Extension
@Component
public class TotpSecurityPolicyService implements Plugin {



	@Autowired
	private TotpSecurityPolicyRepository totpSecurityPolicyRepository;
	@Autowired
	private SecurityPolicyService securityPolicyService;


	public TotpSecurityPolicy createSecurityPolicy(TotpSecurityPolicyCreate securityPolicyCreate, SecurityContextBase securityContext) {
		TotpSecurityPolicy totpSecurityPolicy = createSecurityPolicyNoMerge(securityPolicyCreate, securityContext);
		Baseclass baseclass=new Baseclass(totpSecurityPolicy.getName(),securityContext);
		totpSecurityPolicy.setSecurity(baseclass);
		totpSecurityPolicyRepository.massMerge(Arrays.asList(totpSecurityPolicy,baseclass));
		return totpSecurityPolicy;
	}

	public void merge(Object o) {
		totpSecurityPolicyRepository.merge(o);
	}

	public void massMerge(List<Object> list) {
		totpSecurityPolicyRepository.massMerge(list);
	}

	public <T extends Baseclass> List<T> listByIds(Class<T> c, Set<String> ids, SecurityContextBase securityContext) {
		return totpSecurityPolicyRepository.listByIds(c, ids, securityContext);
	}

	public TotpSecurityPolicy createSecurityPolicyNoMerge(TotpSecurityPolicyCreate securityPolicyCreate, SecurityContextBase securityContext) {
		TotpSecurityPolicy securityPolicy = new TotpSecurityPolicy();
		securityPolicy.setId(Baseclass.getBase64ID());
		updateSecurityPolicyNoMerge(securityPolicyCreate, securityPolicy);
		return securityPolicy;
	}

	public boolean updateSecurityPolicyNoMerge(TotpSecurityPolicyCreate securityPolicyCreate, TotpSecurityPolicy securityPolicy) {
		boolean update = securityPolicyService.updateSecurityPolicyNoMerge(securityPolicyCreate, securityPolicy);
		if(securityPolicyCreate.getForceTotp()!=null&&!securityPolicyCreate.getForceTotp().equals(securityPolicy.isForceTotp())){
			securityPolicy.setForceTotp(securityPolicyCreate.getForceTotp());
			update=true;
		}
		if(securityPolicyCreate.getAllowedConfigureOffsetMs()!=null&&!securityPolicyCreate.getAllowedConfigureOffsetMs().equals(securityPolicy.getAllowedConfigureOffsetMs())){
			securityPolicy.setAllowedConfigureOffsetMs(securityPolicyCreate.getAllowedConfigureOffsetMs());
			update=true;
		}
		return update;
	}

	public TotpSecurityPolicy updateSecurityPolicy(TotpSecurityPolicyUpdate securityPolicyUpdate, SecurityContextBase securityContext) {
		TotpSecurityPolicy TotpSecurityPolicy = securityPolicyUpdate.getTotpSecurityPolicy();
		if (updateSecurityPolicyNoMerge(securityPolicyUpdate, TotpSecurityPolicy)) {
			totpSecurityPolicyRepository.merge(TotpSecurityPolicy);
		}
		return TotpSecurityPolicy;
	}

	public void validate(TotpSecurityPolicyCreate securityPolicyCreate, SecurityContextBase securityContext) {
		securityPolicyService.validate(securityPolicyCreate,securityContext);
	}

	public void validate(TotpSecurityPolicyFilter securityPolicyFilter, SecurityContextBase securityContext) {
		securityPolicyService.validate(securityPolicyFilter, securityContext);


	}

	public <T extends Baseclass> T getByIdOrNull(String id, Class<T> c, SecurityContextBase securityContext) {
		return totpSecurityPolicyRepository.getByIdOrNull(id, c, securityContext);
	}

	public <T extends TotpSecurityPolicy> T getSecurityPolicyByIdOrNull(String id, Class<T> c, SecurityContextBase securityContext) {
		return totpSecurityPolicyRepository.getSecurityPolicyByIdOrNull(id, c, securityContext);
	}

	public PaginationResponse<TotpSecurityPolicy> getAllSecurityPolicies(TotpSecurityPolicyFilter TotpSecurityPolicyFilter, SecurityContextBase securityContext) {
		List<TotpSecurityPolicy> list = listAllSecurityPolicies(TotpSecurityPolicyFilter, securityContext);
		long count = totpSecurityPolicyRepository.countAllSecurityPolicies(TotpSecurityPolicyFilter, securityContext);
		return new PaginationResponse<>(list, TotpSecurityPolicyFilter, count);
	}

	public List<TotpSecurityPolicy> listAllSecurityPolicies(TotpSecurityPolicyFilter TotpSecurityPolicyFilter, SecurityContextBase securityContext) {
		return totpSecurityPolicyRepository.listAllSecurityPolicies(TotpSecurityPolicyFilter, securityContext);
	}

	public <T extends Baseclass> List<T> findByIds(Class<T> c, Set<String> requested) {
		return totpSecurityPolicyRepository.findByIds(c, requested);
	}

}
