package com.flexicore.service;

import com.flexicore.data.jsoncontainers.PaginationResponse;
import com.flexicore.interfaces.FlexiCoreService;
import com.flexicore.model.Baseclass;
import com.flexicore.model.FilteringInformationHolder;
import com.flexicore.request.GetConnectedGeneric;
import com.flexicore.request.GetDisconnectedGeneric;
import com.flexicore.security.SecurityContext;

import java.util.List;

public interface ConnectionSupportingService extends FlexiCoreService {

    <Link extends Baseclass, Base extends Baseclass,BaseFilter extends FilteringInformationHolder,LinkFilter extends FilteringInformationHolder> List<Base> listConnected(GetConnectedGeneric<Base,Link,BaseFilter,LinkFilter> getConnected, SecurityContext securityContext);

    <Link extends Baseclass, Base extends Baseclass,BaseFilter extends FilteringInformationHolder,LinkFilter extends FilteringInformationHolder> long countConnected(GetConnectedGeneric<Base,Link,BaseFilter,LinkFilter> getConnected,  SecurityContext securityContext);

    <Link extends Baseclass, Base extends Baseclass,BaseFilter extends FilteringInformationHolder,LinkFilter extends FilteringInformationHolder> List<Base> listDisconnected(GetDisconnectedGeneric<Base,Link,BaseFilter,LinkFilter> getDisconnected, SecurityContext securityContext);

    <Link extends Baseclass, Base extends Baseclass,BaseFilter extends FilteringInformationHolder,LinkFilter extends FilteringInformationHolder> long countDisconnected(GetDisconnectedGeneric<Base,Link,BaseFilter,LinkFilter> getDisconnected, SecurityContext securityContext);

    <Link extends Baseclass, Base extends Baseclass,BaseFilter extends FilteringInformationHolder,LinkFilter extends FilteringInformationHolder> PaginationResponse<Base> getConnected(GetConnectedGeneric<Base,Link,BaseFilter,LinkFilter> getConnected, SecurityContext securityContext);
    <Link extends Baseclass, Base extends Baseclass,BaseFilter extends FilteringInformationHolder,LinkFilter extends FilteringInformationHolder> PaginationResponse<Base> getDisconnected(GetDisconnectedGeneric<Base,Link,BaseFilter,LinkFilter> getDisconnected, SecurityContext securityContext);


}
