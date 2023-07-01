package com.flexicore.data.jsoncontainers;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;

public class ResetPasswordResponse {
    public static final int TOKEN_EXPIRED = 3;
    public static final int NO_USER =1;
    public static final int INVALID_TOKEN =2;
    public static final int SMTP_FAILED = 4;

    private String message;

    @JsonIgnore
    private String verificationToken;

    private String email;
    private String phoneNumber;


    public ResetPasswordResponse() {
    }

    public ResetPasswordResponse( String verificationToken) {
        this.verificationToken=verificationToken;
    }

    @JsonIgnore

    public String getVerificationToken() {
        return verificationToken;
    }

    public ResetPasswordResponse setVerificationToken(String verificationToken) {
        this.verificationToken = verificationToken;
        return this;
    }
    @Schema(description = "human readable response ,that indicates if the reset process is ok or not")

    public String getMessage() {
        return message;
    }

    public ResetPasswordResponse setMessage(String message) {
        this.message = message;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public <T extends ResetPasswordResponse> T setEmail(String email) {
        this.email = email;
        return (T) this;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public <T extends ResetPasswordResponse> T setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return (T) this;
    }
}
