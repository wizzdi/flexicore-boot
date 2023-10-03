package com.wizzdi.flexicore.security.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.model.SecurityLinkGroup;
import com.wizzdi.flexicore.security.validation.IdValid;
import com.wizzdi.flexicore.security.validation.Update;
import jakarta.validation.constraints.NotNull;

@IdValid.List({
        @IdValid(targetField = "securityLinkGroup", fieldType = SecurityLinkGroup.class, field = "id", groups = {Update.class}),
})
public class SecurityLinkGroupUpdate extends SecurityLinkGroupCreate {

    @NotNull(groups = Update.class)
    private String id;
    @JsonIgnore
    private SecurityLinkGroup securityLinkGroup;

    public String getId() {
        return id;
    }

    public <T extends SecurityLinkGroupUpdate> T setId(String id) {
        this.id = id;
        return (T) this;
    }

    @JsonIgnore
    public SecurityLinkGroup getSecurityLinkGroup() {
        return securityLinkGroup;
    }

    public <T extends SecurityLinkGroupUpdate> T setSecurityLinkGroup(SecurityLinkGroup securityLinkGroup) {
        this.securityLinkGroup = securityLinkGroup;
        return (T) this;
    }
}
