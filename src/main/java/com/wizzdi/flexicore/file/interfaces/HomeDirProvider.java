package com.wizzdi.flexicore.file.interfaces;

import com.flexicore.model.SecurityUser;

import java.io.File;

public interface HomeDirProvider<T extends SecurityUser> {

	File getHomeDir(T securityUser);
	Class<T> getType();

}
