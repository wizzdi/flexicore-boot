package com.flexicore.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

import java.util.ArrayList;
import java.util.List;

@Entity
public class SecurityLinkGroup extends SecuredBasic {

    @JsonIgnore
    @OneToMany(targetEntity = SecurityLink.class, mappedBy = "securityLinkGroup")
    private List<SecurityLink> securityLinks = new ArrayList<>();

    @JsonIgnore
    @OneToMany(targetEntity = SecurityLink.class, mappedBy = "securityLinkGroup")
    public List<SecurityLink> getSecurityLinks() {
        return securityLinks;
    }

    public <T extends SecurityLinkGroup> T setSecurityLinks(List<SecurityLink> securityLinks) {
        this.securityLinks = securityLinks;
        return (T) this;
    }
}
