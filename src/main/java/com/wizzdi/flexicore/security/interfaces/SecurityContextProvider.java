package com.wizzdi.flexicore.security.interfaces;

import com.flexicore.model.SecurityUser;
import com.flexicore.security.SecurityContextBase;

public interface SecurityContextProvider<U extends SecurityUser,T extends SecurityContextBase<?,U,?,?>> {

    T getSecurityContext(U user);
}
