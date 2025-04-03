package com.wizzdi.flexicore.security.interfaces;

import com.flexicore.model.SecurityUser;

public interface SearchStringProvider<T extends SecurityUser> {
	String getSearchString(T t);
	Class<T> forType();
}
