package com.flexicore.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.model.User;
import com.wizzdi.flexicore.security.request.SecurityUserCreate;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.OffsetDateTime;

@Schema(description = "used to create a user")
public class UserCreate extends SecurityUserCreate {
    private String email;
    private String lastName;
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

    public UserCreate(SecurityUserCreate other) {
        super(other);
    }

    public UserCreate() {
    }

    public String getEmail() {
        return email;
    }

    public <T extends UserCreate> T setEmail(String email) {
        this.email = email;
        return (T) this;
    }

    public String getLastName() {
        return lastName;
    }

    public <T extends UserCreate> T setLastName(String lastName) {
        this.lastName = lastName;
        return (T) this;
    }

    public String getPassword() {
        return password;
    }

    public <T extends UserCreate> T setPassword(String password) {
        this.password = password;
        return (T) this;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public <T extends UserCreate> T setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return (T) this;
    }

    public String getUiConfiguration() {
        return uiConfiguration;
    }

    public <T extends UserCreate> T setUiConfiguration(String uiConfiguration) {
        this.uiConfiguration = uiConfiguration;
        return (T) this;
    }

    @JsonIgnore
    public Boolean getDisabled() {
        return disabled;
    }

    public <T extends UserCreate> T setDisabled(Boolean disabled) {
        this.disabled = disabled;
        return (T) this;
    }

    @JsonIgnore
    public OffsetDateTime getDateApproved() {
        return dateApproved;
    }

    public <T extends UserCreate> T setDateApproved(OffsetDateTime dateApproved) {
        this.dateApproved = dateApproved;
        return (T) this;
    }

    @JsonIgnore
    public User getApprovingUser() {
        return approvingUser;
    }

    public <T extends UserCreate> T setApprovingUser(User approvingUser) {
        this.approvingUser = approvingUser;
        return (T) this;
    }

    @JsonIgnore
    public String getHomeDir() {
        return homeDir;
    }

    public <T extends UserCreate> T setHomeDir(String homeDir) {
        this.homeDir = homeDir;
        return (T) this;
    }
}
