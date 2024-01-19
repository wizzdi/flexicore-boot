package com.flexicore.security;

import com.flexicore.annotations.IOperation;
import com.flexicore.model.*;

import java.util.List;

public record SecurityPermissionEntry<T extends SecurityEntity>(T entity,
                                                                List<Baseclass> allowed, List<Baseclass> denied,
                                                                List<Clazz> allowedTypes,List<Clazz> deniedTypes,
                                                                List<PermissionGroup> allowedPermissionGroups , List<PermissionGroup> deniedPermissionGroups,boolean allowAll) {

    public SecurityPermissionEntry {
    }

    public SecurityPermissionEntry(T entity, List<Baseclass> allowed, List<Baseclass> denied, List<Clazz> allowedTypes, List<Clazz> deniedTypes, List<PermissionGroup> allowedPermissionGroups, List<PermissionGroup> deniedPermissionGroups) {
        this(entity, allowed, denied, allowedTypes, deniedTypes, allowedPermissionGroups, deniedPermissionGroups, allowedTypes.stream().anyMatch(f-> SecurityWildcard.class.getCanonicalName().equals(f.getName())));
    }

    public static <T extends SecurityEntity,E extends SecurityLink > SecurityPermissionEntry<T> of(T t, List<E> links) {
        return new SecurityPermissionEntry<>(t,
                links.stream().filter(f -> f.getAccess().equals(IOperation.Access.allow)&&f.getBaseclass()!=null).map(f -> f.getBaseclass()).toList(),
                links.stream().filter(f -> f.getAccess().equals(IOperation.Access.deny)&&f.getBaseclass()!=null).map(f -> f.getBaseclass()).toList(),
                links.stream().filter(f -> f.getAccess().equals(IOperation.Access.allow)&&f.getClazz()!=null).map(f -> f.getClazz()).toList(),
                links.stream().filter(f -> f.getAccess().equals(IOperation.Access.deny)&&f.getClazz()!=null).map(f -> f.getClazz()).toList(),
                links.stream().filter(f -> f.getAccess().equals(IOperation.Access.allow)&&f.getPermissionGroup()!=null).map(f -> f.getPermissionGroup()).toList(),
                links.stream().filter(f -> f.getAccess().equals(IOperation.Access.deny)&&f.getPermissionGroup()!=null).map(f -> f.getPermissionGroup()).toList()
                );

    }


}
