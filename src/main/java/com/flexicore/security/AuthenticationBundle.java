/*******************************************************************************
 *  Copyright (C) FlexiCore, Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Avishay Ben Natan And Asaf Ben Natan, October 2015
 ******************************************************************************/
package com.flexicore.security;

//import io.swagger.annotations.ApiModelProperty;

/**
 * sent at login by client to get a token back.
 * password should be MD5 by the client...(to be implemented).
 *
 * @author avishayb
 */

public class AuthenticationBundle  {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String mail;
    private String authenticationkey;

    public AuthenticationBundle(String mail) {
        this.mail = mail;
    }

    public AuthenticationBundle() {

    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    /**
     * the token use with subsequent calls to secured REST methods.
     *
     * @return
     */
    public String getAuthenticationkey() {
        return authenticationkey;
    }

    public void setAuthenticationkey(String authenticationkey) {
        this.authenticationkey = authenticationkey;
    }

    @Override
    public String toString() {
        return "AuthenticationBundle [" + (mail != null ? "mail=" + mail + ", " : "")
                + (authenticationkey != null ? "authenticationkey=" + authenticationkey : "") + "]";
    }


}
