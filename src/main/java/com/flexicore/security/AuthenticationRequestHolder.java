/*******************************************************************************
 *  Copyright (C) FlexiCore, Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Avishay Ben Natan And Asaf Ben Natan, October 2015
 ******************************************************************************/
package com.flexicore.security;


import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Used to log into the system")
public class AuthenticationRequestHolder  {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String mail;

    private String phoneNumber;

    private String password;

    private String apikey;

    private String facebookUserId;

    private String facebookToken;

    @JsonIgnore
    private String ip;

    public AuthenticationRequestHolder(String mail, String password, String apikey) {
        this.mail = mail;
        this.password = password;
        this.apikey = apikey;
    }

    public AuthenticationRequestHolder(String mail, String phoneNumber, String password, String apikey) {
        this.mail = mail;
        this.password = password;
        this.apikey = apikey;
        this.phoneNumber=phoneNumber;
    }



    public AuthenticationRequestHolder() {

    }

    public AuthenticationRequestHolder(AuthenticationRequestHolder other) {
        this.mail = other.mail;
        this.phoneNumber = other.phoneNumber;
        this.apikey = other.apikey;
        this.facebookUserId = other.facebookUserId;
        this.facebookToken = other.facebookToken;
    }

    //makes sure password wont return
    @Schema(description =  "password , required if facebookToken not provided")
    public String getPassword() {
        return password;
    }

    //makes sure password will be set by jackson so log in is possible
    public AuthenticationRequestHolder setPassword(String password) {
        this.password = password;
        return this;
    }

    @Schema(description =  "email , required if phone number or facebookUserId is not provided")

    public String getMail() {
        return mail;
    }

    public AuthenticationRequestHolder setMail(String mail) {
        this.mail = mail;
        return this;

    }

    @Schema(description =  "login to specific tenant (objects that will be created with the returned key will belong to this tenant)" +
            " , if not provided will use the default tenant for this user")

    @Deprecated
    public String getApikey() {
        return apikey;
    }

    public AuthenticationRequestHolder setApikey(String apikey) {
        this.apikey = apikey;
        return this;

    }


    @Schema(description =  "facebookUserId , required if phone number or email is not provided")
    public String getFacebookUserId() {
        return facebookUserId;
    }

    public AuthenticationRequestHolder setFacebookUserId(String facebookUserId) {
        this.facebookUserId = facebookUserId;
        return this;

    }

    @Schema(description =  "facebookToken , required if password not provided")
    public String getFacebookToken() {
        return facebookToken;
    }

    public AuthenticationRequestHolder setFacebookToken(String facebookToken) {
        this.facebookToken = facebookToken;
        return this;

    }

    @Schema(description =  "phonenumber , required if email or facebookUserId is not provided")
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public AuthenticationRequestHolder setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    @JsonIgnore
    public String getIp() {
        return ip;
    }

    public <T extends AuthenticationRequestHolder> T setIp(String ip) {
        this.ip = ip;
        return (T) this;
    }

    @Override
    public String toString() {
        return "AuthenticationRequestHolder{" +
                "mail='" + mail + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", password='" + password + '\'' +
                ", apikey='" + apikey + '\'' +
                ", facebookUserId='" + facebookUserId + '\'' +
                ", facebookToken='" + facebookToken + '\'' +
                '}';
    }
}
