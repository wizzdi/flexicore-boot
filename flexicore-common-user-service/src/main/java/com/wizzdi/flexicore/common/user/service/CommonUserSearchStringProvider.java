package com.wizzdi.flexicore.common.user.service;

import com.flexicore.model.User;
import com.wizzdi.flexicore.security.interfaces.SearchStringProvider;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CommonUserSearchStringProvider implements SearchStringProvider<User> {

	@Override
	public String getSearchString(User user) {
		return createSearchString(user);
	}

	private String createSearchString(User user) {
		List<String> l = List.of(
				getStringOrPlaceHolder(user.getName()),
				getStringOrPlaceHolder(user.getLastName()),
				getStringOrPlaceHolder(user.getPhoneNumber()),
				getStringOrPlaceHolder(user.getEmail()),
				getStringOrPlaceHolder(user.getDescription())
		);
		return l.stream().map(String::toLowerCase).collect(Collectors.joining("|"));
	}

	public String getStringOrPlaceHolder(String s) {
		return s != null ? s : "#";
	}


	@Override
	public Class<User> forType() {
		return User.class;
	}
}
