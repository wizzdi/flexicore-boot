package com.flexicore.interfaces.dynamic;

import com.flexicore.data.jsoncontainers.PaginationResponse;
import com.flexicore.model.FilteringInformationHolder;
import com.flexicore.security.SecurityContext;

public interface ListingInvoker<T, E extends FilteringInformationHolder> extends Invoker {


    PaginationResponse<T> listAll(E filter, SecurityContext securityContext);
    Class<E> getFilterClass();

}
