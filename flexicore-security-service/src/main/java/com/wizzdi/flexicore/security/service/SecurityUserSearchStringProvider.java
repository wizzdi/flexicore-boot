package com.wizzdi.flexicore.security.service;

import com.flexicore.model.SecurityUser;
import com.wizzdi.flexicore.security.interfaces.SearchStringProvider;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class SecurityUserSearchStringProvider implements SearchStringProvider<SecurityUser> {
	@Override
	public String getSearchString(SecurityUser securityUser) {
		return createSearchString(securityUser);
	}

	private String createSearchString(SecurityUser user) {
		List<String> l = List.of(
				getStringOrPlaceHolder(user.getName()),
				getStringOrPlaceHolder(user.getDescription())
		);
		return l.stream().map(String::toLowerCase).collect(Collectors.joining("|"));
	}

	public String getStringOrPlaceHolder(String s) {
		return s != null ? s : "#";
	}


	@Override
	public Class<SecurityUser> forType() {
		return SecurityUser.class;
	}
}
