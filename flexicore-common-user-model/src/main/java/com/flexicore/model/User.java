/*******************************************************************************
 *  Copyright (C) FlexiCore, Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Avishay Ben Natan And Asaf Ben Natan, October 2015
 ******************************************************************************/
package com.flexicore.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.flexicore.annotations.AnnotatedClazz;

import com.wizzdi.flexicore.boot.rest.views.Views;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;


//the table name 'user' isn't allowed in Postgresql

@AnnotatedClazz(Category = "core", Name = "User", Description = "The basic class which uses the system")
@Table(name = "UserTable", indexes = {
        @Index(name = "user_email_ix", columnList = "email")
})
@Entity
@Schema( description = "The basic entity for controlling access to system functions and objects")
public class User extends SecurityUser {

    private String email;
    private String homeDir;
    private String lastName;
    @JsonIgnore
    private String totpSalt;

    private boolean disabled;
    @Column(columnDefinition = "timestamp with time zone")
    private OffsetDateTime dateApproved;
    @ManyToOne(targetEntity = User.class)
    private User approvingUser;

    @Lob
    private String uiConfiguration;

    @JsonIgnore
    private String forgotPasswordToken;
    @JsonIgnore
    @Column(columnDefinition = "timestamp with time zone")
    private OffsetDateTime forgotPasswordTokenValid;

    @JsonIgnore
    private String emailVerificationToken;
    @JsonIgnore
    @Column(columnDefinition = "timestamp with time zone")
    private OffsetDateTime emailVerificationTokenValid;
    @Column(columnDefinition = "timestamp with time zone")
    private OffsetDateTime lastVerificationDate;
    @JsonIgnore
    private String totpSecret;
    @JsonIgnore
    @Lob
    private String totpRecoveryCodes;
    private boolean totpEnabled;





    @Column(name = "phone_number")
    private String phoneNumber;
    @JsonIgnore
    private String password;

    @Column(name = "phone_number")
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "ID: " + this.id + "Name " + this.name + " Email: " + this.email + "Phone number: " + this.phoneNumber;
    }

    @JsonIgnore
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getHomeDir() {
        return homeDir;
    }

    public void setHomeDir(String homeDir) {
        this.homeDir = homeDir;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }



    @JsonIgnore
    public String getForgotPasswordToken() {
        return forgotPasswordToken;
    }

    public User setForgotPasswordToken(String verificationToken) {
        this.forgotPasswordToken = verificationToken;
        return this;
    }
    @JsonIgnore
    public OffsetDateTime getForgotPasswordTokenValid() {
        return forgotPasswordTokenValid;
    }

    public User setForgotPasswordTokenValid(OffsetDateTime verificationTokenValid) {
        this.forgotPasswordTokenValid = verificationTokenValid;
        return this;
    }

    @JsonIgnore
    public String getEmailVerificationToken() {
        return emailVerificationToken;
    }

    public User setEmailVerificationToken(String emailVerificationToken) {
        this.emailVerificationToken = emailVerificationToken;
        return this;
    }

    @JsonIgnore
    public OffsetDateTime getEmailVerificationTokenValid() {
        return emailVerificationTokenValid;
    }

    public User setEmailVerificationTokenValid(OffsetDateTime emailVerificationTokenValid) {
        this.emailVerificationTokenValid = emailVerificationTokenValid;
        return this;
    }

    public OffsetDateTime getLastVerificationDate() {
        return lastVerificationDate;
    }

    public User setLastVerificationDate(OffsetDateTime lastVerificationDate) {
        this.lastVerificationDate = lastVerificationDate;
        return this;
    }

    @Lob
    public String getUiConfiguration() {
        return uiConfiguration;
    }

    public User setUiConfiguration(String uiConfiguration) {
        this.uiConfiguration = uiConfiguration;
        return this;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public <T extends User> T setDisabled(boolean unSecureRegistration) {
        this.disabled = unSecureRegistration;
        return (T) this;
    }

    public OffsetDateTime getDateApproved() {
        return dateApproved;
    }

    public <T extends User> T setDateApproved(OffsetDateTime dateApproved) {
        this.dateApproved = dateApproved;
        return (T) this;
    }

    @JsonIgnore
    @ManyToOne(targetEntity = User.class)
    public User getApprovingUser() {
        return approvingUser;
    }

    public <T extends User> T setApprovingUser(User approvingUser) {
        this.approvingUser = approvingUser;
        return (T) this;
    }

    @JsonIgnore
    public String getTotpSecret() {
        return totpSecret;
    }

    public <T extends User> T setTotpSecret(String totpSecert) {
        this.totpSecret = totpSecert;
        return (T) this;
    }

    public boolean isTotpEnabled() {
        return totpEnabled;
    }

    public <T extends User> T setTotpEnabled(boolean totpEnabled) {
        this.totpEnabled = totpEnabled;
        return (T) this;
    }
    @Lob
    @JsonIgnore
    public String getTotpRecoveryCodes() {
        return totpRecoveryCodes;
    }

    public <T extends User> T setTotpRecoveryCodes(String totpRecoveryCodes) {
        this.totpRecoveryCodes = totpRecoveryCodes;
        return (T) this;
    }

    @JsonIgnore
    public String getTotpSalt() {
        return totpSalt;
    }

    public <T extends User> T setTotpSalt(String totpSalt) {
        this.totpSalt = totpSalt;
        return (T) this;
    }
}
