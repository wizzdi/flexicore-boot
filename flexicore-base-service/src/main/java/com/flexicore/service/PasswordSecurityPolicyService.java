package com.flexicore.service;

import com.flexicore.data.PasswordSecurityPolicyRepository;
import com.flexicore.model.Baseclass;
import com.flexicore.model.Basic;
import com.flexicore.model.Role;
import com.flexicore.model.SecurityTenant;
import com.flexicore.model.security.PasswordSecurityPolicy;
import com.flexicore.request.PasswordSecurityPolicyCreate;
import com.flexicore.request.PasswordSecurityPolicyFilter;
import com.flexicore.request.PasswordSecurityPolicyUpdate;
import com.flexicore.response.PasswordPolicyError;
import com.flexicore.security.SecurityContextBase;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.security.response.PaginationResponse;
import com.wizzdi.flexicore.security.service.SecurityPolicyService;
import jakarta.persistence.metamodel.SingularAttribute;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

@Extension
@Component
public class PasswordSecurityPolicyService implements Plugin {

	private static final Pattern LOWER_CASE = Pattern.compile("(?=.*[a-z])");
	private static final Pattern UPPER_CASE = Pattern.compile("(?=.*[A-Z])");
	private static final Pattern DIGIT = Pattern.compile("(?=.*\\d)");
	private static final Pattern LETTER = Pattern.compile(".*[a-zA-Z].*");


	@Autowired
	private PasswordSecurityPolicyRepository passwordSecurityPolicyRepository;
	@Autowired
	private SecurityPolicyService securityPolicyService;


	public PasswordSecurityPolicy createSecurityPolicy(PasswordSecurityPolicyCreate securityPolicyCreate, SecurityContextBase securityContext) {
		PasswordSecurityPolicy passwordSecurityPolicy = createSecurityPolicyNoMerge(securityPolicyCreate, securityContext);
		Baseclass baseclass=new Baseclass(passwordSecurityPolicy.getName(),securityContext);
		passwordSecurityPolicy.setSecurity(baseclass);
		passwordSecurityPolicyRepository.massMerge(Arrays.asList(passwordSecurityPolicy,baseclass));
		return passwordSecurityPolicy;
	}



	public PasswordSecurityPolicy createSecurityPolicyNoMerge(PasswordSecurityPolicyCreate securityPolicyCreate, SecurityContextBase securityContext) {
		PasswordSecurityPolicy securityPolicy = new PasswordSecurityPolicy();
		securityPolicy.setId(Baseclass.getBase64ID());
		updateSecurityPolicyNoMerge(securityPolicyCreate, securityPolicy);
		return securityPolicy;
	}

	public boolean updateSecurityPolicyNoMerge(PasswordSecurityPolicyCreate securityPolicyCreate, PasswordSecurityPolicy securityPolicy) {
		boolean update = securityPolicyService.updateSecurityPolicyNoMerge(securityPolicyCreate, securityPolicy);
		if(securityPolicyCreate.getForceCapital()!=null&&!securityPolicyCreate.getForceCapital().equals(securityPolicy.isForceCapital())){
			securityPolicy.setForceCapital(securityPolicyCreate.getForceCapital());
			update=true;
		}
		if(securityPolicyCreate.getForceLowerCase()!=null&&!securityPolicyCreate.getForceLowerCase().equals(securityPolicy.isForceLowerCase())){
			securityPolicy.setForceLowerCase(securityPolicyCreate.getForceLowerCase());
			update=true;
		}
		if(securityPolicyCreate.getForceDigits()!=null&&!securityPolicyCreate.getForceDigits().equals(securityPolicy.isForceDigits())){
			securityPolicy.setForceDigits(securityPolicyCreate.getForceDigits());
			update=true;
		}
		if(securityPolicyCreate.getForceLetters()!=null&&!securityPolicyCreate.getForceLetters().equals(securityPolicy.isForceLetters())){
			securityPolicy.setForceLetters(securityPolicyCreate.getForceLetters());
			update=true;
		}
		if(securityPolicyCreate.getMinLength()!=null&&!securityPolicyCreate.getMinLength().equals(securityPolicy.getMinLength())){
			securityPolicy.setMinLength(securityPolicyCreate.getMinLength());
			update=true;
		}
		return update;
	}

	public PasswordSecurityPolicy updateSecurityPolicy(PasswordSecurityPolicyUpdate securityPolicyUpdate, SecurityContextBase securityContext) {
		PasswordSecurityPolicy PasswordSecurityPolicy = securityPolicyUpdate.getPasswordSecurityPolicy();
		if (updateSecurityPolicyNoMerge(securityPolicyUpdate, PasswordSecurityPolicy)) {
			passwordSecurityPolicyRepository.merge(PasswordSecurityPolicy);
		}
		return PasswordSecurityPolicy;
	}

	public void validate(PasswordSecurityPolicyCreate securityPolicyCreate, SecurityContextBase securityContext) {
		securityPolicyService.validate(securityPolicyCreate,securityContext);
	}

	public void validate(PasswordSecurityPolicyFilter securityPolicyFilter, SecurityContextBase securityContext) {
		securityPolicyService.validate(securityPolicyFilter, securityContext);


	}

	public <T extends Baseclass> T getByIdOrNull(String id, Class<T> c, SecurityContextBase securityContext) {
		return passwordSecurityPolicyRepository.getByIdOrNull(id, c, securityContext);
	}

	public <D extends Basic, E extends Baseclass, T extends D> T getByIdOrNull(String id, Class<T> c, SingularAttribute<D, E> baseclassAttribute, SecurityContextBase securityContext) {
		return passwordSecurityPolicyRepository.getByIdOrNull(id, c, baseclassAttribute, securityContext);
	}

	public <D extends Basic, E extends Baseclass, T extends D> List<T> listByIds(Class<T> c, Set<String> ids, SingularAttribute<D, E> baseclassAttribute, SecurityContextBase securityContext) {
		return passwordSecurityPolicyRepository.listByIds(c, ids, baseclassAttribute, securityContext);
	}

	public <D extends Basic, T extends D> List<T> findByIds(Class<T> c, Set<String> ids, SingularAttribute<D, String> idAttribute) {
		return passwordSecurityPolicyRepository.findByIds(c, ids, idAttribute);
	}

	public <T extends Basic> List<T> findByIds(Class<T> c, Set<String> requested) {
		return passwordSecurityPolicyRepository.findByIds(c, requested);
	}

	public <T> T findByIdOrNull(Class<T> type, String id) {
		return passwordSecurityPolicyRepository.findByIdOrNull(type, id);
	}

	@Transactional
	public void merge(Object base) {
		passwordSecurityPolicyRepository.merge(base);
	}

	@Transactional
	public void massMerge(List<?> toMerge) {
		passwordSecurityPolicyRepository.massMerge(toMerge);
	}


	public PaginationResponse<PasswordSecurityPolicy> getAllSecurityPolicies(PasswordSecurityPolicyFilter PasswordSecurityPolicyFilter, SecurityContextBase securityContext) {
		List<PasswordSecurityPolicy> list = listAllSecurityPolicies(PasswordSecurityPolicyFilter, securityContext);
		long count = passwordSecurityPolicyRepository.countAllSecurityPolicies(PasswordSecurityPolicyFilter, securityContext);
		return new PaginationResponse<>(list, PasswordSecurityPolicyFilter, count);
	}

	public List<PasswordSecurityPolicy> listAllSecurityPolicies(PasswordSecurityPolicyFilter PasswordSecurityPolicyFilter, SecurityContextBase securityContext) {
		return passwordSecurityPolicyRepository.listAllSecurityPolicies(PasswordSecurityPolicyFilter, securityContext);
	}


	public List<PasswordPolicyError> enforcePolicy(PasswordSecurityPolicy passwordSecurityPolicy, String password) {
		List<PasswordPolicyError> errors=new ArrayList<>();
		if(password.length() < passwordSecurityPolicy.getMinLength()){
			errors.add(PasswordPolicyError.PASSWORD_TOO_SHORT);
		}

		if(passwordSecurityPolicy.isForceLowerCase()&&!LOWER_CASE.matcher(password).find()){
			errors.add(PasswordPolicyError.NO_LOWER_CASE);
		}
		if(passwordSecurityPolicy.isForceCapital()&&!UPPER_CASE.matcher(password).find()){
			errors.add(PasswordPolicyError.NO_CAPITAL);
		}
		if(passwordSecurityPolicy.isForceDigits()&&!DIGIT.matcher(password).find()){
			errors.add(PasswordPolicyError.NO_DIGIT);
		}
		if(passwordSecurityPolicy.isForceLetters()&&!LETTER.matcher(password).find()){
			errors.add(PasswordPolicyError.NO_LETTER);
		}
		return errors;
	}

}
