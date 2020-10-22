package com.flexicore.data.jsoncontainers;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "contains verify status")
public class VerifyMailResponse {

    public static final int TOKEN_EXPIRED = 2;
    public static final int SMTP_FAILED = 3;
    public static int INVALID_TOKEN = 1;

    private String message;
    @JsonIgnore
    private String verificationToken;

    public VerifyMailResponse() {
    }

    public VerifyMailResponse(String verificationToken) {
        this.verificationToken = verificationToken;
    }

    @Schema(description = "human readable message")
    public String getMessage() {
        return message;
    }

    public VerifyMailResponse setMessage(String message) {
        this.message = message;
        return this;
    }

    @JsonIgnore
    public String getVerificationToken() {
        return verificationToken;
    }

    public VerifyMailResponse setVerificationToken(String verificationToken) {
        this.verificationToken = verificationToken;
        return this;
    }
}
