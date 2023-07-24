package com.flexicore.response;

import com.flexicore.annotations.IOperation;
import com.flexicore.model.*;

import java.util.*;
import java.util.stream.Collectors;

public class PermissionSummaryEntry {
    private User user;
    private Map<String, String> explanation=new HashMap<>();
    private List<RoleToUser> roles;
    private List<TenantToUser> tenants;
    private Map<String, Baseclass> creator;
    private Map<String, Boolean> allowed = new HashMap<>();
    private Map<String, List<UserToBaseclass>> userToBaseClasses = new HashMap<>();
    private Map<String, List<RoleToBaseclass>> roleToBaseclasses = new HashMap<>();
    private Map<String, List<TenantToBaseclass>> tenantToBaseClassPremissions = new HashMap<>();

    private Map<String, List<UserToBaseclass>> userToClazz = new HashMap<>();
    private Map<String, List<RoleToBaseclass>> roleToClazz = new HashMap<>();
    private Map<String, List<TenantToBaseclass>> tenantToClazz = new HashMap<>();

    private Map<String, List<UserToBaseclass>> userToPermissionGroup = new HashMap<>();
    private Map<String, List<RoleToBaseclass>> roleToPermissionGroup = new HashMap<>();
    private Map<String, List<TenantToBaseclass>> tenantToPermissionGroup = new HashMap<>();


    public User getUser() {
        return user;
    }

    public <T extends PermissionSummaryEntry> T setUser(User user) {
        this.user = user;
        return (T) this;
    }


    public List<RoleToUser> getRoles() {
        return roles;
    }

    public <T extends PermissionSummaryEntry> T setRoles(List<RoleToUser> roles) {
        this.roles = roles;
        return (T) this;
    }

    public List<TenantToUser> getTenants() {
        return tenants;
    }

    public <T extends PermissionSummaryEntry> T setTenants(List<TenantToUser> tenants) {
        this.tenants = tenants;
        return (T) this;
    }

    public Map<String, List<UserToBaseclass>> getUserToBaseClasses() {
        return userToBaseClasses;
    }

    public <T extends PermissionSummaryEntry> T setUserToBaseClasses(Map<String, List<UserToBaseclass>> userToBaseClasses) {
        this.userToBaseClasses = userToBaseClasses;
        return (T) this;
    }

    public Map<String, List<RoleToBaseclass>> getRoleToBaseclasses() {
        return roleToBaseclasses;
    }

    public <T extends PermissionSummaryEntry> T setRoleToBaseclasses(Map<String, List<RoleToBaseclass>> roleToBaseclasses) {
        this.roleToBaseclasses = roleToBaseclasses;
        return (T) this;
    }

    public Map<String, List<TenantToBaseclass>> getTenantToBaseClassPremissions() {
        return tenantToBaseClassPremissions;
    }

    public <T extends PermissionSummaryEntry> T setTenantToBaseClassPremissions(Map<String, List<TenantToBaseclass>> tenantToBaseClassPremissions) {
        this.tenantToBaseClassPremissions = tenantToBaseClassPremissions;
        return (T) this;
    }

    public Map<String, List<UserToBaseclass>> getUserToClazz() {
        return userToClazz;
    }

    public <T extends PermissionSummaryEntry> T setUserToClazz(Map<String, List<UserToBaseclass>> userToClazz) {
        this.userToClazz = userToClazz;
        return (T) this;
    }

    public Map<String, List<RoleToBaseclass>> getRoleToClazz() {
        return roleToClazz;
    }

    public <T extends PermissionSummaryEntry> T setRoleToClazz(Map<String, List<RoleToBaseclass>> roleToClazz) {
        this.roleToClazz = roleToClazz;
        return (T) this;
    }

    public Map<String, List<TenantToBaseclass>> getTenantToClazz() {
        return tenantToClazz;
    }

    public <T extends PermissionSummaryEntry> T setTenantToClazz(Map<String, List<TenantToBaseclass>> tenantToClazz) {
        this.tenantToClazz = tenantToClazz;
        return (T) this;
    }

    public Map<String, List<UserToBaseclass>> getUserToPermissionGroup() {
        return userToPermissionGroup;
    }

    public <T extends PermissionSummaryEntry> T setUserToPermissionGroup(Map<String, List<UserToBaseclass>> userToPermissionGroup) {
        this.userToPermissionGroup = userToPermissionGroup;
        return (T) this;
    }

    public Map<String, List<RoleToBaseclass>> getRoleToPermissionGroup() {
        return roleToPermissionGroup;
    }

    public <T extends PermissionSummaryEntry> T setRoleToPermissionGroup(Map<String, List<RoleToBaseclass>> roleToPermissionGroup) {
        this.roleToPermissionGroup = roleToPermissionGroup;
        return (T) this;
    }

    public Map<String, List<TenantToBaseclass>> getTenantToPermissionGroup() {
        return tenantToPermissionGroup;
    }

    public <T extends PermissionSummaryEntry> T setTenantToPermissionGroup(Map<String, List<TenantToBaseclass>> tenantToPermissionGroup) {
        this.tenantToPermissionGroup = tenantToPermissionGroup;
        return (T) this;
    }

    public Map<String, Baseclass> getCreator() {
        return creator;
    }

    public <T extends PermissionSummaryEntry> T setCreator(Map<String, Baseclass> creator) {
        this.creator = creator;
        return (T) this;
    }

    public Map<String, String> getExplanation() {
        return explanation;
    }

    public <T extends PermissionSummaryEntry> T setExplanation(Map<String, String> explanation) {
        this.explanation = explanation;
        return (T) this;
    }

    public Map<String, Boolean> getAllowed() {
        return allowed;
    }

    public <T extends PermissionSummaryEntry> T setAllowed(Map<String, Boolean> allowed) {
        this.allowed = allowed;
        return (T) this;
    }

    public void updateExplanation(String baseclassId) {
        boolean allowed=this.allowed.get(baseclassId);
       String explanation="User "+user.getId()+" is "+(!allowed?"NOT":"")+" allowed to Baseclass "+baseclassId +" because:";
        Set<String> reasons=new HashSet<>();
        if(creator.get(baseclassId)!=null){
            reasons.add("it is it creator");
        }
        List<UserToBaseclass> userToBaseclasses = this.userToBaseClasses.get(baseclassId);
        if(userToBaseclasses !=null ){
            for (UserToBaseclass userToBaseClass : userToBaseclasses) {
                reasons.add("it has a direct link "+userToBaseClass.getId()+"to it");
            }
        }
        List<RoleToBaseclass> roleToBaseclasses = this.roleToBaseclasses.get(baseclassId);
        if(roleToBaseclasses !=null){
            for (RoleToBaseclass roleToBaseclass : roleToBaseclasses) {
                reasons.add("its role "+roleToBaseclass.getLeftside().getId()+" has a direct link "+roleToBaseclass.getId()+"to it");

            }
        }

        List<TenantToBaseclass> tenantToBaseclasses = this.tenantToBaseClassPremissions.get(baseclassId);
        if(tenantToBaseclasses !=null){
            for (TenantToBaseclass tenantToBaseclass : tenantToBaseclasses) {
                reasons.add("its Tenant "+ tenantToBaseclass.getLeftside().getId()+" has a direct link "+ tenantToBaseclass.getId()+"to it");

            }
        }

        List<UserToBaseclass> userToClazz = this.userToClazz.get(baseclassId);
        if(userToClazz !=null ){
            for (UserToBaseclass userToBaseClass : userToClazz) {
                reasons.add("its clazz "+userToBaseClass.getRightside().getId()+" has a link "+userToBaseClass.getId()+"to it");
            }
        }
        List<RoleToBaseclass> roleToClazzes = this.roleToClazz.get(baseclassId);
        if(roleToClazzes !=null){
            for (RoleToBaseclass roleToClazz : roleToClazzes) {
                reasons.add("its clazz "+roleToClazz.getRightside().getId()+" has a link "+roleToClazz.getId()+"with a role "+roleToClazz.getLeftside().getId()+" it has");

            }
        }

        List<TenantToBaseclass> tenantToClazzes = this.tenantToClazz.get(baseclassId);
        if(tenantToClazzes !=null){
            for (TenantToBaseclass tenantToClazz : tenantToClazzes) {
                reasons.add("its clazz "+tenantToClazz.getRightside().getId()+" has a link "+tenantToClazz.getId()+"with a tenant "+tenantToClazz.getLeftside().getId()+" it is in");

            }
        }

        List<UserToBaseclass> userToPermissionGroups = this.userToPermissionGroup.get(baseclassId);
        if(userToPermissionGroups !=null ){
            for (UserToBaseclass userToPermissionGroup : userToPermissionGroups) {
                reasons.add("it is connected via link "+userToPermissionGroup.getId()+"to a permission group "+userToPermissionGroup.getRightside().getId()+"in which it is in");
            }
        }
        List<RoleToBaseclass> roleToPermissionGroups = this.roleToPermissionGroup.get(baseclassId);
        if(roleToPermissionGroups !=null){
            for (RoleToBaseclass roleToPermissionGroup : roleToPermissionGroups) {
                reasons.add("its role "+roleToPermissionGroup.getLeftside().getId()+"is connected via link "+roleToPermissionGroup.getId()+"to a permission group "+roleToPermissionGroup.getRightside().getId()+"in which it is in");

            }
        }

        List<TenantToBaseclass> tenantToPermissionGroups = this.tenantToBaseClassPremissions.get(baseclassId);
        if(tenantToPermissionGroups !=null){
            for (TenantToBaseclass tenantToPermissionGroup : tenantToPermissionGroups) {
                reasons.add("its Tenant "+tenantToPermissionGroup.getLeftside().getId()+"is connected via link "+tenantToPermissionGroup.getId()+"to a permission group "+tenantToPermissionGroup.getRightside().getId()+"in which it is in");

            }
        }
        if(reasons.isEmpty()){
            reasons.add("He is not this baseclass creator nor does he , his roles or his tenants have any special permission to access it");
        }
        explanation+= System.lineSeparator()+ reasons.parallelStream().collect(Collectors.joining(System.lineSeparator()));
        this.explanation.put(baseclassId,explanation);
    }

    public void updateAllowed(String baseclassId) {
        boolean allowed = userToBaseClasses.getOrDefault(baseclassId, new ArrayList<>()).parallelStream().anyMatch(f -> f.getSimplevalue() != null && IOperation.Access.allow.name().toLowerCase().equals(f.getSimplevalue().toLowerCase()))
                || userToClazz.getOrDefault(baseclassId, new ArrayList<>()).parallelStream().anyMatch(f -> f.getSimplevalue() != null && IOperation.Access.allow.name().toLowerCase().equals(f.getSimplevalue().toLowerCase()))
                || userToPermissionGroup.getOrDefault(baseclassId, new ArrayList<>()).parallelStream().anyMatch(f -> f.getSimplevalue() != null && IOperation.Access.allow.name().toLowerCase().equals(f.getSimplevalue().toLowerCase()))
                || roleToBaseclasses.getOrDefault(baseclassId, new ArrayList<>()).parallelStream().anyMatch(f -> f.getSimplevalue() != null && IOperation.Access.allow.name().toLowerCase().equals(f.getSimplevalue().toLowerCase()))
                || roleToClazz.getOrDefault(baseclassId, new ArrayList<>()).parallelStream().anyMatch(f -> f.getSimplevalue() != null && IOperation.Access.allow.name().toLowerCase().equals(f.getSimplevalue().toLowerCase()))
                || roleToPermissionGroup.getOrDefault(baseclassId, new ArrayList<>()).parallelStream().anyMatch(f -> f.getSimplevalue() != null && IOperation.Access.allow.name().toLowerCase().equals(f.getSimplevalue().toLowerCase()))
                || tenantToBaseClassPremissions.getOrDefault(baseclassId, new ArrayList<>()).parallelStream().anyMatch(f -> f.getSimplevalue() != null && IOperation.Access.allow.name().toLowerCase().equals(f.getSimplevalue().toLowerCase()))
                || tenantToClazz.getOrDefault(baseclassId, new ArrayList<>()).parallelStream().anyMatch(f -> f.getSimplevalue() != null && IOperation.Access.allow.name().toLowerCase().equals(f.getSimplevalue().toLowerCase()))
                || tenantToPermissionGroup.getOrDefault(baseclassId, new ArrayList<>()).parallelStream().anyMatch(f -> f.getSimplevalue() != null && IOperation.Access.allow.name().toLowerCase().equals(f.getSimplevalue().toLowerCase()))
                || creator.get(baseclassId)!=null;
        this.allowed.put(baseclassId, allowed);

    }
}
