package com.wizzdi.flexicore.common.user.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.model.SecurityTenant;
import com.wizzdi.flexicore.security.request.SecurityUserFilter;
import com.wizzdi.flexicore.security.validation.IdValid;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Schema(description = "used to receive users filtered by the properties in this object. filtering will be with logical AND")
@IdValid.List({@IdValid(field ="userSecurityTenantsIds",targetField ="userSecurityTenants",fieldType = SecurityTenant.class)})
public class CommonUserFilter extends SecurityUserFilter {

    private Set<String> emails;
    private Set<String> phoneNumbers;
    private String lastNameLike;
    private Set<String> userIds=new HashSet<>();
    private Set<String> userSecurityTenantsIds=new HashSet<>();
    @JsonIgnore
    private List<SecurityTenant> userSecurityTenants;
    @JsonIgnore
    private String forgotPasswordToken;
    @JsonIgnore
    private String emailVerificationToken;


    @Schema(description = "users with the given mails")
    public Set<String> getEmails() {
        return emails;
    }

    public <T extends CommonUserFilter> T setEmails(Set<String> emails) {
        this.emails = emails;
        return (T) this;
    }

    @Schema(description = "users with the given phone numbers")
    public Set<String> getPhoneNumbers() {
        return phoneNumbers;
    }

    public <T extends CommonUserFilter> T setPhoneNumbers(Set<String> phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
        return (T) this;
    }

    @Schema(description = "users with last name \"like\" given string use % for wildcard ")
    public String getLastNameLike() {
        return lastNameLike;
    }

    public CommonUserFilter setLastNameLike(String lastNameLike) {
        this.lastNameLike = lastNameLike;
        return this;
    }

    @Schema(description = "users with specific ids")
    public Set<String> getUserIds() {
        return userIds;
    }

    public CommonUserFilter setUserIds(Set<String> userIds) {
        this.userIds = userIds;
        return this;
    }

    @JsonIgnore
    public String getForgotPasswordToken() {
        return forgotPasswordToken;
    }

    public CommonUserFilter setForgotPasswordToken(String forgotPasswordToken) {
        this.forgotPasswordToken = forgotPasswordToken;
        return this;
    }

    @JsonIgnore
    public String getEmailVerificationToken() {
        return emailVerificationToken;
    }

    public CommonUserFilter setEmailVerificationToken(String emailVerificationToken) {
        this.emailVerificationToken = emailVerificationToken;
        return this;
    }

    @Schema(description = "users that belongs to the securityTenants that match the given ids")
    public Set<String> getUserSecurityTenantsIds() {
        return userSecurityTenantsIds;
    }

    public <T extends CommonUserFilter> T setUserSecurityTenantsIds(Set<String> userSecurityTenantsIds) {
        this.userSecurityTenantsIds = userSecurityTenantsIds;
        return (T) this;
    }

    @JsonIgnore
    public List<SecurityTenant> getUserSecurityTenants() {
        return userSecurityTenants;
    }

    public <T extends CommonUserFilter> T setUserSecurityTenants(List<SecurityTenant> userSecurityTenants) {
        this.userSecurityTenants = userSecurityTenants;
        return (T) this;
    }
}
