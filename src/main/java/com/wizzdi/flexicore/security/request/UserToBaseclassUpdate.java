package com.wizzdi.flexicore.security.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.model.UserToBaseClass;

public class UserToBaseclassUpdate extends UserToBaseclassCreate{

	private String id;
	@JsonIgnore
	private UserToBaseClass userToBaseclass;

	public String getId() {
		return id;
	}

	public <T extends UserToBaseclassUpdate> T setId(String id) {
		this.id = id;
		return (T) this;
	}

	@JsonIgnore
	public UserToBaseClass getUserToBaseclass() {
		return userToBaseclass;
	}

	public <T extends UserToBaseclassUpdate> T setUserToBaseclass(UserToBaseClass userToBaseclass) {
		this.userToBaseclass = userToBaseclass;
		return (T) this;
	}
}
