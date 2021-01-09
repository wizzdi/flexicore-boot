package com.wizzdi.flexicore.security.data;

import com.flexicore.model.Baseclass;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.flexicore.security.SecurityContextBase;
import com.wizzdi.flexicore.security.request.SecurityEntityFilter;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.*;
import java.util.List;

@Extension
@Component
public class SecurityEntityRepository implements Plugin {

	@Autowired
	private BaseclassRepository baseclassRepository;

	public <T extends Baseclass> void addSecurityEntityPredicates(SecurityEntityFilter securityEntityFilter, CriteriaBuilder cb, CommonAbstractCriteria q, Path<T> r, List<Predicate> predicates, SecurityContextBase securityContext) {
		baseclassRepository.addBaseclassPredicates(cb,q,r,predicates,securityContext);
	}

}
