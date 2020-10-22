package com.flexicore.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.model.FilteringInformationHolder;
import com.flexicore.model.Tenant;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Schema(description = "used to receive users filtered by the properties in this object. filtering will be with logical AND")
public class UserFiltering extends FilteringInformationHolder {

    private Set<String> emails;
    private Set<String> phoneNumbers;
    private String lastNameLike;
    private Set<String> userIds=new HashSet<>();
    private Set<String> userTenantsIds=new HashSet<>();
    @JsonIgnore
    private List<Tenant> userTenants;
    @JsonIgnore
    private String forgotPasswordToken;
    @JsonIgnore
    private String emailVerificationToken;


    @Schema(description = "users with the given mails")
    public Set<String> getEmails() {
        return emails;
    }

    public <T extends UserFiltering> T setEmails(Set<String> emails) {
        this.emails = emails;
        return (T) this;
    }

    @Schema(description = "users with the given phone numbers")
    public Set<String> getPhoneNumbers() {
        return phoneNumbers;
    }

    public <T extends UserFiltering> T setPhoneNumbers(Set<String> phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
        return (T) this;
    }

    @Schema(description = "users with last name \"like\" given string use % for wildcard ")
    public String getLastNameLike() {
        return lastNameLike;
    }

    public UserFiltering setLastNameLike(String lastNameLike) {
        this.lastNameLike = lastNameLike;
        return this;
    }

    @Schema(description = "users with specific ids")
    public Set<String> getUserIds() {
        return userIds;
    }

    public UserFiltering setUserIds(Set<String> userIds) {
        this.userIds = userIds;
        return this;
    }

    @JsonIgnore
    public String getForgotPasswordToken() {
        return forgotPasswordToken;
    }

    public UserFiltering setForgotPasswordToken(String forgotPasswordToken) {
        this.forgotPasswordToken = forgotPasswordToken;
        return this;
    }

    @JsonIgnore
    public String getEmailVerificationToken() {
        return emailVerificationToken;
    }

    public UserFiltering setEmailVerificationToken(String emailVerificationToken) {
        this.emailVerificationToken = emailVerificationToken;
        return this;
    }

    @Schema(description = "users that belongs to the tenants that match the given ids")
    public Set<String> getUserTenantsIds() {
        return userTenantsIds;
    }

    public <T extends UserFiltering> T setUserTenantsIds(Set<String> userTenantsIds) {
        this.userTenantsIds = userTenantsIds;
        return (T) this;
    }

    @JsonIgnore
    public List<Tenant> getUserTenants() {
        return userTenants;
    }

    public <T extends UserFiltering> T setUserTenants(List<Tenant> userTenants) {
        this.userTenants = userTenants;
        return (T) this;
    }
}
