package com.wizzdi.flexicore.common.user.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.model.User;
import com.wizzdi.flexicore.security.request.SecurityUserCreate;
import com.wizzdi.flexicore.security.validation.Create;
import com.wizzdi.flexicore.security.validation.Update;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.OffsetDateTime;

@Schema(description = "used to create a user")
public class CommonUserCreate extends SecurityUserCreate {
    @Email
    @NotNull(groups = Create.class)
    @Size(min = 1,max = 255,groups = {Create.class, Update.class})
    private String email;
    private String lastName;
    @NotNull(groups = Create.class)
    @Size(min = 1,max = 255,groups = {Create.class, Update.class})
    private String password;
    private String phoneNumber;
    private String uiConfiguration;
    @JsonIgnore
    private Boolean disabled;
    @JsonIgnore
    private OffsetDateTime dateApproved;
    @JsonIgnore
    private User approvingUser;
    @JsonIgnore
    private String homeDir;

    @JsonIgnore
    private String totpSalt;

    public CommonUserCreate(SecurityUserCreate other) {
        super(other);
    }

    public CommonUserCreate() {
    }

    public String getEmail() {
        return email;
    }

    public <T extends CommonUserCreate> T setEmail(String email) {
        this.email = email;
        return (T) this;
    }

    public String getLastName() {
        return lastName;
    }

    public <T extends CommonUserCreate> T setLastName(String lastName) {
        this.lastName = lastName;
        return (T) this;
    }

    public String getPassword() {
        return password;
    }

    public <T extends CommonUserCreate> T setPassword(String password) {
        this.password = password;
        return (T) this;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public <T extends CommonUserCreate> T setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return (T) this;
    }

    public String getUiConfiguration() {
        return uiConfiguration;
    }

    public <T extends CommonUserCreate> T setUiConfiguration(String uiConfiguration) {
        this.uiConfiguration = uiConfiguration;
        return (T) this;
    }

    @JsonIgnore
    public Boolean getDisabled() {
        return disabled;
    }

    public <T extends CommonUserCreate> T setDisabled(Boolean disabled) {
        this.disabled = disabled;
        return (T) this;
    }

    @JsonIgnore
    public OffsetDateTime getDateApproved() {
        return dateApproved;
    }

    public <T extends CommonUserCreate> T setDateApproved(OffsetDateTime dateApproved) {
        this.dateApproved = dateApproved;
        return (T) this;
    }

    @JsonIgnore
    public User getApprovingUser() {
        return approvingUser;
    }

    public <T extends CommonUserCreate> T setApprovingUser(User approvingUser) {
        this.approvingUser = approvingUser;
        return (T) this;
    }

    @JsonIgnore
    public String getHomeDir() {
        return homeDir;
    }

    public <T extends CommonUserCreate> T setHomeDir(String homeDir) {
        this.homeDir = homeDir;
        return (T) this;
    }

    @JsonIgnore
    public String getTotpSalt() {
        return totpSalt;
    }

    public <T extends CommonUserCreate> T setTotpSalt(String totpSalt) {
        this.totpSalt = totpSalt;
        return (T) this;
    }
}
