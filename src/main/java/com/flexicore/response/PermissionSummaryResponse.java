package com.flexicore.response;

import com.flexicore.model.PermissionGroupToBaseclass;

import java.util.List;
import java.util.Map;

public class PermissionSummaryResponse {
    private Map<String, List<PermissionGroupToBaseclass>> permissionGroupToBaseclasses;


    private List<PermissionSummaryEntry> permissionSummaryEntries;

    public List<PermissionSummaryEntry> getPermissionSummaryEntries() {
        return permissionSummaryEntries;
    }

    public <T extends PermissionSummaryResponse> T setPermissionSummaryEntries(List<PermissionSummaryEntry> permissionSummaryEntries) {
        this.permissionSummaryEntries = permissionSummaryEntries;
        return (T) this;
    }

    public Map<String, List<PermissionGroupToBaseclass>> getPermissionGroupToBaseclasses() {
        return permissionGroupToBaseclasses;
    }

    public <T extends PermissionSummaryResponse> T setPermissionGroupToBaseclasses(Map<String, List<PermissionGroupToBaseclass>> permissionGroupToBaseclasses) {
        this.permissionGroupToBaseclasses = permissionGroupToBaseclasses;
        return (T) this;
    }
}
