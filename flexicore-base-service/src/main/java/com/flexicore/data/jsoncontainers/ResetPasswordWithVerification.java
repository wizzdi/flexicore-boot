package com.flexicore.data.jsoncontainers;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Used to complete password reset process")
public class ResetPasswordWithVerification {


    private String verification;
    private String password;


    @Schema(description = "verification code received by external means (sms/mail)")
    public String getVerification() {
        return verification;
    }

    public ResetPasswordWithVerification setVerification(String verification) {
        this.verification = verification;
        return this;
    }

    @Schema(description = "new password")
    public String getPassword() {
        return password;
    }

    public ResetPasswordWithVerification setPassword(String password) {
        this.password = password;
        return this;
    }
}
