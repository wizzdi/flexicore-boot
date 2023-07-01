package com.wizzdi.flexicore.security.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.model.SecurityLink;
import com.wizzdi.flexicore.security.validation.IdValid;
import com.wizzdi.flexicore.security.validation.Update;

import jakarta.validation.constraints.NotNull;

@IdValid.List({
        @IdValid(targetField = "securityLink", fieldType = SecurityLink.class, field = "id", groups = {Update.class}),
})
public class SecurityLinkUpdate extends SecurityLinkCreate {

    @NotNull(groups = Update.class)
    private String id;
    @JsonIgnore
    private SecurityLink securityLink;

    public String getId() {
        return id;
    }

    public <T extends SecurityLinkUpdate> T setId(String id) {
        this.id = id;
        return (T) this;
    }

    @JsonIgnore
    public SecurityLink getSecurityLink() {
        return securityLink;
    }

    public <T extends SecurityLinkUpdate> T setSecurityLink(SecurityLink securityLink) {
        this.securityLink = securityLink;
        return (T) this;
    }
}
