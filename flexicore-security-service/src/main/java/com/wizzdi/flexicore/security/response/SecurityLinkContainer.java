package com.wizzdi.flexicore.security.response;

import com.flexicore.model.OperationGroup;
import com.flexicore.model.PermissionGroup;
import com.flexicore.model.SecurityEntity;
import com.flexicore.model.SecurityLinkGroup;
import com.flexicore.model.SecurityOperation;
import com.wizzdi.segmantix.model.Access;

public record SecurityLinkContainer(String id, String securedId,String securedName, String securedType, PermissionGroup permissionGroup,
                                    OperationGroup operationGroup, SecurityLinkGroup securityLinkGroup, Access access,
                                    SecurityOperation operation, SecurityEntity securityEntity) {
}
