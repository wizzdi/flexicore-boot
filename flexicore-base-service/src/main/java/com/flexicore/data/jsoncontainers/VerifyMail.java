package com.flexicore.data.jsoncontainers;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Used to finish mail/phone verification process")
public class VerifyMail {


    private String verification;

    @Schema(description = "verification code received by external process (sms/email)")
    public String getVerification() {
        return verification;
    }

    public VerifyMail setVerification(String verification) {
        this.verification = verification;
        return this;
    }


}
